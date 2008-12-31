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
from google.appengine.ext import db

class Member(db.Model):
    user = db.UserProperty()
    email = db.StringProperty(required=True)
    firstName = db.StringProperty(required=True)
    lastName = db.StringProperty(required=True)

class Glider(db.Model):
    model = db.StringProperty(required=True)
    registration = db.StringProperty(required=True)
    picture = db.LinkProperty()
    
class ScheduleEntry(db.Model):
    date = db.DateProperty(required=True)
    member = db.ReferenceProperty(Member, required=True)
    period = db.IntegerProperty(required=True)
    role = db.StringProperty()    