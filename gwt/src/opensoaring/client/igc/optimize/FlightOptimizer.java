package opensoaring.client.igc.optimize;

import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.Flight;

public interface FlightOptimizer {

	public Fix[] optimize(Flight flight);
	
	public String getDescription();
	
}
