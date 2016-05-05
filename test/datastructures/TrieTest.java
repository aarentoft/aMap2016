package datastructures;

import datastructures.graph.RoadEdge;
import datastructures.graph.RoadNode;
import datastructures.graph.RoadType;
import datastructures.graph.WayData;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrieTest {
	private Trie<RoadEdge> tree;

	@Before
	public void setUp() throws Exception {
		tree = new Trie<RoadEdge>();
	}

	@Test
	public void testExtreme() throws IOException {
		String[] roadNames = {"Æblevej","Ågården","Østerbro","Aarhus", "....","H.C. Andersens Boulevard", "", "A", "Asger Arentofts Allé", "Aaaaaa", "Aaaaab", "/£{&+#"};
		RoadNode rn1 = new RoadNode(8L, 721173.58884, 6174114.04414);
		RoadNode rn2 = new RoadNode(8L, 731173.58884, 6274114.04414);
		for(String roadName : roadNames) {
			tree.insert(new RoadEdge(new WayData(roadName, RoadType.MINOR_ROAD, true), rn1, rn2));
		}

		assertEquals("Æblevej", tree.query("Æ").get(0).data.roadname);
		//Case insensitive
		assertEquals("Æblevej", tree.query("æ").get(0).data.roadname);

		assertEquals("....", tree.query(".").get(0).data.roadname);
		List<RoadEdge> expected = new ArrayList<RoadEdge>();
		expected.add(tree.getAllContents().get(10));
		expected.add(tree.getAllContents().get(11));
		assertTrue(expected.equals(tree.query("Aaaa")));
		assertEquals(roadNames.length, tree.query("").size());
		assertEquals(0, tree.query(null).size());
		assertEquals(0, tree.query("Abevej").size());
	}
	
	@Test
	public void testDuplicates() throws Exception {
		RoadNode rn1 = new RoadNode(8L, 721173.58884, 6174114.04414);
		RoadNode rn2 = new RoadNode(8L, 731173.58884, 6274114.04414);
		WayData wd1 = new WayData("Søndergade", RoadType.MINOR_ROAD, true);
		WayData wd2 = new WayData("Hovedvejen", RoadType.MINOR_ROAD, true);

		for(int i = 0; i < 10; i++) {
			tree.insert(new RoadEdge(wd1, rn1, rn2));
		}

		assertEquals(1, tree.query("Søn").size());

		for(int i = 0; i < 10; i++) {
			tree.insert(new RoadEdge(wd2, rn1, rn2));
		}
		assertEquals("Hovedvejen", tree.query("Hov").get(0).data.roadname);
		assertEquals(2, tree.getAllContents().size());
	}
}