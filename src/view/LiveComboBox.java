package view;

import controller.LiveComboBoxKeyAndActionListener;
import datastructures.TrieSearchable;
import datastructures.graph.RoadEdge;
import datastructures.graph.RoadNode;
import datastructures.graph.RoadType;
import datastructures.graph.WayData;
import model.NameSearchModel;
import model.RouteModel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents the searchresults.
 */
@SuppressWarnings("serial")
public class LiveComboBox extends JComboBox<RoadEdge> implements Observer {
	protected NameSearchModel nameSearchModel;
	protected LiveComboBoxKeyAndActionListener listener;

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

		listener = new LiveComboBoxKeyAndActionListener(
				type, this, routeModel, nameSearchModel);

		setRenderer(new LiveComboBoxRenderer());
		/* Using the following custom editor breaks arrow key interaction with the dropdown list on OSX.
		 * It still works on Linux, however. Windows has not been tested. It is possibly related to this bug
		 * https://bugs.openjdk.java.net/browse/JDK-8139195 so it is probably easiest to wait for a Java update
		 * to fix it. */
		setEditor(new LiveComboBoxEditor());

		editor.getEditorComponent().addKeyListener(listener);
		this.addActionListener(listener);
		setMaximumRowCount(5);
		setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void update(Observable o, Object arg) {
		String enteredString = getEditor().getItem().toString();

		if (getEditor().getEditorComponent().isFocusOwner()
				&& getEditor().getItem() instanceof String) {

			setPopupVisible(false);
			removeAllItems();
			List<RoadEdge> results = nameSearchModel.getRoadNameSearchResult();

			if (results.size() > 0) {
				String firstResultName = results.get(0).getTrieRepresentation().toLowerCase();
				if (!firstResultName.equals(enteredString.toLowerCase())) {
					/* This dummy edge is used to prevent the "JComboBox-automatically-selects-the-first-
			 		 * item-in-the-drop-down"-feature from interfering with user text entry. However,
			 		 * if the text in the combobox is identical to that of an actual edge, this dummy
			 		 * is omitted, such that the real-life edge is selected instead. This also prevents
			 		 * route searches from failing, when the user only enters the name of a road without
			 		 * selecting it from the list. Without this, the route search would have attempted to use
			 		 * the dummy road edge which would of course fail, as it is not a part of the road graph. */
					RoadEdge currentEntryDummy = new RoadEdge(
							new WayData(enteredString, RoadType.UNKNOWN, false),
							new RoadNode(1, 0.0, 0.0),
							new RoadNode(2, 0.1, 0.1)
					);
					addItem(currentEntryDummy);
				}

				for (RoadEdge edge : results) {
					addItem(edge);
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

	/**
	 * Determines how the editor field of {@link LiveComboBox} behaves.
	 * This implementation overrides the default use of the toString() representation of an object
	 * when setting the item of the combo box. Instead, it uses the Trie representation (if available)
	 * which makes sense as searches are matched against the Trie representation, and not the toString()
	 * representation.
	 */
	private class LiveComboBoxEditor extends BasicComboBoxEditor {

		public LiveComboBoxEditor() {
			editor = new JTextField();
		}

		@Override
		public void setItem(Object item) {
			if (item == null)
				return;

			String text = item instanceof TrieSearchable ? ((TrieSearchable) item).getTrieRepresentation() : item.toString();
			editor.setText(text);
		}
	}

	/**
	 * Determines the appearance of objects in the dropdown of {@link LiveComboBox}.
	 * Uses the Trie representation of an object (if available), instead of the toString() representation.
	 */
	private class LiveComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

		public LiveComboBoxRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
													  int index, boolean isSelected, boolean cellHasFocus) {
			String text = value instanceof TrieSearchable ? ((TrieSearchable) value).getTrieRepresentation() : value.toString();
			setText(text);

			if (isSelected) {
				setForeground(Color.WHITE);
				setBackground(Color.RED);
			} else {
				setForeground(Color.BLACK);
				setBackground(Color.WHITE);
			};

			return this;
		}

	}

}