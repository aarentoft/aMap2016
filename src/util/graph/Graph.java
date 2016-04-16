package util.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A graph datastructure which can be used to hold information about nodes in a
 * graph.
 */
public class Graph {
	// All nodes in graph
	protected Map<String, RoadNode> nodes;
	// All edges in graph
	protected List<RoadEdge> edges;
	// outEdges.get(nodeIDstring) = list that represents edges going from node with nodeID ==
	// nodeIDstring
	protected Map<String, List<RoadEdge>> outEdges;

	/**
	 * Constructs the Graph from the given collection of RoadNodes.
	 */
	public Graph(Map<String, RoadNode> nodes) {
		this.nodes = nodes;
		edges = new ArrayList<RoadEdge>();
		outEdges = new HashMap<String, List<RoadEdge>>(nodes.size() + 1);

		for (String nodeID : nodes.keySet()) {
			outEdges.put(nodeID, new ArrayList<RoadEdge>());
		}
	}
	
	/**
	 * Gets the list of outgoing neighbors to the given index.
	 * 
	 * @param v
	 *            The ID of the node in question.
	 * @return A list of the outgoing neighbors.
	 */
	public List<RoadEdge> getAdjacencyList(String v) {
		return outEdges.get(v);
	}
	
	/**
	 * Returns a list of the outgoing edges from the param <code>node</code>.
	 * 
	 * @param node
	 *            Node to get adjacent nodes from
	 * @return an ArrayList of edges
	 */
	public List<RoadEdge> getAdjacencyList(RoadNode node) {
		return outEdges.get(node.ID);
	}

	/**
	 * Add a <code>RoadEdge</code> to this graph. The method will automatically
	 * add the edge to the associated respective adjacency list of each node
	 * (ie. if edge goes FROM v1 TO v2, it will be added to v1's adjacency list.
	 * When direction is reversed, edge will be added to v2's list and if the
	 * road is traversable in both directions, edge will be added to both nodes'
	 * adjacency list)
	 * 
	 * @param e
	 *            Edge to add to the graph
	 */
	public void addEdge(RoadEdge e) {
		edges.add(e);
		if (e.data.oneway) {
			outEdges.get(e.start.ID).add(e);
		} else {
			outEdges.get(e.start.ID).add(e);
			outEdges.get(e.end.ID).add(e);
		}
	}

	/**
	 * @return All the nodes.
	 */
	public Map<String, RoadNode> getNodes() {
		return nodes;
	}

	/**
	 * @return All the edges.
	 */
	public List<RoadEdge> getEdges() {
		return edges;
	}

	/**
	 * @return The amount of nodes.
	 */
	public int size() {
		return nodes.size();
	}

	@Override
	public String toString() {
		return "Graph [nodes=" + nodes + ", edges=" + edges + "]";
	}
}
