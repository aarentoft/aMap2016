package datastructures.graph;

import java.util.*;

/**
 * A graph datastructure which can be used to hold information about nodes in a
 * graph.
 */
public class Graph {
	// All nodes in graph
	protected Map<Long, RoadNode> nodes;
	// outEdges.get(nodeID) = list that represents edges going from node with nodeID ==
	// nodeIDstring
	protected Map<Long, List<RoadEdge>> outEdges;

	// empty list used as return value when a node has no out-edges
	private List<RoadEdge> defaultValue;

	/**
	 * Constructs the Graph from the given collection of RoadNodes.
	 */
	public Graph(Map<Long, RoadNode> nodes) {
		this.nodes = nodes;
		outEdges = new HashMap<Long, List<RoadEdge>>(nodes.size() + 1);
		defaultValue = Collections.emptyList();
	}
	
	/**
	 * Gets the list of outgoing neighbors to the given index.
	 * 
	 * @param v
	 *            The ID of the node in question.
	 * @return A list of the outgoing neighbors.
	 */
	public List<RoadEdge> getAdjacencyList(long v) {
		return outEdges.getOrDefault(v, defaultValue);
	}
	
	/**
	 * Returns a list of the outgoing edges from the param <code>node</code>.
	 * 
	 * @param node
	 *            Node to get adjacent nodes from
	 * @return an ArrayList of edges
	 */
	public List<RoadEdge> getAdjacencyList(RoadNode node) {
		return outEdges.getOrDefault(node.ID, defaultValue);
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
		outEdges.putIfAbsent(e.start.ID, new ArrayList<RoadEdge>());
		outEdges.get(e.start.ID).add(e);
		if (!e.data.oneway) {
			outEdges.putIfAbsent(e.end.ID, new ArrayList<RoadEdge>());
			outEdges.get(e.end.ID).add(e);
		}
	}

	/**
	 * @return All the nodes.
	 */
	public Map<Long, RoadNode> getNodes() {
		return nodes;
	}

	/**
	 * @return The amount of nodes.
	 */
	public int size() {
		return nodes.size();
	}

	@Override
	public String toString() {
		return "Graph [nodes=" + nodes + "]";
	}
}
