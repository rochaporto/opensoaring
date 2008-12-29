import cgi
import logging

from django.utils import simplejson
from google.appengine.api import users
from google.appengine.ext import db, webapp
from google.appengine.ext.webapp.util import run_wsgi_app

from opensoaring.model import Glider, Member

class MemberHandler(webapp.RequestHandler):

    def get(self):
        dsMembers = Member.all()
        members = []
        for member in dsMembers:
            members.append({"email": member.email,
                            "firstName": member.firstName, "lastName": member.lastName})                            
        self.response.out.write(simplejson.dumps(members))
        
    def post(self):
        email = self.request.get("email")
        firstName = self.request.get("firstName")
        lastName = self.request.get("lastName")
        newMember = Member(email=email, firstName=firstName, lastName=lastName)
        newMember.put()

    def delete(self):
        email = self.request.get("email")
        member = Member.gql("WHERE email = :email", email=email)
        member.delete()
        
class GliderHandler(webapp.RequestHandler):

    def get(self):
        user = users.get_current_user()
        model = cgi.escape(self.request.get("model"))
        registration = cgi.escape(self.request.get("registration"))
        gliders = Glider.all()
        if model != "":
            gliders.filter("model =", model)
        if registration != "":
            gliders.filter("registration =", registration)
        gliders.order("model")
        gliders.order("registration")
        jsonGliders = []
        for glider in gliders:
            jsonGliders.append({"model": glider.model, "registration": glider.registration})
        self.response.out.write(simplejson.dumps(jsonGliders))
        
    def post(self):
        if not users.is_current_user_admin():
            self.response.set_status(402)
            return
        model = cgi.escape(self.request.get("model"))
        registration = cgi.escape(self.request.get("registration"))
        newGlider = Glider(model=model, registration=registration)
        newGlider.put()

    def delete(self):
        if not users.is_current_user_admin():
            self.response.set_status(402)
            return
        model = cgi.escape(self.request.get("model"))
        registration = cgi.escape(self.request.get("registration"))
        gliders = Glider.all()
        if model != "":
            gliders.filter("model =", model)
        if registration != "":
            gliders.filter("registration =", registration)
            
        for glider in gliders:
            glider.delete()      

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
        
application = webapp.WSGIApplication([("/glider", GliderHandler),
                                      ("/member", MemberHandler),
                                      ("/schedule", ScheduleHandler)
                                      ])

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()
