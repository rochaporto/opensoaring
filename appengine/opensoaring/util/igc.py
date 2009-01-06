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
from math import acos, atan2, cos, degrees, fabs, pi, radians, sin

class IGCParser(object):
    """
    Member variables (IGC File + Computed)
      self.deviceProps {"MAN", "UID", "EXT"}
      self.events {"UTC", "TLC", "STR"}
      self.extensions {"UTC", EXT}
      self.extensionsExtData {EXT}
        EXT is a tuple (startByte, endByte)
      self.fixes {"UTC", "FIX", "DFIX", "FXA", "PALT", "GALT" + "DIST", "CRS", "TIMED"}
      self.fixesExtData {EXT}
        EXT is a tuple (startByte, endByte)
      self.flightProps {+ "TKF", "LDG"}      
      self.phases {+ "SF", "EF", "T"}
      self.satellites {"UTC", "SATS"}
      self.task {"UTC", "FDATE", "ID", "NTP", "STR", "TKF", "STA", "FIN", "LDG", "TNS"
                 + "DIS", "VAR"}
        "TKF", "STA", "FIN", "LDG" are tuples: (lat, lon, description)
        "TNS" is a list containing tuples: (lat, lon, description)
    
    References used
      http://www.movable-type.co.uk/scripts/latlong.html
      http://williams.best.vwh.net/avform.htm
    """
    EARTH_RADIUS = 6371
    
    def __init__(self, data):
        self.data = data
        self.deviceProps = {}
        self.events = []
        self.extensions = []
        self.extensionsExtData = {}
        self.fixes = []
        self.fixesExtData = {}
        self.flightProps = {}
        self.phases = []        
        self.satellites = []
        self.task = None
        
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
        """
        IGC Record A - Flight Recorder information.
        
        Manufacturer (3 bytes), Unique ID (3 bytes), ID Extension (optional).
        """
        self.deviceProps["MAN"] = line[1:4]
        self.deviceProps["UID"] = line[4:7]
        self.deviceProps["EXT"] = line[7:].strip()
            
    def handleB(self, line):
        """
        IGC Record B - Fix read information.
        
        UTC Time (6 bytes HHMMSS), Latitude (8 bytes DDMMmmmN/S),
        Longitude (9 bytes DDDMMmmmE/W), Fix Validity (1 byte A/V), Pressure Altitude (5 bytes),
        GNSS Altitude (5 bytes), Fix Accuracy (3 bytes), Extensions (optional - see I Record).
        """
        fix = {"UTC": self._parseTime(line[1:7]), "FIX": (line[7:15], line[15:24]),
               "DFIX": (self._dms2Decimal(line[7:15]), self._dms2Decimal(line[15:24])),
               "FXA": line[24], "PALT": int(line[25:30]), "GALT": int(line[30:35])}
        for ext in self.fixesExtData.keys():
            fix[ext] = line[self.fixesExtData[ext][0]-1:self.fixesExtData[ext][1]]
        self.fixes.append(fix)
    
    def handleC(self, line):
        """
        IGC Record C - Task (Pre-flight declaration). 
        """
        if self.task is None:
            self.task = {"UTC": self._parseTime("%s %s" % (line[1:7], line[7:13])), 
                         "FDATE": line[13:19], "ID": line[19:23], "NTP": int(line[23:25]), 
                         "STR": line[25:].strip(), "TKF": None, "STA": None, "FIN": None, 
                         "LDG": None, "TNS": []}
        elif self.task["TKF"] is None:
            self.task["TKF"] = (self._dms2Decimal(line[1:9]), self._dms2Decimal(line[9:18]),
                                line[18:].strip())
        elif self.task["STA"] is None:
            self.task["STA"] = (self._dms2Decimal(line[1:9]), self._dms2Decimal(line[9:18]),
                                line[18:].strip())
        elif len(self.task["TNS"]) < self.task["NTP"]:
            self.task["TNS"].append((self._dms2Decimal(line[1:9]), self._dms2Decimal(line[9:18]), 
                                     line[18:].strip()))
        elif self.task["FIN"] is None:
            self.task["FIN"] = (self._dms2Decimal(line[1:9]), self._dms2Decimal(line[9:18]), 
                                line[18:].strip())
        elif self.task["LDG"] is None:
            self.task["LDG"] = (self._dms2Decimal(line[1:9]), self._dms2Decimal(line[9:18]), 
                                line[18:].strip())
            
    def handleE(self, line):
        """
        IGC Record E - Pilot or sensor triggered events.
        
        UTC Time (6 bytes HHMMSS), Three Letter Code (TLC) (3 bytes), Text String (optional).
        """
        self.events.append({"UTC": self._parseTime(line[1:7]), 
                            "TLC": line[7:10], "STR": line[10:].strip()})
        
    def handleF(self, line):
        """
        IGC Record F - Satellite Constellation.
        """
        sats = []
        for i in range(7, len(line), 2):
            sats.append(line[i:i+2])
        self.satellites.append({"UTC": self._parseTime(line[1:7]), "SATS": sats})
        
    def handleH(self, line):
        """
        IGC Record H - File Header.
        """
        if line.find(":") == -1:
            self.flightProps[line[2:5]] = line[5:].strip()
        else: 
            self.flightProps[line[2:5]] = line.split(":")[1].strip()
        
    def handleI(self, line):
        """
        IGC Record I - Extensions to the Fix (B) Record.
        """
        numExtensions = int(line[1:3])
        for i in range(0, numExtensions):
            self.fixesExtData[line[3+(i*7+4):3+(i*7+7)]] = (int(line[3+(i*7):3+(i*7+2)]),
                                                            int(line[3+(i*7+2):3+(i*7+4)]))
            
    def handleJ(self, line):
        """
        IGC Record J - Extensions to the K Record.
        """
        numExtensions = int(line[1:3])
        for i in range(0, numExtensions):
            self.extensionsExtData[line[3+(i*7+4):3+(i*7+7)]] = (int(line[3+(i*7):3+(i*7+2)]),
                                                                 int(line[3+(i*7+2):3+(i*7+4)]))
    
    def handleK(self, line):
        """
        IGC Record K - Data needed less frequently than fixes.
        """
        point = {"UTC": self._parseTime(line[1:7])}
        for ext in self.extensionsExtData.keys():
            point[ext] = line[self.extensionsExtData[ext][0]-1:self.extensionsExtData[ext][1]]
        self.extensions.append(point)
    
    def handleL(self, line):
        """
        IGC Record L - Logbook / Comments. 
        """
        pass
    
    def analyze(self):
        self._computePhases()
        (self.flightProps["TKF"], self.flightProps["LDG"]) = (self._computeTakeoff(), 
                                                              self._computeLanding())
    
    def takeoff(self):
        return self.flightProps["TKF"]
    
    def landing(self):
        return self.flightProps["LDG"]

    def toKML(self):
        kml = """
            <kml xmlns='http://earth.google.com/kml/2.2'>
                <Document>
                    <Style id="task">
                        <LineStyle>
                            <width>1.5</width>
                            <color>ff0000ff</color>
                        </LineStyle>
                    </Style>
                    <Style id="path">
                        <LineStyle>
                            <width>0.5</width>
                            <color>ffff0000</color>
                        </LineStyle>
                    </Style>
              """
        pMarks = [("TAKEOFF", self.task["TKF"]), ("START", self.task["STA"])]
        for i, tns in enumerate(self.task["TNS"]):
            pMarks.append(("TP%d" % (i+1), tns))
        pMarks.extend([("FINISH", self.task["FIN"]), ("LANDING", self.task["LDG"])])
        taskPath = """
            <Placemark>
                <styleUrl>#task</styleUrl>
                <LineString>
                    <coordinates>"""
        for name, pnt in pMarks:
            if pnt[0] != 0 and pnt[1] != 0:
                kml += """
                    <Placemark>
                        <name>%s: %s</name><description></description>
                        <Point><coordinates>%s,%s</coordinates></Point>
                    </Placemark>
                    """ % (name, pnt[2], pnt[1], pnt[0])
                
                taskPath += "%s,%s " % (pnt[1], pnt[0])
        kml += """
                    %s</coordinates>
                </LineString>
            </Placemark>
            """ % taskPath
        
        kml += """
                    <Placemark>
                        <styleUrl>#path</styleUrl>
                        <LineString>
                            <altitudeMode>absolute</altitudeMode>
                            <coordinates>"""
        for pnt in self.fixes:
            kml += "%s,%s,%s " % (pnt["DFIX"][1], pnt["DFIX"][0], pnt["GALT"]) 
        kml += """
                            </coordinates>
                        </LineString>
                    </Placemark>
              </Document>
            </kml>"""
        return kml
    
    def _computePhases(self): 
        self.fixes[0]["T"], self.fixes[0]["TIMED"], self.fixes[1]["T"] = ("S", 0, "S")
        self.phases.append({"SF": self.fixes[0], "EF": None, "T": "S", "DIS": 0})
        for i in range(1, len(self.fixes), 1):
            curFix, prevFix = self.fixes[i], self.fixes[i-1]
            
            if self.phases[-1]["T"] == "S":
                j = 0
                while (curFix["UTC"] - self.fixes[i-j]["UTC"]).seconds < 45:
                    j += 1
                if i-j > 0 and self._distance(self.fixes[i-j]["DFIX"], curFix["DFIX"]) < 200:
                    self.phases[-1]["EF"] = self.fixes[i-j+1]
                    self.phases.append({"SF": self.fixes[i-j+1], "EF": None, "T": "C"})
                    i += j-1
            elif self.phases[-1]["T"] == "C":
                j = 0
                while (curFix["UTC"] - self.fixes[i-j]["UTC"]).seconds < 45:
                    j += 1
                if i-j> 0 and (self._distance(self.fixes[i-j]["DFIX"], curFix["DFIX"]) /
                    (curFix["UTC"] - self.fixes[i-j]["UTC"]).seconds) > 30:
                    self.phases[-1]["EF"] = self.fixes[i-j+1]
                    self.phases.append({"SF": self.fixes[i-j+1], "EF": None, "T": "S"})
                    i += 1
                
        self.phases[-1]["EF"] = self.fixes[-1]
        
    def _parseTime(self, str):
        if len(str) == 6:
            return datetime.strptime(str, "%H%M%S")
        else:
            return datetime.strptime(str, "%d%m%y %H%M%S")
    
    def _dms2Decimal(self, coord):
        if len(coord) == 8: # latitude
            deg, min = (float(coord[-8:-6]), float(coord[-6:-4]) + (float(coord[-4:-1])/1000))
        else: # longitude
            deg, min = (float(coord[-9:-6]), float(coord[-6:-4]) + (float(coord[-4:-1])/1000))
        value = deg + (min / 60)
        if coord[-1] == "S" or coord[-1] == "W":
            value = -value
        return value
    
    def _distance(self, pnt1, pnt2):
        """
        Uses the spherical law of cosines for the calculation.
        
        pnt1 and pnt2 should be tuples (lat, lon) with decimal coordinates.
        """
        pnt1 = (radians(pnt1[0]), radians(pnt1[1]))
        pnt2 = (radians(pnt2[0]), radians(pnt2[1]))
        return acos( (cos(pnt1[0]) * cos(pnt1[1]) * cos(pnt2[0]) * cos(pnt2[1]))
        + (cos(pnt1[0]) * sin(pnt1[1]) * cos(pnt2[0]) * sin(pnt2[1])) 
        + (sin(pnt1[0]) * sin(pnt2[0]))) * IGCParser.EARTH_RADIUS * 1000
        
    def _course(self, pnt1, pnt2):
        pnt1 = (radians(pnt1[0]), radians(pnt1[1]))
        pnt2 = (radians(pnt2[0]), radians(pnt2[1]))
        value = atan2(sin(pnt2[1]-pnt1[1])*cos(pnt2[0]),
                      cos(pnt1[0])*sin(pnt2[0])-sin(pnt1[0])*cos(pnt2[0])*cos(pnt2[1]-pnt1[1])) 
        return (degrees(value) + 360) % 360
    
    def _computeTakeoff(self):
        for i in range(1, len(self.fixes)):
            if self.fixes[i]["GALT"] > self.fixes[i-1]["GALT"]:
                return self.fixes[i-1]
            
    def _computeLanding(self):
        for i in range(1, len(self.fixes)):
            if self.fixes[-i]["GALT"] < self.fixes[-i-1]["GALT"]:
                return self.fixes[-i+1]

