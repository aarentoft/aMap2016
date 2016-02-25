package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import util.graph.*;
import exceptions.PathNotFoundException;

public class DijkstraTest {
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

	/*
	 * FNODE# TNODE# LENGTH DAV_DK# DAV_DK-ID TYP VEJNAVN FROMLEFT TOLEFT
	 * FROMRIGHT TORIGHT FROMLEFT_BOGSTAV TOLEFT_BOGSTAV FROMRIGHT_BOGSTA
	 * TORIGHT_BOGSTAV V_SOGNENR H_SOGNENR V_POSTNR H_POSTNR KOMMUNENR VEJKODE
	 * SUBNET RUTENR FRAKOERSEL ZONE SPEED DRIVETIME ONE_WAY F_TURN T_TURN VEJNR
	 * AENDR_DATO TJEK_ID
	 */

	// TestData template:
	// "Node1,Node2,LENGTH,DAV_DK,DV_DK-ID,TYP,VEJNAVN,0,0,0,0,,,,,,,,,,,,,,,,,ONE_WAY,,,,,"

	@Before
	public void setup() {
		nodeA = new RoadNode("1", 2, 6);
		nodeB = new RoadNode("2", 1, 5);
		nodeC = new RoadNode("3", 2, 1);
		nodeD = new RoadNode("4", 3, 5);
		nodeE = new RoadNode("5", 4, 3);
		nodeF = new RoadNode("6", 6, 4);
		nodeG = new RoadNode("7", 8, 3);
		nodeH = new RoadNode("8", 5, 6);
		nodeI = new RoadNode("9", 8, 6);
	}

	/**
	 * Helper method that resets the graph, nodes- and edges collections, and
	 * EdgeData and RoadEdge fields. Invoked between every test.
	 */
	public void resetFields() {
		graph = null;
		nodes = new ArrayList<RoadNode>();
		edges = new ArrayList<RoadEdge>();
		edgeA = null;
		edgeB = null;
		edgeC = null;
		edgeD = null;
		edgeE = null;
		edgeF = null;
		edgeG = null;
		edgeH = null;
		edgeI = null;
		edgeJ = null;
		edgeK = null;
	}

