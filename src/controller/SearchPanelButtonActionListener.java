package controller;

import view.SearchPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This listener contains the information and actions related to the
 * "Find Route" button and "Clear" button.
 */
public class SearchPanelButtonActionListener implements ActionListener {
	protected SearchPanel searchPanel;
	
	/**
	 * @param searchPanel The search panel that will be controlled by buttons using this listener
	 */
	public SearchPanelButtonActionListener(SearchPanel searchPanel) {
		this.searchPanel = searchPanel;
	}

	/**
	 * Will conduct a route search in the <code>routemodel</code>.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("search"))
			searchPanel.doRouteSearch();
		if(e.getActionCommand().equals("clear"))
			searchPanel.clearSearch();
	}
}