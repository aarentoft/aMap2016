import org.junit.Test;
import util.UTMConverter;
import util.UTMCoordinateSet;

import static org.junit.Assert.*;

public class UTMConverterTest {

    private double comparisonDelta = 0.0;

    @Test
    public void latLonToUTM() throws Exception {
        comparisonDelta = 0.005;

        testSetHelper(-77.85, 166.66667, 539154.241938677, 1357813.64028423, 58);
        System.out.println();
        testSetHelper(51.0, 60.0, 289511.142963739, 5654109.17867386, 41);
        testSetHelper(37.0, 151, 322037.810222802, 4096742.05929492, 56);
        testSetHelper(24.0, 14.0, 398285.446177773, 2654587.59357066, 33);
        testSetHelper(-21.0, 17.0, 707889.216072834, 7676551.70980184, 33);
        testSetHelper(-36.0, 85.0, 319733.34187718, 6014201.79178776, 45);
        testSetHelper(-50.0, 117.0, 500000, 4461369.29718559, 50);
        testSetHelper(-68.0, 146.0,	458196.725691598, 2456797.45295553, 55);
        testSetHelper(-81.0, 171.0, 500000, 1006795.64840865, 59);
        testSetHelper(-83.0, -12.0, 459200.256323037, 782480.55843159, 29);
        testSetHelper(-61.0, -164.0, 554084.381011733, 3236799.83581419, 3);
        testSetHelper(-43.0, -92.0, 581508.647830616, 5238700.07482722, 15);
        testSetHelper(-21.0, -53.0, 292110.783927166, 7676551.70980184, 22);
        testSetHelper(15.0, -33.0, 500000, 1658325.993542, 25);
        testSetHelper(32.0, -122.0, 594457.463402463, 3540872.53134135, 10);
        testSetHelper(57.0, -82.0, 439253.376341955, 6317830.52992883, 17);
        testSetHelper(74.0, -177.0, 500000, 8212038.09315851, 1);

        // The following test data has been rounded (for unknown reasons), so a much larger comparison delta is needed.
        comparisonDelta = 1.0;
        // Uncertain whether the expected easting for this test case is correct
//        testSetHelper(83.627, -32.664, 504211, 9286900, 25);            // 25XEN0421186800 ; Cape Morris Jessup
        testSetHelper(-55.98, -67.28917, 606750, 3794825, 19);          // 19FFT0675094825 ; Cape Horn
        testSetHelper(-61.453333, -55.495752, 580191, 3185792, 21);     // 21EWM8019185791 ; Gibbs Island
        testSetHelper(37.81972, -122.47861, 545889, 4185941, 10);       // 10SEG4588885941 ; Golden Gate Bridge
        testSetHelper(40.713, -74.0135, 583326, 4507366, 18);           // 18TWL8332507366 ; One World Trade Center
        testSetHelper(3.15785, 101.71165, 801396, 349434, 47);          // 47NRD0139649433 ; Petronas Towers
        testSetHelper(27.98806, 86.92528, 492652, 3095881, 45);         // 45RVL9265295881 ; Mount Everest
        testSetHelper(-3.07583, 37.35333, 317004, 9659884, 37);         // 37MCS1700459883 ; Kilimanjaro
        testSetHelper(25.19714, 55.27411, 326103, 2787892, 40);         // 40RCN2610387892 ; Burj Khalifa
        testSetHelper(41.00855, 28.97994, 666499, 4541594, 35);         // 35TPF6649841594 ; Hagia Sofia
        testSetHelper(41.90222, 12.45333, 288761, 4642057, 33);         // 33TTG8876042056 ; St. Peters
        testSetHelper(48.85822, 2.2945, 448252, 5411935, 31);           // 31UDQ4825111935 ; Eiffel Tower
        testSetHelper(71.1725, 25.78444, 456220, 7897075, 35);          // 35WMU5621997075 ; North Cape, Norway
        testSetHelper(-34.35806, 18.47194, 267496, 6195246, 34);        // 34HBG6749695246 ; Cape of Good Hope
        testSetHelper(-13.16333, -72.54556, 766062, 8543503, 18);       // 18LYL6606143503 ; Machu Picchu
        testSetHelper(-32.65343, -70.01108, 405179, 6386681, 19);       // 19HDD0517986681 ; Aconcagua
        testSetHelper(-43.59575, 170.14104, 430668, 5172667, 59);       // 59GMM3066772666 ; Mount Cook, NZ
        testSetHelper(-33.85867, 151.21403, 334786, 6252080, 56);       // 56HLH3478652079 ; Sydney Opera House
        testSetHelper(25.03361, 121.565, 355224, 2769437, 51);          // 51RUH5522469436 ; Taipei 101
        testSetHelper(35.35806, 138.73111, 293848, 3915114, 54);        // 54STE9384815114 ; Mount Fuji
        testSetHelper(56.05611, 160.64417, 602389, 6213543, 57);        // 57VXC0238913543 ; Klyuchevskaya
        testSetHelper(71.38889, -156.47917, 589769, 7922642, 4);        // 04WEE8976822642 ; Point Barrow
        testSetHelper(21.365, -157.95, 608862, 2362907, 4);             // 04QFJ0886262907 ; Arizona Memorial
        testSetHelper(-27.11667, -109.36667, 661897, 6999591, 12);      // 12JXQ6189699590 ; Easter Island
        testSetHelper(-37.11111, -12.28833, 740947, 5889360, 28);       // 28HGD4094689359 ; Tristan da Cunha
        testSetHelper(-77.85, 166.66667, 539154, 1357814, 58);          // 58CEU3915457813 ; McMurdo Station
    }

    private void testSetHelper(double lat, double lon, double expectedEasting, double expectedNorthing, int expectedZone) {
        UTMConverter converter = new UTMConverter();
        UTMCoordinateSet actual = converter.LatLonToUTM(lat, lon);

        assertEquals(expectedEasting, actual.getEasting(), comparisonDelta);
        assertEquals(expectedNorthing, actual.getNorthing(), comparisonDelta);

        assertEquals(actual.getZone(), expectedZone);
    }
}