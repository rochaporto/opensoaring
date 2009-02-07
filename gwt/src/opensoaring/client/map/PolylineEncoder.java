package opensoaring.client.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;

import opensoaring.client.igc.flight.Fix;

/**
 * An encoder for Google Maps Polylines.
 * 
 * It implements the encoding algorithm defined by the API, and described here:
 * http://code.google.com/apis/maps/documentation/polylinealgorithm.html
 * 
 * The documentation in this link is far from complete, a much better source of information
 * is the implementation of a similar encoder in Javascript, described here:
 * http://facstaff.unca.edu/mcmcclur/GoogleMaps/EncodePolyline/
 * 
 * which includes a very good description on how the different bits and pieces are expected
 * to work. It also additionally implements the Douglas-Peucker algorithm which simplifies the
 * path by removing less important points (this implementation follows the same algorithm).
 * A good description is available here:
 * http://en.wikipedia.org/wiki/Ramer-Douglas-Peucker_algorithm
 * 
 * although this implementation relies on a stack instead of doing the work recursively.
 * 
 * @author Ricardo Rocha <rocha@opensoaring.info>
 *
 */
public class PolylineEncoder {

	/**
	 * The magnification factor for the encoded levels.
	 */
	private int zoomFactor;
	
	/**
	 * The number of levels to spread the points in.
	 */
	private int numLevels;
	
	/**
	 * The tolerance value to use in the simplification (less than this means it is ignored).
	 */
	private double tolerance;
	
	/**
	 * An interval variable mapping each level (the index of the array) to a zoomFactor value. 
	 */
	private double[] zoomBreaks;
	
	/**
	 * Class construtor.
	 * 
	 * It assigns default values to zoomFactor (2), numLevels (18) and tolerance (0.0001).
	 */
	public PolylineEncoder() {
		this(2, 18, 0.0001);
	}
	
	/**
	 * 
	 * @param coordinate
	 * @return
	 */
	private int floor1e5(double coordinate) {
		return (int) Math.floor(coordinate * 1e5);
	}

	/**
	 * Class constructor.
	 * 
	 * @param zoomFactor The magnification factor for the encoded levels
	 * @param numLevels The number of levels to spread the points in
	 * @param tolerance The tolerance value to use in the simplification 
	 * (less than this means it is ignored)
	 */
	public PolylineEncoder(int zoomFactor, int numLevels, double tolerance) {
		this.numLevels = numLevels;
		this.zoomFactor = zoomFactor;
		this.tolerance = tolerance;
		
		// Generate the zoom factor values for each level for later use
		zoomBreaks = new double[numLevels];
		for (int i=0; i<numLevels; i++) {
			zoomBreaks[i] = tolerance * Math.pow(zoomFactor, numLevels-i-1);
		}
	}
	
	/**
	 * Encodes a signed number into a ASCII string according to the Google Maps encoding algorithm.
	 * 
	 * @param signedNumber The number to be encoded
	 * @return A string with the value encoded in ASCII
	 */
	private String encodeSignedNumber(int signedNumber) {
		int shiftedNumber = signedNumber << 1;

		if (signedNumber < 0) {
			shiftedNumber = ~(shiftedNumber);
		}
		return encodeNumber(shiftedNumber);
	}

	/**
	 * Encodes an unsigned number into a ASCII string according to the 
	 * Google Maps encoding algorithm.
	 * 
	 * @param unsignedNumber The unsigned number to be encoded
	 * @return A string with the value encoded in ASCII
	 */
	private String encodeNumber(int unsignedNumber) {

		String encodeString = "";

		while (unsignedNumber >= 0x20) {
			encodeString += (char) ((0x20 | (unsignedNumber & 0x1f)) + 63);
			unsignedNumber >>= 5;
		}

		encodeString += (char) (unsignedNumber + 63);

		return encodeString;
	}

