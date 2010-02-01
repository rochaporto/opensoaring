from django.contrib import admin
from opensoaring.logbook.models import Flight, License, LicenseType, Pilot, Plane, PlaneModel, MachineType

class LicenseInline(admin.TabularInline):
    model = License
    extra = 1
    
class PilotAdmin(admin.ModelAdmin):
    fieldsets = [
        ("Personal Information", {"fields": ["first_name", "last_name", "country", "birth_date"]}),
    ]
    inlines = [LicenseInline]
    list_display = ["first_name", "last_name", "country", "birth_date"]
    search_fields = ["first_name", "last_name"]

class FlightAdmin(admin.ModelAdmin):
    list_display = ["plane", "pilot", "takeoff_date", "landing_date"]
    date_hierarchy = "takeoff_date"
    list_filter = ["takeoff_date", "pilot"]

admin.site.register(Flight, FlightAdmin)
admin.site.register(Pilot, PilotAdmin)
admin.site.register(Plane)
admin.site.register(PlaneModel)
admin.site.register(MachineType)
