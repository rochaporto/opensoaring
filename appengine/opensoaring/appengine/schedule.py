import cgi
import logging
import random
import time

from datetime import datetime, timedelta
from django.utils import simplejson
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from opensoaring.model import Glider

class ScheduleHandler(webapp.RequestHandler):

    
    def get(self):
        members = ["Rocha R.", "Riegel F.", "Riegel J.", "Stilmant E.", 
               "Fert R.", "Rank A.", "Delobel P.", "Peillex G.", "Dury C."]
    
        startDate = datetime.fromtimestamp(float(self.request.get("startDate")))
        endDate = datetime.fromtimestamp(float(self.request.get("endDate")))
        callback = self.request.get("callback")
        
        schedEntries = []
        while startDate < endDate:
            newEntry = {"date": time.mktime(startDate.timetuple())}
            for memberType, num in [("INSTRUCTOR", 2), ("WINCHER", 2), ("TRIALPILOT", 2), 
                                    ("STUDENT", 5)]:
                newEntry[memberType] = []
                for j in range(0, random.randint(1, num)):
                    newEntry[memberType].append({"name": random.sample(members, 1)[0]})
            schedEntries.append(newEntry)
            startDate += timedelta(days=1)
            logging.info(newEntry)
        self.response.out.write("%s(%s)" % (callback, schedEntries))