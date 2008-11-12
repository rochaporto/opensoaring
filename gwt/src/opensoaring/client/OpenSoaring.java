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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenSoaring extends Composite implements EntryPoint, ClickListener {

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel headerPanel = new HorizontalPanel();	
	private HorizontalPanel navItemsPanel = new HorizontalPanel();
	private DeckPanel contentPanel = new DeckPanel();
	
	public void onModuleLoad() {

		OpenSoarMessages openSoarMessages = GWT.create(OpenSoarMessages.class);
		
		// Header Panel
		headerPanel.setStyleName("openSoar-headerPanel");
		
		// Navigation Panel
		HorizontalPanel navPanel = new HorizontalPanel();
		navPanel.add(navItemsPanel);
		navPanel.setStyleName("openSoar-navPanel");
		
		// Content Panel + Adding Applications
		addApplication(openSoarMessages.scheduleAppName(), 	
				new OpenSoarSchedule(openSoarMessages.scheduleAppName()));
		addApplication(openSoarMessages.homeAppName(), 
				new OpenSoarApp(openSoarMessages.homeAppName()));
		addApplication(openSoarMessages.flightInfoAppName(), 
				new OpenSoarApp(openSoarMessages.flightInfoAppName()));
		setSelectedApp(0);
		contentPanel.setStyleName("openSoar-contentPanel");
		
		// Main Panel
		mainPanel.add(headerPanel);
		mainPanel.add(navPanel);
		mainPanel.add(contentPanel);
		mainPanel.setStyleName("openSoar-mainPanel");
		
		RootPanel.get().add(mainPanel); 
	}
	
	protected void addApplication(String name, Widget w) {
		ToggleButton appButton = new ToggleButton(name);
		appButton.addClickListener(this);
		appButton.setStylePrimaryName("openSoar-navButton");
		navItemsPanel.add(appButton);
		contentPanel.add(w);
	}
	
	public void setSelectedApp(int index) {
		for (int i = 0; i < navItemsPanel.getWidgetCount(); i++) {
			if (i == index) {
				((ToggleButton)navItemsPanel.getWidget(i)).setDown(true);
			} else {
				((ToggleButton)navItemsPanel.getWidget(i)).setDown(false);
			}
		}
		contentPanel.showWidget(index);
	}
	
	public void onClick(Widget sender) {
		ToggleButton appButton = (ToggleButton)sender;
		for (int i = 0; i < navItemsPanel.getWidgetCount(); i++) {
			ToggleButton tmpButton = (ToggleButton)navItemsPanel.getWidget(i);
			if (tmpButton.equals(appButton)) {				
				setSelectedApp(i);
			}
		}
	}
}
