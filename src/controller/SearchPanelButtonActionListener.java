package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.RouteModel;

/**
 * This listener contains the information and actions related to the
 * "Find Route" button (Find Route).
 */
public class SearchPanelButtonActionListener implements ActionListener {
	protected RouteModel routeModel;
	
	/**
	 * @param routeModel The routemodel used by the map.
	 */
	public SearchPanelButtonActionListener(RouteModel routeModel) {
		this.routeModel = routeModel;
	}

	/**
	 * Will conduct a route search in the <code>routemodel</code>.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("search"))
			routeModel.setDoRouteSearch(true);
		if(e.getActionCommand().equals("clear"))
			routeModel.clear();
	}
}