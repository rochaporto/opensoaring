import cgi
import logging

from django.utils import simplejson
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from opensoaring.model import Glider

class GliderHandler(webapp.RequestHandler):

    def get(self):
        gliders = Glider.all()
        jsonGliders = []
        for glider in gliders:
            jsonGliders.append({"imat": glider.imat, "finesse": glider.finesse})
        self.response.out.write(simplejson.dumps(jsonGliders))

    def post(self):
        imat = cgi.escape(self.request.get("imat"))
        finesse = cgi.escape(self.request.get("finesse"))
        newGlider = Glider(imat=imat, finesse=int(finesse))
        newGlider.put()

    def delete(self):
        imat = cgi.escape(self.request.get("imat"))
        imatGliders = Glider.gql("WHERE imat = :imat", imat=imat)
        for glider in imatGliders:
            glider.delete()
