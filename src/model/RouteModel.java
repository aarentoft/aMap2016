package model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import util.Dijkstra;
import util.graph.Graph;
import util.graph.RoadEdge;
import exceptions.PathNotFoundException;

/**
 * This model contains information about the route in the map view.
 */
public class RouteModel extends Observable {
	protected Graph graph;
	protected boolean doRouteSearch = false;
	protected Map<Integer, RoadEdge> points = new HashMap<Integer, RoadEdge>();
	protected Collection<RoadEdge> route = Collections.emptyList();

	/**
	 * @param graph
	 *            The graph to use in the route-searching
	 */
	public RouteModel(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Set a point for the route
	 * 
	 * @param type
	 *            the point index. 0 is the starting point, 1 is the ending
	 *            point. 2+ is the stops along the route.
	 * @param roadEdge
	 *            the {@link RoadEdge} to stop at.
	 */
	public void setPoint(int type, RoadEdge roadEdge) {
		if (roadEdge == null) {
			points.remove(type);
		}
		if (points.get(type) != roadEdge) {
			points.put(type, roadEdge);
		}
	}

	/**
	 * Adds a point to the route.
	 * 
	 * @param roadEdge
	 *            The point to add.
	 */
	public void addPoint(RoadEdge roadEdge) {
		if (roadEdge == null)
			return;
		// Currently only works for two points in the route.
		if (points.size() == 2) {
			clear();
		}
		if (!points.containsKey(0)) {
			points.put(0, roadEdge);
			setChanged();
			notifyObservers();
		} else {
			points.put(1, roadEdge);
		}
		if (points.size() == 2) {
			setDoRouteSearch(true);
		}
	}

	/**
	 * Sets the doRouteSearch value, which is checked whenever doRouteSearch()
	 * is called.
	 * 
	 * @param doRouteSearch
	 *            Whether or not to execute the doRouteSearch() method next time
	 *            it is called.
	 */
	public void setDoRouteSearch(boolean doRouteSearch) {
		this.doRouteSearch = doRouteSearch;
		setChanged();
		notifyObservers();
	}

	/**
	 * @throws PathNotFoundException
	 *             If the path cannot be found
	 * @throws IllegalArgumentException
	 *             If one or both of the points are missing.
	 */
	public void doRouteSearch() throws PathNotFoundException,
			IllegalArgumentException {
		if (doRouteSearch) {
			this.doRouteSearch = false;
			if (points.get(0) == null || points.get(1) == null) {
				route = Collections.emptyList();
				throw new IllegalArgumentException(
						"One or both of the requested addresses has not been set");
			} else {
					route = Dijkstra.AStarSearch(graph, points.get(0).start,
							points.get(1).end);

				if (route.size() < 2) {
					clear();
					throw new PathNotFoundException("Zero or 1 edges returned");
				}
			}
		}
	}

	/**
	 * @return The route which has been found.
	 */
	public Collection<RoadEdge> getRoute() {
		return route;
	}

	/**
	 * @return The current points to stop with in the route.
	 */
	public Map<Integer, RoadEdge> getPoints() {
		return points;
	}

	public void clear() {
		this.doRouteSearch = false;
		route = Collections.emptyList();
		points.clear();
		setChanged();
		notifyObservers();
	}
}