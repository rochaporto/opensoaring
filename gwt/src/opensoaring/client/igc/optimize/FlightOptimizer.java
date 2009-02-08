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

	public Fix[] optimize(Flight flight);
	
	public String getDescription();
	
}
