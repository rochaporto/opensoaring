"""
Copyright (C) 2008 OpenSoaring <contact@opensoaring.info>.

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
import pycurl
import simplejson
import urllib

from StringIO import StringIO

class OpenSoaring(object):
    
    def __init__(self, endpoint):
        self.endpoint = endpoint
        self.user = None
    
    def addGlider(self, model=None, registration=None):
        curlObj = pycurl.Curl()
        curlObj.setopt(pycurl.URL, "%s/glider" % self.endpoint)
        curlObj.setopt(pycurl.POST, 1)
        curlObj.setopt(pycurl.HTTPPOST, [("model", "" if model is None else model), 
                                ("registration", "" if registration is None else registration)])
        curlObj.setopt(pycurl.COOKIE, "dev_appserver_login=%s" % self.user)
        curlObj.perform()
        return curlObj.getinfo(pycurl.HTTP_CODE)

    def removeGlider(self, model=None, registration=None):
        curlObj = pycurl.Curl()
        curlObj.setopt(pycurl.URL, "%s/glider?%s" 
                       % (self.endpoint, 
                          urllib.urlencode([("model", "" if model is None else model), 
                                ("registration", "" if registration is None else registration)])))
        curlObj.setopt(pycurl.COOKIE, "dev_appserver_login=%s" % self.user)
        curlObj.setopt(pycurl.CUSTOMREQUEST, "DELETE")
        curlObj.perform()
        return curlObj.getinfo(pycurl.HTTP_CODE)
        
    def listGliders(self, model=None, registration=None):
        result = StringIO()
        curlObj = pycurl.Curl()
        curlObj.setopt(pycurl.URL, "%s/glider?%s" 
                       % (self.endpoint,
                          urllib.urlencode([("model", "" if model is None else model), 
                                ("registration", "" if registration is None else registration)])))
        curlObj.setopt(pycurl.COOKIE, "dev_appserver_login=%s" % self.user)
        curlObj.setopt(pycurl.WRITEFUNCTION, result.write)
        curlObj.perform()
        return simplejson.loads(result.getvalue())
    
    def addMember(self, email=None, firstName="", lastName=""):
        curlObj = pycurl.Curl()
        curlObj.setopt(pycurl.URL, "%s/member" % self.endpoint)
        curlObj.setopt(pycurl.POST, 1)
        curlObj.setopt(pycurl.HTTPPOST, [("email", "" if email is None else email), 
                                         ("firstName", "" if firstName is None else firstName),
                                         ("lastName", "" if lastName is None else lastName)])
        curlObj.setopt(pycurl.COOKIE, "dev_appserver_login=%s" % self.user)
        curlObj.perform()
        return curlObj.getinfo(pycurl.HTTP_CODE)
    
    def removeMember(self, email=None):
        curlObj = pycurl.Curl()
        curlObj.setopt(pycurl.URL, "%s/member?%s" 
                       % (self.endpoint, 
                          urllib.urlencode([("email", "" if email is None else email)])))
        curlObj.setopt(pycurl.COOKIE, "dev_appserver_login=%s" % self.user)
        curlObj.setopt(pycurl.CUSTOMREQUEST, "DELETE")
        curlObj.perform()
        return curlObj.getinfo(pycurl.HTTP_CODE)
    
    def listMembers(self, email=None):
        result = StringIO()
        curlObj = pycurl.Curl()
        curlObj.setopt(pycurl.URL, "%s/member?%s" 
                       % (self.endpoint,
                          urllib.urlencode([("email", "" if email is None else email)])))
        curlObj.setopt(pycurl.COOKIE, "dev_appserver_login=%s" % self.user)
        curlObj.setopt(pycurl.WRITEFUNCTION, result.write)
        curlObj.perform()
        return simplejson.loads(result.getvalue())