package util.graph;

public class WayData {
    public final RoadType type;
    public final String roadname;
    public final boolean oneway;

    public WayData(String roadname, RoadType type, boolean oneway) {
        this.type = type;
        this.roadname = roadname;
        this.oneway = oneway;
    }
}
