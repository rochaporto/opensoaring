"""
Copyright (C) 2008 OpenSoaring <contact@opensoaring.info>.

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
import unittest

from opensoaring.api import OpenSoaring

class GliderTest(unittest.TestCase):
    
    client = OpenSoaring("http://localhost:8080")
    
    # Cleanup and insert test data
    client.user = "test@example.com:True"
    for model in ["OSGT1", "OSGT2", "OSGT3"]:
        client.removeGlider(model=model)
    
    tGliders = [{"model": "OSGT1", "registration": "OSG-01LIST"},
                {"model": "OSGT1", "registration": "OSG-02LIST"},
                {"model": "OSGT2", "registration": "OSG-03LIST"},
                {"model": "OSGT3", "registration": "OSG-04REM"},
                {"model": "OSGT3", "registration": "OSG-05REM"}]
    
    for glider in tGliders:
        client.addGlider(model=glider["model"], registration=glider["registration"])

    def setUp(self):
        self.client.user = "test@example.com:False"
        
    def tearDown(self):
        pass

    def testAddGliderNoAuth(self):
        self.client.user = "test@example.com:False"
        statusCode = self.client.addGlider(model="OSGTNA", registration="OSG-ADDNA")
        self.assertEquals(statusCode, 402)
        
        gliders = self.client.listGliders(model="OSGTNA", registration="OSG-ADDNA")
        self.assertEquals(len(gliders), 0)
    
    def testAddGliderNoModel(self):
        self.client.user = "test@example.com:True"
        statusCode = self.client.addGlider(registration="OSG-ADDNM")
        self.assertEquals(statusCode, 500)
        
        gliders = self.client.listGliders(registration="OSG-ADDNM")
        self.assertEquals(len(gliders), 0)
    
    def testAddGliderNoRegistration(self):
        self.client.user = "test@example.com:True"
        statusCode = self.client.addGlider(model="OSGTNR")
        self.assertEquals(statusCode, 500)
        
        gliders = self.client.listGliders(model="OSGTNR")
        self.assertEquals(len(gliders), 0)
            
    def testAddGliderNoAuth(self):
        self.client.user = "test@example.com:False"
        statusCode = self.client.addGlider(model="OSGT3", registration="OSG-06ADD")
        self.assertEquals(statusCode, 402)
        
        gliders = self.client.listGliders(model="OSGT3", registration="OSG-06ADD")
        self.assertEquals(len(gliders), 0)
            
    def testRemoveGlider(self):
        self.client.user = "test@example.com:True"
        self.client.removeGlider(model="OSGT3", registration="OSG-04REM")                                    
    
        gliders = self.client.listGliders(model="OSGT3", registration="OSG-04REM")
        self.assertEquals(len(gliders), 0)
    
    def testRemoveGliderNoAuth(self):
        self.client.user = "test@example.com:False"
        statusCode = self.client.removeGlider(model="OSGT3", registration="OSG-05REM")
        self.assertEquals(statusCode, 402)
        
        gliders = self.client.listGliders(model="OSGT3", registration="OSG-05REM")
        self.assertEquals(len(gliders), 1)
         
    def testListGlidersByModel(self):
        gliders = self.client.listGliders(model="OSGT1")
        self.assertEquals(len(gliders), 2)
        for i, glider in enumerate(self.tGliders[0:1]):
            for key in glider.keys():
                self.assertEquals(glider[key], gliders[i][key])
        
        gliders = self.client.listGliders(model="OSGT2")
        self.assertEquals(len(gliders), 1)
        for i, glider in enumerate(self.tGliders[2:2]):
            for key in glider.keys():
                self.assertEquals(glider[key], gliders[i][key])

    def testListGlidersByRegistration(self):
        gliders = self.client.listGliders(registration="OSG-03LIST")
        self.assertEquals(len(gliders), 1)
        for i, glider in enumerate(self.tGliders[3:3]):
            for key in glider.keys():
                self.assertEquals(glider[key], gliders[i][key])        
        
class MemberTest(unittest.TestCase):
    
    client = OpenSoaring("http://localhost:8080")
    
    # Cleanup and insert test data
    client.user = "test@example.com:True"    
    tMembers = [{"email": "OSM-01@opensoaring.info", "firstName": "OSM-01-FN",
                 "lastName": "OSM-01-LN"}]
    
    for member in tMembers:
        client.addMember(email=member["email"], firstName=member["firstName"],
                         lastName=member["lastName"])

    def setUp(self):
        self.client.user = "test@example.com:False"
        
    def tearDown(self):
        pass
    
    def testListMembers(self):
        members = self.client.listMembers(email="OSM-01@opensoaring.info")
        self.assertEquals(len(members), 1)
        
        for key in self.tMembers[0].keys():
            self.assertEquals(self.tMembers[0][key], members[0][key])