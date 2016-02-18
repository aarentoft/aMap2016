package model;

import java.util.Observable;

import util.Point;
import util.Rectangle;

/**
 * A model which contains information about the current map-interaction.
 */
public class MapInteractionModel extends Observable {
	protected Rectangle dragBoxZoomRectangle;
	protected boolean dragZooming = false;
	protected Point panned = new Point(0, 0);

	/**
	 * Sets the corners of the current drag-zoom area.
	 * 
	 * @param p1
	 *            The one corner of the zoom area.
	 * @param p2
	 *            The other corner opposite of the first.
	 */
	public void setDragBoxZoomCorners(Point p1, Point p2) {
		this.dragZooming = true;
		this.dragBoxZoomRectangle = new Rectangle(p1, p2);
		setChanged();
		notifyObservers();
	}

	/**
	 * @return The center {@link Rectangle} of the drag-zoom.
	 */
	public Rectangle getDragBoxZoomCenterRectangle() {
		Point center = dragBoxZoomRectangle.getCenter();

		int width = (int) (dragBoxZoomRectangle.getWidth() / 20);
		int height = (int) (dragBoxZoomRectangle.getHeight() / 20);

		int focus = (width > height) ? height : width;

		return new Rectangle(center, focus, focus);
	}

	/**
	 * @return Get the left-rectangle which outlines the left edge of the
	 *         drag-zooming area.
	 */
	public Rectangle getDragBoxZoomRectangle() {
		return dragBoxZoomRectangle;
	}

	/**
	 * @return Whether or not we're drag-zooming.
	 */
	public boolean isDragBoxZooming() {
		return dragZooming;
	}

	/**
	 * Set the drag-zooming status.
	 * 
	 * @param dragZooming
	 *            Whether or not we're drag-zooming.
	 */
	public void setDragBoxZooming(boolean dragZooming) {
		this.dragZooming = dragZooming;
	}

	/**
	 * @return The current panning point.
	 */
	public Point getPanningPoint() {
		return panned;
	}

	/**
	 * Reset the panning point to (0,0).
	 */
	public void resetPanningPoint() {
		panned.setLocation(0, 0);
	}

	/**
	 * Move the panning point.
	 * 
	 * @param diff
	 *            The difference to move it with (ie, if it is (10,10), and diff
	 *            is (1,1), it will be (11,11))
	 */
	public void movePanningPoint(Point diff) {
		panned.setLocation(panned.x + diff.x, panned.y + diff.y);
		setChanged();
		notifyObservers();
	}
}