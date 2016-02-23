import java.io.IOException;

import javax.swing.JOptionPane;

import util.MapLoader;
import util.QuadTree;
import util.Trie;
import util.graph.Graph;
import util.graph.RoadEdge;
import view.Loader;
import view.MapFrame;

/**
 * This is the main class which will start the program. It does not need any
 * arguments to run.
 */
public class Driver {
	/**
	 * This is the main method used for launching the application. It loads the
	 * <code>LoadSequence</code> and and creates a <code>Loader</code> object
	 * and connect them to each other, so the <code>Loader</code> knows whether
	 * or not the <code>LoadSequence</code> is still running.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = "./Txt/";

		// TODO: Fix map data hardcode
		// Allow specifying the data dir from commandline.
//		if (args.length != 0) {
//			dir = args[0];
//		}
//		if (dir.charAt(dir.length() - 1) != '/') {
//			dir += '/';
//		}

		Loader loader = new Loader();

		try {
			// Loading nodes and edges
			MapLoader mapLoader = new MapLoader("map-anholt-mini-1-indexed.osm", loader);
			// Clears the shared data from the roadEdges. Note that it must
			// be placed here in order to make the tests work.
			// RoadEdge.clear();
//			QuadTree tree = mapLoader.getQuadTree();
//			Trie<RoadEdge> searchTrie = mapLoader.getSearchTree();
//			Graph graph = mapLoader.getGraph();

//			new MapFrame("aMap", tree, searchTrie, graph);
//			loader.dispose();
		} catch (IOException e) {
			loader.dispose();
			JOptionPane
					.showMessageDialog(
							null,
							"Map data could not be found. Please make sure that you entered the correct path to your map data.\n"
									+ "Current dir: " + dir + " --- "+e.getMessage(),
							"An error occurred", JOptionPane.ERROR_MESSAGE);
		}
	}
}