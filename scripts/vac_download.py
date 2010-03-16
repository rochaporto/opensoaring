#!/usr/bin/python
import commands
import os
import urllib2
import shutil
import sys
from optparse import OptionParser
from string import ascii_uppercase

VAC_ALL_FILE = "vac_all"
BASE_URL = "https://www.sia.aviation-civile.gouv.fr/aip/enligne/PDF_AIPparSSection/VAC/AD/2/1004_AD-2.%s.pdf"
DW_ICAO_LIST = ["LFHE","LFHH","LFHI","LFHJ","LFHM","LFHN","LFHS","LFHU","LFHV","LFHW","LFHZ","LFJD","LFJF","LFKA","LFKE","LFKD","LFKH","LFKL","LFKP","LFKR","LFKX","LFKY","LFLB","LFLE","LFLG","LFLH","LFLI","LFLJ","LFLK","LFLL","LFLM","LFLP","LFLR","LFLS","LFLU","LFLY","LFNC","LFXA"]

usage = "usage: %prog [options] directory"
description = """
Downloads all french VAC charts into the given directory.
<directory> must be an existing directory.
"""

parser = OptionParser(usage=usage, description=description, version="0.1a")
parser.add_option("-a", "--all", action="store_true", dest="all")
parser.add_option("-m", "--merge", action="store_true", dest="merge")
parser.add_option("-b", "--merge-better", action="store_true", dest="merge_better")
parser.add_option("-5", "--a5", action="store_true", dest="a5")
parser.add_option("-v", "--verbose", action="store_true", dest="verbose")
parser.add_option("-q", "--quiet", action="store_true", dest="quiet")

def main():
    """ Runs the show. """
    (options, args) = parser.parse_args()
    if not len(args) == 1:
        parser.print_help()
        sys.exit(2)
    
    # Check / scratch the destination directory
    directory = args[0]
    check_dir(directory, options)

    # Use the default ICAO list unless --all is given
    icao_code_list = DW_ICAO_LIST
    info("Fetching VAC charts", options)
    if options.all:
        icao_code_list = []
        for c1 in ascii_uppercase:
            for c2 in ascii_uppercase:
                icao_code_list.append("LF%s%s" % (c1, c2))

    for icao_code in icao_code_list:
        fetch_vac(icao_code, directory, options)

    # Merge into a single pdf file if requested
    if options.merge or options.merge_better:
        merge_vac(icao_code_list, directory, options)
        
def fetch_vac(icao_code, directory, options):
    """ Download the VAC chart corresponding to icao_code to directory. """
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
            return
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

def merge_vac(icao_code_list, directory, options):
    """ Merges pages corresponding to icao_code_list in directory to a single pdf file. """
    vac_all_file = "%s/%s.pdf" % (directory, VAC_ALL_FILE)
    vac_a5_file = "%s/%s.a5.pdf" % (directory, VAC_ALL_FILE)
    start_page = 1 if options.merge_better else 0
    info("Merging all VAC charts into one pdf file (%s)..." % vac_all_file, options)
    from pyPdf import PdfFileWriter, PdfFileReader
    out_pdf = PdfFileWriter()
    for icao_code in icao_code_list:
        in_file = open("%s/%s.pdf" % (directory, icao_code), "rb")
        in_pdf = PdfFileReader(in_file)
        in_pdf.decrypt("")
        for i in range(start_page, in_pdf.numPages):
            out_pdf.addPage(in_pdf.getPage(i))
    out_file = open("%s/%s.pdf" % (directory, VAC_ALL_FILE), "wb")
    out_pdf.write(out_file)
    out_file.close()
    if options.a5:
        info("Converting merged file to 2x1 A5 in A4 format (%s)..." % vac_a5_file, options)
        (status, output) = commands.getstatusoutput("pdfnup %s/%s.pdf --outfile %s/%s.a5.pdf" 
                                                    % (directory, VAC_ALL_FILE, directory, VAC_ALL_FILE))
        if status != 0:
            fail("Failed to convert merged file to 2x1 A5 in A4 format.\n%s" % output)

def check_dir(directory, options):
    """ Checks if directory exists, if yes recreates it. """
    if os.path.isdir(directory):
        overwrite_ok = raw_input("Will delete/recreate directory '%s'... continue? [Y/N] " % directory).upper()
        while overwrite_ok not in ["Y", "N"]:
            overwrite_ok = raw_input("Will delete/recreate directory '%s'... continue? [Y/N] " % directory).upper()
        if not overwrite_ok == "Y":
            fail("Canceled by user")
        shutil.rmtree(directory)
    os.makedirs(directory)

def info(msg, options, verbose=False):
    """ Prints some info to the user, taking verbosity into account. """
    if (not verbose and not options.quiet) or (verbose and options.verbose):
        print "INFO: %s" % msg

def fail(msg):
    """ Prints error info into stdree, and exits. """
    print >> sys.stderr, "ERROR: %s" % msg
    sys.exit(2)

if __name__ == "__main__":
    main()
