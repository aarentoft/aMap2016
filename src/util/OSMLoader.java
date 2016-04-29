package util;

import util.graph.Graph;
import util.graph.RoadEdge;

public interface OSMLoader {
    public QuadTree getQuadTree();

    public Trie<RoadEdge> getSearchTree();

    public Graph getGraph();
}
