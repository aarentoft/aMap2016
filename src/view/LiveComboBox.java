package view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

import model.NameSearchModel;
import model.RouteModel;
import util.graph.RoadEdge;
import controller.LiveComboBoxKeyAndActionListener;

/**
 * This class represents the searchresults.
 */
@SuppressWarnings("serial")
public class LiveComboBox extends JComboBox<String> implements Observer {
	protected NameSearchModel nameSearchModel;

	/**
	 * Constructs the combo box. 
	 * 
	 * @param type
	 * @param routeModel
	 * @param nameSearchModel
	 */
	public LiveComboBox(int type, RouteModel routeModel,
			NameSearchModel nameSearchModel) {
		this.nameSearchModel = nameSearchModel;
		
		nameSearchModel.addObserver(this);

		setEditable(true);

		LiveComboBoxKeyAndActionListener listener = new LiveComboBoxKeyAndActionListener(
				type, this, routeModel, nameSearchModel);

		getEditor().getEditorComponent().addKeyListener(listener);
		addActionListener(listener);
		setMaximumRowCount(5);
		setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Sets the input to the marked item.
	 * @param o
	 */
	public void setItem(Object o) {
		if (!getEditor().getItem().equals(o)) {
			getEditor().setItem(o);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (getEditor().getEditorComponent().isFocusOwner()
				&& getEditor().getItem() instanceof String) {
			String enteredString = (String) getEditor().getItem();

			setPopupVisible(false);
			removeAllItems();
			Collection<RoadEdge> results = nameSearchModel
					.getRoadNameSearchResult();
			addItem(enteredString);
			if (results != null) {
				for (RoadEdge edge : results) {
					addItem(edge.data.roadname);
				}
			}
			setPopupVisible(true);
		}
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(170, super.getMinimumSize().height);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getMinimumSize();
	}
}