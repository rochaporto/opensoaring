from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from opensoaring.appengine.glider import GliderHandler
from opensoaring.appengine.member import MemberHandler

application = webapp.WSGIApplication([("/glider", GliderHandler),
                                      ("/member", MemberHandler)])

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()
