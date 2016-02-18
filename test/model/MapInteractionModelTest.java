package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Point;
import util.Rectangle;

public class MapInteractionModelTest {

	@Test
	public void dragBoxZoomNormal() {
		MapInteractionModel model = new MapInteractionModel();

		model.setDragBoxZoomCorners(new Point(10, 10), new Point(210, 210));

		assertEquals(new Rectangle(10, 10, 210, 210), model
				.getDragBoxZoomRectangle());
		assertEquals(new Rectangle(105, 105, 115, 115), model
				.getDragBoxZoomCenterRectangle());
	}

	@Test
	public void dragBoxZoomNegative() {
		MapInteractionModel model = new MapInteractionModel();

		model.setDragBoxZoomCorners(new Point(-10, -10), new Point(210, 210));

		assertEquals(new Rectangle(-10, -10, 210, 210), model
				.getDragBoxZoomRectangle());
		assertEquals(new Rectangle(94.5, 94.5, 105.5, 105.5), model
				.getDragBoxZoomCenterRectangle());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void dragBoxZoomZero() {
		MapInteractionModel model = new MapInteractionModel();

		model.setDragBoxZoomCorners(new Point(50, 50), new Point(50, 50));
	}
	
	@Test
	public void panningPositive() {
		MapInteractionModel model = new MapInteractionModel();
		model.movePanningPoint(new Point(10, 10));
		
		assertEquals(new Point(10, 10), model.getPanningPoint());
		model.resetPanningPoint();
		assertEquals(new Point(0, 0), model.getPanningPoint());
	}
	
	@Test
	public void panningNegative() {
		MapInteractionModel model = new MapInteractionModel();
		model.movePanningPoint(new Point(-10, -10));
		
		assertEquals(new Point(-10, -10), model.getPanningPoint());
		model.resetPanningPoint();
		assertEquals(new Point(0, 0), model.getPanningPoint());
	}
	
	@Test
	public void panningBothPositivNegative() {
		MapInteractionModel model = new MapInteractionModel();
		model.movePanningPoint(new Point(10, -10));
		
		assertEquals(new Point(10, -10), model.getPanningPoint());
		model.resetPanningPoint();
		assertEquals(new Point(0, 0), model.getPanningPoint());
	}
	
	@Test
	public void panningZero() {
		MapInteractionModel model = new MapInteractionModel();
		model.movePanningPoint(new Point(0, 0));
		
		assertEquals(new Point(0, 0), model.getPanningPoint());
	}
}
