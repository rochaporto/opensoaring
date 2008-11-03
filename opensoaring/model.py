from google.appengine.ext import db

class Glider(db.Model):
    imat = db.StringProperty()
    model = db.StringProperty()
    finesse = db.IntegerProperty()

class Member(db.Model):
    user = db.UserProperty()

class Presence(db.Model):
    date = db.DateTimeProperty()
    member = db.ReferenceProperty(Member)
    roles = db.StringListProperty()
