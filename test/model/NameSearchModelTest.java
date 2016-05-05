package model;

import datastructures.QuadTree;
import datastructures.graph.RoadEdge;
import org.junit.Before;
import org.junit.Test;
import util.io.OsmosisMapLoader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NameSearchModelTest {
	NameSearchModel nameSearchModel;
	QuadTree tree;

	@Before
	public void init() throws IOException {
		String testDataFilePath = System.getProperty("testDataPath");

		OsmosisMapLoader mapLoader;
		try {
			mapLoader = new OsmosisMapLoader(testDataFilePath, null);
			nameSearchModel = new NameSearchModel(mapLoader.getSearchTree());
			tree = mapLoader.getQuadTree();
		} catch (IOException|NullPointerException e) {
			System.out.println("NameSearchModelTest:\n" +
					"\ttestDataFilePath variable not set or invalid.\n" +
					"\tUse the Java VM option -DtestDataPath=<path to test data file>.\n" +
					"\tTest data file should be in the OSM format.\n");
			org.junit.Assume.assumeNoException(e);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void queryNoLetters() {
		String q = "";

		nameSearchModel.doRoadNameSearch(q);
	}

	@Test
	public void queryOneLetter() {
		String q = "v";

		Set<String> expected = new HashSet<String>();

		for (RoadEdge roadEdge : tree.getContents()) {
			if (roadEdge.data.roadname.toLowerCase().startsWith(q.toLowerCase())) {
				expected.add(roadEdge.data.roadname);
			}
		}

		nameSearchModel.doRoadNameSearch(q);

		Set<String> actual = new HashSet<String>();

		for (RoadEdge roadEdge : nameSearchModel.getRoadNameSearchResult()) {
			actual.add(roadEdge.data.roadname);
		}
		
		assertEquals(expected, actual);
	}

	@Test
	public void queryTwoLetters() {
		String q = "ve";

		Set<String> expected = new HashSet<String>();

		for (RoadEdge roadEdge : tree.getContents()) {
			if (roadEdge.data.roadname.toLowerCase().startsWith(q.toLowerCase())) {
				expected.add(roadEdge.data.roadname);
			}
		}

		nameSearchModel.doRoadNameSearch(q);

		Set<String> actual = new HashSet<String>();

		for (RoadEdge roadEdge : nameSearchModel.getRoadNameSearchResult()) {
			actual.add(roadEdge.data.roadname);
		}
		
		assertEquals(expected, actual);
	}

	@Test
	public void queryTwoLettersWithDanishLetter() {
		String q = "æb";

		Set<String> expected = new HashSet<String>();

		for (RoadEdge roadEdge : tree.getContents()) {
			if (roadEdge.data.roadname.toLowerCase().startsWith(q.toLowerCase())) {
				expected.add(roadEdge.data.roadname);
			}
		}

		nameSearchModel.doRoadNameSearch(q);

		Set<String> actual = new HashSet<String>();

		for (RoadEdge roadEdge : nameSearchModel.getRoadNameSearchResult()) {
			actual.add(roadEdge.data.roadname);
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void queryWithSpace() {
		String q = "Ole Bor";

		Set<String> expected = new HashSet<String>();

		for (RoadEdge roadEdge : tree.getContents()) {
			if (roadEdge.data.roadname.toLowerCase().startsWith(q.toLowerCase())) {
				expected.add(roadEdge.data.roadname);
			}
		}

		nameSearchModel.doRoadNameSearch(q);

		Set<String> actual = new HashSet<String>();

		for (RoadEdge roadEdge : nameSearchModel.getRoadNameSearchResult()) {
			actual.add(roadEdge.data.roadname);
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void queryWithSpaceAndDanishLetter() {
		String q = "Danshøj ";

		Set<String> expected = new HashSet<String>();

		for (RoadEdge roadEdge : tree.getContents()) {
			if (roadEdge.data.roadname.toLowerCase().startsWith(q.toLowerCase())) {
				expected.add(roadEdge.data.roadname);
			}
		}

		nameSearchModel.doRoadNameSearch(q);

		Set<String> actual = new HashSet<String>();

		for (RoadEdge roadEdge : nameSearchModel.getRoadNameSearchResult()) {
			actual.add(roadEdge.data.roadname);
		}
		
		assertEquals(expected, actual);
	}

	@Test
	public void noResults() {
		String q = "icannotbefound";

		Set<String> expected = new HashSet<String>();

		for (RoadEdge roadEdge : tree.getContents()) {
			if (roadEdge.data.roadname.toLowerCase().startsWith(q.toLowerCase())) {
				expected.add(roadEdge.data.roadname);
			}
		}

		nameSearchModel.doRoadNameSearch(q);

		Set<String> actual = new HashSet<String>();

		for (RoadEdge roadEdge : nameSearchModel.getRoadNameSearchResult()) {
			actual.add(roadEdge.data.roadname);
		}

		assertEquals(expected, actual);
		assertEquals(actual.size(), 0);
	}
}