import util.MapLoader;
import util.QuadTree;
import util.Trie;
import util.graph.Graph;
import util.graph.RoadEdge;
import view.Loader;
import view.MapFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

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
		} else {
			inputPath = showMapDataBrowser();
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

	/**
	 * Show a file chooser so the user can choose which map data to input to the application without
	 * launching the application from command line. If a file is not selected (eg. if the user clicks cancel
	 * or closes the file chooser), the entire application will close.
	 *
	 * @return The path of the user selected file
     */
	private static String showMapDataBrowser() {
		String fileChooserDefault = null;

		// Get the directory of the jar/class file/whatever the application is launched from
		try {
			String rawPathOfApp = Driver.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			fileChooserDefault = URLDecoder.decode(rawPathOfApp, "UTF-8");
		} catch (UnsupportedEncodingException | URISyntaxException e) {
			e.printStackTrace();
		}

		// Create file Chooser dialog which only accepts .OSM files
		JFileChooser fileChooser = new JFileChooser(fileChooserDefault);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("OSM Map Data Files", "osm");
		fileChooser.setFileFilter(filter);

		int fileChooserStatus = fileChooser.showOpenDialog(null);

		if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile().getAbsolutePath();
		} else {
			System.exit(0);
		}

		// Only necessary for compilation to succeed. This will never be reached.
		return "";
	}
}