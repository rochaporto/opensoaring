#!/usr/bin/python
import os
from urllib.request import FancyURLopener
import sys
from optparse import OptionParser
from string import ascii_uppercase

BASE_URL = "https://www.sia.aviation-civile.gouv.fr/aip/enligne/PDF_AIPparSSection/VAC/AD/2/1002_AD-2.%s.pdf"

usage = "usage: %prog [options] directory"
description = """
Downloads all french VAC charts into the given directory.
<directory> must be an existing directory.
"""

http://docs.python.org/library/urllib2.html

parser = OptionParser(usage=usage, description=description, version="0.1a")
parser.add_option("-v", "--verbose", action="store_true", dest="verbose")
parser.add_option("-q", "--quiet", action="store_true", dest="quiet")

def main():
    (options, args) = parser.parse_args()
    if not len(args) == 1:
        parser.print_help()
        sys.exit(2)
    
    directory = args[0]
    if not os.path.isdir(directory):
        print "ERROR: Please provide an existing directory"
        parser.print_help()
        sys.exit(2)

    if not options.quiet:
        print "Fetching VAC charts"

    url_opener = FancyURLopener()
    for c1 in ascii_uppercase:
        for c2 in ascii_uppercase:
            icao_code = "LF%s%s" % (c1, c2)
            vac_url = BASE_URL % icao_code
            url_opener.retrieve(vac_url, filename="%s/%s.pdf" % (directory, icao_code))
                
            if options.verbose:
                print "Fetched VAC chart for %s" % icao_code
                print "( %s )" % vac_url
                print res
            elif not options.quiet:
                sys.stdout.write(".")
                sys.stdout.flush()
                
    if not options.quiet:
        print "\nDone."

if __name__ == "__main__":
    main()
