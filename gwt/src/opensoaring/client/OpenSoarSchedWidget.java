package opensoaring.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpenSoarSchedWidget extends Composite {

	private OpenSoarSchedule schedParentApp = null;
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable schedTable = new FlexTable();

	private JSONArray currentData = null;
	
	private int requestCounter = 0;
	
	private String[] memberTypes = new String[] { "INSTRUCTOR", "WINCHER",
			"TRIALPILOT", "STUDENT" };
	
	private String[] members = new String[] {"Rocha R.", "Riegel F.", "Riegel J.",
			"Stilmant E.", "Fert R.", "Rank A.", "Delobel P.", "Peillex G.",
			"Dury C."};
	
	private	DateTimeFormat dayTimeFormat = DateTimeFormat.getFormat("EEE, dd MMM yy");
	
	public OpenSoarSchedWidget(OpenSoarSchedule schedApp) {
		mainPanel.add(schedTable);
		mainPanel.setStyleName("openSoarSched-mainPanel");
		
		schedParentApp = schedApp;
		
		initWidget(mainPanel);
	}
	
	public void redraw() {
		Date today = OpenSoarSchedWidget.cleanTime(new Date());
		
		mainPanel.remove(schedTable);
		schedTable = new FlexTable();
		schedTable.setCellPadding(0);
		schedTable.setStyleName("openSoarSched-schedTable");
		
		for (int i=0; i < currentData.size(); i++) {
			JSONObject schedDay = (JSONObject)currentData.get(i);
			
			Date entryDate = new Date((long)((JSONNumber)schedDay.get("date")).doubleValue() * 1000);
			
			Label dayLabel = new Label(dayTimeFormat.format(entryDate));
			dayLabel.setStylePrimaryName("openSoarSched-schedHeader");
			schedTable.setWidget(0, i, dayLabel);
			if (entryDate.toString().equals(today.toString())) {
				dayLabel.addStyleDependentName("today");	
			}
			schedTable.getColumnFormatter().setStyleName(i, "openSoarSched-schedDay");
			
			int entryCount = 1;
			
			for (String memberType: memberTypes) {
			
				JSONArray members = (JSONArray)schedDay.get(memberType);
				for (int j=0; j < members.size(); j++) {
					JSONObject tmpEntry = (JSONObject)members.get(j);
					Label memberEntry = new Label(((JSONString)tmpEntry.get("name")).stringValue());
					memberEntry.setStylePrimaryName("openSoarSched-schedEntry");
					memberEntry.addStyleDependentName(memberType);
					schedTable.setWidget(entryCount, i, memberEntry);
					++entryCount;
				}
				
			}
			
		}
		mainPanel.add(schedTable);
	}
	
	public void fetchScheduleData(Date startDate, Date endDate) {
		getJson(requestCounter++, "http://opensoaring.info/schedule?startDate=" 
				+ startDate.getTime() / 1000.0 + "&endDate=" 
				+ endDate.getTime()  / 1000.0
				+ "&callback=", this);
	}
	
	public void handleJsonResponse(JavaScriptObject jso) {
		if (jso == null) {
			schedParentApp.error("Failed to retrieve schedule data");
			return;
		}
		currentData = new JSONArray(jso);
		redraw();
	}
	
	public native static void getJson(int requestId, String url, OpenSoarSchedWidget handler) /*-{
	alert(url);
    var callback = "callback" + requestId;
    
    var script = document.createElement("script");
    script.setAttribute("src", url+callback);
    script.setAttribute("type", "text/javascript");
    
    window[callback] = function(jsonObj) {
      handler.@opensoaring.client.OpenSoarSchedWidget::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
      window[callback + "done"] = true;
    }
    
    // JSON download has 1-second timeout
    setTimeout(function() {
      if (!window[callback + "done"]) {
        handler.@opensoaring.client.OpenSoarSchedWidget::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
      } 

      // cleanup
      document.body.removeChild(script);
      delete window[callback];
      delete window[callback + "done"];
    }, 1000);
    
    document.body.appendChild(script);
}-*/;
		
	@SuppressWarnings({ "deprecation" })
	public static Date cleanTime(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
}
