package util;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.openstreetmap.osmosis.core.task.v0_6.*;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;
import util.graph.*;
import view.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OsmosisLoader implements OSMLoader {
    protected Loader loader;
    protected QuadTree quadTree;
    protected Trie<RoadEdge> searchTree = new Trie<RoadEdge>();
    protected Graph graph;

    private Map<String, RoadNode> nodes;

    public OsmosisLoader(String path) throws FileNotFoundException {
        File file = new File(path); // the input file

        if (!file.getName().endsWith(".pbf")) {
            // TODO Proper exception type
            throw new FileNotFoundException("Wrong file type");
        }

        nodes = new TreeMap<>();
        searchTree = new Trie<RoadEdge>();
        UTMConverter utmConverter = new UTMConverter();

        Sink sinkImplementation = new Sink() {

            private boolean isFirstWay = false;

            @Override
            public void initialize(Map<String, Object> metaData) { }

            public void process(EntityContainer entityContainer) {
                Entity entity = entityContainer.getEntity();
                if (entity instanceof Bound) {
                    Bound bd = (Bound) entity;
                    // bottom = minlat, left = minlon
                    // top = maxlat, right = maxlon
                    UTMCoordinateSet utmCoords1 = utmConverter.LatLonToUTM(bd.getBottom(), bd.getLeft());
                    UTMCoordinateSet utmCoords2 = utmConverter.LatLonToUTM(bd.getTop(), bd.getRight());
                    Rectangle bound = new Rectangle(
                            utmCoords1.getEasting(), utmCoords1.getNorthing(),
                            utmCoords2.getEasting(), utmCoords2.getNorthing());
                    quadTree = new QuadTree(bound);
                } else if (entity instanceof Node) {
                    Node nd = (Node) entity;
                    String id = nd.getId()+"";
                    UTMCoordinateSet utmCoords = utmConverter.LatLonToUTM(nd.getLatitude(), nd.getLongitude());
                    nodes.put(id, new RoadNode(id, utmCoords.getEasting(), utmCoords.getNorthing()));
                } else if (entity instanceof Way) {
                    if (!isFirstWay) {
                        graph = new Graph(nodes);
                    }
                    Way way = (Way) entity;
                    Collection<Tag> tags = way.getTags();

                    String roadname = "";
                    RoadType type = RoadType.UNKNOWN;
                    boolean oneway = false;
                    for (Tag t: tags) {
                        oneway = false;
                        switch (t.getKey()) {
                            case "highway": type = RoadType.getEnum(t.getValue()); break;
                            case "name": roadname = t.getValue(); break;
                            case "oneway": oneway = Boolean.parseBoolean(t.getValue()); break;
                        }
                    }
                    WayData data = new WayData(roadname, type, oneway);

                    List<WayNode> waynodes = way.getWayNodes();
                    for (int i = 0; i < waynodes.size() - 1; i++) {
                        RoadNode n1 = graph.getNodes().get(waynodes.get(i).getNodeId()+"");
                        RoadNode n2 = graph.getNodes().get(waynodes.get(i + 1).getNodeId()+"");
                        RoadEdge edge = new RoadEdge(data, n1, n2);

                        if (!roadname.isEmpty())
                            searchTree.insert(edge);

                        quadTree.insert(edge);
                        graph.addEdge(edge);
                    }
                }
            }
            public void release() { }
            public void complete() { }
        };

        RunnableSource reader = new crosby.binary.osmosis.OsmosisReader(new FileInputStream(file));

        reader.setSink(sinkImplementation);

        Thread readerThread = new Thread(reader);
        readerThread.start();

        while (readerThread.isAlive()) {
            try {
                readerThread.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public QuadTree getQuadTree() {
        return quadTree;
    }

    @Override
    public Trie<RoadEdge> getSearchTree() {
        return searchTree;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }
}