	public void buildG1() {
//		try {
//			dataA = new EdgeData(
//					"1,2,1.41,1,1,1,VejA,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataB = new EdgeData(
//					"2,3,4.12,2,2,1,VejB,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataC = new EdgeData(
//					"3,1,5.00,3,3,1,VejC,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataD = new EdgeData(
//					"3,4,4.12,4,4,1,VejD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataE = new EdgeData(
//					"4,5,2.24,5,5,1,VejE,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataF = new EdgeData(
//					"4,8,2.24,6,6,1,VejF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataG = new EdgeData(
//					"5,8,3.16,7,7,1,VejG,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataH = new EdgeData(
//					"5,6,2.24,8,8,1,VejH,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataI = new EdgeData(
//					"6,8,2.24,9,9,1,VejI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataJ = new EdgeData(
//					"8,9,3.00,10,10,1,VejJ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataK = new EdgeData(
//					"7,6,2.24,11,11,1,VejK,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataL = new EdgeData(
//					"3,7,6.32,12,12,1,VejL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataM = new EdgeData(
//					"3,5,2.83,13,13,1,VejM,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataN = new EdgeData(
//					"7,9,3.00,14,14,1,VejN,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//
		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, (byte)0);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, (byte)0);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, (byte)0);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, (byte)0);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, (byte)0);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, (byte)0);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, (byte)0);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, (byte)0);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, (byte)0);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, (byte)0);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, (byte)0);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, (byte)0);
		WayData dataM = new WayData("VejM", RoadType.MOTORWAY, (byte)0);
		WayData dataN = new WayData("VejN", RoadType.MOTORWAY, (byte)0);
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
		edgeF = new RoadEdge(dataF, nodeD, nodeH);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeE, nodeH);
		nodeH.addEdge(edgeG);
		nodeE.addEdge(edgeG);
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
		edgeL = new RoadEdge(dataL, nodeC, nodeG);
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
	// ---------- Positive tests ----------
	// ------- All nodes connected --------


	public void buildG2() {
//			dataA = new EdgeData(
//					"1,2,1.41,1,1,1,VejA,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataB = new EdgeData(
//					"2,3,4.12,2,2,1,VejB,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataC = new EdgeData(
//					"3,1,5.00,3,3,1,VejC,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataD = new EdgeData(
//					"3,4,4.12,4,4,1,VejD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataE = new EdgeData(
//					"4,5,2.24,5,5,1,VejE,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataF = new EdgeData(
//					"4,8,2.24,6,6,1,VejF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataG = new EdgeData(
//					"5,8,3.16,7,7,1,VejG,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataH = new EdgeData(
//					"5,6,2.24,8,8,1,VejH,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataI = new EdgeData(
//					"6,8,2.24,9,9,1,VejI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataJ = new EdgeData(
//					"8,9,3.00,10,10,1,VejJ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataK = new EdgeData(
//					"7,6,2.24,11,11,1,VejK,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataL = new EdgeData(
//					"3,7,6.32,12,12,1,VejL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataM = new EdgeData(
//					"3,5,2.83,13,13,1,VejM,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataN = new EdgeData(
//					"7,9,3.00,14,14,1,VejN,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");

		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, (byte)1);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, (byte)1);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, (byte)1);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, (byte)1);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, (byte)1);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, (byte)1);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, (byte)1);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, (byte)1);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, (byte)1);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, (byte)1);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, (byte)1);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, (byte)1);
		WayData dataM = new WayData("VejM", RoadType.MOTORWAY, (byte)1);
		WayData dataN = new WayData("VejN", RoadType.MOTORWAY, (byte)1);
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
		edgeF = new RoadEdge(dataF, nodeD, nodeH);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeE, nodeH);
		nodeH.addEdge(edgeG);
		nodeE.addEdge(edgeG);
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
		edgeL = new RoadEdge(dataL, nodeC, nodeG);
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

	public void buildG3() {
//			dataA = new EdgeData(
//					"1,2,1.41,1,1,1,VejA,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataB = new EdgeData(
//					"2,3,4.12,2,2,1,VejB,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataC = new EdgeData(
//					"3,1,5.00,3,3,1,VejC,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataD = new EdgeData(
//					"3,4,4.12,4,4,1,VejD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataE = new EdgeData(
//					"4,5,2.24,5,5,1,VejE,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataF = new EdgeData(
//					"4,8,2.24,6,6,1,VejF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataG = new EdgeData(
//					"5,8,3.16,7,7,1,VejG,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataH = new EdgeData(
//					"5,6,2.24,8,8,1,VejH,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataI = new EdgeData(
//					"6,8,2.24,9,9,1,VejI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataJ = new EdgeData(
//					"8,9,3.00,10,10,1,VejJ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataK = new EdgeData(
//					"7,6,2.24,11,11,1,VejK,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataL = new EdgeData(
//					"3,7,6.32,12,12,1,VejL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataM = new EdgeData(
//					"3,5,2.83,13,13,1,VejM,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataN = new EdgeData(
//					"7,9,3.00,14,14,1,VejN,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");

		// edgeF, edgeG and edgeL have been reversed to create obstacles
		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, (byte)1);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, (byte)1);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, (byte)1);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, (byte)1);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, (byte)1);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, (byte)2);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, (byte)2);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, (byte)1);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, (byte)1);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, (byte)1);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, (byte)1);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, (byte)2);
		WayData dataM = new WayData("VejM", RoadType.MOTORWAY, (byte)1);
		WayData dataN = new WayData("VejN", RoadType.MOTORWAY, (byte)1);
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
		edgeF = new RoadEdge(dataF, nodeD, nodeH);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeE, nodeH);
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
		edgeL = new RoadEdge(dataL, nodeC, nodeG);
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

	public void buildG4() {
//			dataA = new EdgeData(
//					"1,2,1.41,1,1,1,VejA,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataB = new EdgeData(
//					"2,3,4.12,2,2,1,VejB,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataC = new EdgeData(
//					"3,1,5.00,3,3,1,VejC,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataD = new EdgeData(
//					"3,4,4.12,4,4,1,VejD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataE = new EdgeData(
//					"4,5,2.24,5,5,1,VejE,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataF = new EdgeData(
//					"4,8,2.24,6,6,1,VejF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataG = new EdgeData(
//					"8,5,3.16,7,7,1,VejG,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataH = new EdgeData(
//					"5,6,2.24,8,8,1,VejH,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataI = new EdgeData(
//					"6,8,2.24,9,9,1,VejI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataJ = new EdgeData(
//					"8,9,3.00,10,10,1,VejJ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataK = new EdgeData(
//					"7,6,2.24,11,11,1,VejK,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataL = new EdgeData(
//					"3,7,6.32,12,12,1,VejL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataM = new EdgeData(
//					"3,5,2.83,13,13,1,VejM,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
//			dataN = new EdgeData(
//					"7,9,3.00,14,14,1,VejN,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");

		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, (byte)0);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, (byte)0);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, (byte)0);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, (byte)0);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, (byte)0);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, (byte)0);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, (byte)0);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, (byte)0);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, (byte)0);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, (byte)0);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, (byte)0);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, (byte)0);
		WayData dataM = new WayData("VejM", RoadType.MOTORWAY, (byte)0);
		WayData dataN = new WayData("VejN", RoadType.MOTORWAY, (byte)0);
		edgeA = new RoadEdge(dataA, nodeA, nodeB);
		nodeA.addEdge(edgeA);
		nodeB.addEdge(edgeA);
		edgeB = new RoadEdge(dataB, nodeC, nodeB);
		nodeC.addEdge(edgeB);
		nodeB.addEdge(edgeB);
		edgeC = new RoadEdge(dataC, nodeC, nodeA);
		nodeC.addEdge(edgeC);
		nodeA.addEdge(edgeC);
		edgeE = new RoadEdge(dataE, nodeD, nodeE);
		nodeD.addEdge(edgeE);
		nodeE.addEdge(edgeE);
		edgeF = new RoadEdge(dataF, nodeD, nodeH);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeH, nodeE);
		nodeH.addEdge(edgeG);
		nodeE.addEdge(edgeG);
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
		edges.add(edgeN);

		Map<String, RoadNode> m = new TreeMap<String, RoadNode>();
		for (RoadNode roadNode : nodes) {
			m.put(roadNode.ID, roadNode);
		}

		graph = new Graph(m);

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
	}

	public void buildG5() {
//			dataA = new EdgeData(
//					"1,2,1.41,1,1,1,VejA,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataB = new EdgeData(
//					"2,3,4.12,2,2,1,VejB,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataC = new EdgeData(
//					"3,1,5.00,3,3,1,VejC,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataE = new EdgeData(
//					"4,5,2.24,5,5,1,VejE,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataF = new EdgeData(
//					"4,8,2.24,6,6,1,VejF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataG = new EdgeData(
//					"5,8,3.16,7,7,1,VejG,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataH = new EdgeData(
//					"5,6,2.24,8,8,1,VejH,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataI = new EdgeData(
//					"6,8,2.24,9,9,1,VejI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataJ = new EdgeData(
//					"8,9,3.00,10,10,1,VejJ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataK = new EdgeData(
//					"7,6,2.24,11,11,1,VejK,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataN = new EdgeData(
//					"7,9,3.00,14,14,1,VejN,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");

		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, (byte)1);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, (byte)1);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, (byte)1);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, (byte)1);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, (byte)1);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, (byte)1);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, (byte)1);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, (byte)1);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, (byte)1);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, (byte)1);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, (byte)1);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, (byte)1);
		WayData dataM = new WayData("VejM", RoadType.MOTORWAY, (byte)1);
		WayData dataN = new WayData("VejN", RoadType.MOTORWAY, (byte)1);
		edgeA = new RoadEdge(dataA, nodeA, nodeB);
		nodeA.addEdge(edgeA);
		nodeB.addEdge(edgeA);
		edgeB = new RoadEdge(dataB, nodeC, nodeB);
		nodeC.addEdge(edgeB);
		nodeB.addEdge(edgeB);
		edgeC = new RoadEdge(dataC, nodeC, nodeA);
		nodeC.addEdge(edgeC);
		nodeA.addEdge(edgeC);
		edgeE = new RoadEdge(dataE, nodeD, nodeE);
		nodeD.addEdge(edgeE);
		nodeE.addEdge(edgeE);
		edgeF = new RoadEdge(dataF, nodeD, nodeH);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeE, nodeH);
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
		edges.add(edgeN);

		Map<String, RoadNode> m = new TreeMap<String, RoadNode>();
		for (RoadNode roadNode : nodes) {
			m.put(roadNode.ID, roadNode);
		}

		graph = new Graph(m);

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
	}

	public void buildG6() {
//		dataA = new EdgeData(
//					"1,2,1.41,1,1,1,VejA,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataB = new EdgeData(
//					"2,3,4.12,2,2,1,VejB,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataC = new EdgeData(
//					"3,1,5.00,3,3,1,VejC,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataD = new EdgeData(
//					"3,4,4.12,4,4,1,VejD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataE = new EdgeData(
//					"4,5,2.24,5,5,1,VejE,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataF = new EdgeData(
//					"4,8,2.24,6,6,1,VejF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//		dataG = new EdgeData(
//					"5,8,3.16,7,7,1,VejG,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//		dataH = new EdgeData(
//					"5,6,2.24,8,8,1,VejH,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataI = new EdgeData(
//					"6,8,2.24,9,9,1,VejI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataJ = new EdgeData(
//					"8,9,3.00,10,10,1,VejJ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataK = new EdgeData(
//					"7,6,2.24,11,11,1,VejK,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataL = new EdgeData(
//					"3,7,6.32,12,12,1,VejL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//		dataM = new EdgeData(
//					"3,5,2.83,13,13,1,VejM,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//		dataN = new EdgeData(
//					"7,9,3.00,14,14,1,VejN,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");

		// edgeF and edgeG is reversed to create obstacles
		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, (byte)1);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, (byte)1);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, (byte)1);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, (byte)1);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, (byte)1);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, (byte)2);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, (byte)2);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, (byte)1);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, (byte)1);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, (byte)1);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, (byte)1);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, (byte)1);
		WayData dataM = new WayData("VejM", RoadType.MOTORWAY, (byte)1);
		WayData dataN = new WayData("VejN", RoadType.MOTORWAY, (byte)1);
		edgeA = new RoadEdge(dataA, nodeA, nodeB);
		nodeA.addEdge(edgeA);
		nodeB.addEdge(edgeA);
		edgeB = new RoadEdge(dataB, nodeC, nodeB);
		nodeC.addEdge(edgeB);
		nodeB.addEdge(edgeB);
		edgeC = new RoadEdge(dataC, nodeC, nodeA);
		nodeC.addEdge(edgeC);
		nodeA.addEdge(edgeC);
		edgeE = new RoadEdge(dataE, nodeD, nodeE);
		nodeD.addEdge(edgeE);
		nodeE.addEdge(edgeE);
		edgeF = new RoadEdge(dataF, nodeD, nodeH);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeE, nodeH);
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
		edges.add(edgeN);

		Map<String, RoadNode> m = new TreeMap<String, RoadNode>();
		for (RoadNode roadNode : nodes) {
			m.put(roadNode.ID, roadNode);
		}

		graph = new Graph(m);

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
	}

	public void buildG7() {
//			dataA = new EdgeData(
//					"1,2,1.41,1,1,1,VejA,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataB = new EdgeData(
//					"2,3,4.12,2,2,1,VejB,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataC = new EdgeData(
//					"3,1,5.00,3,3,1,VejC,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataD = new EdgeData(
//					"3,4,4.12,4,4,1,VejD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataE = new EdgeData(
//					"4,5,2.24,5,5,1,VejE,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataF = new EdgeData(
//					"4,8,2.24,6,6,1,VejF,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataG = new EdgeData(
//					"5,8,3.16,7,7,1,VejG,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataH = new EdgeData(
//					"5,6,2.24,8,8,1,VejH,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataI = new EdgeData(
//					"6,8,2.24,9,9,1,VejI,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataJ = new EdgeData(
//					"8,9,3.00,10,10,1,VejJ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataK = new EdgeData(
//					"7,6,2.24,11,11,1,VejK,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataL = new EdgeData(
//					"3,7,6.32,12,12,1,VejL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,ft,0,0,0,0,0");
//			dataM = new EdgeData(
//					"3,5,2.83,13,13,1,VejM,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");
//			dataN = new EdgeData(
//					"7,9,3.00,14,14,1,VejN,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,tf,0,0,0,0,0");

		// edgeF, edgeG, edgeH and edgeL have been reversed to prevent a
		// path
		WayData dataA = new WayData("VejA", RoadType.MOTORWAY, (byte)1);
		WayData dataB = new WayData("VejB", RoadType.MOTORWAY, (byte)1);
		WayData dataC = new WayData("VejC", RoadType.MOTORWAY, (byte)1);
		WayData dataD = new WayData("VejD", RoadType.MOTORWAY, (byte)1);
		WayData dataE = new WayData("VejE", RoadType.MOTORWAY, (byte)1);
		WayData dataF = new WayData("VejF", RoadType.MOTORWAY, (byte)2);
		WayData dataG = new WayData("VejG", RoadType.MOTORWAY, (byte)2);
		WayData dataH = new WayData("VejH", RoadType.MOTORWAY, (byte)2);
		WayData dataI = new WayData("VejI", RoadType.MOTORWAY, (byte)1);
		WayData dataJ = new WayData("VejJ", RoadType.MOTORWAY, (byte)1);
		WayData dataK = new WayData("VejK", RoadType.MOTORWAY, (byte)1);
		WayData dataL = new WayData("VejL", RoadType.MOTORWAY, (byte)2);
		WayData dataM = new WayData("VejM", RoadType.MOTORWAY, (byte)1);
		WayData dataN = new WayData("VejN", RoadType.MOTORWAY, (byte)1);
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
		edgeF = new RoadEdge(dataF, nodeD, nodeH);
		nodeD.addEdge(edgeF);
		nodeH.addEdge(edgeF);
		edgeG = new RoadEdge(dataG, nodeE, nodeH);
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
		edgeL = new RoadEdge(dataL, nodeC, nodeG);
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

	public void buildG8() {
		nodes.add(nodeA);
		nodes.add(nodeB);
		nodes.add(nodeC);
		nodes.add(nodeD);
		nodes.add(nodeE);
		nodes.add(nodeF);
		nodes.add(nodeG);
		nodes.add(nodeH);
		nodes.add(nodeI);

		Map<String, RoadNode> m = new TreeMap<String, RoadNode>();
		for (RoadNode roadNode : nodes) {
			m.put(roadNode.ID, roadNode);
		}

		graph = new Graph(m);
	}


	// -------------------- Positive -----------------------

	@Test
	public void distToTarget() {
		try {
			nodeA = new RoadNode("1", 2, 6);
			nodeB = new RoadNode("2", 6, 9);
		} catch (Exception e) {
			System.out.println("Oh no");
		}
		double actResult = nodeA.distance(nodeB);
		assertEquals(5.0, actResult, 0.01);

		// Coordinates result in negative length
		actResult = nodeB.distance(nodeA);
		assertEquals(5.0, actResult, 0.01);
	}

	// ---------- All nodes connected (graph 1-3) ----------

	/**
	 * A* and Dijkstra on graph #1 (undirected)
	 */
	@Test
	public void undirected() {
		buildG1();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(edgeM);
		expResult.add(edgeG);
		expResult.add(edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.DijkstraSearch(graph, nodeC, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, nodeC, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);

		// Clean-up
		resetFields();
	}

	/**
	 * A* and Dijkstra on graph #2 (One-way w/o obstacles)
	 */
	@Test
	public void oneWayNoObstacles() {
		buildG2();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(edgeM);
		expResult.add(edgeG);
		expResult.add(edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.DijkstraSearch(graph, nodeC, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, nodeC, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);

		// Clean-up
		resetFields();
	}

	/**
	 * A* and Dijkstra on graph #3 (One-way w/ obstacles)
	 */
	@Test
	public void oneWayObstacles() {
		buildG3();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(edgeM);
		expResult.add(edgeH);
		expResult.add(edgeI);
		expResult.add(edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.DijkstraSearch(graph, nodeC, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, nodeC, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);

		// Clean-up
		resetFields();
	}

	// ---------- Groups of nodes (graph 4-6)

	// ---------- Groups of separated nodes ----------

	/**
	 * A* and Dijkstra on graph #4 (Undirected groups)
	 */
	@Test
	public void undirectedGroups() {
		// Uses test graph #4
		buildG4();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(edgeG);
		expResult.add(edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.AStarSearch(graph, nodeE, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, nodeE, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);

		// Clean-up
		resetFields();
	}

	/**
	 * A* and Dijkstra on graph #5 (One-way groups w/o obstacles)
	 */
	@Test
	public void oneWayNoObstaclesGroups() {
		// Uses test graph #5
		buildG5();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(edgeG);
		expResult.add(edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.AStarSearch(graph, nodeE, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, nodeE, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);

		// Clean-up
		resetFields();
	}

	/**
	 * A* and Dijkstra on graph #6 (One-way groups w/ obstacles)
	 */
	@Test
	public void oneWayObstaclesGroups() {
		// Uses test graph #6
		buildG6();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(edgeH);
		expResult.add(edgeI);
		expResult.add(edgeJ);

		// Dijkstra test
		Collection<RoadEdge> actResultD = null;
		try {
			actResultD = Dijkstra.AStarSearch(graph, nodeE, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultD);

		// A* test
		Collection<RoadEdge> actResultA = null;
		try {
			actResultA = Dijkstra.AStarSearch(graph, nodeE, nodeI);
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(expResult, actResultA);

		// Clean-up
		resetFields();
	}



	// -------------------- Negative -----------------------

	// ---------- All nodes connected (graph 7) ------------
	// ---------- Negative tests ----------
	@Test
	public void directedEdgesPreventPath() {
		resetFields();
		buildG7();

		try {
			Dijkstra.DijkstraSearch(graph, nodeC, nodeI);

			// No exception was thrown so fail the test
			fail("Dijkstra should've thrown an exception!");
		} catch (PathNotFoundException expected) {
			// Exception thrown, test passed
		}

		try {
			Dijkstra.AStarSearch(graph, nodeC, nodeI);

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
		// Uses test graph #4
		resetFields();
		buildG4();
		try {
			Dijkstra.DijkstraSearch(graph, nodeC, nodeI);
			fail("Dijkstra should've thrown an exception!");
		} catch (PathNotFoundException e) {
		}

		try {
			Dijkstra.AStarSearch(graph, nodeC, nodeI);
			fail("AStar should've thrown an exception!");
		} catch (PathNotFoundException e) {
		}

	}

	/**
	 * A* and Dijkstra on test graph #6 (One-way w/ obstacles) negative
	 */
	@Test
	public void negOneWayObstaclesGroups() {
		// Uses test graph #6
		resetFields();
		buildG6();

		LinkedList<RoadEdge> expResult = new LinkedList<RoadEdge>();
		expResult.add(edgeH);
		expResult.add(edgeI);
		expResult.add(edgeJ);

		try {
			Dijkstra.DijkstraSearch(graph, nodeC, nodeI);
			fail("Dijkstra should've thrown an exception!");
		} catch (PathNotFoundException e) {
		}

		try {
			Dijkstra.AStarSearch(graph, nodeC, nodeI);
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
		resetFields();

		buildG8();

		try {
			Dijkstra.DijkstraSearch(graph, nodeC, nodeI);
			fail("should've thrown an exception!");
		} catch (PathNotFoundException expected) {
		}

		try {
			Dijkstra.AStarSearch(graph, nodeC, nodeI);
			fail("should've thrown an exception!");
		} catch (PathNotFoundException expected) {
		}

	}

}