#!/usr/bin/python
import commands
import os
import urllib2
import shutil
import sys
from optparse import OptionParser
from string import ascii_uppercase

VAC_ALL_FILE = "vac_all.pdf"
BASE_URL = "https://www.sia.aviation-civile.gouv.fr/aip/enligne/PDF_AIPparSSection/VAC/AD/2/1002_AD-2.%s.pdf"
DW_ICAO_LIST = ["LFAB", "LFAC"]

usage = "usage: %prog [options] directory"
description = """
Downloads all french VAC charts into the given directory.
<directory> must be an existing directory.
"""

parser = OptionParser(usage=usage, description=description, version="0.1a")
parser.add_option("-a", "--all", action="store_true", dest="all")
parser.add_option("-m", "--merge", action="store_true", dest="merge")
parser.add_option("-v", "--verbose", action="store_true", dest="verbose")
parser.add_option("-q", "--quiet", action="store_true", dest="quiet")

def main():
    (options, args) = parser.parse_args()
    if not len(args) == 1:
        parser.print_help()
        sys.exit(2)
    
    directory = args[0]
    if os.path.isdir(directory):
        overwrite_ok = raw_input("Will delete/recreate directory '%s'... continue? [Y/N] " % directory).upper()
        while overwrite_ok not in ["Y", "N"]:
            overwrite_ok = raw_input("Will delete/recreate directory '%s'... continue? [Y/N] " % directory).upper()
        if not overwrite_ok == "Y":
            fail("Canceled by user")
    shutil.rmtree(directory)
    os.makedirs(directory)

    icao_code_list = DW_ICAO_LIST
    info("Fetching VAC charts", options)
    if options.all:
        icao_code_list = []
        for c1 in ascii_uppercase:
            for c2 in ascii_uppercase:
                icao_code_list.append("LF%s%s" % (c1, c2))
    for icao_code in icao_code_list:
        vac_url = BASE_URL % icao_code
        try:
            resp = urllib2.urlopen(vac_url)
            vac_file = "%s/%s.pdf" % (directory, icao_code)
            vac_fileh = open(vac_file, "w")
            vac_fileh.write(resp.read())
            resp.close()
            vac_fileh.close()
        except urllib2.HTTPError, exc:
            if exc.code == 404:
                continue
            fail("Could not retrieve data: %s" % vac_url)
        except urllib2.URLError, exc:
            fail("Could not retrieve data: %s" % vac_url)
        except IOError, exc:
            fail("Failed to write to file: %s" % vac_file)

        if options.verbose:
            info("Fetched VAC chart for %s" % icao_code, options, True)
            info("( %s )" % vac_url, options, True)
        elif not options.quiet:
            sys.stdout.write(".")
            sys.stdout.flush()
    info("Done.", options)

    if options.merge:
        info("Merging all VAC charts into one pdf file...", options)
        cmd = "gs -dBATCH -dNOPAUSE -q -sDEVICE=pdfwrite -sOutputFile=%s %s" \
              % ("%s/%s" % (directory, VAC_ALL_FILE), " ".join(map(lambda c: "%s/%s.pdf" % (directory, c), icao_code_list)))
        info(cmd, options, True)
        (code, output) = commands.getstatusoutput(cmd)
        if code != 0:
            fail("Failed to generate all VAC charts pdf file...\n%s" % output)
        
def info(msg, options, verbose=False):
    if (not verbose and not options.quiet) or (verbose and options.verbose):
        print "INFO: %s" % msg

def fail(msg):
    print >> sys.stderr, "ERROR: %s" % msg
    sys.exit(2)

if __name__ == "__main__":
    main()
