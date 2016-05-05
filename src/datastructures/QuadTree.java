package datastructures;

import datastructures.graph.RoadEdge;
import datastructures.graph.RoadType;
import util.Rectangle;

import java.util.*;

/**
 * Optimized data-structure for inserting and getting RoadEdges, based on the
 * geographical location.
 */
public class QuadTree {
	protected final int capacity;

	// contains a root for each RoadType
	protected HashMap<RoadType, QuadTreeNode<RoadEdge>> root;
	protected Rectangle                                 bounds;

	/**
	 * Constructs a QuadTree with the bounds of the Rectangle given as parameter
	 * 
	 * @param bounds
	 *            The rectangle specifying the bounds.
	 */
	public QuadTree(Rectangle bounds) {
		capacity = 500;
		this.bounds = bounds;
		root = new HashMap<RoadType, QuadTreeNode<RoadEdge>>();
	}

	/**
	 * Constructs a QuadTree with the bounds of the Rectangle and the capacity
	 * as the edge limit in each node
	 * 
	 * @param bounds
	 *            The rectangle specifying the bounds.
	 * @param capacity
	 *            the edge limit in each node
	 */
	public QuadTree(Rectangle bounds, int capacity) {
		this.capacity = capacity;
		this.bounds = bounds;
		root = new HashMap<RoadType, QuadTreeNode<RoadEdge>>();
	}

	/**
	 * @return the bounds of the QuadTree as a Rectangle.
	 */
	public Rectangle getBounds() {
		return bounds.clone();
	}

	/**
	 * @return all edges in the QuadTree.
	 */
	public Set<RoadEdge> getContents() {
		Set<RoadEdge> ret = new HashSet<RoadEdge>();
		for (RoadType key : root.keySet())
			ret.addAll(root.get(key).getAllContents());
		return ret;
	}

	/**
	 * Inserts a RoadEdge into the QuadTree.
	 */
	public void insert(RoadEdge roadedge) {
		if (!root.containsKey(roadedge.data.type))
			root.put(roadedge.data.type, new QuadTreeNode<RoadEdge>(bounds, 1,
					capacity));
		root.get(roadedge.data.type).insert(roadedge);
	}

	/**
	 * @param rectangle
	 *            The rectangle that limits the RoadEdges that will be returned.
	 * @param roadTypes
	 *            The types of RoadEdges we want to be returned.
	 * @return RoadEdges that lie within the given rectangle.
	 */
	public Set<RoadEdge> query(Rectangle rectangle,
			Collection<RoadType> roadTypes) {
		Set<RoadEdge> ret = new LinkedHashSet<RoadEdge>();
		if (roadTypes != null) {
			for (RoadType rt : roadTypes) {
				if (root.containsKey(rt))
					ret.addAll(root.get(rt).query(rectangle));
			}
		}
		return ret;
	}
}