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
		String inputPath = "";

		// Allow specifying the data dir from commandline.
		if (args.length != 0) {
			inputPath = args[0];
		}

		Loader loader = new Loader();

		try {
			// Loading nodes and edges
			MapLoader mapLoader = new MapLoader(inputPath, loader);
			// Clears the shared data from the roadEdges. Note that it must
			// be placed here in order to make the tests work.
			// RoadEdge.clear();
			QuadTree tree = mapLoader.getQuadTree();
			Trie<RoadEdge> searchTrie = mapLoader.getSearchTree();
			Graph graph = mapLoader.getGraph();

			new MapFrame("aMap", tree, searchTrie, graph);
			loader.dispose();
		} catch (IOException e) {
			loader.dispose();
			JOptionPane
					.showMessageDialog(
							null,
							"Map data could not be found. Please make sure that you entered the correct path to your map data.\n"
									+ "---\n"
									+ e.getMessage(),
							"An error occurred", JOptionPane.ERROR_MESSAGE);
		}
	}
}