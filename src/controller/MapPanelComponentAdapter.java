package controller;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import model.MapInteractionModel;
import model.MapModel;
import util.Point;
import view.MapPanel;

/**
 * This listener handles the resize events in the {@link MapPanel}
 */
public class MapPanelComponentAdapter extends ComponentAdapter {
	protected MapModel mapModel;
	protected MapInteractionModel mapInteractionModel;

	/**
	 * Make a new {@link MapPanelComponentAdapter} with access to the
	 * {@link MapModel} used by the {@link MapPanel}.
	 * 
	 * @param model
	 *            The model used by the map panel which this listener is added
	 *            to.
	 */
	public MapPanelComponentAdapter(MapModel model,
			MapInteractionModel mapInteractionModel) {
		this.mapModel = model;
		this.mapInteractionModel = mapInteractionModel;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Dimension d = ((MapPanel) e.getSource()).getSize();
		if (d.width > 0 && d.height > 0) {
			Dimension oldDimension = mapModel.getMapDimension();
			if (oldDimension != null) {
				// Make sure the map stays centered when we resize the window.
				Point mapMoved = new Point((oldDimension.getWidth()
						- d.getWidth())/2, (oldDimension.getHeight()
						- d.getHeight())/2);
				mapInteractionModel.movePanningPoint(mapMoved);
			}
			mapModel.setMapDimension(d);
		}
	}
}