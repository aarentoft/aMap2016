package view;

import controller.MapPanelComponentAdapter;
import controller.MapPanelMouseListener;
import controller.OSMCopyrightNoticeMouseAdapter;
import exceptions.PathNotFoundException;
import model.MapInteractionModel;
import model.MapModel;
import model.NameSearchModel;
import model.RouteModel;
import util.DrawingThread;
import util.Point;
import util.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.lang.Thread.State;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is used to make a panel which contains a map with the data given
 * to it through the constructor.
 */
@SuppressWarnings("serial")
public class MapPanel extends JLayeredPane implements Observer {
	public final Object lock = new Object();

	protected Thread drawingThread;
	protected MapModel mapModel;
	protected RouteModel routeModel;
	protected MapInteractionModel mapInteractionModel = new MapInteractionModel();
	protected Image buffer, bufferBG, bufferRoute;
	protected SearchPanel searchPanel;
	protected JPanel overlay;

	/**
	 * Creates a new {@link MapPanel}.
	 * 
	 * @param mapModel
	 *            The {@link MapModel} which this panel should be based on.
	 */
	public MapPanel(MapModel mapModel, RouteModel routeModel,
			NameSearchModel nameSearchModel) {
		this.mapModel = mapModel;
		this.routeModel = routeModel;

		String copyrightURL = "http://www.openstreetmap.org";

		// Build the search panel and place it in the upper left corner.
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		searchPanel = new SearchPanel(routeModel, nameSearchModel);
		searchPanel.setBounds(new java.awt.Rectangle(searchPanel
				.getPreferredSize()));

		// Build OpenStreetMap copyright notice and place it in lower right corner.
		// This is mainly achieved by the GridBagConstraints of the search panel, though.
		GridBagConstraints gbc2  = new GridBagConstraints();
		gbc2.anchor = GridBagConstraints.SOUTHEAST;
		JLabel copyrightNotice = new JLabel("<html>© <a href=>OpenStreetMap</a> contributors</html>");
		copyrightNotice.setOpaque(true);
		copyrightNotice.setBackground(Color.WHITE);
		copyrightNotice.setToolTipText(copyrightURL);
		copyrightNotice.addMouseListener(new OSMCopyrightNoticeMouseAdapter(copyrightURL));

		// Build invisible center thing, which pushes the search panel and the OSM contributor notice
		// to their respective corner. This approach, instead of just using the search panel to push the OSM notice,
		// prevents the notice from jumping to the center when the search panel is hidden.
		GridBagConstraints gbcCenterThing = new GridBagConstraints();
		gbcCenterThing.anchor = GridBagConstraints.CENTER;
		gbcCenterThing.weightx = 1.0;
		gbcCenterThing.weighty = 1.0;
		JPanel centerThing = new JPanel(new FlowLayout());
		centerThing.setOpaque(false);
		centerThing.setSize(getSize());

		// Build an overlay container for the search panel and copyright notice to put
		// on top of the map.
		overlay = new JPanel(new GridBagLayout());
		overlay.setOpaque(false);
		overlay.setBounds(this.getBounds());
		overlay.add(searchPanel, gbc);
		overlay.add(centerThing, gbcCenterThing);
		overlay.add(copyrightNotice, gbc2);
		this.add(overlay, JLayeredPane.PALETTE_LAYER);

		// Setup this panel
		this.setFocusable(true);

		// Add as observer to the two models
		mapModel.addObserver(this);
		mapInteractionModel.addObserver(this);

		routeModel.addObserver(this);

		// Add listeners for mouse, component and key
		MapPanelMouseListener mouseListener = new MapPanelMouseListener(
				mapModel, mapInteractionModel, routeModel);
		addMouseWheelListener(mouseListener);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addComponentListener(new MapPanelComponentAdapter(mapModel, mapInteractionModel));

		drawingThread = new DrawingThread(mapModel, mapInteractionModel,
				routeModel, this);

		setDoubleBuffered(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// Set the x and y position of the images (used for panning)
		int x = 0, y = 0;
		if (buffer != null) {
			Dimension d = mapModel.getMapDimension();
			Point c = mapModel.getBufferCenter();
			Point p = mapInteractionModel.getPanningPoint();
			x = (int) (d.width / 2 - c.x - p.x);
			y = (int) (d.height / 2 - c.y - p.y);
		}

		g2.drawImage(bufferBG, x, y, null);
		g2.drawImage(buffer, x, y, null);

		// Draw the route with a 45% alpha value
		AlphaComposite myAlpha = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.45f);
		g2.setComposite(myAlpha);
		g2.drawImage(bufferRoute, x, y, null);

		// If we're zooming, we should draw the zoom-box on top of the rest
		if (mapInteractionModel.isDragBoxZooming()) {
			// Reset the composite
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					1.0f));
			g2.setColor(new Color(0, 0, 0, 200));

			Area greyOut = new Area(
					new Rectangle(0, 0, getWidth(), getHeight()));
			greyOut.subtract(new Area(mapInteractionModel
					.getDragBoxZoomRectangle()));
			greyOut.add(new Area(mapInteractionModel
					.getDragBoxZoomCenterRectangle()));

			g2.fill(greyOut);
		}
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}

	@Override
	public void update(Observable observable, Object obj) {
		// Start the seperate drawing thread if it's not started
		if (!drawingThread.isAlive()) {
			drawingThread.start();
			// Since it updates the buffers when it starts, we don't have to do
			// anything here.
			return;
		}

		if (observable instanceof MapModel || observable instanceof RouteModel) {
			try {
				routeModel.doRouteSearch();
			} catch (PathNotFoundException e) {
				JOptionPane.showMessageDialog(this,
						"No route found between the points", "No route",
						JOptionPane.WARNING_MESSAGE);
			} catch (IllegalArgumentException e) {
				JOptionPane
						.showMessageDialog(
								this,
								"Both points need to be set before a route can be found",
								"No route", JOptionPane.WARNING_MESSAGE);
			}

			if (drawingThread.getState().equals(State.WAITING)) {
				synchronized (lock) {
					// It's waiting, lets wake it up
					lock.notify();
				}
			} else {
				// It's drawing, let's interrupt it
				drawingThread.interrupt();
			}
		} else {
			repaint();
		}
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		overlay.setBounds(x, y, width, height);
	}

	@Override
	public void setBounds(java.awt.Rectangle r) {
		super.setBounds(r);
		overlay.setBounds(r);
	}

	/**
	 * Sets the buffer image
	 * 
	 * @param buffer
	 */
	public void setBuffer(Image buffer) {
		this.buffer = buffer;
	}

	/**
	 * Sets the background image
	 * 
	 * @param bufferBG
	 */
	public void setBufferBG(Image bufferBG) {
		this.bufferBG = bufferBG;
	}

	/**
	 * Sets the route image
	 * 
	 * @param bufferRoute
	 */
	public void setBufferRoute(Image bufferRoute) {
		this.bufferRoute = bufferRoute;
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}
}