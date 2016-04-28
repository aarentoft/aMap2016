package view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.NameSearchModel;
import model.RouteModel;
import controller.SearchPanelButtonActionListener;

/**
 * This class defines the Searchpanel in the top of the window.
 */
@SuppressWarnings("serial")
public class SearchPanel extends JPanel implements Observer {
	protected LiveComboBox from;
	protected LiveComboBox to;
	protected JButton submit = new JButton("Find Route");
	protected JButton bClear = new JButton("Clear");
	protected RouteModel routeModel;
	protected List<JTextField> stops = new ArrayList<JTextField>();

	/**
	 * Constructor for the class.
	 * 
	 * @param routeModel
	 * @param nameSearchModel
	 */
	public SearchPanel(RouteModel routeModel, NameSearchModel nameSearchModel) {
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.routeModel = routeModel;
		routeModel.addObserver(this);
		
		setOpaque(false);

		setLayout(new FlowLayout());

		from = new LiveComboBox(0, routeModel, nameSearchModel);
		to = new LiveComboBox(1, routeModel, nameSearchModel);

		submit.setActionCommand("search");
		bClear.setActionCommand("clear");
		submit.setCursor(Cursor.getDefaultCursor());
		bClear.setCursor(Cursor.getDefaultCursor());
		
		submit.addActionListener(new SearchPanelButtonActionListener(routeModel));
		bClear.addActionListener(new SearchPanelButtonActionListener(routeModel));

		//LiveComboBoxes
		add(from, gbc);
		add(to, gbc);
		
		//Buttons
		add(submit, gbc);
		add(bClear, gbc);
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
				from.setItem(routeModel.getPoints().get(0).getSearchFieldRepresentation());
			if (routeModel.getPoints().get(1) != null)
				to.setItem(routeModel.getPoints().get(1).getSearchFieldRepresentation());
		}
	}
}
