package util.graph;

import org.junit.Test;
import testhelpers.TestGraphBuilder;

import java.util.*;

import static org.junit.Assert.*;

public class GraphTest {
	Graph graph = TestGraphBuilder.getGraph3();

	@Test
	public void adjacencyListInt() {
		// Out degree = 4
		ArrayList<RoadEdge> exp = new ArrayList<RoadEdge>();
		exp.add(TestGraphBuilder.edgeB);
		exp.add(TestGraphBuilder.edgeC);
		exp.add(TestGraphBuilder.edgeD);
		exp.add(TestGraphBuilder.edgeM);
		List<RoadEdge> act = graph.getAdjacencyList("C");

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));

		// Out degree = 0
		act = graph.getAdjacencyList("I");
		exp.clear();

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		assertTrue(exp.equals(act));
	}

	@Test
	public void adjacencyListNode() {
		// Out degree = 4
		ArrayList<RoadEdge> exp = new ArrayList<RoadEdge>();
		exp.add(TestGraphBuilder.edgeB);
		exp.add(TestGraphBuilder.edgeC);
		exp.add(TestGraphBuilder.edgeD);
		exp.add(TestGraphBuilder.edgeM);
		List<RoadEdge> act = graph.getAdjacencyList(graph.getNodes().get("C"));

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));

		// Out degree = 0
		act = graph.getAdjacencyList(graph.getNodes().get("I"));
		exp.clear();

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		assertTrue(exp.equals(act));
	}

	@Test
	public void getNodes() {
		Map<String, RoadNode> exp = new HashMap<String, RoadNode>();
		exp.put("A", TestGraphBuilder.nodeA);
		exp.put("B", TestGraphBuilder.nodeB);
		exp.put("C", TestGraphBuilder.nodeC);
		exp.put("D", TestGraphBuilder.nodeD);
		exp.put("E", TestGraphBuilder.nodeE);
		exp.put("F", TestGraphBuilder.nodeF);
		exp.put("G", TestGraphBuilder.nodeG);
		exp.put("H", TestGraphBuilder.nodeH);
		exp.put("I", TestGraphBuilder.nodeI);
		Map<String, RoadNode> act = graph.getNodes();

		if (exp.size() != act.size())
			fail("The results are not the same size!");
		for (int i = 1; i <= exp.size() && i <= act.size(); i++) {
			assertTrue( exp.get(getCharForNumber(i)).equals(act.get(getCharForNumber(i))) );
		}

		// Completely empty graph
		Map<String, RoadNode> emptymap = Collections.emptyMap();
		Graph empty = new Graph(emptymap);
		act = empty.getNodes();
		if (emptymap.size() != act.size())
			fail("The results are not the same size!");
		for (int i = 1; i < exp.size() && i < act.size(); i++) {
			assertTrue( emptymap.get(getCharForNumber(i)).equals(act.get(getCharForNumber(i))) );
		}
	}

	@Test
	public void sizeGraph() {
		assertEquals(graph.size(), 9);
	}
	
	@Test
	public void sizeEmptyGraph() {
		Map<String, RoadNode> emptymap = Collections.emptyMap();
		Graph empty = new Graph(emptymap);
		assertEquals(empty.size(), 0);
	}

	/**
	 * @param i number to convert
	 * @return the alphabetic value for a number (1=A, 2=A, 3=C, etc.)
     */
	private String getCharForNumber(int i) {
		return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;
	}

}
