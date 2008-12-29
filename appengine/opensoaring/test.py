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
        