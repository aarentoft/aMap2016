package model;

import datastructures.QuadTree;
import datastructures.graph.RoadEdge;
import datastructures.graph.RoadType;
import util.Point;
import util.Rectangle;

import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * This class contains the model, which contains all the information about a
 * map. This is both map data, but also current zooming and bounds etc.
 */
public class MapModel extends Observable {
	protected static final double coordinateRatioLowerLimit = 1.0;
	protected static final double bufferZone = 1.5;
	protected final QuadTree tree;

	protected Rectangle bounds;
	protected Dimension mapDimension;

	/**
	 * Create a new model based on a {@link QuadTree}.
	 * 
	 * @param tree
	 *            The {@link QuadTree} which this model should be based on.
	 */
	public MapModel(QuadTree tree) {
		this.tree = tree;
		this.bounds = tree.getBounds();
		setChanged();
	}

	/**
	 * @return All the edges which match the current bounds.
	 */
	public Set<RoadEdge> getEdgeSet() {
		return tree.query(bounds.scale(bufferZone), RoadType
				.getRoadtypesFromZoomLevel(getXAxisCoordinateAspectRatio()));
	}

	/**
	 * Gets all the drivable roads within the specified bounds.
	 * 
	 * @param r
	 *            The bounds to check
	 * @return all roads which are within these bounds.
	 */
	public Set<RoadEdge> getEdgeSet(Rectangle r, List<RoadType> types) {
		return tree.query(r, types);
	}
	
	/**
	 * @return The maximum bounds of the data
	 */
	protected Rectangle getMaxBounds() {
		return tree.getBounds();
	}

	/**
	 * @return The current bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Sets the current bounds and notifies the observers of this change.
	 * 
	 * @param bounds
	 *            The new bounds.
	 */
	public void setBounds(Rectangle bounds) throws IllegalArgumentException {
		if (!bounds.equals(this.bounds)) {
			if (getXAxisCoordinateAspectRatio(bounds) > coordinateRatioLowerLimit
					&& !bounds.contains(getMaxBounds())) {
				this.bounds = bounds;
			} else if (bounds.contains(getMaxBounds())
					&& !this.bounds.equals(getMaxBounds())) {
				this.bounds = getMaxBounds();
			}
			this.bounds.setAspectRatio(getMapDimensionRatio());
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * @return The current map dimension in pixels.
	 */
	public Dimension getMapDimension() {
		return mapDimension;
	}

	/**
	 * @return The dimension with the buffer-zone added.
	 */
	public Dimension getBufferMapDimension() {
		return new Dimension((int) (mapDimension.width * bufferZone),
				(int) (mapDimension.height * bufferZone));
	}

	/**
	 * @return The center of the buffered map dimension.
	 */
	public Point getBufferCenter() {
		return new Point(getBufferMapDimension().getWidth() / 2,
				getBufferMapDimension().getHeight() / 2);
	}

	/**
	 * @return The x-coordinate offset for the buffer.
	 */
	public int getBufferXOffset() {
		return (getBufferMapDimension().width - mapDimension.width) / 2;
	}

	/**
	 * @return The y-coordinate offset for the buffer.
	 */
	public int getBufferYOffset() {
		return (getBufferMapDimension().height - mapDimension.height) / 2;
	}

	/**
	 * Sets the current map dimension, as well as sets the current bounds to fit
	 * the ratio of the new dimension. At the end, all observers are notified
	 * about this change.
	 * 
	 * @param mapDimension
	 *            The new dimensions for the mapping area, in pixels.
	 */
	public void setMapDimension(Dimension mapDimension) {
		this.mapDimension = mapDimension;
		bounds.setAspectRatio(getMapDimensionRatio());
		setChanged();
		notifyObservers();
	}

	/**
	 * @return The ratio of the mapping area.
	 */
	protected double getMapDimensionRatio() {
		return mapDimension.getWidth() / mapDimension.getHeight();
	}

	/**
	 * @return The pixel-to-UTM aspect ratio, ie, how many meters a pixel
	 *         corresponds to. This is on the X-axis.
	 */
	protected double getXAxisCoordinateAspectRatio(Rectangle bounds) {
		if (mapDimension == null)
			return Double.MAX_VALUE;
		return bounds.getWidth() / mapDimension.getWidth();
	}

	/**
	 * @return The coordinate aspect ratio for the current bounds. This is on
	 *         the X-axis.
	 */
	public double getXAxisCoordinateAspectRatio() {
		return getXAxisCoordinateAspectRatio(getBounds());
	}

	/**
	 * @return The pixel-to-UTM aspect ratio, ie, how many meters a pixel
	 *         corresponds to. This is on the Y-axis.
	 */
	protected double getYAxisCoordinateAspectRatio(Rectangle bounds) {
		if (mapDimension == null)
			return Double.MAX_VALUE;
		return bounds.getHeight() / mapDimension.getHeight();
	}

	/**
	 * @return The coordinate aspect ratio for the current bounds. This is on
	 *         the Y-axis.
	 */
	public double getYAxisCoordinateAspectRatio() {
		return getYAxisCoordinateAspectRatio(getBounds());
	}

	/**
	 * @return The aspect ratio of the maximum bounds.
	 */
	public double getMaxCoordinateAspectRatio() {
		// XXX Misleading name and function - Should be the the "real" ratio,
		// ie, including both X and Y
		return getXAxisCoordinateAspectRatio(getMaxBounds());
	}

	/**
	 * Set the center point of the bound and notify the observers.
	 * 
	 * @param c
	 *            The new center for the bounds.
	 */
	public void setBoundCenter(Point c) {
		bounds.setCenter(c);
		setChanged();
		notifyObservers();
	}

	/**
	 * Move the bounds with the differences contained in the point.
	 * 
	 * @param p
	 *            The x and y difference to move the bounds with.
	 */
	public void moveBound(Point p) {
		bounds.move(p);
		setChanged();
		notifyObservers();
	}

	/**
	 * Zoom the bounds
	 * 
	 * @param factor
	 *            The factor to multiply the bounds with.
	 * @param focusPoint
	 *            The point which should be focused on when the zooming is done.
	 */
	public void zoomBound(double factor, Point focusPoint) {
		Rectangle bounds = this.bounds.clone();
		bounds.setBounds(factor, focusPoint);

		setBounds(bounds);
	}
}