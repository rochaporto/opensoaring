package opensoaring.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenSoarApp extends Composite {
	
	public static enum msgLevel { INFO, ERROR };
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel headerPanel = new HorizontalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
	
	private SimplePanel appTitlePanel = new SimplePanel();	
	private DecoratorPanel infoMsgPanel = new DecoratorPanel();
	private Label infoMsg = new Label();
	
	public OpenSoarApp(String caption) {
		
		// App Header Panel
		HTML appTitle = new HTML(caption);
		appTitle.setStyleName("openSoarApp-appTitle");
		appTitlePanel.add(appTitle);
		appTitlePanel.setStyleName("openSoarApp-appTitlePanel");
		headerPanel.add(appTitlePanel);
		infoMsg.addStyleName("openSoarApp-infoMsg");
		infoMsgPanel.setWidget(infoMsg);
		infoMsgPanel.setVisible(false);
		infoMsgPanel.setStylePrimaryName("openSoarApp-infoMsgPanel");
		headerPanel.add(infoMsgPanel);
		headerPanel.setStyleName("openSoarApp-headerPanel");
		mainPanel.add(headerPanel);
		
		// App Content Panel(s)
		contentPanel.setStyleName("openSoarApp-contentPanel");
		mainPanel.add(contentPanel);
		
		// Finish up the composite
		initWidget(mainPanel);
		
		setStyleName("openSoarApp-mainPanel");
	}
	
	public void putMessage(OpenSoarApp.msgLevel level, final String msg, int duration) {
		infoMsg.setText(msg);
		infoMsg.setStyleName("openSoarApp-infoMsg" + level.name());
		infoMsg.addStyleName("openSoarApp-infoMsg");
		infoMsgPanel.setVisible(true);
		
		if (duration != -1) {
			Timer tmpTimer = new Timer() {
				public void run() {
					if (infoMsg.getText().equals(msg)) { 
						cleanMessage();
					}
			    }
			};
			tmpTimer.schedule(duration * 1000);
		}
	}
	
	public void putMessage(OpenSoarApp.msgLevel level, String msg) {
		putMessage(level, msg, 2);
	}
	
	public void cleanMessage() {
		infoMsg.setText("");
		infoMsgPanel.setVisible(false);
	}
	
	public void info(String msg) {
		putMessage(OpenSoarApp.msgLevel.INFO, msg);
	}
	
	public void error(String msg) {
		putMessage(OpenSoarApp.msgLevel.ERROR, msg);
	}
	
	protected void setContentPanel(Widget w) {
		this.contentPanel.add(w);
	}
}