package util.graph;

import java.util.HashMap;

import util.Rectangle;
import util.TrieSearchable;

/**
 * A graph edge created from an EdgeData object.
 */
public class RoadEdge extends Rectangle implements Comparable<RoadEdge>, TrieSearchable {
	// Length in meters
	public final double length;
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

		/* NOTE: Since very few edges (if any at all) represent a part of a road which spans more than
		 * a small part of a UTM zone, the error due to the projection will be relatively small so
		 * a regular Euclidean distance gets the job done for now.
		 * A more correct solution would be to use the Haversine formula using the lat long coordinates,
		 * but at the moment these coords are unavailable as they are converted to UTM as
		 * soon as the data is read from the map file and the RoadNode is constructed.
		 */
		double len = Math.pow(v1.x - v2.x, 2.0) + Math.pow(v1.y - v2.y, 2.0);
		this.length = Math.sqrt(len);
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

	/**
	 * @return Determines how the road edge appears in the search fields when selected.
     */
	public String getSearchFieldRepresentation() {
		return getTrieRepresentation();
	}

	@Override
	public String getTrieRepresentation() {
		return data.roadname;
	}

	@Override
	public String toString() {
		String s = "[RoadEdge name=" + data.roadname + ", length=" + length + ", data=" + data.toString() + "]";
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
