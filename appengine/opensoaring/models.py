from appengine_django.models import BaseModel
from google.appengine.ext import db

class Poll(BaseModel):
    question = db.StringProperty()
    pub_date = db.DateTimeProperty('date published')

class Choice(BaseModel):
    poll = db.ReferenceProperty(Poll)
    choice = db.StringProperty()
    voice = db.IntegerProperty()

