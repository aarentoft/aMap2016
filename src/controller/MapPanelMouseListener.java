package controller;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Set;

import model.MapInteractionModel;
import model.MapModel;
import model.RouteModel;
import util.Converter;
import util.Point;
import util.Rectangle;
import util.graph.RoadEdge;
import util.graph.RoadType;
import view.MapPanel;

/**
 * This class handles all the mouse events for the {@link MapPanel}. This is
 * mainly related to zooming and panning on the map.
 */
public class MapPanelMouseListener extends MouseAdapter {
	/**
	 * The factor used for zooming in
	 */
	protected static final double SCROLL_ZOOM_IN_FACTOR = 0.6;
	/**
	 * The factor used for zooming out
	 */
	protected static final double SCROLL_ZOOM_OUT_FACTOR = 1.8;

	protected MapModel mapModel;
	protected MapInteractionModel mapInteractionModel;
	protected RouteModel routeModel;
	protected Converter converter;

	protected Point mousePressPoint;

	/**
	 * Creates a new {@link MapPanelMouseListener} based on the model which the
	 * owning {@link MapPanel} is based on.
	 * 
	 * @param mapModel
	 *            The model used by the map panel which this listerner is added
	 *            to.
	 */
	public MapPanelMouseListener(MapModel mapModel,
			MapInteractionModel mapInteractionModel, RouteModel routeModel) {
		this.mapModel = mapModel;
		this.mapInteractionModel = mapInteractionModel;
		this.routeModel = routeModel;
		this.converter = new Converter(mapModel);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double x = converter.xPixel2UTM(e.getX());
		double y = converter.yPixel2UTM(e.getY());

		Point point = new Point(x, y);
		double factor = (e.getWheelRotation() < 0) ? SCROLL_ZOOM_IN_FACTOR
				: SCROLL_ZOOM_OUT_FACTOR;

		mapModel.zoomBound(factor, point);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressPoint = new Point(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Change the cursor whenever we move, so that we indicate the action to
		// the user.
		if (e.isControlDown()) {
			((Component) e.getSource()).setCursor(Cursor
					.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		} else {
			((Component) e.getSource()).setCursor(Cursor
					.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		// We need both of the mouseMoved and mouseDragged to make it work on
		// all platforms (MouseDragged doesn't get called on mac, but mouse
		// moved doesn't get called on windows if the mouse is being dragged)
		mouseDragged(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mousePressPoint != null) {
			// Panning if control is not down
			if (!e.isControlDown()) {
				mapInteractionModel.setDragBoxZooming(false);
				Point diff = new Point(mousePressPoint.getX()
						- e.getPoint().getX(), mousePressPoint.getY()
						- e.getPoint().getY());

				mousePressPoint = new Point(e.getX(), e.getY());
				mapInteractionModel.movePanningPoint(diff);
				mapModel.moveBound(converter.pointPixel2UTM(diff, true));
				((Component) e.getSource()).setCursor(Cursor
						.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
			// Otherwise drag-zoom
			else {
				((Component) e.getSource()).setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				Point point = new Point(e.getX(), e.getY());
				mapInteractionModel.setDragBoxZoomCorners(mousePressPoint,
						point);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Reset cursor to the default action (moving/panning)
		((Component) e.getSource()).setCursor(Cursor
				.getPredefinedCursor(Cursor.MOVE_CURSOR));

		// If we have been dragging we need special actions, since we don't do
		// anything before the mouse release (unlike the panning, which is done
		// in the mouseMoved/mouseDragged)
		if (mapInteractionModel.isDragBoxZooming()) {
			Point p1 = new Point(e.getX(), e.getY());
			Point p2 = new Point(mousePressPoint.getX(), mousePressPoint.getY());

			p1 = converter.pointPixel2UTM(p1);
			p2 = converter.pointPixel2UTM(p2);

			mapModel.setBounds(new Rectangle(p1, p2));
		}
		// Reset the action controllers in the map interaction model and this
		// controller.
		mapInteractionModel.setDragBoxZooming(false);
		mousePressPoint = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// doubleclick to move the center
		if (e.getClickCount() == 2) {
			mousePressPoint = null;
			Point newCenter = converter.pointPixel2UTM(new Point(e.getX(), e
					.getY()));
			mapModel.setBoundCenter(newCenter);
		}
		// Alt should be down to place the route-markers.
		else if (e.isAltDown()) {
			// Get a 5x5 pixel bound around the click to ensure that we get
			// results.
			Point min = converter.pointPixel2UTM(new Point(e.getX() - 2, e
					.getY() - 2));
			Point max = converter.pointPixel2UTM(new Point(e.getX() + 2, e
					.getY() + 2));
			Set<RoadEdge> roads = mapModel.getEdgeSet(new Rectangle(min, max),
					RoadType.getAllDrivableRoads());

			// Find the closets RoadEdge in the data we have received. Note
			// that this is based on the distance from the nodes to the click
			// point
			double shortestDistance = Double.MAX_VALUE;
			Point clickPoint = converter.pointPixel2UTM(new Point(e.getX(), e
					.getY()));
			RoadEdge match = null;

			for (RoadEdge r : roads) {
				if (r.end.distance(clickPoint) < shortestDistance) {
					match = r;
					shortestDistance = r.end.distance(clickPoint);
				}
				if (r.start.distance(clickPoint) < shortestDistance) {
					match = r;
					shortestDistance = r.start.distance(clickPoint);
				}
			}
			routeModel.addPoint(match);
		}
	}
}