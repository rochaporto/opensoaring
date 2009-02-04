/*
 * Copyright (C) 2008 OpenSoaring <contact@opensoaring.info>.
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

package opensoaring.client.json;

public class JsonClient {

	private static int requestId = 0;
	
	public static void getJsonp(String url, JsonpListener listener) {
		JsonClient.getJsonp(JsonClient.requestId++, url, listener);
	}
	
	public native static void getJsonp(int requestId, String url, JsonpListener listener) /*-{
	++requestId;
	
    var callback = "callback" + requestId;
    
    var script = document.createElement("script");
    script.setAttribute("src", url+callback);
    script.setAttribute("type", "text/javascript");
    
    window[callback] = function(jsonObj) {
      listener.@opensoaring.client.json.JsonpListener::onJsonpResponse(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(url, jsonObj);
      window[callback + "done"] = true;
    }
    
    // JSON download has 1-second timeout
    setTimeout(function() {
      if (!window[callback + "done"]) {
        listener.@opensoaring.client.json.JsonpListener::onJsonpResponse(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(url, null);
      } 

      // cleanup
      document.body.removeChild(script);
      delete window[callback];
      delete window[callback + "done"];
    }, 1000);
    
    document.body.appendChild(script);
	}-*/;
	
}
