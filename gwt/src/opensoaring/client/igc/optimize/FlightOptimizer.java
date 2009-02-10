/*
 * Copyright (C) 2009 OpenSoaring <contact@opensoaring.info>.
 * 
 * This file is part of OpenSoaring.
 *
 * OpenSoaring is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenSoaring is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenSoaring.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package opensoaring.client.igc.optimize;

/**
 * http://github.com/twpayne/maxxc/blob/6a6e779c1dec2a597fed90116af551332a942ee0/track.c
 * ( full code : http://github.com/twpayne/maxxc/tree/master )
 * 
 * http://freenet-homepage.de/streckenflug/optigc.html
 */
import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;

public interface FlightOptimizer {

	public Fix[] optimize();
	
	public String getDescription();
	
}
