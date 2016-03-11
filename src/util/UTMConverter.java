package util;

/**
 * Contains logic which allows converting latitude and longitude to UTM coordinates.
 * This is done using the WGS84 ellipsoid as a model of the earth. It uses hardcoded scale factor
 * and "false easting" values which should be sufficient for use in this application.
 */
public class UTMConverter {
    // NOTE: This class has been written using the information available here http://www.uwgb.edu/dutchs/usefuldata/utmformulas.htm)
    // and the spreadsheet implementation available on the same page (direct link: http://www.uwgb.edu/dutchs/usefuldata/UTMConversion2015m.xls).
    // Links last checked 2016/03/11.

    // WGS 84 constants (defines an ellipsoid) (source: http://www.uwgb.edu/dutchs/usefuldata/utmformulas.htm)
    // Equatorial radius in meters
    private final double a = 6378137.0;
    // Polar radius in meters
    private final double b = 6356752.3142;
    // Flattening
    private final double f = (a - b) / a;

    // pre-computed, preliminary values
    private final double n = f / (2.0 - f);

    // scale factor
    private final double k0 = 0.9996;

    // eccentricity =SQRT(1-(b/a)^2)
    private final double e = Math.sqrt(1.0 - Math.pow(b/a, 2.0));

    // Copied from spreadsheet: "False easting is a value added to the easting coordinate.
    // For standard UTM it is 500,000. That ensures that all easting values are positive six-digit numbers.
    // Other values may be input for special applications.
    // Do not change this value unless specifically required for your purposes."
    private final double falseEasting = 500000.0;

    // Meridian Radius (sometimes called 'AA' or just 'A'). "[D]efines the scale of the ellipse. 2*pi*A= circumference of meridian."
    // = (a/(1+n)) * (1 + (1/4)*n^2 + (1/64)*n^4 + (1/256)*n^6 + (25/16384)*n^8 + (49/65536)*n^10)
    private final double meridianRadius = (a/(1.0+n)) *
            (1.0 +
                    (1.0/4.0)   * Math.pow(n, 2.0) +
                    (1.0/64.0)  * Math.pow(n, 4.0) +
                    (1.0/256.0) * Math.pow(n, 6.0) +
                    (25.0/16384.0) * Math.pow(n, 8.0) +
                    (49.0/65536.0) * Math.pow(n, 10.0)
            );

    // Kruger series numbers
    // =(1/2)*n - (2/3)*n^2 + (5/16)*n^3 + (41/180)*n^4 - (127/288)*n^5 + (7891/37800)*n^6 + (72161/387072)*n^7 - (18975107/50803200)*n^8 + (60193001/290304000)*n^9 + (134592031/1026432000)*n^10
    private final double alpha1 =
            (1.0/2.0)*n - (2.0/3.0)*Math.pow(n, 2.0) + (5.0/16.0)*Math.pow(n, 3.0) + (41.0/180.0)*Math.pow(n, 4.0) -
            (127.0/288.0)*Math.pow(n, 5.0) + (7891.0/37800.0)*Math.pow(n, 6.0) + (72161.0/387072)*Math.pow(n, 7.0) -
            (18975107.0/50803200.0)*Math.pow(n, 8.0) + (60193001.9/290304000.0)*Math.pow(n, 9.0) + (134592031.0/1026432000.0)*Math.pow(n, 10.0);

    // =(13/48)*n^2 - (3/5)*n^3 + (557/1440)*n^4 + (281/630)*n^5 - (1983433/1935360)*n^6 + (13769/28800)*n^7 + (148003883/174182400)*n^8 - (705286231/465696000)*n^9 + (1703267974087/3218890752000)*n^10
    private final double alpha2 =
            (13.0/48.0)*Math.pow(n, 2.0) - (3.0/5.0)*Math.pow(n, 3.0) + (557.0/1440.0)*Math.pow(n, 4.0) + (281.0/630.0)*Math.pow(n, 5.0) -
            (1983433.0/1935360.0)*Math.pow(n, 6.0) +
            (13769.0/28800.0)*Math.pow(n, 7.0) +
            (148003883.0/174182400.0)*Math.pow(n, 8.0) +
            (705286231.0/465696000.0)*Math.pow(n, 9.0) +
            (1703267974087.0/3218890752000.0)*Math.pow(n, 10.0);

    // =(61/240)*n^3-(103/140)*n^4+(15061/26880)*n^5+(167603/181440)*n^6-(67102379/29030400)*n^7+(79682431/79833600)*n^8+(6304945039/2128896000)*n^9-(6601904925257/1307674368000)*n^10
    private final double alpha3 =
            (61.0/240.0)*Math.pow(n, 3.0) - (103.0/140.0)*Math.pow(n, 4.0) + (15061.0/26880.0)*Math.pow(n, 5.0) +
            (167603.0/181440.0)*Math.pow(n, 6.0) - (67102379.0/29030400.0)*Math.pow(n, 7.0) +
            (79682431.0/79833600.0)*Math.pow(n, 8.0) + (6304945039.0/2128896000.0)*Math.pow(n, 9.0) - (6601904925257.0/1307674368000.0)*Math.pow(n, 10.0);

