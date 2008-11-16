package opensoaring.client.json;

import com.google.gwt.core.client.JavaScriptObject;

public interface JsonpListener {

	public void onJsonpResponse(JavaScriptObject jsonResponse);
	
}
