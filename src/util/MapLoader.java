package util;

import util.graph.*;
import view.Loader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * This class contains static methods for loading data from the files into the
 * project. It will both convert data into nodes and connect the nodes with
 * edges for use in a <code>Graph</code>.
 */
public class MapLoader {

	protected QuadTree quadTree;
	protected Trie<RoadEdge> searchTree = new Trie<RoadEdge>();
	protected Graph graph;

	public MapLoader(String mapDataFile) throws IOException {
		this(mapDataFile, null);
	}

	public MapLoader(String mapDataFile, Loader loader) throws IOException {
		// Attempts to read an OSM map data file. For more info about at the OSM data format,
		// see http://wiki.openstreetmap.org/wiki/OSM_XML
		try {
			XMLInputFactory xmlif = XMLInputFactory.newInstance();

			XMLStreamReader xmlr = xmlif.createXMLStreamReader(mapDataFile,
					new FileInputStream(mapDataFile));

			xmlr.nextTag();		// Skips <?xml ...> tag (START_DOCUMENT event)
			xmlr.nextTag();		// Skips <osm version=... > tag

			quadTree = new QuadTree(readBounds(xmlr));

			nextStartElement(xmlr);		// Continue to the list of <node ...> tags

			graph = new Graph(readRoadNodes(xmlr));
			readRoadEdges(xmlr);

			xmlr.close();

			// Tell the loader that we're loading edges
//          if (loader != null) {
//              // There are around 1.2 times as many edges as nodes
//              loader.setProgress("Loading roads...", 0,
//                      (int) (nodes.size() * 1.2));
//          }
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
			double val = Double.parseDouble(xmlr.getAttributeValue(i));

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

		return new Rectangle(boundCoords[0], boundCoords[1], boundCoords[2], boundCoords[3]);
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

			nodes.put(id, new RoadNode(id, lat, lon));
			nextStartElement(xmlr);
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
		Map<String, String> tags = new HashMap<String, String>();

		String roadname = "";
		RoadType type = RoadType.MOTORWAY;
		byte direction = 0;

		nextStartElement(xmlr);		// Skip past way tag to the first nd tag

		// Once a tag element is reached, all nd references have been read, due to the way OSM data is structured.
		while (xmlr.hasNext() && !xmlr.getLocalName().equals("tag")) {
			nodes.add( xmlr.getAttributeValue(0) );
			nextStartElement(xmlr);
		}

		// Once a relation element is reached, all tag elements of the way have been read
		while (xmlr.hasNext() && !xmlr.getLocalName().equals("relation")) {
			tags.put(xmlr.getAttributeValue(0), xmlr.getAttributeValue(1));
			nextStartElement(xmlr);
		}

		// TODO: Get road type and direction from tags
		roadname = tags.get("name");
		WayData data = new WayData(roadname, type, direction);

		for (int i = 0; i < nodes.size() - 1; i++) {
			RoadNode n1 = graph.getNodes().get( nodes.get(i) );
			RoadNode n2 = graph.getNodes().get( nodes.get(i+1) );
			RoadEdge edge = new RoadEdge(data, n1, n2);

			if (!roadname.isEmpty()) {
				searchTree.insert(edge);
			}
			n1.addEdge(edge);
			n2.addEdge(edge);
			quadTree.insert(edge);

			graph.addEdge(edge);
		}
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