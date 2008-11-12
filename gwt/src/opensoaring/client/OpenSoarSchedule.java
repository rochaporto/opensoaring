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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenSoarSchedule extends OpenSoarApp implements ClickListener {
	
	private OpenSoarMessages openSoarMessages = GWT.create(OpenSoarMessages.class);
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel navPanel = new HorizontalPanel();
	private DeckPanel contentPanel = new DeckPanel();
	
	private ToggleButton fourDaysNavButton = new ToggleButton(openSoarMessages.schedule4Days());
	private ToggleButton weekNavButton = new ToggleButton(openSoarMessages.scheduleWeek());
	private ToggleButton monthNavButton = new ToggleButton(openSoarMessages.scheduleMonth());
	
	public OpenSoarSchedule(String caption) {
		super(caption);
		
		// Build the Schedule application panel
		HorizontalPanel navItemsPanel = new HorizontalPanel();
		fourDaysNavButton.setStylePrimaryName("openSoarSched-navButton");
		weekNavButton.setStylePrimaryName("openSoarSched-navButton");
		monthNavButton.setStylePrimaryName("openSoarSched-navButton");
		navItemsPanel.add(fourDaysNavButton);
		navItemsPanel.add(weekNavButton);
		navItemsPanel.add(monthNavButton);
		navPanel.add(navItemsPanel);
		navPanel.setStyleName("openSoarSched-navPanel");
		mainPanel.add(navPanel);

		//contentPanel.showWidget(0);
		fourDaysNavButton.setDown(true);
		contentPanel.setStyleName("openSoarSched-contentPanel");
		mainPanel.add(contentPanel);
		mainPanel.setStyleName("openSoarSched-mainPanel");
		
		// Add it to the app's content panel
		setContentPanel(mainPanel);
	}
	
	public void onClick(Widget sender) {
		if(sender.equals(fourDaysNavButton) || sender.equals(weekNavButton) 
				|| sender.equals(monthNavButton)) {		
			
		}
	}
	
}