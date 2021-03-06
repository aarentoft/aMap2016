package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import datastructures.graph.RoadType;
import model.NameSearchModel;
import model.RouteModel;
import datastructures.graph.RoadEdge;
import view.LiveComboBox;

/**
 * This is the keylistner used by the combo box. This class makes it possible to
 * get results while you type.
 */
public class LiveComboBoxKeyAndActionListener
		implements
			KeyListener,
			ActionListener {

	protected LiveComboBox liveComboBox;
	protected RouteModel routeModel;
	protected NameSearchModel nameSearchModel;
	protected int type;

	/**
	 * Constructer for the actionlistner.
	 * 
	 * @param type
	 * @param liveComboBox
	 * @param routeModel
	 * @param nameSearchModel
	 */
	public LiveComboBoxKeyAndActionListener(int type,
			LiveComboBox liveComboBox, RouteModel routeModel,
			NameSearchModel nameSearchModel) {
		this.type = type;
		this.liveComboBox = liveComboBox;
		this.routeModel = routeModel;
		this.nameSearchModel = nameSearchModel;
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Object item = liveComboBox.getEditor().getItem();
		String enteredString = (String) item;

		if (String.valueOf(e.getKeyChar()).matches("\\w") || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (enteredString.length() > 2) {
				nameSearchModel.doRoadNameSearch(enteredString.toLowerCase());
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				nameSearchModel.resetRoadNameSearch();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (liveComboBox.getSelectedItem() != null && liveComboBox.getSelectedItem() instanceof RoadEdge) {
			routeModel.setPoint(type, (RoadEdge) liveComboBox.getSelectedItem());
		}
	}
}