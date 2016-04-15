package util.graph;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * RoadType enum class which holds information about the different RoadTypes.
 */
public enum RoadType implements Comparable<RoadType> {
	MOTORWAY(1, new Color(255, 195, 69), new Color(210, 147, 0), 1.5, 10), MOTORWAY_TUNNEL(
			41, new Color(255, 195, 69), new Color(210, 147, 0), 1.5, 10), MOTORWAY_EXIT(
			31, new Color(255, 224, 104), new Color(229, 169, 25), 1, 2),

	EXPRESSWAY(2, new Color(255, 195, 69), new Color(210, 147, 0), 1.5, 9), EXPRESSWAY_TUNNEL(
			42, new Color(255, 195, 69), new Color(210, 147, 0), 1.5, 9), EXPRESSWAY_EXIT(
			32, new Color(255, 195, 69), new Color(210, 147, 0), 1, 1),

	PRIMARY_ROAD(3, new Color(255, 253, 139), new Color(207, 185, 119), 1, 8), PRIMARY_ROAD_EXIT(
			33, new Color(255, 253, 139), new Color(207, 185, 119), 1, 1),

	SECONDARY_ROAD(4, new Color(255, 253, 139), new Color(207, 185, 119), 1, 7), SECONDARY_ROAD_EXIT(
			34, new Color(255, 253, 139), new Color(207, 185, 119), 1, 1),

	TERTIARY_ROAD(17, new Color(241, 241, 243), new Color(150, 150, 152), 1, 1),

	EXIT(35, Color.WHITE, new Color(195, 187, 163), 1, 1),
	ROAD(5, Color.WHITE, new Color(195, 187, 163), 0.7, 6),
	MINOR_ROAD(6, Color.WHITE, new Color(195, 187, 163), 0.7, 5),

	PEDESTRIANISHED_ZONE(11, new Color(203, 203, 217), new Color(145, 143, 144), 0.5, 1),
	FOOTWAY(12, new Color(234, 116, 104), new Color(234, 116, 104), 0.0, 1),
	STEPS(17, new Color(234, 116, 104), new Color(234, 116, 104), 0.0, 1), // Visually identical to FOOTWAY
	CYCLEWAY(13, new Color(57, 28, 253), new Color(57, 28, 253), 0.0, 1),
	RESIDENTIAL(14, new Color(241, 241, 243), new Color(181, 176, 176), 1, 1),
	SERVICE(15, new Color(241, 241, 243), new Color(181, 176, 176), 0.5, 1),
	TRACK(16, new Color(148, 110, 32), new Color(148, 110, 32), 0, 1),
	LIVING_STREET(18, new Color(221, 221, 221), new Color(176, 176, 176), 1, 1),

	PATH(8, new Color(250, 250, 245), new Color(216, 205, 198), 0.5, 1),

	DIRT_ROAD(10, Color.WHITE, new Color(195, 187, 163), 0.5, 3),

	FERRY(80, new Color(145, 171, 196), new Color(145, 171, 196), 1.5, 10),

	UNKNOWN(99);

	protected static final double coordinateRatioRoadLimit1 = 15.0;
	protected static final double coordinateRatioRoadLimit2 = 35.0;
	protected static final double coordinateRatioRoadLimit3 = 120.0;

	public final int id;
	public final Color color;
	public final Color colorBG;
	public final double width;
	public final int priority;

	/**
	 * Constructs this enum with default values for all other fields than id.
	 * 
	 * @param id
	 *            The id of this RoadType
	 */
	private RoadType(int id) {
		this.id = id;
		this.color = Color.BLACK;
		this.colorBG = Color.GRAY;
		this.width = 0;
		this.priority = 1;
	}

	/**
	 * @param id
	 *            The id of this RoadType
	 * @param color
	 *            The color of this RoadType
	 * @param border
	 *            The color color of this RoadType
	 * @param width
	 *            The width of this RoadType
	 * @param priority
	 *            The priority of this RoadType
	 */
	private RoadType(int id, Color color, Color border, double width,
			int priority) {
		this.id = id;
		this.color = color;
		this.colorBG = border;
		this.width = width;
		this.priority = priority;
	}

