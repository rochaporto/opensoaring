"""
Copyright (C) 2009 OpenSoaring <contact@opensoaring.info>.

This file is part of OpenSoaring.

OpenSoaring is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

OpenSoaring is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with OpenSoaring.  If not, see <http://www.gnu.org/licenses/>.
"""
from datetime import datetime, time

class IGCParser(object):
    
    def __init__(self, data):
        self.data = data
        self.deviceProps = {}
        self.flightProps = {}
        self.task = None
        self.events = []
        self.fixes = []
        self.fixesExtData = {}
        self.extensions = []
        self.extensionsExtData = {}
        self.satellites = []
        
    def parse(self):
        if self.data is None:
            return
        
        for line in self.data:
            try:                
                handleMethod = getattr(self, "handle%s" % line[0])
            except AttributeError:
                continue
            handleMethod(line)
            
    def handleA(self, line):
        self.deviceProps["MAN"] = line[1:4]
        self.deviceProps["UID"] = line[4:7]
        self.deviceProps["EXT"] = line[7:].strip()
            
    def handleB(self, line):
        point = {"TIME": self._parseTime(line[1:7]), "LAT": line[7:15], "LON": line[15:24],
                 "FXA": line[25], "PALT": int(line[26:31]), "GALT": int(line[31:36])}
        for ext in self.fixesExtData.keys():
            point[ext] = line[self.fixesExtData[ext][0]-1:self.fixesExtData[ext][1]]
        self.fixes.append(point)
    
    def handleC(self, line):
        if self.task is None:
            self.task = {"TIME": self._parseTime("%s %s" % (line[1:7], line[7:13])), 
                         "FDATE": line[13:19], "ID": line[19:23], "NTP": int(line[23:25]), 
                         "STR": line[25:].strip(), "TKF": None, "STA": None, "FIN": None, 
                         "LDG": None, "TNS": []}
        elif self.task["TKF"] is None:
            self.task["TKF"] = (line[1:9], line[9:18], line[18:].strip())
        elif self.task["STA"] is None:
            self.task["STA"] = (line[1:9], line[9:18], line[18:].strip())
        elif len(self.task["TNS"]) < self.task["NTP"]:
            self.task["TNS"].append((line[1:9], line[9:18], line[18:].strip()))
        elif self.task["FIN"] is None:
            self.task["FIN"] = (line[1:9], line[9:18], line[18:].strip())
        elif self.task["LDG"] is None:
            self.task["LDG"] = (line[1:9], line[9:18], line[18:].strip())
            
    def handleE(self, line):
        self.events.append({"TIME": self._parseTime(line[1:7]), "TLC": line[7:10], "STR": line[10:].strip()})
        
    def handleF(self, line):
        sats = []
        for i in range(7, len(line), 2):
            sats.append(line[i:i+2])
        self.satellites.append({"TIME": self._parseTime(line[1:7]), "SATS": sats})
        
    def handleH(self, line):
        if line.find(":") == -1:
            self.flightProps[line[2:5]] = line[5:].strip()
        else: 
            self.flightProps[line[2:5]] = line.split(":")[1].strip()
        
    def handleI(self, line):
        numExtensions = int(line[1:3])
        for i in range(0, numExtensions):
            self.fixesExtData[line[3+(i*7+4):3+(i*7+7)]] = (int(line[3+(i*7):3+(i*7+2)]),
                                                            int(line[3+(i*7+2):3+(i*7+4)]))
            
    def handleJ(self, line):
        numExtensions = int(line[1:3])
        for i in range(0, numExtensions):
            self.extensionsExtData[line[3+(i*7+4):3+(i*7+7)]] = (int(line[3+(i*7):3+(i*7+2)]),
                                                                 int(line[3+(i*7+2):3+(i*7+4)]))
    
    def handleK(self, line):
        point = {"TIME": self._parseTime(line[1:7])}
        for ext in self.extensionsExtData.keys():
            point[ext] = line[self.extensionsExtData[ext][0]-1:self.extensionsExtData[ext][1]]
        self.extensions.append(point)
    
    def toKML(self):
        kml = "<kml xmlns='http://earth.google.com/kml/2.2'><Document>"
        pMarks = self.task["TNS"]
        for pnt in ["TKF", "STA", "FIN", "LDG"]:
            pMarks.append(self.task[pnt])
        for pnt in pMarks:
            if pnt[0] != "0000000N" and pnt[1] != "00000000E":
                kml += """
                    <Placemark>
                        <name>%s</name><description></description>
                        <Point><coordinates>%s,%s</coordinates></Point>
                    </Placemark>
                    """ % (pnt[2], self._dms2Decimal(pnt[0]), self._dms2Decimal(pnt[1]))
        kml += "<Placemark><LineString><altitudeMode>absolute</altitudeMode><coordinates>"
        for pnt in self.fixes:
            kml += "%s,%s,100 " % (self._dms2Decimal(pnt["LAT"]), self._dms2Decimal(pnt["LON"])) 
        kml += "</coordinates></LineString></Placemark>"
        kml += "</Document></kml>"
        return kml
        
    def _parseTime(self, str):
        if len(str) == 6:
            return datetime.strptime(str, "%H%M%S").time()
        else:
            return datetime.strptime(str, "%d%m%y %H%M%S")
    
    def _dms2Decimal(self, coord):
        dms = float(int(coord[-6:-4]) * 60 + int(coord[-4:-1])) / 3600
        if len(coord) == 8: # latitude
            dms += float(coord[0:2])
            if coord[-1] == "S":
                dms = -dms
        else:
            dms += float(coord[0:3])
            if coord[-1] == "W":
                dms = -dms  
        
        return dms
        
import sys
def main():
    igcFile = open(sys.argv[1], "r")
    parser = IGCParser(igcFile)
    parser.parse()
    
    print "DEV PROPS :: %s" % parser.deviceProps
    print "FLT PROPS :: %s" % parser.flightProps
    print "TASK :: %s" % parser.task
    print "EVENTS :: %s" % parser.events
    print "STSs :: %s" % parser.satellites
    print "FIX EXTS :: %s" % parser.fixesExtData
    print "FIX :: %s" % parser.fixes[0]
    print "EXTD EXTS :: %s" % parser.extensionsExtData
    if len(parser.extensions) > 0:
        print "EXT :: %s" % parser.extensions[0]
    
    #print parser.toKML()
    
if __name__ == "__main__":
    main()
