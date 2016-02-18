package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Search tree for finding roads by name. This Trie is a datastructure that
 * makes it super fast to search in strings of any length.
 * 
 * @param <T>
 */
public class Trie<T> {
	protected T element;
	protected int level;
	protected Map<Character, Trie<T>> children;

	/**
	 * Constructor for Trie with level default (zero)
	 */
	public Trie() {
		level = 0;
		children = new TreeMap<Character, Trie<T>>();
	}

	/**
	 * Used for adding a string to the Trie.
	 * 
	 * @param e
	 */
	public void insert(T e) {
		insert(e, e.toString().toLowerCase());
	}

	/**
	 * Used for adding an object to the Trie.
	 * 
	 * @param e
	 * @param str
	 */
	protected void insert(T e, String str) {
		// The object is put on the level corresponding to the length of the
		// String
		if (str.length() > 0) {
			char key = str.charAt(0);
			if (this.children.containsKey(key)) {
				this.children.get(key).insert(e, str.substring(1));
			} else {
				Trie<T> children = new Trie<T>();
				this.children.put(key, children);
				children.insert(e, str.substring(1));
			}
		} else {
			element = e;
		}
	}

	/**
	 * Returns all contents recursively. red. children inclusive
	 * 
	 * @return All content of the <code>Trie</code>
	 */
	public List<T> getAllContents() {
		List<T> ret = new ArrayList<T>();
		if (this.element != null) {
			ret.add(element);
		}
		for (char key : children.keySet())
			ret.addAll(children.get(key).getAllContents());
		return ret;
	}

	/**
	 * Returns the result of a search query (no limits)
	 * 
	 * @param query
	 *            Search query
	 * @return a <code>List</code> of results starting with the search query
	 */
	public List<T> query(String query) {
		if (query == null) {
			return Collections.emptyList();
		}
		List<T> edges = new ArrayList<T>();
		query = query.toLowerCase();
		if (query.length() > 0) {
			char key = query.charAt(0);
			if (children.containsKey(key)) {
				edges.addAll(children.get(key).query(query.substring(1)));
			}
		} else {
			edges.addAll(getAllContents());
		}
		return edges;
	}
}