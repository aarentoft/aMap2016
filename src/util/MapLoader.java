package util;

import util.graph.*;
import view.Loader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * This class contains static methods for loading data from the files into the
 * project. It will both convert data into nodes and connect the nodes with
 * edges for use in a <code>Graph</code>.
 */
public class MapLoader {

	protected Loader loader;
	protected QuadTree quadTree;
	protected Trie<RoadEdge> searchTree = new Trie<RoadEdge>();
	protected Graph graph;

	protected UTMConverter utmConverter = new UTMConverter();

	public MapLoader(String mapDataFilePath) throws IOException {
		this(mapDataFilePath, null);
	}

	public MapLoader(String mapDataFilePath, Loader loader) throws IOException {
		this.loader = loader;

		// Attempt to read an OSM map data file. For more info about at the OSM data format,
		// see http://wiki.openstreetmap.org/wiki/OSM_XML
		try {
			int fileLength = (int) new File(mapDataFilePath).length();

			XMLInputFactory xmlif = XMLInputFactory.newInstance();

			XMLStreamReader xmlr = xmlif.createXMLStreamReader(mapDataFilePath,
					new FileInputStream(mapDataFilePath));

			xmlr.nextTag();		// Skips <?xml ...> tag (START_DOCUMENT event) where getLocalName() cannot be used.

			/* Skip data until <bounds ...> element is reached. Ensures that OSM data can be read, no matter what data
			   precedes the list of nodes and the list of ways. This is important because OSM can be formatted
			   slightly different depending on how the data was exported. */
			while(!xmlr.getLocalName().equals("bounds")) {
				/* Do-while() necessary. With a regular while() the body will only keep executing until
				   the first START_ELEMENT. If that is not a <bounds ...> element, execution will be stuck in an
				   infinite loop here, since the inner while condition will continue to be false, but the outer
				   while condition is true. The Do-while() allows the body to execute before the inner while condition
				   is checked, thus skipping past the START_ELEMENT.*/
				do {
					xmlr.next();
				} while (xmlr.getEventType() != XMLStreamConstants.START_ELEMENT);
			}

			quadTree = new QuadTree(readBounds(xmlr));

			nextStartElement(xmlr);		// Continue to the list of <node ...> tags

			setProgress("Loading nodes...", xmlr.getLocation().getCharacterOffset(), fileLength);
			graph = new Graph(readRoadNodes(xmlr));

			setProgress("Loading edges...", xmlr.getLocation().getCharacterOffset(), fileLength);
			readRoadEdges(xmlr);

			// Fill the progress bar completely. As the OSM data's <relation ...> elements are not used,
			// the progress bar would stop part way without this.
			setProgress("Done", fileLength, fileLength);

			xmlr.close();
		} catch (XMLStreamException e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Parse the two coordinate sets of the bounds of the map data. This data is contained in the
	 * {@literal <bounds ...>} tag.
	 *
	 * @param xmlr {@code XMLStreamReader} positioned at a {@literal <bounds ...>} tag.
	 * @return {@code Rectangle} with the bound coordinates.
     */
	private Rectangle readBounds(XMLStreamReader xmlr) {
		double[] boundCoords = new double[4];

		// get attribute values of the <bounds ...> tag
		for (int i = 0; i < xmlr.getAttributeCount(); i++) {
			double val = 0.0;
			try {
				// This try/catch handles cases where the bounds tag contain other values than coordinates.
				// Osmosis, for instance, puts an extra attribute with an alphabetic value in the bounds tag.
				val = Double.parseDouble(xmlr.getAttributeValue(i));
			} catch (NumberFormatException e) {
				continue;
			}

			switch (xmlr.getAttributeLocalName(i)) {
				case "minlat":
					boundCoords[0] = val;
					break;
				case "minlon":
					boundCoords[1] = val;
					break;
				case "maxlat":
					boundCoords[2] = val;
					break;
				case "maxlon":
					boundCoords[3] = val;
					break;
			}
		}

		UTMCoordinateSet utmCoords1 = utmConverter.LatLonToUTM(boundCoords[0], boundCoords[1]);
		UTMCoordinateSet utmCoords2 = utmConverter.LatLonToUTM(boundCoords[2], boundCoords[3]);

		return new Rectangle(
				utmCoords1.getEasting(), utmCoords1.getNorthing(),
				utmCoords2.getEasting(), utmCoords2.getNorthing());
	}

	/**
	 * Reads a list of nodes from OSM data and generates a Map (NodeID-&gt;NodeObject). {@link RoadNode} objects are
	 * generated based on the OSM data. This method assumes that the {@code XMLStreamReader} is positioned at the beginning
     * of a list of {@literal <node ...>} tags, otherwise it will skip XML data until it reaches a node list,
     * and then read that list. If there is no node list at all, it will return an empty map and the {@code XMLStreamReader}
     * will be positioned either at a {@literal <way ...>} tag or at the end of the document.
	 * <p>In short, ensure that the {@code XMLStreamReader} is positioned at a node list.</p>
	 *
	 * @param xmlr {@code XMLStreamReader} positioned at the beginning of a list of {@literal <node ...>} tags.
	 * @return A (NodeID-&gt;NodeObject) mapping of {@code RoadNode}.
	 * @throws XMLStreamException Rethrown from {@code XMLStreamReader}.
     */
	private Map<String, RoadNode> readRoadNodes(XMLStreamReader xmlr) throws XMLStreamException {
		Map<String, RoadNode> nodes = new TreeMap<String, RoadNode>();
		String id = "";
		Double lat = -1.0;
		Double lon = -1.0;

		while (xmlr.hasNext()) {
			// Once a way tag is reached, all nodes have been read, due to the way OSM data is structured.
			if (xmlr.getLocalName().equals("way"))
				break;

			// If a node has, sub XML tags (eg. <tag ...>, they are skipped
			if (!xmlr.getLocalName().equals("node")) {
				nextStartElement(xmlr);
				continue;
			}

			// get node attribute values
			for (int i = 0; i < xmlr.getAttributeCount(); i++) {
				switch (xmlr.getAttributeLocalName(i)) {
					case "id":
						id = xmlr.getAttributeValue(i);
						break;
					case "lat":
						lat = Double.parseDouble(xmlr.getAttributeValue(i));
						break;
					case "lon":
						lon = Double.parseDouble(xmlr.getAttributeValue(i));
						break;
				}
			}

			UTMCoordinateSet utmCoords = utmConverter.LatLonToUTM(lat, lon);

			nodes.put(id, new RoadNode(id, utmCoords.getEasting(), utmCoords.getNorthing()));
			nextStartElement(xmlr);

			// NOTE: Despite its name, getCharacterOffset actually returns byte offset
			setProgress(xmlr.getLocation().getCharacterOffset());
		}

		return nodes;
	}

	/**
	 * Reads a list of Way tags from OSM data. A {@link RoadEdge} is generated for each node pair referenced by the Way
	 * and the relevant data from the Way is attached to the edges. The edges are added both to the graph,
	 * the quad tree, and the trie used for searching. This method assumes that the {@code XMLStreamReader} is
	 * positioned at the beginning of a list of {@literal <way ...>} tags. Otherwise, it will not do anything, except
	 * moving the {@code XMLStreamReader} unpredictably through the XML.
	 *
	 * @param xmlr {@code XMLStreamReader} positioned at the beginning of a list of {@literal <way ...>} tags.
	 * @throws XMLStreamException Rethrown from {@code XMLStreamReader}.
     */
	private void readRoadEdges(XMLStreamReader xmlr) throws XMLStreamException {
		// Node IDs stored as strings
		List<String> nodes = new ArrayList<String>();
		// Key -> Value mapping of tags
		Map<String, String> tags;

		String roadname = "";
		RoadType type = RoadType.MOTORWAY;
		boolean oneway = false;

		while (xmlr.hasNext() && xmlr.getLocalName().equals("way")) {
			nextStartElement(xmlr);        // Skip past way tag to the first nd tag

			nodes.clear();
			// Once a tag element is reached, all nd references have been read, due to the way OSM data is structured.
			while (xmlr.hasNext() && xmlr.getLocalName().equals("nd")) {
				nodes.add(xmlr.getAttributeValue(0));
				nextStartElement(xmlr);
			}

			tags = readTags(xmlr);

			// way is not a road or route, so it shouldn't be added to the road network model
			if (tags.get("highway") == null && tags.get("route") == null)
				continue;

			roadname = tags.get("name") != null ? tags.get("name") : "";

			// Ferries are a special case since they are not highways in OSM data, only routes.
			type = tags.get("route") != null && tags.get("route").equals("ferry") ? RoadType.FERRY : RoadType.getEnum(tags.get("highway"));

			// If oneway tag exists, and set to a value that indicates oneway
			oneway = tags.get("oneway") == null || tags.get("oneway").equals("yes") || tags.get("oneway").equals("-1");

			WayData data = new WayData(roadname, type, oneway);

			for (int i = 0; i < nodes.size() - 1; i++) {
				RoadNode n1 = graph.getNodes().get(nodes.get(i));
				RoadNode n2 = graph.getNodes().get(nodes.get(i + 1));
				RoadEdge edge = new RoadEdge(data, n1, n2);

				if (!roadname.isEmpty())
					searchTree.insert(edge);

				quadTree.insert(edge);
				graph.addEdge(edge);
			}

			setProgress(xmlr.getLocation().getCharacterOffset());
		}
	}

	/**
	 * Reads a list of tags from OSM data and generates a Key Value pairs for each tag. This method assumes that the
	 * {@code XMLStreamReader} is positioned at the beginning of a list of {@literal <tag...>} element,
	 * otherwise it will return an empty map.
	 *
	 * @param xmlr {@code XMLStreamReader} positioned at the beginning of a list of {@literal <tag ...>} element.
	 * @return A Map of tags as Key-Value pairs.
	 * @throws XMLStreamException Rethrown from {@code XMLStreamReader}
     */
	private Map<String, String> readTags(XMLStreamReader xmlr) throws XMLStreamException {
		Map<String, String>	tags = new HashMap<>();

		while (xmlr.hasNext() && xmlr.getLocalName().equals("tag")) {
			tags.put(xmlr.getAttributeValue(0), xmlr.getAttributeValue(1));
			nextStartElement(xmlr);
		}

		return tags;
	}

	/**
	 * Helper method which calls {@code nextTag()} on an {@code XMLStreamReader} object until a new
	 * {@code START_ELEMENT} event is reached.
	 *
	 * @param xmlr {@code XMLStreamReader} to "fast-forward"
	 * @throws XMLStreamException Rethrown from {@code XMLStreamReader}.
     */
	private void nextStartElement(XMLStreamReader xmlr) throws XMLStreamException {
		// Do-while() necessary. while() will never execute the body since the current event is already START_ELEMENT,
		// but the XMLStreamReader should skip to the next one.
		do {
			xmlr.nextTag();
		} while (xmlr.getEventType() != XMLStreamConstants.START_ELEMENT && xmlr.hasNext());
	}

	/**
	 * Convenience method to only set UI loader progress if a UI loader object is present.
	 * @param progress New progress value
     */
	private void setProgress(int progress) {
		if (loader != null) {
			loader.setProgress(progress);
		}
	}

	/**
	 * Convenience method to only set UI loader label, progress, and max values if a UI loader object is present.
	 * @param label New label value
	 * @param progress New progress value
	 * @param max New maximum loader value. Should be larger than progress
     */
	private void setProgress(String label, int progress, int max) {
		if (loader != null) {
			loader.setProgress(label, progress, max);
		}
	}

	/**
	 * Get the generated <code>QuadTree</code>
	 * 
	 * @return The <code>QuadTree</code>
	 */
	public QuadTree getQuadTree() {
		return quadTree;
	}

	/**
	 * Get the generated <code>SearchTree</code>
	 * 
	 * @return The <code>SearchTree</code>
	 */
	public Trie<RoadEdge> getSearchTree() {
		return searchTree;
	}

	/**
	 * Get the generated <code>Graph</code>
	 * 
	 * @return The Graph contained in this class.
	 */
	public Graph getGraph() {
		return graph;
	}
}