package util;

import datastructures.graph.RoadEdge;
import model.MapInteractionModel;
import model.MapModel;
import model.RouteModel;
import view.MapPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * This class will make a thread which runs in the background and waits for
 * notifies. When it receives a notify, it will draw the roads for the current
 * view and set the buffered images in the {@link MapPanel}
 */
public class DrawingThread extends Thread {
	protected MapModel mapModel;
	protected MapInteractionModel mapInteractionModel;
	protected RouteModel routeModel;
	protected MapPanel panel;
	protected Object lock;
	protected Converter converter;
	protected Rectangle lastDrawnBounds = null;

	/**
	 * Makes a new drawing thread.
	 * 
	 * @param mapModel
	 *            The model which holds information about the map
	 * @param mapInteractionModel
	 *            The model which holds information about the current
	 *            interaction with the map. This is needed to ensure that the
	 *            model gets notified when the panning point should be reset.
	 * @param routeModel
	 *            The model which holds information about the current route.
	 * @param panel
	 *            The owning MapPanel which should be updated with the drawn
	 *            {@link BufferedImage}s
	 */
	public DrawingThread(MapModel mapModel,
			MapInteractionModel mapInteractionModel, RouteModel routeModel,
			MapPanel panel) {
		this.mapModel = mapModel;
		this.mapInteractionModel = mapInteractionModel;
		this.routeModel = routeModel;
		this.panel = panel;
		this.lock = panel.lock;
		this.converter = new Converter(mapModel);
	}

	@Override
	public void run() {
		while (true) {
			synchronized (lock) {
				try {
					updateBuffers();
					lock.wait();
				} catch (InterruptedException ignore) {
				}
			}
		}
	}

	/**
	 * Internal method to update the {@link BufferedImage}s which the
	 * {@link MapPanel} is based on.
	 * 
	 * @throws InterruptedException
	 *             If this thread gets interrupted while drawing.
	 */
	protected void updateBuffers() throws InterruptedException {
		// Only update the drawing of the edges if we actually have changed the
		// bounds since the last time. This makes it much faster to place
		// markers for the route and ensures that we don't redraw unless it's
		// needed.
		if (!mapModel.getBounds().equals(lastDrawnBounds)) {
			// Make the new images
			BufferedImage buffer = new BufferedImage(
					mapModel.getBufferMapDimension().width,
					mapModel.getBufferMapDimension().height,
					BufferedImage.TYPE_INT_ARGB);
			BufferedImage bufferBG = new BufferedImage(
					mapModel.getBufferMapDimension().width,
					mapModel.getBufferMapDimension().height,
					BufferedImage.TYPE_INT_ARGB);

			// Get the graphics objects for the images
			Graphics2D bufferBGGraphics = (Graphics2D) bufferBG.getGraphics();
			Graphics2D bufferGraphics = (Graphics2D) buffer.getGraphics();

			// Set the background color for the BG buffer image
			bufferBGGraphics.setColor(new Color(242, 239, 233));
			bufferBGGraphics.fillRect(0, 0, bufferBG.getWidth(null),
					bufferBG.getHeight(null));

			// Set antialiased on for the buffers
			bufferBGGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			double coordinateRatio = Math.log(mapModel
					.getMaxCoordinateAspectRatio())
					- Math.log(mapModel.getXAxisCoordinateAspectRatio());

			// It is possible that the coordinate ratio is less than 0, which
			// gives an exception. So we'll make it 0.0 if it is negative.
			if (coordinateRatio < 0.0) {
				coordinateRatio = 0.0;
			}

			// Loop through all the edges
			Set<RoadEdge> edges = mapModel.getEdgeSet();
			for (RoadEdge edge : edges) {
				if (Thread.interrupted()) {
					// Dispose and stop if we've been interrrupted
					bufferGraphics.dispose();
					bufferBGGraphics.dispose();
					throw new InterruptedException();
				}
				float stroke = (float) (coordinateRatio * edge.data.type.width);
				drawRoadEgde(edge, stroke, bufferBGGraphics, bufferGraphics,
						null);
			}
			bufferBGGraphics.dispose();
			bufferGraphics.dispose();
			panel.setBufferBG(bufferBG);
			panel.setBuffer(buffer);
			lastDrawnBounds = mapModel.getBounds().clone();

			// XXX MVC conflict: Not allowed to update the model directly from
			// the view (allowed for now) (DO NOT DELETE!)
			mapInteractionModel.resetPanningPoint();
		}

		BufferedImage bufferRoute = new BufferedImage(mapModel
				.getBufferMapDimension().width, mapModel
				.getBufferMapDimension().height,
				BufferedImage.TYPE_INT_ARGB);
		// Draw the route if we have one
		if (routeModel.getPoints().size() > 0) {
			Graphics2D bufferRouteGraphics = (Graphics2D) bufferRoute
					.getGraphics();
			bufferRouteGraphics.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			for (RoadEdge edge : routeModel.getRoute()) {
				drawRoadEgde(edge, 5.0f, null, bufferRouteGraphics, new Color(
						0, 0, 255));
			}

			for (RoadEdge edge : routeModel.getPoints().values()) {
				drawRoadEgde(edge, 5.0f, null, bufferRouteGraphics, new Color(
						255, 0, 0));
			}

			bufferRouteGraphics.dispose();
		}
		panel.setBufferRoute(bufferRoute);

		panel.repaint();
	}

	/**
	 * Internal method to draw an {@link RoadEdge}
	 * 
	 * @param edge
	 *            The edge to draw.
	 * @param stroke
	 *            The stroke which should be around the line.
	 * @param backgroundGraphics
	 *            The background graphics object.
	 * @param foregroundGraphics
	 *            The foreground graphics object.
	 * @param color
	 *            The color of the road.
	 */
	protected void drawRoadEgde(RoadEdge edge, float stroke,
			Graphics2D backgroundGraphics, Graphics2D foregroundGraphics,
			Color color) {
		// If the edge is null, it will be rather hard to draw it, so we wont do
		// anything. This can happen in situations are the points in the route
		// search contain null for the first point.
		if (edge == null) {
			return;
		}
		int x1 = converter.xUTM2Pixel(edge.start.getX());
		int y1 = converter.yUTM2Pixel(edge.start.getY());
		int x2 = converter.xUTM2Pixel(edge.end.getX());
		int y2 = converter.yUTM2Pixel(edge.end.getY());

		if (backgroundGraphics != null) {
			// Make the "border" for the roads
			backgroundGraphics.setColor(edge.data.type.colorBG);
			backgroundGraphics.setStroke(new BasicStroke(stroke + 1,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			// Dashed: , 0.0f, new float[] { 10.0f, 10.0f }, 0.0f
			backgroundGraphics.drawLine(x1, y1, x2, y2);
		}

		if (color != null) {
			foregroundGraphics.setColor(color);
		} else {
			foregroundGraphics.setColor(edge.data.type.color);
		}

		// Draw the road itself
		foregroundGraphics.setStroke(new BasicStroke(stroke,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		foregroundGraphics.drawLine(x1, y1, x2, y2);
	}
}
