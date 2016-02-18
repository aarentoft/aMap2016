package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import util.graph.RoadEdge;
import util.graph.RoadNode;

public class TrieTest {
	Trie<RoadEdge> tree;

	@Before
	public void setUp() throws Exception {
		tree = new Trie<RoadEdge>();
	}

	@Test
	public void testExtreme() throws IOException {
		String[] roadNames = {"Æblevej","Ågården","Østerbro","Aarhus", "....","H.C. Andersens Boulevard", "", null, "A", "Asger Arentofts Allé", "Aaaaaa", "Aaaaab", "/£{&+#"};
		RoadNode rn1 = new RoadNode(8, 721173.58884, 6174114.04414);
		RoadNode rn2 = new RoadNode(8, 731173.58884, 6274114.04414);
//		for(String roadName : roadNames) {
//			tree.insert(new RoadEdge(new EdgeData("677,714,13.60581,199941,199941,6," + roadName + ",0,0,0,0,,,,,7027,7027,2500,2500,101,7300,0,,0,10,50,0.019,tf,,,10147200,07/21/00,1038528"), rn1, rn2));
//		}
//
//		assertEquals("Æblevej", tree.query("Æ").get(0).roadname);
//		//Case insensitive
//		assertEquals("Æblevej", tree.query("æ").get(0).roadname);
//
//		assertEquals("....", tree.query(".").get(0).roadname);
//		List<RoadEdge> expected = new ArrayList<RoadEdge>();
//		expected.add(tree.getAllContents().get(10));
//		expected.add(tree.getAllContents().get(11));
//		assertTrue(expected.equals(tree.query("Aaaa")));
//		assertEquals(roadNames.length, tree.query("").size());
//		assertEquals(0, tree.query(null).size());
//		assertEquals(0, tree.query("Abevej").size());
//
//		//This works in the tree, but is filtered out in the program
//		assertEquals("", tree.query(", 2500").get(0).roadname);
	}
	
	@Test
	public void testDuplicates() throws Exception {
		RoadNode rn1 = new RoadNode(8, 721173.58884, 6174114.04414);
		RoadNode rn2 = new RoadNode(8, 731173.58884, 6274114.04414);
		
//		for(int i = 0; i < 10; i++) {
//			tree.insert(new RoadEdge(new EdgeData("660,625,14.93378,199940,199940,6,'Søndergade',0,0,0,0,,,,,7030,7030,2500,2500,101,7300,0,,0,10,50,0.021,tf,,,10147200,07/21/00,1038521"), rn1, rn2));
//		}
//
//		assertEquals(1, tree.query("Søn").size());
//
//		for(int i = 0; i < 10; i++) {
//			tree.insert(new RoadEdge(new EdgeData("660,625,14.93378,199940,199940,6,'Hovedvejen',0,0,0,0,,,,,7030,7030,2500,2500,101,7300,0,,0,10,50,0.021,tf,,,10147200,07/21/00,1038521"), rn1, rn2));
//		}
//		assertEquals("Hovedvejen", tree.query("Hov").get(0).roadname);
//		assertEquals(2, tree.getAllContents().size());
	}
}