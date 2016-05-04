package util.graph;

import org.junit.Test;
import testhelpers.TestGraphBuilder;

import java.util.*;

import static org.junit.Assert.*;

public class GraphTest {
	private Graph graph = TestGraphBuilder.getGraph3();

	@Test
	public void adjacencyListInt() {
		// Out degree = 4
		ArrayList<RoadEdge> exp = new ArrayList<RoadEdge>();
		exp.add(TestGraphBuilder.edgeB);
		exp.add(TestGraphBuilder.edgeC);
		exp.add(TestGraphBuilder.edgeD);
		exp.add(TestGraphBuilder.edgeM);
		List<RoadEdge> act = graph.getAdjacencyList(3L); // 3 = C

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));

		// Out degree = 0
		act = graph.getAdjacencyList(9L); // 9 = I
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
		List<RoadEdge> act = graph.getAdjacencyList(graph.getNodes().get(3L)); // 3 = C

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));

		// Out degree = 0
		act = graph.getAdjacencyList(graph.getNodes().get(9L)); // 9 = I
		exp.clear();

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		assertTrue(exp.equals(act));
	}

	@Test
	public void getNodes() {
		Map<Long, RoadNode> exp = new HashMap<Long, RoadNode>();
		// 1 = A, 2 = B, etc.
		exp.put(1L, TestGraphBuilder.nodeA);
		exp.put(2L, TestGraphBuilder.nodeB);
		exp.put(3L, TestGraphBuilder.nodeC);
		exp.put(4L, TestGraphBuilder.nodeD);
		exp.put(5L, TestGraphBuilder.nodeE);
		exp.put(6L, TestGraphBuilder.nodeF);
		exp.put(7L, TestGraphBuilder.nodeG);
		exp.put(8L, TestGraphBuilder.nodeH);
		exp.put(9L, TestGraphBuilder.nodeI);
		Map<Long, RoadNode> act = graph.getNodes();

		if (exp.size() != act.size())
			fail("The results are not the same size!");
		for (long i = 1L; i <= exp.size() && i <= act.size(); i++) {
			assertTrue( exp.get(i).equals(act.get(i)) );
		}

		// Completely empty graph
		Map<Long, RoadNode> emptymap = Collections.emptyMap();
		Graph empty = new Graph(emptymap);
		act = empty.getNodes();
		if (emptymap.size() != act.size())
			fail("The results are not the same size!");
		for (Long i = 1L; i < exp.size() && i < act.size(); i++) {
			assertTrue( emptymap.get(i).equals(act.get(i)) );
		}
	}

	@Test
	public void sizeGraph() {
		assertEquals(graph.size(), 9);
	}
	
	@Test
	public void sizeEmptyGraph() {
		Map<Long, RoadNode> emptymap = Collections.emptyMap();
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
