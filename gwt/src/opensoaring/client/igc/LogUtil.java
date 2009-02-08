package opensoaring.client.igc;

import opensoaring.client.igc.flight.Fix;

/**
 * A set of utility methods for IGC related information management.
 * 
 * It includes functionality for calculating great circle distances from two geographical
 * coordinates, speed between two geographical coordinates, etc.
 * 
 * @author Ricardo Rocha <rocha@opensoaring.info>
 *
 */
public class LogUtil {

	/**
	 * The mean Earth radius.
	 */
	public static int EARTH_RADIUS = 6371;
	
	/**
	 * Calculates the great circle distance between two geographical coordinates, taking into
	 * account the given Earth radius.
	 * 
	 * @param from The origin coordinate
	 * @param to The destination coordinate
	 * @param earthRadius The Earth radius to be considered for the calculation
	 * @return The great circle distance between the two given coordinates
	 */
	public static double distance(Fix from, Fix to, double earthRadius) {
        if (!LogUtil.isSameLocation(from, to)) {
            return Math.acos(Math.sin(from.getLongitudeRadians()) * Math.sin(to.getLongitudeRadians()) +
                    Math.cos(from.getLongitudeRadians()) * Math.cos(to.getLongitudeRadians()) 
                    * Math.cos(to.getLatitudeRadians() - from.getLatitudeRadians())) 
                    * LogUtil.EARTH_RADIUS;
        }
        return 0;
	}
	
	/**
	 * Calculates the great circle distance between two geographical coordinates, taking into
	 * account the mean Earth radius.
	 * 
	 * @param from The origin coordinate
	 * @param to The destination coordinate
	 * @return The great circle distance between the two given coordinates
	 */
	public static double distance(Fix from, Fix to) {
		return LogUtil.distance(from, to, LogUtil.EARTH_RADIUS);
	}
	
	/**
	 * The speed calculated to go from a given point to another, taking into account the time
	 * different between both and the great circle distance between them.
	 * 
	 * @param from The origin coordinate
	 * @param to The destination coordinate
	 * @return The speed (in km/h) calculate to go from origin to destination
	 */
	public static double groundSpeed(Fix from, Fix to) {
		return (LogUtil.distance(from, to) 
				/ ((to.getTime().getTime() - from.getTime().getTime()) / 1000)) * 3600;
	}
	
	/**
	 * Tests if the two given points are at the same location (latitude, longitude and altitude).
	 * 
	 * @param fix1 The first point
	 * @param fix2 The second point
	 * @return True if the two points are at the same location, False otherwise
	 */
	public static boolean isSameLocation(Fix fix1, Fix fix2) {
		if (fix1.getLatitude() == fix2.getLatitude()
				&& fix1.getLongitude() == fix2.getLongitude()
				&& fix1.getGnssAltitude() == fix2.getGnssAltitude()) {
			return true;
		}
		return false;
	}
	 
	/**
	 * Returns the course (heading) from one Fix (point) to another.
	 * 
	 * @param from The start point
	 * @param to The end point
	 * @return The course (in degrees) to get from the given first point to the second one
	 */
	public static double course(Fix from, Fix to) {
		double fromLat = from.getLatitudeRadians();
		double fromLon = from.getLongitudeRadians();
		double toLat = to.getLatitudeRadians();
		double toLon = to.getLongitudeRadians();
		
		double course = Math.atan2(
				(
						(Math.cos(fromLat) * Math.sin(toLat)) 
						- (Math.sin(fromLat) * Math.cos(toLat) * Math.cos(toLon - fromLon))
				), Math.sin(toLon - fromLon) * Math.cos(toLat));

		return (Math.toDegrees(course) + 360) % 360;
	}
	
}
