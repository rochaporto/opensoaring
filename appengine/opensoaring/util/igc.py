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

class IGCParser(object):
    
    def __init__(self, data):
        self.data = data
        self.deviceProps = {}
        self.flightProps = {}
        self.events = []
        self.points = []
        self.satellites = []
        
    def parse(self):
        if self.data is None:
            return
        
        for line in self.data:
            try:                
                handleMethod = getattr(self, "handle%s" % line[0])
                handleMethod(line)
            except AttributeError:
                pass
            
    def handleA(self, line):
        self.deviceProps["MAN"] = line[1:4]
        self.deviceProps["UID"] = line[4:7]
        self.deviceProps["EXT"] = line[7:].strip()
            
    def handleB(self, line):
        self.points.append({"TIME": line[1:7], "LAT": line[7:15], "LON": line[15:24],
                            "FXA": line[25], "PALT": line[26:31], "GALT": line[31:36]})
    
    def handleE(self, line):
        self.events.append({"TIME": line[1:7], "TLC": line[7:10], "STR": line[10:].strip()})
        
    def handleF(self, line):
        sats = []
        for i in range(7, len(line), 2):
            sats.append(line[i:i+2])
        self.satellites.append({"TIME": line[1:7], "SATS": sats})
        
    def handleH(self, line):
        if line.find(":") == -1:
            self.flightProps[line[2:5]] = line[5:].strip()
        else: 
            self.flightProps[line[2:5]] = line.split(":")[1].strip()
        
            
def main():
    igcFile = open("/home/rocha/tmp/igc/88dz5kv1.igc", "r")
    parser = IGCParser(igcFile)
    parser.parse()
    
    print parser.deviceProps
    print parser.flightProps
    print len(parser.points)
    print parser.events
    print parser.satellites

if __name__ == "__main__":
    main()
