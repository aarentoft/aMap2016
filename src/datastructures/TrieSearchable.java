package datastructures;

/**
 * Classes which is going to be a part of the {@link Trie} structure must implement this interface.
 */
public interface TrieSearchable {
    /**
     * Returns a string representation used by the {@link Trie} structure to put the implementing object
     * in the correct order in the trie.
     *
     * @return the trie ordering string
     */
    public String getTrieRepresentation();
}
