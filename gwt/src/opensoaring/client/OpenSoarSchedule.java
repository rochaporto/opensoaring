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

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenSoarSchedule extends OpenSoarApp implements ClickListener {
	
	private OpenSoarMessages openSoarMessages = GWT.create(OpenSoarMessages.class);
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel navPanel = new HorizontalPanel();
	private OpenSoarSchedWidget schedWidget = new OpenSoarSchedWidget(this);
	
	private ToggleButton fourDaysNavButton = new ToggleButton(openSoarMessages.schedule4Days());
	private ToggleButton weekNavButton = new ToggleButton(openSoarMessages.scheduleWeek());
	private ToggleButton monthNavButton = new ToggleButton(openSoarMessages.scheduleMonth());
	
	public OpenSoarSchedule(String caption) {
		super(caption);
		
		// Build the Schedule application panel
		HorizontalPanel navItemsPanel = new HorizontalPanel();
		fourDaysNavButton.setStylePrimaryName("openSoarSched-navButton");
		fourDaysNavButton.addClickListener(this);
		weekNavButton.setStylePrimaryName("openSoarSched-navButton");
		weekNavButton.addClickListener(this);
		monthNavButton.setStylePrimaryName("openSoarSched-navButton");
		monthNavButton.addClickListener(this);
		navItemsPanel.add(fourDaysNavButton);
		navItemsPanel.add(weekNavButton);
		navItemsPanel.add(monthNavButton);
		navPanel.add(navItemsPanel);
		navPanel.setStyleName("openSoarSched-navPanel");
		mainPanel.add(navPanel);

		fourDaysNavButton.setDown(true);		
		mainPanel.add(schedWidget);
		mainPanel.setStyleName("openSoarSched-mainPanel");
	
		onClick(fourDaysNavButton);
		
		// Add it to the app's content panel
		setContentPanel(mainPanel);
	}
	
	@SuppressWarnings("deprecation")
	public void onClick(Widget sender) {
		fourDaysNavButton.setDown(false);
		weekNavButton.setDown(false);
		monthNavButton.setDown(false);
		Date startDate = OpenSoarSchedWidget.cleanTime(new Date());
		Date endDate = OpenSoarSchedWidget.cleanTime(new Date());
		if(sender.equals(fourDaysNavButton)) {
			endDate.setDate(startDate.getDate()+4);
			fourDaysNavButton.setDown(true);
		} else if (sender.equals(weekNavButton)) {
			startDate.setDate(startDate.getDate()-startDate.getDay()+1);
			endDate.setDate(startDate.getDate()+7);
			weekNavButton.setDown(true);
		} else if (sender.equals(monthNavButton)) {
			monthNavButton.setDown(true);
		}
		schedWidget.fetchScheduleData(startDate, endDate);
	}
}