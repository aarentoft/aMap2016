package util.graph;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import util.UTMConverter;
import util.UTMCoordinateSet;

public class GraphDataLoadTest {
	@Test
	public void roadNodeNormal() throws IOException {
		RoadNode n = new RoadNode("1", 720050.2418, 6173986.37339);
		assertEquals("1", n.ID);

		// Allow a 0.0000001 difference between the saved and the actual values,
		// since floating points are not very precise in Java
		assertEquals(n.getX(), 720050.2418, 0.0000001);
		assertEquals(n.getY(), 6173986.37339, 0.0000001);
	}

	@Test
	public void roadNodeNegative() throws IOException {
		RoadNode n = new RoadNode("-14", -720050.2418, -6173986.37339);
		assertEquals("-14", n.ID);

		// Allow a 0.0000001 difference between the saved and the actual values,
		// since floating points are not very precise in Java
		assertEquals(-720050.2418, n.getX(), 0.0000001);
		assertEquals(-6173986.37339, n.getY(), 0.0000001);
	}

	@Test
	public void roadNodeMaximum() throws IOException {
		RoadNode n = new RoadNode("1", Double.MAX_VALUE, Double.MAX_VALUE);

		assertEquals(Double.MAX_VALUE, n.getX(), 0.0000001);
		assertEquals(Double.MAX_VALUE, n.getY(), 0.0000001);
	}

	@Test
	public void EdgeNormal() throws IOException {
		UTMConverter conv = new UTMConverter();
		UTMCoordinateSet set1 = conv.LatLonToUTM(55.17686, 14.7067943);		// Just south of Hasle, Bornholm
		UTMCoordinateSet set2 = conv.LatLonToUTM(55.1179148, 14.7046616);	// Just north of Rønne, Bornholm
		RoadNode n1 = new RoadNode("1", set1.getEasting(), set1.getNorthing());
		RoadNode n2 = new RoadNode("2", set2.getEasting(), set2.getNorthing());

		String roadname = "Søndervangs Allé";
		WayData wayData = new WayData(roadname, RoadType.MOTORWAY, true);

		RoadEdge e = new RoadEdge(wayData, n1, n2);

		// Actual result according to http://williams.best.vwh.net/gccalc.htm
		// NOTE: This is a very long edge compared to what appears in actual data, but has an error margin of less
		// than 3 meters.
		assertEquals(6563.556315297661, e.length, 3.0);
		assertEquals(e.data, wayData);
		assertEquals(n1, e.start);
		assertEquals(n2, e.end);
	}
}