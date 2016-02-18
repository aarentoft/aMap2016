package util;

import java.util.HashSet;
import java.util.Set;

/**
 * The actual data-structure behind the QuadTree.
 */
public class QuadTreeNode<T extends Rectangle> {
	// The boundaries of the node.
	protected Rectangle bounds;

	// The number of nodes in depth
	protected final int maxLevel = 7;
	
	// The level at which the node is positioned in the tree.
	protected final int level;

	// Does the node have any children?
	protected boolean hasChildren = false;

	// The amount of Edges a node can contain, before splitting into
	// subnodes.
	protected final int capacity;

	// Edges on this level.
	protected Set<T> contents = new HashSet<T>();

	// The children of the node.
	protected QuadTreeNode<T> NW, SW, NE, SE;

	/**
	 * @param bounds
	 *            The boundaries of the node.
	 * @param level
	 *            What level the node is positioned at in the tree.
	 */
	public QuadTreeNode(Rectangle bounds, int level, int capacity) {
		this.bounds = bounds;
		this.level = level;
		this.capacity = capacity;
	}

	/**
	 * @return whether the node or its children contain any Edges.
	 */
	public boolean isEmpty() {
		return (hasChildren) ? false : contents.isEmpty();
	}

	/**
	 * @return the bounds of the QuadTree as a Rectangle.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * @return all edges contained in the QuadTreeNode or its children.
	 */
	public Set<T> getAllContents() {
		if (this.hasChildren) {
			HashSet<T> contents = new HashSet<T>();

			contents.addAll(NW.getAllContents());
			contents.addAll(SW.getAllContents());
			contents.addAll(NE.getAllContents());
			contents.addAll(SE.getAllContents());

			return contents;
		} else {
			return this.contents;
		}
	}

	/**
	 * @return The Edges that overlaps the queryArea
	 */
	protected Set<T> query(Rectangle queryArea) {
		if (!(queryArea.intersects(bounds) || queryArea.contains(bounds)
				|| bounds.intersects(queryArea) || bounds.contains(queryArea)))
			return new HashSet<T>();

		if (hasChildren) {
			HashSet<T> ret = new HashSet<T>();
			if (!NW.isEmpty())
				ret.addAll(NW.query(queryArea));
			if (!SW.isEmpty())
				ret.addAll(SW.query(queryArea));
			if (!NE.isEmpty())
				ret.addAll(NE.query(queryArea));
			if (!SE.isEmpty())
				ret.addAll(SE.query(queryArea));
			return ret;
		} else {
			return contents;
		}
	}

	/**
	 * Inserts an Edge into the QuadTreeNode.
	 */
	protected void insert(T e) {
		if (!hasChildren && contents.size() >= capacity && level < maxLevel) {
			split();
		}

		if ((e.intersects(bounds) || e.contains(bounds) || bounds.intersects(e) || bounds
				.contains(e))) {
			// if the node has children, pass the edge to the children instead.
			if (hasChildren) {
				// insert into children recursively
				NW.insert(e);
				SW.insert(e);
				NE.insert(e);
				SE.insert(e);
				// if not, insert the edge into the node.
			} else {
				contents.add(e);
			}
		}
	}

	/**
	 * Splits the node into four sub-nodes, if its element-capacity has been
	 * exceeded.
	 */
	protected void split() {
		double halfWidth = bounds.getHalfWidth();
		double halfHeight = bounds.getHalfHeight();

		Rectangle rectNorthWest = new Rectangle(bounds.getMinX(),
				bounds.getMinY(), bounds.getMinX() + halfWidth,
				bounds.getMinY() + halfHeight);
		Rectangle rectNorthEast = new Rectangle(bounds.getMinX() + halfWidth,
				bounds.getMinY(), bounds.getMaxX(), bounds.getMinY()
						+ halfHeight);
		Rectangle rectSouthWest = new Rectangle(bounds.getMinX(),
				bounds.getMinY() + halfHeight, bounds.getMinX() + halfWidth,
				bounds.getMaxY());
		Rectangle rectSouthEast = new Rectangle(bounds.getMinX() + halfWidth,
				bounds.getMinY() + halfHeight, bounds.getMaxX(),
				bounds.getMaxY());

		NW = new QuadTreeNode<T>(rectNorthWest, level + 1, capacity);
		NE = new QuadTreeNode<T>(rectNorthEast, level + 1, capacity);
		SW = new QuadTreeNode<T>(rectSouthWest, level + 1, capacity);
		SE = new QuadTreeNode<T>(rectSouthEast, level + 1, capacity);

		for (T e : contents) {
			NW.insert(e);
			NE.insert(e);
			SW.insert(e);
			SE.insert(e);
		}
		contents = null;
		hasChildren = true;
	}

}
