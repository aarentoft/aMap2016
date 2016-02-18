package util.graph;

import java.util.HashMap;

import util.Rectangle;

/**
 * A graph edge created from an EdgeData object.
 */
@SuppressWarnings("serial")
public class RoadEdge extends Rectangle implements Comparable<RoadEdge> {
	/*
	 * To avoid making a new String for each road name, since many roads have
	 * the same name, new road names are added to this HashMap, and if we
	 * encounter duplicate names, we discard the duplicate, and use the String
	 * stored in this HashMap. RoadEdges with the same name, will now share
	 * their Strings.
	 */
	protected static HashMap<String, String> interner = new HashMap<String, String>();
	public final double length;
	public final String roadname;
	public final RoadNode start;
	public final RoadNode end;
	public final WayData data;

	/**
	 * Creates a RoadEdge from:
	 * 
	 * @param data
	 *            EdgeData object containing required data.
	 * @param v1
	 *            The start node.
	 * @param v2
	 *            The end node.
	 */
	public RoadEdge(WayData data, RoadNode v1, RoadNode v2) {
		super(v1, v2);
		this.data = data;
		this.start = v1;
		this.end = v2;

		this.roadname = data.roadname;

		// TODO: Calculate length based on the coords of v1 and v2
		double len = Math.pow(v1.x - v2.x, 2.0) + Math.pow(v1.y - v2.y, 2.0);
		this.length = Math.sqrt(len);
//		String dir = data.ONE_WAY;

//		// one-way (from-to)
//		if (dir.equalsIgnoreCase("ft")) {
//			this.direction = 1;
//		}
//		// one-way (to-from)
//		else if (dir.equalsIgnoreCase("tf")) {
//			this.direction = 2;
//		}
//		// no driving allowed.
//		else if (dir.equalsIgnoreCase("n")) {
//			this.direction = 3;
//		}
//		// no direction
//		else {
//			this.direction = 0;
//		}
	}

	/**
	 * Clears the 'interner' which keeps all the shared road name Strings.
	 * 
	 * Should be used when done creating RoadEdges to clear up space.
	 */
	public static void clear() {
		interner = null;
	}

	/**
	 * Get the other node.
	 * 
	 * @param node
	 *            The first node.
	 * @return The other node.
	 */
	public RoadNode getOther(RoadNode node) {
		return (node.equals(start)) ? end : start;
	}

	@Override
	public String toString() {
		String s = data.roadname + ", ";
//		if (postnummer_h != postnummer_v) {
//			s += ((postnummer_h < postnummer_v) ? postnummer_h + "-"
//					+ postnummer_v : postnummer_v + "-" + postnummer_h);
//		} else {
//			s += postnummer_h;
//		}
		return s;
	}

	@Override
	public int compareTo(RoadEdge edge) {
		if (this.length > edge.length) {
			return -1;
		} else if (this.length < edge.length) {
			return 1;
		} else {
			return 0;
		}
	}
}
