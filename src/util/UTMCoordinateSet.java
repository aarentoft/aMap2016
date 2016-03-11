package util;

/**
 * Contains all information related to a UTM coordinate set: easting, northing, and UTM zone.
 */
public class UTMCoordinateSet {
    private double easting;
    private double northing;
    private int zone;

    public UTMCoordinateSet(double easting, double northing, int zone) {
        this.easting = easting;
        this.northing = northing;
        this.zone = zone;
    }

    public double getEasting() {
        return easting;
    }

    public double getNorthing() {
        return northing;
    }

    public int getZone() {
        return zone;
    }
}
