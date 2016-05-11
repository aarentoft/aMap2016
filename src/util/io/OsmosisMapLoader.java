package util.io;

import datastructures.QuadTree;
import datastructures.Trie;
import datastructures.graph.*;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import util.Rectangle;
import util.UTMConverter;
import util.UTMCoordinateSet;
import view.Loader;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class contains code for loading data from .osm and .pbf files into the
 * project. It will both convert data into nodes and connect the nodes with
 * edges for use in a {@link Graph}.
 */
public class OsmosisMapLoader {
    protected Loader   loader;
    protected QuadTree quadTree;
    protected Trie<RoadEdge> searchTree = new Trie<RoadEdge>();
    protected Graph graph;

    private Map<Long, RoadNode> nodes;
    private final Object lock = new Object();

    /**
     * Loads the OSM data of a file and creates the data structures used by this application.
     * Passing null to the {@code path} parameter will run the loader in headless mode with no GUI.
     *
     * @param path path to the map data file
     * @param loader reference to a graphical loader to attach to this loading process
     * @throws FileNotFoundException when {@code path} does not refer to a file
     */
    public OsmosisMapLoader(String path, Loader loader) throws FileNotFoundException {
        File file = new File(path);

        if (!file.getName().endsWith(".pbf") && !file.getName().endsWith(".osm")) {
            throw new IllegalArgumentException("Invalid file type. Must be either .osm or .pbf.");
        }

        this.loader = loader;
        nodes = new TreeMap<>();
        searchTree = new Trie<RoadEdge>();

        DataModelBuilderSink sinkImplementation = new DataModelBuilderSink();

        RunnableSource reader;
        // TODO rename monitor to avoid confusing it with the term "monitor lock" from threading
        OsmosisReaderProgressInputStream monitor = new OsmosisReaderProgressInputStream(new FileInputStream(file), loader, (int)file.length());

        if (file.getName().endsWith(".pbf")) {
            reader = new crosby.binary.osmosis.OsmosisReader(monitor);
        } else {
            reader = new OsmosisXmlProgressReader(file, false, CompressionMethod.None, monitor);
        }

        reader.setSink(sinkImplementation);

        Thread readerThread = new Thread(reader);
        readerThread.start();

        Thread freeMemCheckerThread = null;
        if (loader != null) {
            freeMemCheckerThread = new Thread(new FreeMemoryCheckerThread(readerThread));
            freeMemCheckerThread.start();
        }

        while (readerThread.isAlive()) {
            try {
                readerThread.join();
                if (freeMemCheckerThread != null)
                    freeMemCheckerThread.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Convenience method to only set UI loader label if a UI loader object is present.
     * @param label New label value
     */
    private void setStatus(String label) {
        if (loader != null) {
            loader.setStatus(label);
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

    /**
     * This thread runs for the lifespan of another thread and checks the amount of free memory.
     * If the amount gets lower than a certain threshold, the user is asked whether he/she wishes to continue
     * executing the other thread. If not, the entire application is closed.
     */
    private class FreeMemoryCheckerThread implements Runnable {
        private final Thread thread;
        // Millisecond(s)
        private int checkInterval = 1000;
        // Minimum amount of free memory needed
        private int memoryThreshold = 30 * (1024 * 1024);

        public FreeMemoryCheckerThread(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            int ct = 0;

            while (thread.isAlive()) {
                System.out.println("running "+ (ct++));

                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) { e.printStackTrace(); }

                if (Runtime.getRuntime().freeMemory() > memoryThreshold)
                    continue;

                int userResponse;
                synchronized (thread) {
                    userResponse = JOptionPane.showConfirmDialog(null,
                            "The JVM is running out of memory. Are you sure, that you want to continue loading this map?",
                            "Low Memory",
                            JOptionPane.YES_NO_OPTION);

                    // If the user chooses to continue loading, this thread (the one checking the amount of
                    // available memory) stops. As the user has indicated that loading should continue despite low
                    // memory, there is no need to keep checking.
                    if (userResponse == JOptionPane.YES_OPTION) {
                        break;
                    } else {
                        System.exit(0);
                    }
                }
            }
        }
    }


    private class DataModelBuilderSink implements Sink {

        private boolean isFirstWay = true;
        private int commonUTMzone;
        private UTMConverter utmConverter = new UTMConverter();

        @Override
        public void initialize(Map<String, Object> metaData) {
            setStatus("Loading nodes...");
        }

        public synchronized void process(EntityContainer entityContainer) {
            synchronized (Thread.currentThread()) {
                // Simply blocks the sink thread from executing due to the free memory checking thread
                // also using a synchronized block when asking the user what to do about the low memory.
                // It is blocked here, because the sync block in the free memory checking thread contains
                // a blocking call (JOptionPane.showDialog(...)).
            }

            Entity entity = entityContainer.getEntity();
            if (entity instanceof Bound) {
                Bound bd = (Bound) entity;
                // bottom = minlat, left = minlon
                // top = maxlat, right = maxlon
                UTMCoordinateSet utmCoords1 = utmConverter.LatLonToUTM(bd.getBottom(), bd.getLeft());
                UTMCoordinateSet utmCoords2 = utmConverter.LatLonToUTM(bd.getTop(), bd.getRight());
                commonUTMzone = (utmCoords1.getZone() + utmCoords2.getZone()) / 2;

                utmCoords1 = utmConverter.LatLonToUTM(bd.getBottom(), bd.getLeft(), commonUTMzone);
                utmCoords2 = utmConverter.LatLonToUTM(bd.getTop(), bd.getRight(), commonUTMzone);

                Rectangle bound = new Rectangle(
                        utmCoords1.getEasting(), utmCoords1.getNorthing(),
                        utmCoords2.getEasting(), utmCoords2.getNorthing());
                quadTree = new QuadTree(bound);
            } else if (entity instanceof Node) {
                Node nd = (Node) entity;
                Long id = nd.getId();
                UTMCoordinateSet utmCoords = utmConverter.LatLonToUTM(nd.getLatitude(), nd.getLongitude(), commonUTMzone);
                nodes.put(id, new RoadNode(id, utmCoords.getEasting(), utmCoords.getNorthing()));
            } else if (entity instanceof Way) {
                if (isFirstWay) {
                    graph = new Graph(nodes);
                    setStatus("Loading edges...");
                    isFirstWay = false;
                }

                Way way = (Way) entity;
                Collection<Tag> tags = way.getTags();

                if (!containsTagWithValue(tags, "highway", null) && !containsTagWithValue(tags, "route", "ferry"))
                    return;

                String roadname = "";
                RoadType type = RoadType.UNKNOWN;
                boolean oneway = false;
                for (Tag t : tags) {
                    oneway = false;
                    switch (t.getKey()) {
                        case "route":
                            if (t.getValue().equals("ferry"))
                                type = RoadType.FERRY;
                            break;
                        case "highway": type = RoadType.getEnum(t.getValue()); break;
                        case "name":    roadname = t.getValue(); break;
                        case "oneway":  oneway = Boolean.parseBoolean(t.getValue()); break;
                    }
                }
                WayData data = new WayData(roadname, type, oneway);

                List<WayNode> waynodes = way.getWayNodes();
                for (int i = 0; i < waynodes.size() - 1; i++) {
                    RoadNode n1 = graph.getNodes().get(waynodes.get(i).getNodeId());
                    RoadNode n2 = graph.getNodes().get(waynodes.get(i + 1).getNodeId());
                    try {
                        RoadEdge edge = new RoadEdge(data, n1, n2);

                        if (!roadname.isEmpty())
                            searchTree.insert(edge);

                        quadTree.insert(edge);
                        graph.addEdge(edge);
                    } catch (IllegalArgumentException e) {
                        // Some road edges may be invalid, eg. by having identical end nodes.
                        // These are are just ignored
                    }
                }
            }
        }

        public void release() {}

        public void complete() {
            setStatus("Done");
        }

        private boolean containsTagWithValue(Collection<Tag> tags, String key, String value) {
            for (Tag t : tags) {
                if (t.getKey().equals(key))
                    if (value == null || t.getValue().equals(value))
                        return true;
            }

            return false;
        }
    }

}
