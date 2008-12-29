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
        curlObj.setopt(pycurl.HTTPPOST, [("model", model), ("registration", registration)])
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
    