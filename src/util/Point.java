package util;

import java.awt.geom.Point2D;

/**
 * A simple child of Point2D made for small conveniences.
 */
@SuppressWarnings("serial")
public class Point extends Point2D.Double {
	/**
	 * @param x
	 *            The x coordinate for this point.
	 * @param y
	 *            The y coordinate for this point.
	 */
	public Point(double x, double y) {
		super(x, y);
	}

	@Override
	public Point clone() {
		return new Point(x, y);
	}
}