package opensoaring.client.igc;

import java.util.Date;

import opensoaring.client.igc.flight.Fix;
import opensoaring.client.igc.flight.FixExtension;
import opensoaring.client.igc.flight.FlightProperties;
import opensoaring.client.igc.flight.Fix.FixValidity;

import com.google.gwt.i18n.client.DateTimeFormat;

public class LogParser {
	
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
			DateTimeFormat dateFormat = DateTimeFormat.getFormat("ddMMyy");
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
	 */
	public static Fix parseFix(String record, FlightProperties flightProps) {

		DateTimeFormat timeFormat = DateTimeFormat.getFormat("HHmmss");
		String strTime = record.substring(1, 7);
		Date time = timeFormat.parse(strTime);
		
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
