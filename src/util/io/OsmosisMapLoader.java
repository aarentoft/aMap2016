package util.io;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import util.*;
import util.graph.*;
import view.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OsmosisMapLoader {
    protected QuadTree quadTree;
    protected Trie<RoadEdge> searchTree = new Trie<RoadEdge>();
    protected Graph graph;

    private Map<String, RoadNode> nodes;

    public OsmosisMapLoader(String path, Loader loader) throws FileNotFoundException {
        File file = new File(path);

        if (!file.getName().endsWith(".pbf") && !file.getName().endsWith(".osm")) {
            throw new IllegalArgumentException("Invalid file type. Must be either .osm or .pbf.");
        }

        nodes = new TreeMap<>();
        searchTree = new Trie<RoadEdge>();

        Sink sinkImplementation = new Sink() {

            private boolean isFirstWay = true;
            private UTMConverter utmConverter = new UTMConverter();

            @Override
            public void initialize(Map<String, Object> metaData) { loader.setStatus("Loading nodes..."); }

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
                    if (isFirstWay) {
                        graph = new Graph(nodes);
                        loader.setStatus("Loading edges...");
                        isFirstWay = false;
                    }
                    Way way = (Way) entity;
                    Collection<Tag> tags = way.getTags();

                    if (!containsTagWithValue(tags, "highway", null) && !containsTagWithValue(tags, "route", "ferry"))
                        return;

                    String roadname = "";
                    RoadType type = RoadType.UNKNOWN;
                    boolean oneway = false;
                    for (Tag t: tags) {
                        oneway = false;
                        switch (t.getKey()) {
                            case "route":
                                if (t.getValue().equals("ferry"))
                                    type = RoadType.FERRY;
                                break;
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
            public void complete() { loader.setStatus("Done"); }

            private boolean containsTagWithValue(Collection<Tag> tags, String key, String value) {
                for (Tag t : tags) {
                    if (t.getKey().equals(key))
                        if (value == null || t.getValue().equals(value))
                            return true;
                }

                return false;
            }
        };

        RunnableSource reader;
        OsmosisReaderProgressInputStream monitor = new OsmosisReaderProgressInputStream(new FileInputStream(file), loader, (int)file.length());

        if (file.getName().endsWith(".pbf")) {
            reader = new crosby.binary.osmosis.OsmosisReader(monitor);
        } else {
            reader = new OsmosisXmlProgressReader(file, false, CompressionMethod.None, monitor);
        }

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

    public QuadTree getQuadTree() {
        return quadTree;
    }

    public Trie<RoadEdge> getSearchTree() {
        return searchTree;
    }

    public Graph getGraph() {
        return graph;
    }
}
