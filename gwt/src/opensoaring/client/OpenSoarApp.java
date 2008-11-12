/*
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
		putMessage(level, msg, 3);
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
		contentPanel.add(w);
	}
}