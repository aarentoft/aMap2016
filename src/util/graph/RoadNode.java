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
	public final long ID;

	/**
	 * Create a new node based on the input.
	 * @param id An integer identifying the node.
	 * @param x x coordinate of the node.
	 * @param y y coordinate of the node.
	 */
	public RoadNode(long id, double x, double y) {
		super(0, 0);
		this.ID = id;
		this.x  = x;
		this.y  = y;
	}
}