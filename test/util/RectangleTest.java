package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class RectangleTest {

	@Test
	public void equals() {
		Rectangle rect1 = new Rectangle(10, 12, 12, 10);
		Rectangle rect2 = new Rectangle(10, 12, 12, 10);
		assertEquals(rect1, rect2);
		rect1 = new Rectangle(-1, -2, -2, -1);
		rect2 = new Rectangle(-1, -2, -2, -1);
		assertEquals(rect1, rect2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructZero() {
		new Rectangle(0, 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructNotARect() {
		new Rectangle(10, 10, 10, 10);
	}

	@Test
	public void constructMax() {
		new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE,
				Integer.MIN_VALUE);
	}

	@Test
	public void constructNegative() {
		new Rectangle(-10, -10, -5, -5);
	}

	@Test
	public void constructAutocorrectPoints() {
		Rectangle rect1 = new Rectangle(10, 12, 5, 6);
		Rectangle rect2 = new Rectangle(5, 6, 10, 12);
		assertEquals(rect1, rect2);
	}

	@Test
	public void intersectCorner() {
		Rectangle rect1 = new Rectangle(10, 12, 12, 10);
		Rectangle rect2 = new Rectangle(9, 13, 11, 11);

		assertTrue(rect1.intersects(rect2));
		assertTrue(rect2.intersects(rect1));
	}

	@Test
	public void intersectOneSide() {
		Rectangle rect1 = new Rectangle(10, 12, 12, 10);
		Rectangle rect2 = new Rectangle(10, 13, 12, 11);

		assertTrue(rect1.intersects(rect2));
		assertTrue(rect2.intersects(rect1));
	}

	@Test
	public void intersectInside() {
		Rectangle rect1 = new Rectangle(11, 13, 13, 11);
		Rectangle rect2 = new Rectangle(10, 14, 14, 10);

		assertTrue(rect1.intersects(rect2));
		assertTrue(rect2.intersects(rect1));
	}

	@Test
	public void containsCorner() {
		Rectangle rect1 = new Rectangle(10, 12, 12, 10);
		Rectangle rect2 = new Rectangle(9, 13, 11, 11);

		assertFalse(rect1.contains(rect2));
		assertFalse(rect2.contains(rect1));
	}

	@Test
	public void containsOneSide() {
		Rectangle rect1 = new Rectangle(10, 12, 12, 10);
		Rectangle rect2 = new Rectangle(10, 13, 12, 11);

		assertFalse(rect1.contains(rect2));
		assertFalse(rect2.contains(rect1));
	}

	@Test
	public void containsInside() {
		Rectangle rect1 = new Rectangle(11, 13, 13, 11);
		Rectangle rect2 = new Rectangle(10, 14, 14, 10);

		assertFalse(rect1.contains(rect2));
		assertTrue(rect2.contains(rect1));
	}

	@Test
	public void setBoundsPositive() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setBounds(2, new Point(15, 15));

		assertEquals(new Rectangle(5, 5, 25, 25), rect1);
	}

	@Test
	public void setBoundsNegativ() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setBounds(0.5, new Point(15, 15));

		assertEquals(new Rectangle(12.5, 12.5, 17.5, 17.5), rect1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setBoundsZero() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setBounds(0, new Point(15, 15));
	}

	@Test
	public void setBoundsUnchanged() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setBounds(1, new Point(15, 15));

		assertEquals(new Rectangle(10, 10, 20, 20), rect1);
	}

	@Test
	public void setCenter() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setCenter(new Point(20, 20));

		assertEquals(new Rectangle(15, 15, 25, 25), rect1);
	}

	@Test
	public void setAspectRatioX() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setAspectRatio(2);

		assertEquals(new Rectangle(5, 10, 25, 20), rect1);
	}

	@Test
	public void setAspectRatioY() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setAspectRatio(0.5);

		assertEquals(new Rectangle(10, 5, 20, 25), rect1);
	}

	@Test
	public void setAspectRatioUnchanged() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);
		rect1.setAspectRatio(1);

		assertEquals(new Rectangle(10, 10, 20, 20), rect1);
	}

	@Test
	public void scaleUp() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);

		assertEquals(new Rectangle(5, 5, 25, 25), rect1.scale(2));
	}

	@Test
	public void scaleDown() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);

		assertEquals(new Rectangle(12.5, 12.5, 17.5, 17.5), rect1.scale(0.5));
	}

	@Test
	public void scaleUnchanged() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);

		assertEquals(new Rectangle(10, 10, 20, 20), rect1.scale(1));
	}

	@Test
	public void movePositiv() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);

		rect1.move(new Point(10, 10));

		assertEquals(new Rectangle(20, 20, 30, 30), rect1);
	}

	@Test
	public void moveNegativ() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);

		rect1.move(new Point(-10, -10));

		assertEquals(new Rectangle(0, 0, 10, 10), rect1);
	}

	@Test
	public void movePositivAndNegative() {
		Rectangle rect1 = new Rectangle(10, 10, 20, 20);

		rect1.move(new Point(10, -10));

		assertEquals(new Rectangle(20, 0, 30, 10), rect1);
	}
}