package model;

import datastructures.Trie;
import datastructures.graph.RoadEdge;

import java.util.Collections;
import java.util.List;
import java.util.Observable;

/**
 * Defines a name search model. Used to connect the actionlistner and gui with
 * the backend red. searchtree by using this structure we can use almost any
 * kind of source to get the results.
 */
public class NameSearchModel extends Observable {
	protected Trie<RoadEdge> searchTree;
	protected List<RoadEdge> roadNameSearchResult = Collections.emptyList();

	/**
	 * Constructucting the model with the source as parameter
	 * 
	 * @param searchTree
	 *            The {@link Trie} datastructure to base this search on.
	 */
	public NameSearchModel(Trie<RoadEdge> searchTree) {
		this.searchTree = searchTree;
	}

	/**
	 * Searches for a item matching og partiali matching the query
	 * 
	 * @param query
	 *            The query to search for.
	 * @throws IllegalArgumentException
	 *             If the query is empty or null.
	 */
	public void doRoadNameSearch(String query) {
		if (query == null || query.isEmpty()) {
			throw new IllegalArgumentException(
					"Empty string is not allowed as a query");
		}
		roadNameSearchResult = searchTree.query(query);
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the result of the pervious query
	 * 
	 * @return The results of the query.
	 */
	public List<RoadEdge> getRoadNameSearchResult() {
		return roadNameSearchResult;
	}

	/**
	 * Resets the searchmodel and clears the search queries
	 */
	public void resetRoadNameSearch() {
		roadNameSearchResult = Collections.emptyList();
		setChanged();
		notifyObservers();
	}
}