	/**
	 * Generates both the encoded points list and the encoded levels list.
	 * 
	 * @param fixes The complete list of points which should be (potentially) encoded (the actual
	 * points encoded are the ones passed in the pointsToKeep list)
	 * @param pointsToKeep A list of integers with the indexes of the points to be encoded
	 * @param distances A mapping between the points given in pointsToKeep and the distances to
	 * the corresponding line segment, calculated in method simplify()
	 * @return An EncodedPath object containing both the encoded points and encoded levels
	 */
	private EncodedPath createEncodings(Fix[] fixes, ArrayList<Integer> pointsToKeep,
			HashMap<Integer,Double> distances) {

        String encodedPoints = "";
        String encodedLevels = "";

        int plat = 0;
        int plng = 0;

        for (Integer i: pointsToKeep) {

            int late5 = floor1e5(fixes[i].getLatitude());
            int lnge5 = floor1e5(fixes[i].getLongitude());

            int dlat = late5 - plat;
            int dlng = lnge5 - plng;

            plat = late5;
            plng = lnge5;

            encodedPoints += encodeSignedNumber(dlat) + encodeSignedNumber(dlng);
            
            int level = 0;
            if (distances.get(i) > 0) {
	            while (distances.get(i) < zoomBreaks[level]) {
	            	++level;
	            }
            }
            encodedLevels += encodeNumber(17-level);

        }
        
        return new EncodedPath(encodedPoints, encodedLevels);        
	}
	
	/**
	 * Encodes the given list of fixes (points) into a string of encoded points and a string of
	 * encoded levels, according to the Google Maps encoding algorithm.
	 * 
	 * @param fixes The list of fixes to be encoded
	 * @return An EncodedPath object containing both the encoded points and encoded levels
	 */
	public EncodedPath encode(Fix[] fixes) {
		// Reinitialize the local variables for points to keep and distances to segment
		ArrayList<Integer> pointsToKeep = new ArrayList<Integer>();
		HashMap<Integer,Double> distances = new HashMap<Integer,Double>();
		
		int start = 0;
		int end = fixes.length-1;
		
		// We always keep the first and last (and assign a distance of 0 so that
		// are always visible)
		pointsToKeep.add(0);
		distances.put(0, 0.0);
		pointsToKeep.add(end);
		distances.put(end, 0.0);
		
		// Make sure the first and the last point used from here on are not the same
	    while (fixes[start].getLatitude() == fixes[end].getLatitude() &&
	    		fixes[start].getLongitude() == fixes[end].getLongitude()) {
	        --end;
	    }

		// Apply the Douglas-Peucker algorithm (and collect the distances for calculating levels)
		encode(fixes, 0, fixes.length-1, pointsToKeep, distances);
		
		// Sort out the points 
		Collections.sort(pointsToKeep);
		
		// Create the encodings and return
		EncodedPath encodedPath = createEncodings(fixes, pointsToKeep, distances);
		return encodedPath;
	}
	
	/**
	 * The implementation of the Douglas-Peucker algorithm.
	 * 
	 * @param fixes The complete list of fixes (points)
	 * @param start The index of the start point of the segment we are looking at now
	 * @param end The index of the end point of the segment we are looking at now 
	 * @param pointsToKeep The current list of points to keep (to which we might add one)
	 * @param distances The current distances corresponding to pointsToKeep 
	 * (to which we might add one)
	 */
	private void encode(Fix[] fixes, int start, int end, 
			ArrayList<Integer> pointsToKeep, HashMap<Integer,Double> distances) {

		double maxDistance = 0;
		int farthestIndex = 0;
		
		for (int i=start; i<end; i++) {
			double distance = distanceToSegment(fixes[start].getLatitude(), 
					fixes[start].getLongitude(), fixes[end].getLatitude(), 
					fixes[end].getLongitude(), fixes[i].getLatitude(), 
					fixes[i].getLongitude());
			if (distance > maxDistance) {
				maxDistance = distance;
				farthestIndex = i;
			}
		}
		
		if (maxDistance > tolerance && farthestIndex != 0) {
			pointsToKeep.add(farthestIndex);
			distances.put(farthestIndex, maxDistance);
			encode(fixes, start, farthestIndex, pointsToKeep, distances);
			encode(fixes, farthestIndex, end, pointsToKeep, distances);
		}
	}
	
