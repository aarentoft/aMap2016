package util.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class GraphTest {
	Graph graph = null;
	List<RoadNode> nodes = new ArrayList<RoadNode>();
	List<RoadEdge> edges = new ArrayList<RoadEdge>();
	RoadEdge edgeA;
	RoadNode nodeA = null;
	RoadEdge edgeB;
	RoadNode nodeB = null;
	RoadEdge edgeC;
	RoadNode nodeC = null;
	RoadEdge edgeD;
	RoadNode nodeD = null;
	RoadEdge edgeE;
	RoadNode nodeE = null;
	RoadEdge edgeF;
	RoadNode nodeF = null;
	RoadEdge edgeG;
	RoadNode nodeG = null;
	RoadEdge edgeH;
	RoadNode nodeH = null;
	RoadEdge edgeI;
	RoadNode nodeI = null;
	RoadEdge edgeJ;
	RoadEdge edgeK;
	RoadEdge edgeL;
	RoadEdge edgeM;
	RoadEdge edgeN;

	/**
	 * Set up a test graph.
	 * 
	 * @throws Exception
	 *             if there is some IO error
	 */
	@Before
	public void setUp() throws Exception {
		nodeA = new RoadNode("1", 2, 6);
		nodeB = new RoadNode("2", 1, 2);
		nodeC = new RoadNode("3", 2, 1);
		nodeD = new RoadNode("4", 3, 5);
		nodeE = new RoadNode("5", 4, 3);
		nodeF = new RoadNode("6", 6, 4);
		nodeG = new RoadNode("7", 8, 3);
		nodeH = new RoadNode("8", 5, 6);
		nodeI = new RoadNode("9", 8, 6);

		// Graph #3
		// edgeF, edgeG and edgeL have been reversed to create obstacles
		// NOTE: Direction 1 is from-to, 2 is to-from
		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, true);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, true);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, true);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, true);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, true);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, true);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, true);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, true);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, true);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, true);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, true);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, true);
		WayData dataM = new WayData("VejN", RoadType.MOTORWAY, true);
		WayData dataN = new WayData("VejM", RoadType.MOTORWAY, true);

		edgeA = new RoadEdge(dataA, nodeA, nodeB);
		nodeA.addEdge(edgeA);
		nodeB.addEdge(edgeA);
		edgeB = new RoadEdge(dataB, nodeC, nodeB);
		nodeC.addEdge(edgeB);
		nodeB.addEdge(edgeB);
		edgeC = new RoadEdge(dataC, nodeC, nodeA);
		nodeC.addEdge(edgeC);
		nodeA.addEdge(edgeC);
		edgeD = new RoadEdge(dataD, nodeC, nodeD);
		nodeD.addEdge(edgeD);
		nodeC.addEdge(edgeD);
		edgeE = new RoadEdge(dataE, nodeD, nodeE);
		nodeD.addEdge(edgeE);
		nodeE.addEdge(edgeE);
		// Note the direction of edge F and edge G
		edgeF = new RoadEdge(dataF, nodeH, nodeD);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeH, nodeE);
		nodeE.addEdge(edgeG);
		nodeH.addEdge(edgeG);
		edgeH = new RoadEdge(dataH, nodeE, nodeF);
		nodeE.addEdge(edgeH);
		nodeF.addEdge(edgeH);
		edgeI = new RoadEdge(dataI, nodeF, nodeH);
		nodeF.addEdge(edgeI);
		nodeH.addEdge(edgeI);
		edgeJ = new RoadEdge(dataJ, nodeH, nodeI);
		nodeH.addEdge(edgeJ);
		nodeI.addEdge(edgeJ);
		edgeK = new RoadEdge(dataK, nodeG, nodeF);
		nodeG.addEdge(edgeK);
		nodeF.addEdge(edgeK);
		// Note the direction of edge L
		edgeL = new RoadEdge(dataL, nodeG, nodeC);
		nodeC.addEdge(edgeL);
		nodeG.addEdge(edgeL);
		edgeM = new RoadEdge(dataM, nodeC, nodeE);
		nodeC.addEdge(edgeM);
		nodeE.addEdge(edgeM);
		edgeN = new RoadEdge(dataN, nodeG, nodeI);
		nodeG.addEdge(edgeN);
		nodeI.addEdge(edgeN);

		nodes.add(nodeA);
		edges.add(edgeA);
		nodes.add(nodeB);
		edges.add(edgeB);
		nodes.add(nodeC);
		edges.add(edgeC);
		nodes.add(nodeD);
		edges.add(edgeD);
		nodes.add(nodeE);
		edges.add(edgeE);
		nodes.add(nodeF);
		edges.add(edgeF);
		nodes.add(nodeG);
		edges.add(edgeG);
		nodes.add(nodeH);
		edges.add(edgeH);
		nodes.add(nodeI);
		edges.add(edgeI);
		edges.add(edgeJ);
		edges.add(edgeK);
		edges.add(edgeL);
		edges.add(edgeM);
		edges.add(edgeN);

		Map<String, RoadNode> m = new TreeMap<String, RoadNode>();
		for (RoadNode roadNode : nodes) {
			m.put(roadNode.ID, roadNode);
		}

		graph = new Graph(m);

		graph.addEdge(edgeA);
		graph.addEdge(edgeB);
		graph.addEdge(edgeC);
		graph.addEdge(edgeD);
		graph.addEdge(edgeE);
		graph.addEdge(edgeF);
		graph.addEdge(edgeG);
		graph.addEdge(edgeH);
		graph.addEdge(edgeI);
		graph.addEdge(edgeJ);
		graph.addEdge(edgeK);
		graph.addEdge(edgeL);
		graph.addEdge(edgeM);
		graph.addEdge(edgeN);
	}

	@Test
	public void adjacencyListInt() {
		// Out degree = 4
		ArrayList<RoadEdge> exp = new ArrayList<RoadEdge>();
		exp.add(edgeB);
		exp.add(edgeC);
		exp.add(edgeD);
		exp.add(edgeM);
		List<RoadEdge> act = graph.getAdjacencyList("3");

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));

		// Out degree = 0
		act = graph.getAdjacencyList("9");
		exp.clear();

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		assertTrue(exp.equals(act));
	}

	@Test
	public void adjacencyListNode() {
		// Out degree = 4
		ArrayList<RoadEdge> exp = new ArrayList<RoadEdge>();
		exp.add(edgeB);
		exp.add(edgeC);
		exp.add(edgeD);
		exp.add(edgeM);
		List<RoadEdge> act = graph.getAdjacencyList(nodeC);

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));

		// Out degree = 0
		act = graph.getAdjacencyList(nodeI);
		exp.clear();

		if (exp.size() != act.size())
			fail("The results are not the same size! Is: "+act.size()+" Should be: "+exp.size());
		assertTrue(exp.equals(act));
	}

	@Test
	public void getNodes() {
		Map<String, RoadNode> exp = new HashMap<String, RoadNode>();
		exp.put("1", nodeA);
		exp.put("2", nodeB);
		exp.put("3", nodeC);
		exp.put("4", nodeD);
		exp.put("5", nodeE);
		exp.put("6", nodeF);
		exp.put("7", nodeG);
		exp.put("8", nodeH);
		exp.put("9", nodeI);
		Map<String, RoadNode> act = graph.getNodes();

		if (exp.size() != act.size())
			fail("The results are not the same size!");
		for (int i = 1; i < exp.size() && i < act.size(); i++) {
			assertTrue(exp.get(""+i).equals(act.get(""+i)));
		}

		// Completely empty graph
		Map<String, RoadNode> emptymap = Collections.emptyMap();
		Graph empty = new Graph(emptymap);
		act = empty.getNodes();
		if (emptymap.size() != act.size())
			fail("The results are not the same size!");
		for (int i = 1; i < exp.size() && i < act.size(); i++) {
			assertTrue(emptymap.get(""+i).equals(act.get(""+i)));
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

	@Test
	public void getEdges() {
		List<RoadEdge> exp = new ArrayList<RoadEdge>();
		exp.add(edgeA);
		exp.add(edgeB);
		exp.add(edgeC);
		exp.add(edgeD);
		exp.add(edgeE);
		exp.add(edgeF);
		exp.add(edgeG);
		exp.add(edgeH);
		exp.add(edgeI);
		exp.add(edgeJ);
		exp.add(edgeK);
		exp.add(edgeL);
		exp.add(edgeM);
		exp.add(edgeN);
		List<RoadEdge> act = graph.getEdges();

		if (exp.size() != act.size())
			fail("The results are not the same size!");
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));

		// Empty graph
		List<RoadEdge> emptylist = Collections.emptyList();
		Map<String, RoadNode> emptymap = Collections.emptyMap();
		Graph empty = new Graph(emptymap);
		act = empty.getEdges();
		if (emptylist.size() != act.size())
			fail("The results are not the same size!");
		for (int i = 1; i < exp.size() && i < act.size(); i++) {
			assertTrue(emptylist.get(i).equals(act.get(i)));
		}

	}

	@Test
	public void addEdge() {
		WayData testdata  = new WayData("VejTest", RoadType.ROAD, false);
		RoadEdge testedge = new RoadEdge(testdata, nodeB, nodeD);

		graph.addEdge(testedge);
		List<RoadEdge> exp = new ArrayList<RoadEdge>();
		exp.add(edgeA);
		exp.add(edgeB);
		exp.add(edgeC);
		exp.add(edgeD);
		exp.add(edgeE);
		exp.add(edgeF);
		exp.add(edgeG);
		exp.add(edgeH);
		exp.add(edgeI);
		exp.add(edgeJ);
		exp.add(edgeK);
		exp.add(edgeL);
		exp.add(edgeM);
		exp.add(edgeN);
		exp.add(testedge);
		List<RoadEdge> act = graph.getEdges();

		if (exp.size() != act.size())
			fail("The results are not the same size!");
		for (int i = 0; i < exp.size() && i < act.size(); i++)
			assertTrue(exp.contains(act.get(i)));
	}

}
