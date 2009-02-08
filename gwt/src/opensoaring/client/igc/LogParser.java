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
package opensoaring.client.igc;

import java.util.Date;

import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.FixExtension;
import opensoaring.client.igc.flight.FlightDeclaration;
import opensoaring.client.igc.flight.FlightProperties;
import opensoaring.client.igc.flight.Fix.FixValidity;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * A IGC Log parsing utility, following the technical specification for IGC approved GNSS
 * flight records. This implementation was based on the document from the 20th May 2008.
 * 
 * The document should be available here:
 * http://www.fai.org/gliding/gnss/tech_spec_gnss.asp
 * 
 * @author Ricardo Rocha <rocha@opensoaring.info>
 * 
 */
public class LogParser {

	/**
	 * The date format used in the IGC records.
	 */
	public static DateTimeFormat dateFormat = DateTimeFormat.getFormat("ddMMyy");
	
	/**
	 * The time format used in the IGC records.
	 */
	public static DateTimeFormat timeFormat = DateTimeFormat.getFormat("HHmmssZZZ");
	
	/**
	 * An enumeration containing all defined IGC TLCs (Three Letter Codes).
	 */
	public static enum TLC {
		ATS, CCL, CCN, CCO, CDC, CGD, CID, CLB, CM2, DAE, DAN, DB1, DB2, DOB, DTE, 
		DTM, EDN, ENL, EOF, EON, EUP, FIN, FLP, FRS, FTY, FXA, GAL, GCN, GDC, GID, 
		GLO, GPS, GSP, GTY, HDM, HDT, IAS, LAD, LOD, LOV, MAC, OAT, ONT, OOI, PEV, 
		PFC, PHO, PLT, PRS, RAI, REX, RFW, RHW, RPM, SCM, SEC, SIT, SIU, STA, TAS,
		TDS, TEN, TPC, TRM, TRT, TZN, UND, UNT, VAR, VAT, VXA, WDI, WSP
	};
	
	/**
	 * Parses IGC 'A' records - Flight Recorder Information.
	 * 
	 * Format: A[MAN][UID][EXT(OPTIONAL)]
	 * 
	 * MAN == Recorder Manufacturer
	 * UID == Recorder Unique ID
	 * EXT == Recorder ID Extension (optional)
	 * 
	 * Manufacturer (3 bytes), Unique ID (3 bytes), ID Extension (optional).
	 * 
	 * @param record The string containing the I record to parse
	 * @param flightProps The FlightProperties object to be updated with the parsed info
	 */
	public static void parseRecorderInfo(String record, FlightProperties flightProps) {
		flightProps.setRecorderManufacturer(record.substring(1, 4));
		flightProps.setRecorderId(record.substring(4, 7));
		flightProps.setRecorderIdExtension(record.substring(7));
	}
	
