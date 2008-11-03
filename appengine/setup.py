import os

from distutils.cmd import Command
from distutils.core import setup

class dev(Command):
    description = "launches the google app engine dev server"
    user_options = [("appengine=", "a", "location of the google app engine dev libraries")]
    
    def initialize_options(self):
        self.appengine = None
        
    def finalize_options(self):
        pass
    
    def run(self):
        print "[INFO] Using Google App Engine Libraries: %s" % self.appengine
        os.system("%s/dev_appserver.py ." % self.appengine)

class upload(Command):
    description = "uploads the application to google app engine"
    
    user_options = [("appengine=", "a", "location of the google app engine dev libraries")]
    
    def initialize_options(self):
        self.appengine = None
        
    def finalize_options(self):
        pass
    
    def run(self):
        print "[INFO] Using Google App Engine Libraries: %s" % self.appengine
        os.system("%s/appcfg.py update ." % self.appengine)
            
setup(cmdclass={"dev": dev, "upload": upload},
      name="opensoaring-appengine",
      packages=['opensoaring', 'opensoaring.appengine'],
      package_dir={'': ''},
)