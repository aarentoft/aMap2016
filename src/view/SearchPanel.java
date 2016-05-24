package view;

import model.NameSearchModel;
import model.RouteModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class defines the Searchpanel in the top of the window.
 */
@SuppressWarnings("serial")
public class SearchPanel extends JPanel implements Observer {
	protected LiveComboBox from;
	protected LiveComboBox to;
	protected JButton buttonSubmit = new JButton("Find Route");
	protected JButton buttonClear  = new JButton("Clear");
	protected RouteModel routeModel;
	protected List<JTextField> stops = new ArrayList<JTextField>();

	/**
	 * Constructor for the class.
	 * 
	 * @param theRouteModel
	 * @param nameSearchModel
	 */
	public SearchPanel(RouteModel theRouteModel, NameSearchModel nameSearchModel) {
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.routeModel = theRouteModel;
		routeModel.addObserver(this);
		
		setOpaque(false);

		setLayout(new FlowLayout());

		from = new LiveComboBox(0, routeModel, nameSearchModel);
		to = new LiveComboBox(1, routeModel, nameSearchModel);

		buttonSubmit.setActionCommand("search");
		buttonClear.setActionCommand("clear");
		buttonSubmit.setCursor(Cursor.getDefaultCursor());
		buttonClear.setCursor(Cursor.getDefaultCursor());
		
		buttonSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				routeModel.setDoRouteSearch(true);
			}
		});
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				from.getEditor().setItem("");
				to.getEditor().setItem("");
				routeModel.clear();
			}
		});

		//LiveComboBoxes
		add(from, gbc);
		add(to, gbc);
		
		//Buttons
		add(buttonSubmit, gbc);
		add(buttonClear, gbc);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(200, 100);
	}

	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getMinimumSize();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof RouteModel) {
			// If the routeModel has another point saved for this field, we
			// should update this field to be that value
			if (routeModel.getPoints().get(0) != null)
				from.getEditor().setItem(routeModel.getPoints().get(0).getSearchFieldRepresentation());
			if (routeModel.getPoints().get(1) != null)
				to.getEditor().setItem(routeModel.getPoints().get(1).getSearchFieldRepresentation());
		}
	}
}
