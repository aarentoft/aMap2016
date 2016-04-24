package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import util.*;
import util.graph.RoadEdge;
import util.graph.RoadType;

public class MapModelTest {
	protected MapModel model;
	protected QuadTree tree;

	@Before
	public void init() throws IOException {
		String testDataFilePath = System.getProperty("testDataPath");

		MapLoader mapLoader;
		try {
			mapLoader = new MapLoader(testDataFilePath);
			tree = mapLoader.getQuadTree();
			model = new MapModel(tree);
			model.setMapDimension(new Dimension(400, 400));
		} catch (IOException | NullPointerException e) {
			System.out.println("MapModelTest:\n" +
					"\ttestDataFilePath variable not set or invalid.\n" +
					"\tUse the Java VM option -DtestDataPath=<path to test data file>.\n" +
					"\tTest data file should be in the OSM format.\n");
			org.junit.Assume.assumeNoException(e);
		}
	}

	@Test
	public void allEdges() {
		assertEquals(tree.getContents(), model.getEdgeSet());
	}

	@Test
	public void someEdgesByNoType() {
		Set<RoadEdge> expected = new HashSet<RoadEdge>();
		assertEquals(expected, model.getEdgeSet(model
				.getBounds(), null));
	}
	
	@Test
	public void someEdgesByOneType() {
		Set<RoadEdge> expected = new HashSet<RoadEdge>();
		for (RoadEdge roadEdge : tree.getContents()) {
			if (roadEdge.data.type == RoadType.SECONDARY_ROAD) {
				expected.add(roadEdge);
			}
		}
		
		List<RoadType> types = new ArrayList<RoadType>();
		types.add(RoadType.SECONDARY_ROAD);
		
		assertEquals(expected, model.getEdgeSet(model
				.getBounds(), types));
	}
	
	@Test
	public void someEdgesByMultipleTypes() {
		Set<RoadEdge> expected = new HashSet<RoadEdge>();
		for (RoadEdge roadEdge : tree.getContents()) {
			if (RoadType.getAllDrivableRoads().contains(roadEdge.data.type)) {
				expected.add(roadEdge);
			}
		}
		
		assertEquals(expected, model.getEdgeSet(model
				.getBounds(), RoadType.getAllDrivableRoads()));
	}

	@Test
	public void someEdgesByArea() {
		Converter c = new Converter(model);

		Rectangle utmBounds = new Rectangle(c
				.pointPixel2UTM(new Point(200, 200)), c
				.pointPixel2UTM(new Point(350, 350)));
		
		model.setBounds(utmBounds);

		Set<RoadEdge> expected = new HashSet<RoadEdge>();
		for (RoadEdge roadEdge : tree.getContents()) {
			if ((roadEdge.intersects(utmBounds) || utmBounds.contains(roadEdge))
					&& RoadType.getRoadtypesFromZoomLevel(
							model.getXAxisCoordinateAspectRatio()).contains(
							roadEdge.data.type)) {
				expected.add(roadEdge);
			}
		}

		Set<RoadEdge> actual = model.getEdgeSet();

		for (RoadEdge roadEdge : expected) {
			assertTrue(actual.contains(roadEdge));
		}
	}
	
	@Test
	public void setBoundsNormal() {
		Converter c = new Converter(model);
		
		Rectangle utmBounds = new Rectangle(c
				.pointPixel2UTM(new Point(200, 200)), c
				.pointPixel2UTM(new Point(350, 350)));
		model.setBounds(utmBounds);
		
		assertEquals(utmBounds, model.getBounds());
	}
	
	@Test
	public void setBoundsMinimumLimit() {
		Converter c = new Converter(model);
		
		Rectangle smallBounds = new Rectangle(c
				.pointPixel2UTM(new Point(200, 200)), c
				.pointPixel2UTM(new Point(205, 205)));
		
		Rectangle expected = model.getBounds();
		
		model.setBounds(smallBounds);
		
		assertEquals(expected, model.getBounds());
	}

	@Test
	public void setBoundsExtremeMaximum() {
		model
				.setBounds(new Rectangle(0, 0, Double.MAX_VALUE,
						Double.MAX_VALUE));
		assertEquals(tree.getBounds(), model.getBounds());
	}

	@Test
	public void setBoundsExtremeMinimum() {
		model
				.setBounds(new Rectangle(Double.MIN_VALUE, Double.MIN_VALUE, 0,
						0));
		
		assertEquals(tree.getBounds(), model.getBounds());
	}

	@Test
	public void setBoundsExtreme() {
		model.setBounds(new Rectangle(Double.MIN_VALUE, Double.MIN_VALUE,
				Double.MAX_VALUE, Double.MAX_VALUE));
		
		assertEquals(tree.getBounds(), model.getBounds());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setBoundszeroArea() {
		model.setBounds(new Rectangle(50, 50, 50, 50));
	}
}