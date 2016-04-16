package testhelpers;

import util.graph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestGraphBuilder {
    private final static List<RoadNode> nodes = new ArrayList<RoadNode>();
    private final static Map<String, RoadNode> nodesMap = new TreeMap<String, RoadNode>();
    private final static RoadNode nodeA;
    private final static RoadNode nodeB;
    private final static RoadNode nodeC;
    private final static RoadNode nodeD;
    private final static RoadNode nodeE;
    private final static RoadNode nodeF;
    private final static RoadNode nodeG;
    private final static RoadNode nodeH;
    private final static RoadNode nodeI;

    public final static RoadEdge bidirectionalEdgeA;
    public final static RoadEdge bidirectionalEdgeB;
    public final static RoadEdge bidirectionalEdgeC;
    public final static RoadEdge bidirectionalEdgeD;
    public final static RoadEdge bidirectionalEdgeE;
    public final static RoadEdge bidirectionalEdgeF;
    public final static RoadEdge bidirectionalEdgeG;
    public final static RoadEdge bidirectionalEdgeH;
    public final static RoadEdge bidirectionalEdgeI;
    public final static RoadEdge bidirectionalEdgeJ;
    public final static RoadEdge bidirectionalEdgeK;
    public final static RoadEdge bidirectionalEdgeL;
    public final static RoadEdge bidirectionalEdgeM;
    public final static RoadEdge bidirectionalEdgeN;

    public final static RoadEdge edgeA;
    public final static RoadEdge edgeB;
    public final static RoadEdge edgeC;
    public final static RoadEdge edgeD;
    public final static RoadEdge edgeE;
    public final static RoadEdge edgeF;
    public final static RoadEdge edgeG;
    public final static RoadEdge edgeH;
    public final static RoadEdge edgeI;
    public final static RoadEdge edgeJ;
    public final static RoadEdge edgeK;
    public final static RoadEdge edgeL;
    public final static RoadEdge edgeM;
    public final static RoadEdge edgeN;

    public final static RoadEdge reverseEdgeF;
    public final static RoadEdge reverseEdgeG;
    public final static RoadEdge reverseEdgeH;
    public final static RoadEdge reverseEdgeL;

    static {
        nodeA = new RoadNode("A", 2, 6);
        nodeB = new RoadNode("B", 1, 5);
        nodeC = new RoadNode("C", 2, 1);
        nodeD = new RoadNode("D", 3, 5);
        nodeE = new RoadNode("E", 4, 3);
        nodeF = new RoadNode("F", 6, 4);
        nodeG = new RoadNode("G", 8, 3);
        nodeH = new RoadNode("H", 5, 6);
        nodeI = new RoadNode("I", 8, 6);
        nodes.add(nodeA);
        nodes.add(nodeB);
        nodes.add(nodeC);
        nodes.add(nodeD);
        nodes.add(nodeE);
        nodes.add(nodeF);
        nodes.add(nodeG);
        nodes.add(nodeH);
        nodes.add(nodeI);

        for (RoadNode roadNode : nodes) {
            nodesMap.put(roadNode.ID, roadNode);
        }

        bidirectionalEdgeA = new RoadEdge(new WayData("VejA", RoadType.MOTORWAY, false), nodeA, nodeB);
        bidirectionalEdgeB = new RoadEdge(new WayData("VejB", RoadType.MOTORWAY, false), nodeC, nodeB);
        bidirectionalEdgeC = new RoadEdge(new WayData("VejC", RoadType.MOTORWAY, false), nodeC, nodeA);
        bidirectionalEdgeD = new RoadEdge(new WayData("VejD", RoadType.MOTORWAY, false), nodeC, nodeD);
        bidirectionalEdgeE = new RoadEdge(new WayData("VejE", RoadType.MOTORWAY, false), nodeD, nodeE);
        bidirectionalEdgeF = new RoadEdge(new WayData("VejF", RoadType.MOTORWAY, false), nodeD, nodeH);
        bidirectionalEdgeG = new RoadEdge(new WayData("VejG", RoadType.MOTORWAY, false), nodeE, nodeH);
        bidirectionalEdgeH = new RoadEdge(new WayData("VejH", RoadType.MOTORWAY, false), nodeE, nodeF);
        bidirectionalEdgeI = new RoadEdge(new WayData("VejI", RoadType.MOTORWAY, false), nodeF, nodeH);
        bidirectionalEdgeJ = new RoadEdge(new WayData("VejJ", RoadType.MOTORWAY, false), nodeH, nodeI);
        bidirectionalEdgeK = new RoadEdge(new WayData("VejK", RoadType.MOTORWAY, false), nodeG, nodeF);
        bidirectionalEdgeL = new RoadEdge(new WayData("VejL", RoadType.MOTORWAY, false), nodeC, nodeG);
        bidirectionalEdgeM = new RoadEdge(new WayData("VejM", RoadType.MOTORWAY, false), nodeC, nodeE);
        bidirectionalEdgeN = new RoadEdge(new WayData("VejN", RoadType.MOTORWAY, false), nodeG, nodeI);

        edgeA = new RoadEdge(new WayData("VejA", RoadType.MOTORWAY, true), nodeA, nodeB);
        edgeB = new RoadEdge(new WayData("VejB", RoadType.MOTORWAY, true), nodeC, nodeB);
        edgeC = new RoadEdge(new WayData("VejC", RoadType.MOTORWAY, true), nodeC, nodeA);
        edgeD = new RoadEdge(new WayData("VejD", RoadType.MOTORWAY, true), nodeC, nodeD);
        edgeE = new RoadEdge(new WayData("VejE", RoadType.MOTORWAY, true), nodeD, nodeE);
        edgeF = new RoadEdge(new WayData("VejF", RoadType.MOTORWAY, true), nodeD, nodeH);
        edgeG = new RoadEdge(new WayData("VejG", RoadType.MOTORWAY, true), nodeE, nodeH);
        edgeH = new RoadEdge(new WayData("VejH", RoadType.MOTORWAY, true), nodeE, nodeF);
        edgeI = new RoadEdge(new WayData("VejI", RoadType.MOTORWAY, true), nodeF, nodeH);
        edgeJ = new RoadEdge(new WayData("VejJ", RoadType.MOTORWAY, true), nodeH, nodeI);
        edgeK = new RoadEdge(new WayData("VejK", RoadType.MOTORWAY, true), nodeG, nodeF);
        edgeL = new RoadEdge(new WayData("VejL", RoadType.MOTORWAY, true), nodeC, nodeG);
        edgeM = new RoadEdge(new WayData("VejM", RoadType.MOTORWAY, true), nodeC, nodeE);
        edgeN = new RoadEdge(new WayData("VejN", RoadType.MOTORWAY, true), nodeG, nodeI);

        reverseEdgeF = new RoadEdge(new WayData("VejF", RoadType.MOTORWAY, true), nodeH, nodeD);
        reverseEdgeG = new RoadEdge(new WayData("VejG", RoadType.MOTORWAY, true), nodeH, nodeE);
        reverseEdgeH = new RoadEdge(new WayData("VejH", RoadType.MOTORWAY, true), nodeF, nodeE);
        reverseEdgeL = new RoadEdge(new WayData("VejL", RoadType.MOTORWAY, true), nodeG, nodeC);
    }

    public static Graph getGraph1() {
        Graph graph = new Graph(nodesMap);
        graph.addEdge(bidirectionalEdgeA);
        graph.addEdge(bidirectionalEdgeB);
        graph.addEdge(bidirectionalEdgeC);
        graph.addEdge(bidirectionalEdgeD);
        graph.addEdge(bidirectionalEdgeE);
        graph.addEdge(bidirectionalEdgeF);
        graph.addEdge(bidirectionalEdgeG);
        graph.addEdge(bidirectionalEdgeH);
        graph.addEdge(bidirectionalEdgeI);
        graph.addEdge(bidirectionalEdgeJ);
        graph.addEdge(bidirectionalEdgeK);
        graph.addEdge(bidirectionalEdgeL);
        graph.addEdge(bidirectionalEdgeM);
        graph.addEdge(bidirectionalEdgeN);
        return graph;
    }

    public static Graph getGraph2() {
        Graph graph = new Graph(nodesMap);
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
        return graph;
    }

    public static Graph getGraph3() {
        Graph graph = new Graph(nodesMap);
        graph.addEdge(edgeA);
        graph.addEdge(edgeB);
        graph.addEdge(edgeC);
        graph.addEdge(edgeD);
        graph.addEdge(edgeE);
        graph.addEdge(reverseEdgeF);
        graph.addEdge(reverseEdgeG);
        graph.addEdge(edgeH);
        graph.addEdge(edgeI);
        graph.addEdge(edgeJ);
        graph.addEdge(edgeK);
        graph.addEdge(reverseEdgeL);
        graph.addEdge(edgeM);
        graph.addEdge(edgeN);
        return graph;
    }

    public static Graph getGraph4() {
        Graph graph = new Graph(nodesMap);
        graph.addEdge(bidirectionalEdgeA);
        graph.addEdge(bidirectionalEdgeB);
        graph.addEdge(bidirectionalEdgeC);
        graph.addEdge(bidirectionalEdgeE);
        graph.addEdge(bidirectionalEdgeF);
        graph.addEdge(bidirectionalEdgeG);
        graph.addEdge(bidirectionalEdgeH);
        graph.addEdge(bidirectionalEdgeI);
        graph.addEdge(bidirectionalEdgeJ);
        graph.addEdge(bidirectionalEdgeK);
        graph.addEdge(bidirectionalEdgeN);
        return graph;
    }

    public static Graph getGraph5() {
        Graph graph = new Graph(nodesMap);
        graph.addEdge(edgeA);
        graph.addEdge(edgeB);
        graph.addEdge(edgeC);
        graph.addEdge(edgeE);
        graph.addEdge(edgeF);
        graph.addEdge(edgeG);
        graph.addEdge(edgeH);
        graph.addEdge(edgeI);
        graph.addEdge(edgeJ);
        graph.addEdge(edgeK);
        graph.addEdge(edgeN);
        return graph;
    }

    public static Graph getGraph6() {
        Graph graph = new Graph(nodesMap);
        graph.addEdge(edgeA);
        graph.addEdge(edgeB);
        graph.addEdge(edgeC);
        graph.addEdge(edgeE);
        graph.addEdge(reverseEdgeF);
        graph.addEdge(reverseEdgeG);
        graph.addEdge(edgeH);
        graph.addEdge(edgeI);
        graph.addEdge(edgeJ);
        graph.addEdge(edgeK);
        graph.addEdge(edgeN);
        return graph;
    }

    public static Graph getGraph7() {
        Graph graph = new Graph(nodesMap);
        graph.addEdge(edgeA);
        graph.addEdge(edgeB);
        graph.addEdge(edgeC);
        graph.addEdge(edgeD);
        graph.addEdge(edgeE);
        graph.addEdge(reverseEdgeF);
        graph.addEdge(reverseEdgeG);
        graph.addEdge(reverseEdgeH);
        graph.addEdge(edgeI);
        graph.addEdge(edgeJ);
        graph.addEdge(edgeK);
        graph.addEdge(reverseEdgeL);
        graph.addEdge(edgeM);
        graph.addEdge(edgeN);
        return graph;
    }

    public static Graph getGraph8() {
        Graph graph = new Graph(nodesMap);
        return new Graph(nodesMap);
    }
}
