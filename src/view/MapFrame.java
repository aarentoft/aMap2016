package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import model.MapModel;
import model.NameSearchModel;
import model.RouteModel;
import util.QuadTree;
import util.Trie;
import util.graph.Graph;
import util.graph.RoadEdge;

/**
 * This class contains the main frame for the progam.
 */
@SuppressWarnings("serial")
public class MapFrame extends JFrame {
	protected final MapPanel mapPanel;
	QuadTree tree;
	MapModel model;

	/**
	 * Creates a new {@link MapFrame} which will then setup all the different
	 * panels and layouts needed in the program.
	 * 
	 * @param title
	 *            The title of the window
	 */
	public MapFrame(String title, QuadTree tree,
			Trie<RoadEdge> searchTree, Graph graph) {
		super(title);
		this.tree = tree;
		model = new MapModel(tree);

		// Set the general layout
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		RouteModel routeModel = new RouteModel(graph);
		NameSearchModel nameSearchModel = new NameSearchModel(searchTree);

		// Make the mapPanel which holds the map itself and add it to the layout
		mapPanel = new MapPanel(model, routeModel, nameSearchModel);
		getContentPane().add(mapPanel, BorderLayout.CENTER);

		buildMenubar();
		
		// Center
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(screenSize.width / 4, screenSize.height / 4);
	    
		pack();
		setVisible(true);
	}

	private JMenuBar buildMenubar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// File menu
		JMenu mFile = new JMenu("File");
		menuBar.add(mFile);

		final JMenuItem miRouteSearch = new JMenuItem("Hide Route Search");
		miRouteSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mapPanel.getSearchPanel().isVisible()) {
					mapPanel.getSearchPanel().setVisible(false);
					miRouteSearch.setText("Show Route Search");
				} else {
					mapPanel.getSearchPanel().setVisible(true);
					miRouteSearch.setText("Hide Route Search");
				}
			}
		});
		mFile.add(miRouteSearch);
		
		final JMenuItem miFitToScreen = new JMenuItem("Fit map to screen");
		miFitToScreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.setBounds(tree.getBounds());
			}
		});
		mFile.add(miFitToScreen);
		
		mFile.add(new JSeparator());

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mFile.add(mntmExit);

		// Help menu
		JMenu mHelp = new JMenu("Help");
		menuBar.add(mHelp);

		JMenuItem mntmNewMenuItem = new JMenuItem("Hotkeys");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Hotkeys();
			}
		});
		mHelp.add(mntmNewMenuItem);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new About();
			}
		});
		mHelp.add(mntmAbout);

		// Space and toolbar
		Component horizontalStrut = Box.createHorizontalStrut(40);
		menuBar.add(horizontalStrut);

		return menuBar;
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(800, 600);
	}

	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
}