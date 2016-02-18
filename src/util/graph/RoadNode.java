package util.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.Point;

/**
 * A class used for nodes on the roads.
 */
@SuppressWarnings("serial")
public class RoadNode extends Point {
	private List<RoadEdge> edges = new ArrayList<RoadEdge>();
	public final int ID;

	/**
	 * Create a new node based on the input.
	 * @param id An integer identifying the node.
	 * @param x x coordinate of the node.
	 * @param y y coordinate of the node.
	 */
	public RoadNode(int id, double x, double y) {
		super(0, 0);
		this.ID = id;
		this.x  = x;
		this.y  = y;
	}

	// ----- Adders -----

	/**
	 * A <code>RoadNode</code> can be connected to several edges (roads). This
	 * method will store these edges.
	 * 
	 * @param edge
	 *            edge to store
	 */
	public void addEdge(RoadEdge edge) {
		edges.add(edge);
	}
}