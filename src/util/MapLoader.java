package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import util.graph.*;
import view.Loader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
		// TODO: Should read OSM data and build data structures from this data
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