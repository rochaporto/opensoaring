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
      listener.@opensoaring.client.json.JsonpListener::onJsonpResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
      window[callback + "done"] = true;
    }
    
    // JSON download has 1-second timeout
    setTimeout(function() {
      if (!window[callback + "done"]) {
        listener.@opensoaring.client.json.JsonpListener::onJsonpResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
      } 

      // cleanup
      document.body.removeChild(script);
      delete window[callback];
      delete window[callback + "done"];
    }, 1000);
    
    document.body.appendChild(script);
	}-*/;
	
}
