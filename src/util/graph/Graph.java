package util.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A graph datastructure which can be used to hold information about nodes in a
 * graph.
 */
public class Graph {
	// All nodes in graph
	protected Map<Integer, RoadNode> nodes;
	// All edges in graph
	protected List<RoadEdge> edges;
	// outEdges[int] = list that represents edges going from node with KDVID ==
	// int
	protected List<RoadEdge>[] outEdges;

	/**
	 * Constructs the Graph from the given collection of RoadNodes.
	 */
	public Graph(Map<Integer, RoadNode> nodes) {
		this.nodes = nodes;
		edges = new ArrayList<RoadEdge>();
		outEdges = new ArrayList[nodes.size() + 1];

		for (int v = 0; v < nodes.size() + 1; v++) {
			outEdges[v] = new ArrayList<RoadEdge>();
		}
	}
	
	/**
	 * Gets the list of outgoing neighbors to the given index.
	 * 
	 * @param v
	 *            The index of the node in question.
	 * @return A list of the outgoing neighbors.
	 */
	public List<RoadEdge> getAdjacencyList(int v) {
		return outEdges[v];
	}
	
	/**
	 * Returns a list of the outgoing edges from the param <code>node</code>.
	 * 
	 * @param node
	 *            Node to get adjacent nodes from
	 * @return an ArrayList of edges
	 */
	public List<RoadEdge> getAdjacencyList(RoadNode node) {
		return outEdges[node.ID];
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
		// To-from
		if (e.data.direction == 1)
			outEdges[e.start.ID].add(e);
		// From-To
		else if (e.data.direction == 2)
			outEdges[e.end.ID].add(e);
		// No-go
		else if (e.data.direction == 3) {
			// do nothing (road not traversable)
		} else { // Both ways
			outEdges[e.start.ID].add(e);
			outEdges[e.end.ID].add(e);
		}
	}

	/**
	 * @return All the nodes.
	 */
	public Map<Integer, RoadNode> getNodes() {
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