	/**
	 * Get enum for a specific road type ID from OSM data.
	 *
	 * @param str road type string to translate
	 * @return corresponding {@link RoadType} enum
     */
	public static RoadType getEnum(String str) {
		switch (str) {
			case "motorway"       : return MOTORWAY;
			case "trunk"          : return EXPRESSWAY;
			case "primary"        : return PRIMARY_ROAD;
			case "secondary"      : return SECONDARY_ROAD;
			case "tertiary"       : return TERTIARY_ROAD;
			case "road"           : return ROAD;
			case "unclassified"   : return MINOR_ROAD;
			case "path"           : return PATH;
			case "pedestrian"     : return PEDESTRIANISHED_ZONE;
			case "motorway_link"  : return MOTORWAY_EXIT;
			case "trunk_link"     : return EXPRESSWAY_EXIT;
			case "primary_link"   : return PRIMARY_ROAD_EXIT;
			case "secondary_link" : return SECONDARY_ROAD_EXIT;
			case "footway" 		  : return FOOTWAY;
			case "cycleway"		  : return CYCLEWAY;
			case "residential"    : return RESIDENTIAL;
			case "service"        : return SERVICE;
			case "track"          : return TRACK;
			case "steps"          : return STEPS;
			case "living_street"  : return LIVING_STREET;
			default : return UNKNOWN;
		}
	}

	/**
	 * Finds all the road types for a given coordinate aspect ratio.
	 * 
	 * @param coordinateAspectRatio
	 *            The aspect ratio to base the selection on.
	 * @return A list of RoadTypes
	 */
	public static List<RoadType> getRoadtypesFromZoomLevel(
			double coordinateAspectRatio) {
		List<RoadType> list = new LinkedList<RoadType>();

		// Default - Always shown
		list.addAll(Arrays.asList(FERRY, MOTORWAY,
				MOTORWAY_TUNNEL, MOTORWAY_EXIT, EXPRESSWAY, EXPRESSWAY_TUNNEL,
				EXPRESSWAY_EXIT, PRIMARY_ROAD, PRIMARY_ROAD_EXIT,
				SECONDARY_ROAD, SECONDARY_ROAD_EXIT));

		if (coordinateAspectRatio < coordinateRatioRoadLimit3) {
			list.addAll(Arrays.asList(ROAD));
		}

		if (coordinateAspectRatio < coordinateRatioRoadLimit2) {
			list.addAll(Arrays.asList(MINOR_ROAD, EXIT, RESIDENTIAL, LIVING_STREET, TRACK, SERVICE, TERTIARY_ROAD));
		}

		if (coordinateAspectRatio < coordinateRatioRoadLimit1) {
			list.addAll(Arrays.asList(PATH, DIRT_ROAD,
					PEDESTRIANISHED_ZONE, FOOTWAY, CYCLEWAY, STEPS));
		}
		Collections.sort(list, new PriorityComparator());

		return list;
	}
	
	/**
	 * @return All RoadTypes as a Set
	 */
	
	public static List<RoadType> getAllRoadTypes() {
		return getRoadtypesFromZoomLevel(0);
	}

	/**
	 * @return A list of all drivable RoadTypes
	 */
	public static List<RoadType> getAllDrivableRoads() {
		List<RoadType> list = new LinkedList<RoadType>();
		list.addAll(Arrays.asList(new RoadType[]{FERRY, MOTORWAY,
				MOTORWAY_TUNNEL, MOTORWAY_EXIT, EXPRESSWAY, EXPRESSWAY_TUNNEL,
				EXPRESSWAY_EXIT, PRIMARY_ROAD, PRIMARY_ROAD_EXIT,
				SECONDARY_ROAD, SECONDARY_ROAD_EXIT, TERTIARY_ROAD, ROAD, MINOR_ROAD, EXIT,
				DIRT_ROAD, LIVING_STREET}));
		Collections.sort(list, new PriorityComparator());
		return list;
	}

	/**
	 * A simple Comparator Which will sort the RoadTypes based on their
	 * priority.
	 */
	static protected class PriorityComparator implements Comparator<RoadType> {
		@Override
		public int compare(RoadType o1, RoadType o2) {
			return o1.priority - o2.priority;
		}
	}
}