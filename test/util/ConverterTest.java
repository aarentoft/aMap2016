package util;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.io.IOException;

import model.MapModel;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConverterTest {
	static Converter converter;
	static MapModel model;

	@BeforeClass
	public static void initConverter() throws IOException {
		// UTM coordinates of the boundaries of Valby
		double valbyX1 = 718462.56835;
		double valbyY1 = 6171703.101570001;
		double valbyX2 = valbyX1 + 4014.420420000097;	// valbyX1 + Width of Valby's area
		double valbyY2 = valbyY1 + 4087.5294500002638;  // valbyY1 + Height of Valby's area

		// Create mock objects
		Rectangle bounds = new Rectangle(valbyX1, valbyY1, valbyX2, valbyY2);
		QuadTree qTree = new QuadTree(bounds);

		model = new MapModel(qTree);
		model.setMapDimension(new Dimension(400, 400));
		converter = new Converter(model);
	}

	@Test
	public void xUTM2PixelNormal() {
		assertEquals(258, converter.xUTM2Pixel(720050.2418));
	}

	@Test
	public void xUTM2PixelResultNegative() {
		assertEquals(-738, converter.xUTM2Pixel(710050.2418));
	}
	
	@Test
	public void xUTM2PixelResultZero() {
		assertEquals(0, converter.xUTM2Pixel(717450.9632));
	}
	
	@Test
	public void xUTM2PixelNegative() {
		assertEquals(-73907, converter.xUTM2Pixel(-24285.2347));
	}
	
	@Test
	public void xUTM2PixelZero() {
		assertEquals(-71488, converter.xUTM2Pixel(0));
	}

	@Test
	public void yUTM2PixelNormal() {
		assertEquals(276, converter.yUTM2Pixel(6173986.3733));
	}
	
	@Test
	public void yUTM2PixelNegative() {
		assertEquals(605108, converter.yUTM2Pixel(-6686.3733));
	}
	
	@Test
	public void yUTM2PixelZero() {
		assertEquals(604454, converter.yUTM2Pixel(0));
	}

	@Test
	public void yUTM2PixelResultNegative() {
		assertEquals(-85, converter.yUTM2Pixel(6177686.3733));
	}
	
	@Test
	public void yUTM2PixelResultZero() {
		assertEquals(0, converter.yUTM2Pixel(6176820.513382));
	}

	@Test
	public void xPixel2UTMNormal() {
		// Note that since the converter actively accounts for the bufferzone,
		// we need to subtract the bufferzone from the pixel-coordinates to make
		// them convert in a stable manor. Furthermore, since a pixel covers a
		// large area, we need to set the accepted marging of error to be the
		// "aspect ratio". The same goes for the below methods.
		assertEquals(720050.2418, converter.xPixel2UTM(258 - model
				.getBufferXOffset()), model.getXAxisCoordinateAspectRatio());
	}

	@Test
	public void xPixel2UTMNegative() {
		assertEquals(710050.2418, converter.xPixel2UTM(-738 - model
				.getBufferXOffset()), model.getXAxisCoordinateAspectRatio());
	}

	@Test
	public void xPixel2UTMZero() {
		assertEquals(718462.56835, converter.xPixel2UTM(0), model.getXAxisCoordinateAspectRatio());
	}

	@Test
	public void yPixel2UTMNormal() {
		assertEquals(6173986.37339, converter.yPixel2UTM(276 - model
				.getBufferYOffset()), model.getYAxisCoordinateAspectRatio());
	}

	@Test
	public void yPixel2UTMNegative() {
		assertEquals(6177686.37339, converter.yPixel2UTM(-85 - model
				.getBufferYOffset()), model.getYAxisCoordinateAspectRatio());
	}

	@Test
	public void yPixel2UTMZero() {
		assertEquals(6174176.05688, converter.yPixel2UTM(258 - model
				.getBufferYOffset()), model.getYAxisCoordinateAspectRatio());
	}
}