	/**
	 * Parses IGC 'I' records - File Header Information. 
	 * 
	 * Formats: 
	 *  H[F|O|P][DTE][DDMMYY]
	 *  H[F|O|P][FXA][AAA]
	 * 	H[F|O|P][CCC][STR(30)]
	 * 
	 * F|O|P  == Data Source ('F' for Flight Recorder, 'P' and 'O' if manually after flight)
	 * CCC    == Record Subtype (can be any of the TLC codes below)
	 * DDMMYY == Flight UTC Date
	 * AAA    == Fix Accuracy
	 * STR    == Text String used when CCC is 'PLT' (pilot in charge) or 'CM2' (crew member 2)
	 * NNN    == GPS Datum
	 * 
	 * Mandatory records (CCC == record subtypes):
	 * 	DTE == Flight Date
	 *  FXA == Fix Accuracy
	 *  PLT == Pilot in Charge
	 *  CM2 == Crew Member 2
	 *  GTY == Glider Type
	 *  GID == Glider Id
	 *  DTM == GPS Datum
	 *  RFW == Recorder Firmware Version
	 *  RHW == Recorder Hardware Version
	 *  FTY == Flight Recorder Type
	 *  GPS == GPS Information
	 *  PRS == Pressure Altitude Sensor Information
	 * 
	 * Optional records (CCC == record subtypes):
	 *  CID == Competition ID
	 *  CCL == Competition Class
	 *  
	 * @param record The string containing the I record to parse
	 * @param flightProps The FlightProperties object to be updated with the parsed info
	 */
	public static void parseHeaderInfo(String record, FlightProperties flightProps) {
		// Load the TLC
		TLC recordSubType = null;
		try {
			recordSubType = TLC.valueOf(record.substring(2, 5));
		} catch(IllegalArgumentException e) {
			return;
		}
		
		// Fill in the flight properties object
		switch(recordSubType) {
		case DTE:
			String strDate = record.substring(5, 11);
			flightProps.setFlightDate(dateFormat.parse(strDate));
			break;
		case FXA:
			flightProps.setFixAccuracy(record.substring(5, 8));
			break;
		case PLT:
			flightProps.setPilotInCharge(record.substring(record.indexOf(":")+1).trim());
			break;
		case CM2:
			flightProps.setCrewMember2(record.substring(record.indexOf(":")+1).trim());
			break;
		case GTY:
			flightProps.setGliderType(record.substring(record.indexOf(":")+1).trim());
			break;
		case GID:
			flightProps.setGliderId(record.substring(record.indexOf(":")+1).trim());
			break;
		default:
			return;
		}
	}
	
	public static void parseFlightDeclaration(String record, FlightProperties flightProps) {
		FlightDeclaration declaration = flightProps.getFlightDeclaration();
		
		if (declaration == null) {
			declaration = new FlightDeclaration();
			flightProps.setFlightDeclaration(declaration);
			DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("ddMMyyHHmmss");
			declaration.setDate(dateTimeFormat.parse(record.substring(1, 7).concat(
					record.substring(7, 13))));
			declaration.setFlightDate(dateFormat.parse(record.substring(13, 19)));
			declaration.setTaskNumber(Integer.parseInt(record.substring(19, 23)));
			declaration.setNumberTurnpoints(Integer.parseInt(record.substring(23, 25)));
		} else if (declaration.getNumberTurnpoints() > 0 
				&& declaration.getTakeoff() == null) {
			double latitude = LogParser.dms2Decimal(record.substring(1, 9));
			double longitude = LogParser.dms2Decimal(record.substring(9, 18));
			declaration.setTakeoff(new Fix(latitude, longitude, 0));
		} else if (declaration.getNumberTurnpoints() > 0 
				&& declaration.getStart() == null) {
			double latitude = LogParser.dms2Decimal(record.substring(1, 9));
			double longitude = LogParser.dms2Decimal(record.substring(9, 18));
			declaration.setStart(new Fix(latitude, longitude, 0));
		} else if (declaration.getNumberTurnpoints() > 0 
				&& declaration.getTurnPoints().size() 
				< declaration.getNumberTurnpoints()) {
			double latitude = LogParser.dms2Decimal(record.substring(1, 9));
			double longitude = LogParser.dms2Decimal(record.substring(9, 18));
			declaration.getTurnPoints().add(new Fix(latitude, longitude, 0));
		} else if (declaration.getNumberTurnpoints() > 0 
				&& declaration.getFinish() == null) {
			double latitude = LogParser.dms2Decimal(record.substring(1, 9));
			double longitude = LogParser.dms2Decimal(record.substring(9, 18));
			declaration.setFinish(new Fix(latitude, longitude, 0));
		} else if (declaration.getNumberTurnpoints() > 0 
				&& declaration.getLanding() == null) {
			double latitude = LogParser.dms2Decimal(record.substring(1, 9));
			double longitude = LogParser.dms2Decimal(record.substring(9, 18));
			declaration.setLanding(new Fix(latitude, longitude, 0));
		}
	}

