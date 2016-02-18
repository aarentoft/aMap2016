package util;

import java.awt.geom.Rectangle2D;

/**
 * This class can be used to represent a rectangle, based on double values.
 */
@SuppressWarnings("serial")
public class Rectangle extends Rectangle2D.Double {
	/**
	 * Creates a rectangle, using two points as two opposite corners.
	 * 
	 * @param p1
	 *            first Point.
	 * @param p2
	 *            second Point.
	 */
	public Rectangle(Point p1, Point p2) {
		this(p1.x, p1.y, p2.x, p2.y);
	}

	/**
	 * Creates a rectangle, using two points as two opposite corners.
	 * 
	 * @param x1
	 *            The x-coordinate of the first point.
	 * @param y1
	 *            The y-coordinate of the first point.
	 * @param x2
	 *            The x-coordinate of the second point.
	 * @param y2
	 *            The y-coordinate of the second point.
	 */
	public Rectangle(double x1, double y1, double x2, double y2) {
		// Throw an error if the rectangle is a Point.
		if (x1 == x2 && y1 == y2) {
			throw new IllegalArgumentException(
					"x and y coordinates must not be the same");
		}
		// Make sure that min and max are the actual min and max.
		this.x = (x1 < x2) ? x1 : x2;
		this.width = ((x1 < x2) ? x2 : x1) - x;

		this.y = (y1 < y2) ? y1 : y2;
		this.height = ((y1 < y2) ? y2 : y1) - y;
	}

	/**
	 * @param center
	 *            The center of the Rectangle, represented as a Point
	 * @param width
	 *            The width of the Rectangle.
	 * @param height
	 *            The height of the Rectangle.
	 */
	public Rectangle(Point center, double width, double height) {
		this.x = center.x - width / 2;
		this.y = center.y - height / 2;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return The center of the rectangle, represented as a Point.
	 */
	public Point getCenter() {
		return new Point(getCenterX(), getCenterY());
	}

	/**
	 * @return The width of the rectangle divided by 2.
	 */
	public double getHalfWidth() {
		return this.width / 2;
	}

	/**
	 * @return The height of the rectangle divided by 2.
	 */
	public double getHalfHeight() {
		return this.height / 2;
	}

	/**
	 * Change the bounds of this rectangle.
	 * 
	 * @param factor
	 *            The factor to multiply the bounds with.
	 * @param point
	 *            The point which should be focused on when the zooming is done.
	 * @throws IllegalArgumentException
	 *             If the factor is not allowed (<= 0)
	 */
	public void setBounds(double factor, Point point)
			throws IllegalArgumentException {
		if (factor <= 0) {
			throw new IllegalArgumentException(
					"Negative or zero factor is not allowed");
		}
		double newWidth = getWidth() * factor;
		double newHeight = getHeight() * factor;

		// Find the percentages of the current placement of the focus point
		double pctX = (point.x - getMinX()) / getWidth();
		double pctY = (point.y - getMinY()) / getHeight();

		this.x = point.x - (newWidth * pctX);
		this.y = point.y - (newHeight * pctY);
		this.width = this.width * factor;
		this.height = this.height * factor;
	}

	/**
	 * Sets a new center for the Rectangle
	 * 
	 * @param center
	 *            The new center, represented as a Point.
	 */
	public void setCenter(Point center) {
		this.x = center.getX() - getHalfWidth();
		this.y = center.getY() - getHalfHeight();
	}

	/**
	 * Sets the aspect ratio of the Rectangle.
	 * 
	 * @param ratio
	 *            The aspect the as a double. E.g. 4:3 = 1.3333...
	 */
	public void setAspectRatio(double ratio) {
		// if we already have this dimension, there's no reason to set it again.
		// Note that the double math has an error margin, so we check if they
		// are less than .0000001 different
		if (Math.abs(getAspectRatio() - ratio) < 0.0000001) {
			return;
		}

		if (ratio > 1) {
			double newX1 = getCenterX() - getHalfWidth()
					* (ratio / getAspectRatio());
			double newX2 = getCenterX() + getHalfWidth()
					* (ratio / getAspectRatio());

			this.x = newX1;
			this.width = newX2 - x;
		} else if (ratio < 1) {
			double newY1 = getCenterY() - getHalfHeight()
					/ (ratio / getAspectRatio());
			double newY2 = getCenterY() + getHalfHeight()
					/ (ratio / getAspectRatio());

			this.y = newY1;
			this.height = newY2 - y;
		}
	}

	/**
	 * @return The aspect ratio of the Rectangle.
	 */
	public double getAspectRatio() {
		return getWidth() / getHeight();
	}

	/**
	 * @param constant
	 *            The scaling constant.
	 * @return A new scaled rectangle.
	 */
	public Rectangle scale(double constant) {
		Point center = getCenter();
		double width = this.width * constant;
		double height = this.height * constant;
		double x1 = center.x - width / 2;
		double y1 = center.y - height / 2;
		double x2 = center.x + width / 2;
		double y2 = center.y + height / 2;
		return new Rectangle(x1, y1, x2, y2);
	}

	@Override
	public Rectangle clone() {
		return new Rectangle(getCenter(), width, height);
	}

	/**
	 * Moves the rectangle.
	 * 
	 * @param p
	 *            is handled as a vector, moving the rectangle in the indicated
	 *            direction.
	 */
	public void move(Point p) {
		this.x += p.x;
		this.y += p.y;
	}
}