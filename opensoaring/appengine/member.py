import logging

from django.utils import simplejson
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from opensoaring.model import Member

class MemberHandler(webapp.RequestHandler):

    def get(self):
        members = Member.all()
        jsonMembers = []
        for member in members:
            jsonMembers.append({"user": member.user})
        self.response.out.write(simplejson.dumps(jsonMembers))
