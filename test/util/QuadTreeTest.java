package util;

import org.junit.Before;
import org.junit.Test;
import util.graph.RoadEdge;
import util.graph.RoadNode;
import util.graph.RoadType;
import util.graph.WayData;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuadTreeTest {
	private QuadTree tree;

	@Before
	public void setUp() throws Exception {
		// Construct a new QuadTree with a capacity of 2
		tree = new QuadTree(new Rectangle(0, 0, 200, 200), 2);
	}

	@Test
	public void testPositive() throws Exception {
		RoadNode rn1 = new RoadNode(8L, 55, 55);
		RoadNode rn2 = new RoadNode(8L, 66, 66);
		RoadNode rn3 = new RoadNode(8L, 51, 199);
		RoadNode rn4 = new RoadNode(8L, 45, 45);
		RoadNode rn5 = new RoadNode(8L, 51, 51);
		RoadNode rn6 = new RoadNode(8L, 52, 52);
		RoadNode rn7 = new RoadNode(8L, 5, 5);
		RoadNode rn8 = new RoadNode(8L, 20, 20);
		RoadNode rn9 = new RoadNode(8L, 90, 90);
		RoadNode rn10 = new RoadNode(8L, 98, 9);
		RoadNode rn12 = new RoadNode(8L, 9, 9);

		WayData wd1 = new WayData("Søndergade", RoadType.MINOR_ROAD, true);

		RoadEdge re1 = new RoadEdge(wd1, rn1, rn2);
		RoadEdge re2 = new RoadEdge(wd1, rn3, rn4);
		RoadEdge re3 = new RoadEdge(wd1, rn5, rn6);
		RoadEdge re4 = new RoadEdge(wd1, rn7, rn8);
		RoadEdge re5 = new RoadEdge(wd1, rn9, rn10);
		RoadEdge re6 = new RoadEdge(wd1, rn9, rn12);

		tree.insert(re1);
		tree.insert(re2);
		tree.insert(re3);
		tree.insert(re4);
		tree.insert(re5);
		tree.insert(re6);

		Rectangle queryArea;
		Collection<RoadEdge> expected;

		// getContents() method
		expected = Arrays.asList(re1, re2, re3, re4, re5, re6);
		assertTrue(tree.getContents().containsAll(expected));

		// query() method

		// Test Bounds, should return all edges
		assertEquals(tree.getContents(),
				tree.query(tree.getBounds(), RoadType.getAllRoadTypes()));

		// Test Bounds, should return all edges
		queryArea = new Rectangle(0, 0, 200, 200);
		expected = tree.getContents();
		assertTrue(tree.query(queryArea, RoadType.getAllRoadTypes())
				.containsAll(expected));

		// Rectangle intersecting with tree bounds
		queryArea = new Rectangle(0, 0, 10, 10);
		expected = Arrays.asList(re4, re6);
		assertEquals(2, tree.query(queryArea, RoadType.getAllRoadTypes())
				.size());
		assertTrue(tree.query(queryArea, RoadType.getAllRoadTypes())
				.containsAll(expected));

		// Rectangle intersecting with tree bounds
		queryArea = new Rectangle(0, 0, 30, 30);
		expected = Arrays.asList(re4, re6, re2);
		assertEquals(3, tree.query(queryArea, RoadType.getAllRoadTypes())
				.size());
		assertTrue(tree.query(queryArea, RoadType.getAllRoadTypes())
				.containsAll(expected));

		// Rectangle contained by subnode
		queryArea = new Rectangle(40, 40, 49, 49);
		expected = Arrays.asList(re2, re6);
		assertEquals(2, tree.query(queryArea, RoadType.getAllRoadTypes())
				.size());
		assertTrue(tree.query(queryArea, RoadType.getAllRoadTypes())
				.containsAll(expected));

		// Rectangle intersecting with subnode
		queryArea = new Rectangle(50, 50, 60, 60);
		expected = Arrays.asList(re1, re3, re2, re6);
		assertEquals(4, tree.query(queryArea, RoadType.getAllRoadTypes())
				.size());
		assertTrue(tree.query(queryArea, RoadType.getAllRoadTypes())
				.containsAll(expected));

		// Rectangle intersecting with edges
		queryArea = new Rectangle(66, 66, 70, 70);
		expected = Arrays.asList(re1, re6);
		assertEquals(2, tree.query(queryArea, RoadType.getAllRoadTypes())
				.size());
		assertTrue(tree.query(queryArea, RoadType.getAllRoadTypes())
				.containsAll(expected));
	}

	@Test
	public void testNegative() throws Exception {
		RoadNode rn1 = new RoadNode(8L, -1, 23);
		RoadNode rn2 = new RoadNode(8L, 66, 66);
		RoadNode rn3 = new RoadNode(8L, 0, -1);

		WayData wdA = new WayData("Agade", RoadType.MINOR_ROAD, true);
		WayData wdB = new WayData("Bgade", RoadType.MINOR_ROAD, true);
		WayData wdC = new WayData("Cgade", RoadType.MINOR_ROAD, true);

		RoadEdge re1 = new RoadEdge(wdA, rn1, rn2);
		RoadEdge re2 = new RoadEdge(wdB, rn2, rn3);
		RoadEdge re3 = new RoadEdge(wdC, rn1, rn3);

		tree.insert(re1);
		tree.insert(re2);
		tree.insert(re3);

		Collection<RoadEdge> expected = Arrays.asList(re1, re2);
		assertEquals(2, tree.getContents().size());
		assertTrue(expected.containsAll(tree.getContents()));
	}

	@Test
	public void testDuplicateEdges() throws Exception {
		RoadNode rn1 = new RoadNode(8L, 57, 57);
		RoadNode rn2 = new RoadNode(8L, 68, 68);
		WayData wd1 = new WayData("Søndergade", RoadType.MINOR_ROAD, true);
		RoadEdge re1 = new RoadEdge(wd1, rn1, rn2);
		RoadEdge re2 = new RoadEdge(wd1, rn1, rn2);

		tree.insert(re1);
		tree.insert(re2);
		assertEquals(1, tree.getContents().size());
		assertTrue(tree.getContents().iterator().next() == re1);
		assertFalse(tree.getContents().iterator().next() == re2);
	}
}