    // =(49561/161280)*n^4-(179/168)*n^5+(6601661/7257600)*n^6+(97445/49896)*n^7-(40176129013/7664025600)*n^8+(138471097/66528000)*n^9+(48087451385201/5230697472000)*n^10
    private final double alpha4 =
            (49561.0/161280.0)*Math.pow(n, 4.0) - (179.0/168.0)*Math.pow(n, 5.0) + (6601661.0/7257600.0)*Math.pow(n, 6.0) +
            (97445.0/49896.0)*Math.pow(n, 7.0) - (40176129013.0/7664025600.0)*Math.pow(n, 8.0) +
            (138471097.0/66528000.0)*Math.pow(n, 9.0) + (48087451385201.0/5230697472000.0)*Math.pow(n, 10.0);

    private double atanh(double x)  {
        double y = Math.abs(x);     // Enforce odd parity
        y = Math.log1p(2 * y/(1 - y))/2;
        return x < 0 ? -y : y;
    }

    private double asinh(double x)
    {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    private double acosh(double x)
    {
        return Math.log(x + Math.sqrt(x*x - 1.0));
    }

    /**
     * Converts a set of coordinates consisting of a latitude and a longitude to
     * a set of UTM coordinates including the UTM zone.
     * @param lat The latitude coordinate
     * @param lon The longitude coordinate
     * @return The corresponding UTM coordinate set and zone.
     */
    public UTMCoordinateSet LatLonToUTM(double lat, double lon) {
        // 180 = half earth circumference in degrees, 6 = standard width of a UTM zone in degrees
        int zone = lon < 0 ? (int) ((180.0+lon)/6.0) + 1 : (int) Math.abs((lon/6.0)+31.0);

        double centralMeridian = 6.0 * zone - 183.0;

        double absLat = Math.abs(lat);
        double Latr = absLat * Math.PI / 180.0;          // =absLat*PI()/180
        double sigma = Math.sinh( e*atanh(e*Math.tan(Latr)/Math.sqrt(1+Math.pow(Math.tan(Latr), 2.0))) );     // =SINH( e*ATANH(e*TAN(Latr)/SQRT(1+TAN(Latr)^2)) )
        double ConfLat = Math.atan( Math.sinh( asinh(Math.tan(Latr))-e*atanh(e*Math.sin(Latr)) ) );      // =ATAN( SINH( ASINH(TAN(Latr))-e*ATANH(e*SIN(LAtr)) ) )
        double Dlon = Math.abs(lon - centralMeridian);
        double Dlonr = Dlon * Math.PI / 180.0;               // =Dlon*PI()/180  Dlon =ABS(trueLon - Meridian) : Meridian is always 165 for standard maps?
        double tauPrime = Math.tan(ConfLat);
        double xiPrime = Math.atan(tauPrime/Math.cos(Dlonr));       // =ATAN(tauPrime/COS(Dlonr))
        double etaPrime = asinh(
                Math.sin(Dlonr) /
                Math.sqrt(tauPrime * tauPrime + (Math.pow(Math.cos(Dlonr), 2.0)) )
        );      // =ASINH(SIN(Dlonr)/SQRT(tauPrime*Prime+(COS(Dlonr)^2)))
        double xi = xiPrime + alpha1 * Math.sin(2.0*xiPrime) * Math.cosh(2.0*etaPrime) +
                alpha2 * Math.sin(4.0*xiPrime) * Math.cosh(4.0*etaPrime) +
                alpha3 * Math.sin(6.0*xiPrime) * Math.cosh(6.0*etaPrime) +
                alpha4 * Math.sin(8.0*xiPrime) * Math.cosh(8.0*etaPrime);
        // xi =xiPrime + alpha1*SIN(2*xiPrime)*COSH(2*etaPrime) +
        // alpha2*SIN(4*xiPrime)*COSH(4*etaPrime) +
        // alpha3*SIN(6*xiPrime)*COSH(6*etaPrime) +
        // alpha4*SIN(8*xiPrime)*COSH(8*etaPrime) +
        // alpha5*SIN(10*xiPrime)*COSH(10*etaPrime) +
        // alpha6*SIN(12*xiPrime)*COSH(12*etaPrime) +
        // alpha7*SIN(14*xiPrime)*COSH(14*etaPrime)

        double eta = etaPrime + alpha1 * Math.cos(2.0*xiPrime) * Math.sinh(2.0*etaPrime) +
                alpha2 * Math.cos(4.0*xiPrime) * Math.sinh(4.0*etaPrime) +
                alpha3 * Math.cos(6.0*xiPrime) * Math.sinh(6.0*etaPrime) +
                alpha4 * Math.cos(8.0*xiPrime) * Math.sinh(8.0*etaPrime);
        // eta = etaPrime + alpha1*COS(2*xiPrime)*SINH(2*etaPrime) +
        // alpha2*COS(4*xiPrime)*SINH(4*etaPrime) +
        // alpha3*COS(6*xiPrime)*SINH(6*etaPrime) +
        // alpha4*COS(8*xiPrime)*SINH(8*etaPrime) +
        // alpha5*COS(10*xiPrime)*SINH(10*etaPrime) +
        // alpha6*COS(12*xiPrime)*SINH(12*etaPrime) +
        // alpha7*COS(14*xiPrime)*SINH(14*etaPrime)


        double easting = k0 * meridianRadius * eta;
        double northing = k0 * meridianRadius * xi;

        // if the resulting easting is west of the central meridian, negate it.
        // NOTE: Important that this is done before adding falseEasting
        easting = lon < centralMeridian ? -easting : easting;
        // Add False Easting to actual easting
        easting += falseEasting;
        // If Southern Lattitude, Subtract northing from 10,000,000
        northing = lat < 0 ? 10000000-northing : northing;

        return new UTMCoordinateSet(easting, northing, zone);
    }
}