	/**
	 * Calculates the orthogonal (perpendicular) distance between a given point and a line
	 * segment (the minimum distance from the point to the line).
	 * 
	 * @param p1x The X coordinate of the start point of the line segment
	 * @param p1y The Y coordinate of the start point of the line segment
	 * @param p2x The X coordinate of the end point of the line segment
	 * @param p2y The Y coordinate of the end point of the line segment
	 * @param px The X coordinate of the point
	 * @param py The Y coordinate of the point
	 * @return The distance between the point and the line segment
	 */
	private double distanceToSegment(double p1x, double p1y, double p2x, double p2y,
			double px, double py) {

		double area = Math.abs(.5 * (p1x * p2y + p2x * py + px * p1y - p2x * p1y 
				- px * p2y - p1x * py));
		double bottom = Math.sqrt(Math.pow(p1x - p2x, 2) + Math.pow(p1y - p2y, 2));
		double height = area / bottom * 2;

		return height;
	}
    

	/**
	 * Returns the current number of levels.
	 * 
	 * @return The current number of levels
	 */
	public int getNumLevels() {
		return numLevels;
	}

	/**
	 * Sets the number of levels.
	 * 
	 * @param numLevels The number of levels to set
	 */
	public void setNumLevels(int numLevels) {
		this.numLevels = numLevels;
	}

	/**
	 * Returns the current zoom factor.
	 * 
	 * @return The current zoom factor
	 */
	public int getZoomFactor() {
		return zoomFactor;
	}

	/**
	 * Sets the zoom factor.
	 * 
	 * @param zoomFactor The zoom factor to set
	 */
	public void setZoomFactor(int zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	/**
	 * Returns the current tolerance value.
	 * 
	 * @return The tolerance value
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * Sets the tolerance value.
	 * 
	 * @param tolerance The tolerance value to set
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * An utility class for storing encoded points and their corresponding encoded levels, as
	 * String objects, as defined in the Google Maps encoding algorithm.
	 * 
	 * @author Ricardo Rocha <rocha@opensoaring.info>
	 *
	 */
	public class EncodedPath {
		
		/**
		 * The encoding of the points.
		 */
		private String encodedPoints;
		
		/**
		 * The encoding of the levels.
		 */
		private String encodedLevels;

		/**
		 * Class constructor.
		 * 
		 * @param encodedPoints A string containing the encoding of the points
		 * @param encodedLevels A string containing the encoding of the levels
		 */
		public EncodedPath(String encodedPoints, String encodedLevels) {
			this.encodedPoints = encodedPoints;
			this.encodedLevels = encodedLevels;
		}
		
		/**
		 * Returns the String object containing the encoded points.
		 * 
		 * @return The String object containing the encoded points
		 */
		public String getEncodedPoints() {
			return encodedPoints;
		}

		/**
		 * Sets the String object containing the encoded points.
		 * 
		 * @param encodedPoints A String object with the encoded points
		 */
		public void setEncodedPoints(String encodedPoints) {
			this.encodedPoints = encodedPoints;
		}

		/**
		 * Returns the String object containing the encoded levels.
		 * 
		 * @return The String object containing the encoded levels
		 */
		public String getEncodedLevels() {
			return encodedLevels;
		}

		/**
		 * Sets the String object containing the encoded levels.
		 * 
		 * @param encodedPoints A String object with the encoded levels
		 */
		public void setEncodedLevels(String encodedLevels) {
			this.encodedLevels = encodedLevels;
		}
	}

}
