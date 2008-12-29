from google.appengine.ext import db

class Member(db.Model):
    user = db.UserProperty()
    email = db.StringProperty(required=True)
    firstName = db.StringProperty(required=True)
    lastName = db.StringProperty(required=True)

class Glider(db.Model):
    model = db.StringProperty()
    registration = db.StringProperty()
    picture = db.LinkProperty()
    
class ScheduleEntry(db.Model):
    date = db.DateProperty(required=True)
    member = db.ReferenceProperty(Member, required=True)
    period = db.IntegerProperty(required=True)
    role = db.StringProperty()    