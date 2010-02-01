from django.http import HttpResponse, Http404
from django.shortcuts import render_to_response

from opensoaring.logbook.models import Flight

def index(request):
    flight_list = Flight.objects.all().order_by("-takeoff_date")
    return render_to_response("logbook/flights/index.html", {"flight_list": flight_list})

def detail(request, flight_id):
    try:
        Flight.objects.get(pk=flight_id)
    except Flight.DoesNotExist:
        raise Http404
    return HttpResponse("Details for flight %s" % flight_id)