import sys
def main():
    igcFile = open(sys.argv[1], "r")
    parser = IGCParser(igcFile)
    parser.parse()
    igcFile.close()
    
    kmlFile = open("/tmp/test.kml", "w") 
    kmlFile.write(parser.toKML())
    kmlFile.close()
    
    points = [parser.task["STA"]]
    for turn in parser.task["TNS"]:
        points.append(turn)
    points.append(parser.task["FIN"])
    
    total = 0
    for i in range(1, len(points)):
        dist = parser._distance((points[i-1][0], points[i-1][1]), (points[i][0], points[i][1]))
        total += dist
    
    parser.analyze()
    
    print "%s :: %s :: %s" % (parser.takeoff()["UTC"], parser.landing()["UTC"],
                              (parser.landing()["UTC"] - parser.takeoff()["UTC"]).seconds)

    for phase in parser.phases:
        print "%2s :: %8s :: %8s :: %5s :: %5s :: %5s :: %5s" \
            % (phase["T"], phase["SF"]["UTC"].time(), phase["EF"]["UTC"].time(), 
               (phase["EF"]["UTC"] - phase["SF"]["UTC"]).seconds, phase["SF"]["GALT"],
               phase["EF"]["GALT"], (phase["EF"]["GALT"] - phase["SF"]["GALT"]))
    
if __name__ == "__main__":
    main()
