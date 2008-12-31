"""
Copyright (C) 2009 OpenSoaring <contact@opensoaring.info>.

This file is part of OpenSoaring.

OpenSoaring is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

OpenSoaring is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with OpenSoaring.  If not, see <http://www.gnu.org/licenses/>.
"""
import imp
import os
import re
import sys
import unittest

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
            
class test(Command):    
    description = "executes all tests of this application"
    user_options = []
    
    def initialize_options(self):
        pass
        
    def finalize_options(self):
        pass
    
    def run(self):
        from opensoaring.test import GliderTest, MemberTest
        suites = [unittest.makeSuite(GliderTest), unittest.makeSuite(MemberTest)]
        unittest.TextTestRunner(verbosity=2).run(unittest.TestSuite(suites))
        
setup(cmdclass={"dev": dev, "upload": upload, "test": test},
      name="opensoaring-appengine",
      packages=['opensoaring', 'opensoaring.appengine'],
      package_dir={'': ''},
)
