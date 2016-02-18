package util.graph;

public class WayData {
    public final RoadType type;
    public final String roadname;
    public final byte direction;

    public WayData(String roadname, RoadType type, byte direction) {
        this.type = type;
        this.roadname = roadname;
        // TODO: Find out how to get direction
        this.direction = direction;
    }
}
