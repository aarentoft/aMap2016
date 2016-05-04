package util;

import exceptions.PathNotFoundException;
import org.junit.Test;
import testhelpers.TestGraphBuilder;
import util.graph.Graph;
import util.graph.RoadEdge;
import util.graph.RoadNode;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DijkstraTest {
	// ---------- Positive tests ----------
	// ------- All nodes connected --------


	// -------------------- Positive -----------------------

	@Test
	public void distToTarget() {
		try {
			RoadNode nodeA = new RoadNode(1, 2, 6);
			RoadNode nodeB = new RoadNode(2, 6, 9);

			double actResult = nodeA.distance(nodeB);
			assertEquals(5.0, actResult, 0.01);

			// Coordinates result in negative length
			actResult = nodeB.distance(nodeA);
			assertEquals(5.0, actResult, 0.01);
		} catch (Exception e) {
			System.out.println("distToTarget: Oh no. RoadNode initialisation failed.");
		}

	}

	// ---------- All nodes connected (graph 1-3) ----------

	/**
	 * A* and Dijkstra on graph #1 (undirected)
	 */
	@Test
	public void undirected() {
		Graph graph = TestGraphBuilder.getGraph1();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(TestGraphBuilder.bidirectionalEdgeM);
		expResult.add(TestGraphBuilder.bidirectionalEdgeG);
		expResult.add(TestGraphBuilder.bidirectionalEdgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.DijkstraSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);
	}

	/**
	 * A* and Dijkstra on graph #2 (One-way w/o obstacles)
	 */
	@Test
	public void oneWayNoObstacles() {
		Graph graph = TestGraphBuilder.getGraph2();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(TestGraphBuilder.edgeM);
		expResult.add(TestGraphBuilder.edgeG);
		expResult.add(TestGraphBuilder.edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.DijkstraSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);
	}

	/**
	 * A* and Dijkstra on graph #3 (One-way w/ obstacles)
	 */
	@Test
	public void oneWayObstacles() {
		Graph graph = TestGraphBuilder.getGraph3();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(TestGraphBuilder.edgeM);
		expResult.add(TestGraphBuilder.edgeH);
		expResult.add(TestGraphBuilder.edgeI);
		expResult.add(TestGraphBuilder.edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.DijkstraSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);
	}

	// ---------- Groups of nodes (graph 4-6)

	// ---------- Groups of separated nodes ----------

	/**
	 * A* and Dijkstra on graph #4 (Undirected groups)
	 */
	@Test
	public void undirectedGroups() {
		Graph graph = TestGraphBuilder.getGraph4();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(TestGraphBuilder.bidirectionalEdgeG);
		expResult.add(TestGraphBuilder.bidirectionalEdgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.AStarSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 5 = E, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 5 = E, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);
	}

	/**
	 * A* and Dijkstra on graph #5 (One-way groups w/o obstacles)
	 */
	@Test
	public void oneWayNoObstaclesGroups() {
		Graph graph = TestGraphBuilder.getGraph5();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(TestGraphBuilder.edgeG);
		expResult.add(TestGraphBuilder.edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.AStarSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 5 = C, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 5 = E, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);
	}

	/**
	 * A* and Dijkstra on graph #6 (One-way groups w/ obstacles)
	 */
	@Test
	public void oneWayObstaclesGroups() {
		Graph graph = TestGraphBuilder.getGraph6();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(TestGraphBuilder.edgeH);
		expResult.add(TestGraphBuilder.edgeI);
		expResult.add(TestGraphBuilder.edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.AStarSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 5 = E, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 5 = E, 9 = I
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);
	}



	// -------------------- Negative -----------------------

	// ---------- All nodes connected (graph 7) ------------
	// ---------- Negative tests ----------
	@Test
	public void directedEdgesPreventPath() {
		Graph graph = TestGraphBuilder.getGraph7();

		try {
			Dijkstra.DijkstraSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 3 = C, 9 = I

			// No exception was thrown so fail the test
			fail("Dijkstra should've thrown an exception!");
		} catch (PathNotFoundException expected) {
			// Exception thrown, test passed
		}

		try {
			Dijkstra.AStarSearch(graph, graph.getNodes().get(5L), graph.getNodes().get(9L)); // 3 = C, 9 = I

			// No exception was thrown so fail the test
			fail("A* should've thrown an exception!");
		} catch (PathNotFoundException expected) {
			// Exception thrown, test passed
		}
	}

	// ---------- Groups of separated nodes ----------
	/**
	 * A* and Dijkstra on test graph #4 (undirected groups) negative
	 */
	@Test
	public void negUndirectedGroups() {
		Graph graph = TestGraphBuilder.getGraph4();
		try {
			Dijkstra.DijkstraSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
			fail("Dijkstra should've thrown an exception!");
		} catch (PathNotFoundException e) {
		}

		try {
			Dijkstra.AStarSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
			fail("AStar should've thrown an exception!");
		} catch (PathNotFoundException e) {
		}
	}

	/**
	 * A* and Dijkstra on test graph #6 (One-way w/ obstacles) negative
	 */
	@Test
	public void negOneWayObstaclesGroups() {
		Graph graph = TestGraphBuilder.getGraph6();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(TestGraphBuilder.edgeH);
		expResult.add(TestGraphBuilder.edgeI);
		expResult.add(TestGraphBuilder.edgeJ);

		try {
			Dijkstra.DijkstraSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
			fail("Dijkstra should've thrown an exception!");
		} catch (PathNotFoundException e) {
		}

		try {
			Dijkstra.AStarSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
			fail("A* should've thrown an exception!");
		} catch (PathNotFoundException e) {
		}
	}

	// ---------------- No edges (graph 8) -----------------
	/**
	 * A* and Dijkstra on a graph with no edges
	 */
	@Test
	public void negDijkstraNoEdges() {
		Graph graph = TestGraphBuilder.getGraph8();

		try {
			Dijkstra.DijkstraSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
			fail("should've thrown an exception!");
		} catch (PathNotFoundException expected) {
		}

		try {
			Dijkstra.AStarSearch(graph, graph.getNodes().get(3L), graph.getNodes().get(9L)); // 3 = C, 9 = I
			fail("should've thrown an exception!");
		} catch (PathNotFoundException expected) {
		}
	}

}