from datetime import datetime
from django.db import models

class Role(models.Model):
    name = models.CharField(max_length=30)

class Pilot(models.Model):
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    birth_date = models.DateField()
    country = models.CharField(max_length=100)
    roles = models.ManyToManyField(Role)
    def __unicode__(self):
        return "%s, %s" % (self.last_name, self.first_name)

class LicenseType(models.Model):
    type = models.CharField(max_length=100)
    def __unicode__(self):
        return self.type

class License(models.Model):
    member = models.ForeignKey(Pilot)
    license_type = models.ForeignKey(LicenseType)
    number = models.CharField(max_length=100)
    expiration_date = models.DateField()
    def __unicode__(self):
        return self.number

class MachineType(models.Model):
    type = models.CharField(max_length=10)
    def __unicode__(self):
        return self.type

class PlaneModel(models.Model):
    constructor = models.CharField(max_length=100)
    country = models.CharField(max_length=10)
    name = models.CharField(max_length=100)
    type = models.ForeignKey(MachineType)
    def __unicode__(self):
        return self.name

class Plane(models.Model):
    registration = models.CharField(max_length=10)
    model = models.ForeignKey(PlaneModel)
    year = models.IntegerField()
    def __unicode__(self):
        return self.registration

class Flight(models.Model):
    takeoff_date = models.DateTimeField("takeoff")
    landing_date = models.DateTimeField("landing")
    duration = models.IntegerField()
    plane = models.ForeignKey(Plane)
    pilot = models.ForeignKey(Pilot)
    
