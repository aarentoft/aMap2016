package util.graph;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class GraphDataLoadTest {
	@Test
	public void roadNodeNormal() throws IOException {
		RoadNode n = new RoadNode("1", 720050.2418, 6173986.37339);
		assertEquals(1, n.ID);

		// Allow a 0.0000001 difference between the saved and the actual values,
		// since floating points are not very precise in Java
		assertEquals(n.getX(), 720050.2418, 0.0000001);
		assertEquals(n.getY(), 6173986.37339, 0.0000001);
	}

	@Test
	public void roadNodeNegative() throws IOException {
		RoadNode n = new RoadNode("-14", -720050.2418, -6173986.37339);
		assertEquals(-14, n.ID);

		// Allow a 0.0000001 difference between the saved and the actual values,
		// since floating points are not very precise in Java
		assertEquals(-720050.2418, n.getX(), 0.0000001);
		assertEquals(-6173986.37339, n.getY(), 0.0000001);
	}

	@Test(expected = IOException.class)
	public void roadNodeMaximum() throws IOException {
		new RoadNode("1", 720050.2418, 6173986.37339);
	}

	@Test
	public void EdgeDataNormal() throws IOException {
//		EdgeData ed = new EdgeData(
//				"6,38,20.14234,199908,199908,6,'Søndervangs Allé',0,0,46,46,,,,,7027,7027,2500,2500,101,7300,0,,0,10,50,0.028,ft,,,10147200,07/21/00,4023316");
//
//		assertEquals(6, ed.FNODE);
//		assertEquals(38, ed.TNODE);
//		assertEquals(20.14234, ed.LENGTH, 0.00000001);
//		assertEquals(199908, ed.DAV_DK);
//		assertEquals(199908, ed.DAV_DK_ID);
//		assertEquals(6, ed.TYP);
//		assertEquals("Søndervangs Allé", ed.VEJNAVN);
//		assertEquals(0, ed.FROMLEFT);
//		assertEquals(0, ed.TOLEFT);
//		assertEquals(46, ed.FROMRIGHT);
//		assertEquals(46, ed.TORIGHT);
//		assertEquals("", ed.FROMLEFT_BOGSTAV);
//		assertEquals("", ed.TOLEFT_BOGSTAV);
//		assertEquals("", ed.FROMRIGHT_BOGSTAV);
//		assertEquals("", ed.TORIGHT_BOGSTAV);
//		assertEquals(7027, ed.V_SOGNENR);
//		assertEquals(7027, ed.H_SOGNENR);
//		assertEquals(2500, ed.V_POSTNR);
//		assertEquals(2500, ed.H_POSTNR);
//		assertEquals(101, ed.KOMMUNENR);
//		assertEquals(7300, ed.VEJKODE);
//		assertEquals(0, ed.SUBNET);
//		assertEquals("", ed.RUTENR);
//		assertEquals("0", ed.FRAKOERSEL);
//		assertEquals(10, ed.ZONE);
//		assertEquals(50, ed.SPEED);
//		assertEquals(0.028,ed.DRIVETIME, 0.0000001);
//		assertEquals("ft", ed.ONE_WAY);
//		assertEquals("", ed.F_TURN);
//		assertEquals("", ed.T_TURN);
//		assertEquals("10147200", ed.VEJNR);
//		assertEquals("07/21/00", ed.AENDR_DATO);
//		assertEquals(4023316, ed.TJEK_ID);
	}

	@Test
	public void EdgeDataNegative() throws IOException {
//		EdgeData ed = new EdgeData(
//				"-6,-38,-20.14234,-199908,-199908,-6,'Søndervangs Allé',-0,-0,-46,-46,,,,,7027,7027,2500,2500,101,7300,0,,0,10,50,0.028,ft,,,10147200,07/21/00,4023316");
//
//		assertEquals(-6, ed.FNODE);
//		assertEquals(-38, ed.TNODE);
//		assertEquals(-20.14234, ed.LENGTH, 0.00000001);
//		assertEquals(-199908, ed.DAV_DK);
//		assertEquals(-199908, ed.DAV_DK_ID);
//		assertEquals(-6, ed.TYP);
//		assertEquals("Søndervangs Allé", ed.VEJNAVN);
//		assertEquals(-0, ed.FROMLEFT);
//		assertEquals(-0, ed.TOLEFT);
//		assertEquals(-46, ed.FROMRIGHT);
//		assertEquals(-46, ed.TORIGHT);
//		assertEquals("", ed.FROMLEFT_BOGSTAV);
//		assertEquals("", ed.TOLEFT_BOGSTAV);
//		assertEquals("", ed.FROMRIGHT_BOGSTAV);
//		assertEquals("", ed.TORIGHT_BOGSTAV);
//		assertEquals(7027, ed.V_SOGNENR);
//		assertEquals(7027, ed.H_SOGNENR);
//		assertEquals(2500, ed.V_POSTNR);
//		assertEquals(2500, ed.H_POSTNR);
//		assertEquals(101, ed.KOMMUNENR);
//		assertEquals(7300, ed.VEJKODE);
//		assertEquals(0, ed.SUBNET);
//		assertEquals("", ed.RUTENR);
//		assertEquals("0", ed.FRAKOERSEL);
//		assertEquals(10, ed.ZONE);
//		assertEquals(50, ed.SPEED);
//		assertEquals(0.028,ed.DRIVETIME, 0.0000001);
//		assertEquals("ft", ed.ONE_WAY);
//		assertEquals("", ed.F_TURN);
//		assertEquals("", ed.T_TURN);
//		assertEquals("10147200", ed.VEJNR);
//		assertEquals("07/21/00", ed.AENDR_DATO);
//		assertEquals(4023316, ed.TJEK_ID);
	}

	@Test(expected = IOException.class)
	public void EdgeDataMaximum() throws IOException {
//		new EdgeData(
//				"78536756237856,3489652375892375,20.14234,199908,199908,6,'Søndervangs Allé',0,0,46,46,,,,,7027,7027,2500,2500,101,7300,0,,0,10,50,0.028,ft,,,10147200,07/21/00,4023316");
	}

	@Test
	public void EdgeNormal() throws IOException {
		RoadNode n1 = new RoadNode("1", 720050.2418, 6173986.37339);
		RoadNode n2 = new RoadNode("2", 124124.2124, 61234126.1249);
//		EdgeData ed = new EdgeData(
//				"6,38,20.14234,199908,199908,6,'Søndervangs Allé',0,0,46,46,,,,,7027,7027,2500,2500,101,7300,0,,0,10,50,0.028,ft,,,10147200,07/21/00,4023316");

//		RoadEdge e = new RoadEdge(ed, n1, n2);
//
//		assertEquals(ed.LENGTH,e.length, 0.0000001);
//		assertEquals(ed.DAV_DK, e.DAV_DK);
//		assertEquals(ed.DAV_DK_ID, e.DAV_DK_ID);
//		assertEquals(RoadType.getEnum(ed.TYP), e.type);
//		assertEquals(ed.VEJNAVN, e.roadname);
//		assertEquals(ed.V_POSTNR, e.postnummer_v);
//		assertEquals(ed.H_POSTNR, e.postnummer_h);
//		assertEquals(ed.FROMLEFT, e.FROMLEFT);
//		assertEquals(ed.TOLEFT, e.TOLEFT);
//		assertEquals(ed.FROMRIGHT, e.FROMRIGHT);
//		assertEquals(ed.TORIGHT, e.TORIGHT);
//		assertEquals(n1, e.start);
//		assertEquals(n2, e.end);
	}
}