package model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import util.MapLoader;
import util.QuadTree;
import util.graph.RoadEdge;

public class NameSearchModelTest {
	NameSearchModel nameSearchModel;
	QuadTree tree;

	@Before
	public void init() throws IOException {
		String dir = "";
		MapLoader mapLoader = new MapLoader("map-anholt-mini.osm");

		nameSearchModel = new NameSearchModel(mapLoader.getSearchTree());
		tree = mapLoader.getQuadTree();
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