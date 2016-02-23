package util;

import util.graph.Graph;
import util.graph.RoadEdge;
import view.Loader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

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
		try {
			XMLInputFactory xmlif = XMLInputFactory.newInstance();

			XMLStreamReader xmlr = xmlif.createXMLStreamReader(mapDataFile,
					new FileInputStream(mapDataFile));

			xmlr.nextTag();		// Skips <?xml ...> tag
			xmlr.nextTag();		// Skips <osm version=... > tag

			quadTree = new QuadTree(parseBounds(xmlr));

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
	 * @param xmlr XMLStreamReader positioned at a {@literal <bounds ...>} tag.
	 * @return Rectangle with the bound coordinates.
     */
	private Rectangle parseBounds(XMLStreamReader xmlr) {
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