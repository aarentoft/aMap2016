package util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import util.graph.Graph;
import util.graph.RoadEdge;
import util.graph.RoadNode;
import exceptions.PathNotFoundException;

/**
 * This class is a "toolbox" containing two methods. An implementation of
 * Dijkstra's algorithm and an implementation of the A* algorithm. These are
 * static and as such can be used anywhere in the code to find a shortest path
 * between two nodes in a given graph.
 */
public class Dijkstra {
	/**
	 * This is a Dijkstra implementation to find the shortest path from one node
	 * to another in a graph.
	 * 
	 * @param graph
	 *            The graph in which to perform the source
	 * @param source
	 *            The start point of the path
	 * @param target
	 *            The point to which a path is wanted
	 * @return A collection of <code>RoadEdge</code>s which is the shortest path
	 *         between <code>source</code> and <code>target</code>
	 * @throws PathNotFoundException
	 *             If a path from <code>source</code> to <code>target</code> is
	 *             not found.
	 */
	public static Collection<RoadEdge> DijkstraSearch(Graph graph,
			RoadNode source, RoadNode target) throws PathNotFoundException {
		// Distance of shortest s->v path
		Double[] dist = new Double[graph.size() + 1];
		// Last edge on shortest s->v path
		RoadEdge[] edgeTo = new RoadEdge[graph.size() + 1];
		// Priority queue of edges
		IndexedMinPriorityQueue<Double> pq = new IndexedMinPriorityQueue<Double>(graph.size() + 1);
		// Whether or not we actually have a result
		boolean found = false;
		// Map of the nodes
		Map<Integer, RoadNode> nodemap = graph.getNodes();

		// init
		for (int v = 0; v < graph.size() + 1; v++) {
			dist[v] = Double.POSITIVE_INFINITY;
			edgeTo[v] = null;
		}

		RoadNode node1;
		int currIndex;
		// Initializee
		dist[source.ID] = 0.0;
		// Dist to source = 0, Insert this into PQ
		pq.insert(source.ID, dist[source.ID]);

		// Dijkstra loop
		// While pq is not empty
		while (!pq.isEmpty()) {
			// Remove node closest adjacent node from PQ
			currIndex = pq.popMin();
			// and set it to current node
			node1 = nodemap.get(currIndex);

			// if dist to adjacent node is infinite, rest cannot be be part of
			// SPT
			if (dist[node1.ID] == Double.POSITIVE_INFINITY) {
				break;
			}
			if (node1.equals(target)) {
				found = true;
			}
			// For each outgoing edge from adjacent node
			for (RoadEdge edge : graph.getAdjacencyList(currIndex)) {
				int adjacentNode = edge.getOther(node1).ID;

				if (dist[currIndex] + edge.length < dist[adjacentNode]) {
					// Overwrite dist value
					dist[adjacentNode] = dist[currIndex] + edge.length;
					edgeTo[adjacentNode] = edge;
					// If adjacent node is in PQ, decrease it
					if (pq.contains(adjacentNode))
						pq.decreaseItemValue(adjacentNode, dist[adjacentNode]);
						// Else add it
					else
						pq.insert(adjacentNode, dist[adjacentNode]);
				}
			}
		}

		if (!found) {
			throw new PathNotFoundException(
					"At path between "
							+ source
							+ " and "
							+ target
							+ " could not be found. \n"
							+ "Maybe you need to swim or canoe across some water somewhere or go off road.");
		}

		// Get route nodes
		return getRoute(edgeTo, target);
	}

	/**
	 * This is an A* implementation of the search algorithm. It is faster than
	 * the original Dijkstra algorithm even though the majority of the code is
	 * the same.
	 * 
	 * @param graph
	 *            The graph in which to perform the source
	 * @param source
	 *            The start point of the path
	 * @param target
	 *            The point to which a path is wanted
	 * @return A collection of <code>RoadEdge</code>s which is the shortest path
	 *         between <code>source</code> and <code>target</code>
	 * @throws PathNotFoundException
	 *             If a path from <code>source</code> to <code>target</code> is
	 *             not found.
	 */
	public static Collection<RoadEdge> AStarSearch(Graph graph,
			RoadNode source, RoadNode target) throws PathNotFoundException {
		// Distance of shortest s->v path
		Double[] dist = new Double[graph.size() + 1];
		// Last edge on shortest s->v path
		RoadEdge[] edgeTo = new RoadEdge[graph.size() + 1];
		// Priority queue of edges
		IndexedMinPriorityQueue<Double> pq = new IndexedMinPriorityQueue<Double>(graph.size() + 1);
		// Whether or not we actually have a result
		boolean found = false;
		// Map of the nodes
		Map<Integer, RoadNode> nodemap = graph.getNodes();

		// init
		for (int v = 0; v < graph.size() + 1; v++) {
			dist[v] = Double.POSITIVE_INFINITY;
			edgeTo[v] = null;
		}

		RoadNode node1;
		int currIndex;
		// Initialize
		dist[source.ID] = 0.0;
		// Dist to source = 0, Insert this into PQ
		pq.insert(source.ID, dist[source.ID]);

		// Dijkstra loop
		// While pq is not empty
		while (!pq.isEmpty()) {
			// Remove node closest adjacent node from PQ
			currIndex = pq.popMin();
			// and set it to current node
			node1 = nodemap.get(currIndex);

			// if dist to adjacent node is infinite, rest cannot be be part of
			// SPT
			if (dist[node1.ID] == Double.POSITIVE_INFINITY) {
				break;
			}
			if (node1.equals(target)) {
				found = true;
				break;
			}
			// For each outgoing edge from adjacent node
			for (RoadEdge edge : graph.getAdjacencyList(currIndex)) {
				int adjacentNode = edge.getOther(node1).ID;
				// If dist[current node] + edge length < dist[adjacent node]
				double distanceToTarget = edge.getOther(node1).distance(target);
				if (dist[currIndex] + edge.length < dist[adjacentNode]) {

					// Overwrite dist value
					dist[adjacentNode] = dist[currIndex] + edge.length;
					edgeTo[adjacentNode] = edge;
					// If adjacent node is in PQ, decrease it
					if (pq.contains(adjacentNode))
						pq.decreaseItemValue(adjacentNode,
								(dist[adjacentNode] + distanceToTarget));
					// Else add it
					else
						pq.insert(adjacentNode,
								(dist[adjacentNode] + distanceToTarget));
				}
			}
		}

		if (!found) {
			throw new PathNotFoundException(
					"At path between "
							+ source
							+ " and "
							+ target
							+ " could not be found. \n"
							+ "Maybe you need to swim or canoe across some water somewhere or go off road.");
		}

		return getRoute(edgeTo, target);
	}

	/**
	 * The purpose of this method is to take the (finished or unfinished)
	 * Shortest Path Tree and filter out the route to the requested target.
	 * 
	 * @param edges
	 *            Array containing the SPT
	 * @param target
	 *            The target node
	 * @return A collection returning the specific route
	 */
	protected static Collection<RoadEdge> getRoute(RoadEdge[] edges,
			RoadNode target) {
		LinkedList<RoadEdge> route = new LinkedList<RoadEdge>();
		RoadNode u = target;
		RoadEdge i = edges[target.ID];
		while (edges[u.ID] != null) {
			route.add(i);
			u = i.getOther(u);
			i = edges[u.ID];
		}
		Collections.reverse(route);
		return route;
	}
}