	/**
	 * Parses IGC 'I' records - Extensions to the Fix 'B' Record.
	 * 
	 * Only one of these should appear per IGC file.
	 * 
	 * Format: I[NN]([SS][FF][CCC])*
	 * 
	 * NN  == Number of extensions
	 * SS  == Start byte number
	 * FF  == Finish byte number
	 * CCC == TLC code for the extension
	 * 
	 * @param record The string containing the I record to parse
	 * @param flightProps The FlightProperties object to be updated with the parsed info
	 */
	public static void parseFixExtensions(String record, FlightProperties flightProps) {
		int numExtensions = Integer.parseInt(record.substring(1, 3));
		
		for (int i=0; i<numExtensions; i++) {
			int offset = 3+(i*7);
			int startByte = Integer.parseInt(record.substring(offset, offset+2));
			int endByte = Integer.parseInt(record.substring(offset+2, offset+4));
			TLC extension = TLC.valueOf(record.substring(offset+4, offset+7));
			flightProps.addFixExtension(new FixExtension(extension, startByte, endByte));
		}
	}
	
	/**
	 * Parses IGC 'B' records - Fix data.
	 * 
	 * Format: B[HHMMSS][DDMMmmmN/S][DDDMMmmmE/W][A|V][PPPPP][GGGGG]([EXTENSION])*
	 * 
	 * HHMMSS      == Time (in UTC)
	 * DDMMmmmN/S  == Latitude
	 * DDDMMmmmE/W == Longitude
	 * A|V         == Fix Validity
	 * PPPPP       == Pressure Altitude
	 * GGGGG       == GNSS Altitude
	 * EXTENSION*  == Values corresponding to the extensions defined in the 'I' record 
	 * 
	 * @param record A string containing the IGC 'B' record to parse
	 * @param flightProps The FlightProperties object containing the extra Fix extensions
	 * in the 'B' record, which should also be collected
	 * 
	 * @return The Fix object containing the parsed info
	 */
	public static Fix parseFix(String record, FlightProperties flightProps) {

		String strTime = record.substring(1, 7);
		Date time = timeFormat.parse(strTime + "GMT");
		
		double latitude = LogParser.dms2Decimal(record.substring(7, 15));
		double longitude = LogParser.dms2Decimal(record.substring(15, 24));
		FixValidity fixValidity = FixValidity.valueOf(record.substring(24, 25));
		int pressureAltitude = Integer.parseInt(record.substring(25, 30));
		int gnssAltitude = Integer.parseInt(record.substring(30, 35));

		Fix fix = new Fix(time, latitude, longitude, fixValidity, pressureAltitude, gnssAltitude);
		for (FixExtension extension: flightProps.getFixExtensions()) {
			fix.addExtension(extension.getExtension(), 
					record.substring(extension.getStartByte()-1, extension.getEndByte()-1));
		}
		
		return fix;
	}
	
	/**
	 * Converts a DMS coordinate (degrees,minutes,seconds) to a decimal coordinate.
	 * 
	 * @param dmsStr A string containing the DMS coordinate
	 * @return The decimal equivalent to the DMS coordinate
	 */
    public static double dms2Decimal(String dmsStr) {
    	double degrees, minutes, value;
    	if (dmsStr.length() == 8) { // Latitude
    		degrees = Integer.parseInt(dmsStr.substring(0, 2));
    		minutes = Integer.parseInt(dmsStr.substring(2, 4)) 
    			+ (Double.parseDouble(dmsStr.substring(4, 7)) / 1000.0);
    	} else { // Longitude
    		degrees = Integer.parseInt(dmsStr.substring(0, 3));
    		minutes = Integer.parseInt(dmsStr.substring(3, 5))
    			+ (Double.parseDouble(dmsStr.substring(5, 8)) / 1000.0);
    	}
    	value = degrees + (minutes / 60);
    	
    	char direction = dmsStr.charAt(dmsStr.length()-1);
    	if (direction == 'S' || direction == 'W') {
    		value = -value;
    	}
    	return value;
    }
}
