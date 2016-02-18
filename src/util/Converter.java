package util;

import model.MapModel;

/**
 * Handles conversion from/to UTM-coordinates to/from pixel-coordinates.
 */
public class Converter {
	protected MapModel mapModel;

	/**
	 * Creates a new Converter with access the the {@link MapModel} which
	 * contains the details about bounds and map dimension.
	 * 
	 * @param model
	 *            The {@link MapModel} to base this converter on.
	 */
	public Converter(MapModel model) {
		this.mapModel = model;
	}

	/**
	 * Converts an UTM-x-coordinate to a pixel-x-coordinate.
	 * 
	 * @param utmX
	 *            x-coordinate (UTM)
	 * @return x-coordinate (pixel)
	 */
	public int xUTM2Pixel(double utmX) {
		int pixelX = (int) ((utmX - mapModel.getBounds().getMinX())
				/ mapModel.getBounds().getWidth() * mapModel.getMapDimension()
				.getWidth());
		return pixelX + mapModel.getBufferXOffset();
	}

	/**
	 * Converts a UTM-y-coordinate to a pixel-y-coordinate.
	 * 
	 * @param utmY
	 *            y-coordinate (UTM)
	 * @return y-coordinate (pixel)
	 */
	public int yUTM2Pixel(double utmY) {
		int pixelY = (int) (mapModel.getMapDimension().getHeight() - ((utmY - mapModel
				.getBounds().getMinY()) / mapModel.getBounds().getHeight())
				* mapModel.getMapDimension().getHeight());
		return pixelY + mapModel.getBufferYOffset();
	}

	/**
	 * Shorthand method for calling the other conversion methods on a point.
	 * 
	 * @param p
	 *            The pixel point to converter
	 * @return A point in UTM coordinates corrosponding to the given pixel
	 *         point.
	 */
	public Point pointUTM2Pixel(Point p) {
		return new Point(xUTM2Pixel(p.getX()), yUTM2Pixel(p.getY()));
	}

	/**
	 * Convenience method to call xPixel2UTM(pixelX, false)
	 * 
	 * @param pixelX
	 *            x-coordinate (pixel)
	 * @return x-coordinate (UTM)
	 */
	public double xPixel2UTM(int pixelX) {
		return xPixel2UTM(pixelX, false);
	}

	/**
	 * Converts a pixel-x-coordinate to a UTM-x-coordinate. (Uses same formula
	 * as gFromX, just reversed)
	 * 
	 * @param pixelX
	 *            x-coordinate (pixel)
	 * @param relative
	 *            Whether or not the conversion should be relative. If it's
	 *            relative, the point will only be converted into the
	 *            corresponding values in UTM, without the addition of the
	 *            limits of the current UTM view.
	 * @return x-coordinate (UTM)
	 */
	public double xPixel2UTM(int pixelX, boolean relative) {
		// XXX Is it bad design that the converter accounts for the buffer zone?
		// This makes the conversion unstable.
		if (!relative) {
			pixelX = pixelX
					+ (int) (mapModel.getBufferMapDimension().width / 2 - mapModel
							.getBufferCenter().x);
		}
		double utmX = pixelX * mapModel.getBounds().getWidth()
				/ mapModel.getMapDimension().getWidth();
		if (!relative) {
			utmX += mapModel.getBounds().getMinX();
		}
		return utmX;
	}

	/**
	 * Convenience to yPixel2UTM(pixelY, false)
	 * 
	 * @param pixelY
	 *            y-coordinate (pixel)
	 * @return y-coordinate (UTM)
	 */
	public double yPixel2UTM(int pixelY) {
		return yPixel2UTM(pixelY, false);
	}

	/**
	 * Converts a pixel-y-coordinate to a UTM-y-coordinate. (Uses same formula
	 * as gFromY, just reversed)
	 * 
	 * @param pixelY
	 *            y-coordinate (pixel)
	 * @return y-coordinate (UTM)
	 */
	public double yPixel2UTM(int pixelY, boolean relative) {
		// XXX Is it bad design that the converter accounts for the buffer zone?
		// This makes the conversion unstable.
		if (!relative) {
			pixelY = pixelY
					- (int) (mapModel.getBufferMapDimension().height / 2 - mapModel
							.getBufferCenter().y);
		}
		double utmY = -pixelY * mapModel.getBounds().getHeight()
				/ mapModel.getMapDimension().getHeight();
		if (!relative) {
			utmY += mapModel.getBounds().getHeight()
					+ mapModel.getBounds().getMinY();
		}
		return utmY;
	}

	/**
	 * Shorthand method for calling the other conversion methods on a point.
	 * 
	 * @param p
	 *            The UTM point to converter
	 * @return A point in pixel coordinates corrosponding to the given UTM
	 *         point.
	 */
	public Point pointPixel2UTM(Point p) {
		return new Point(xPixel2UTM((int) p.getX()), yPixel2UTM((int) p.getY()));
	}

	/**
	 * This method will convert a {@link Point} from pixel coordinates to UTM
	 * coordinates
	 * 
	 * @param p
	 *            The point to convert
	 * @param relative
	 *            Whether or not the conversion should be relative. If it's
	 *            relative, the point will only be converted into the
	 *            corresponding values in UTM, without the addition of the
	 *            limits of the current UTM view.
	 * @return A {@link Point} in UTM units.
	 */
	public Point pointPixel2UTM(Point p, boolean relative) {
		return new Point(xPixel2UTM((int) p.getX(), relative), yPixel2UTM(
				(int) p.getY(), relative));
	}
}