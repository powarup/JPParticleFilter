package uk.ac.cam.jsp50.JPParticleFilter;

import static org.junit.Assert.*;

import org.junit.Test;

public class PFFloorPlanTest {

	
	// EDGE TESTS
	
	@Test
	public void checkHorizontalCrossings() {
		Edge e;
		
		//horizontal edge
		e = new Edge(4, 1, 7, 1);
		assertTrue(e.crosses(4.1, 1, 4.2, 1));
		assertTrue(e.crosses(1,1,4.2,1));
		assertTrue(e.crosses(4,0,4,9));
		assertTrue(e.crosses(4.7,2,6,0.3));
		assertTrue(e.crosses(6.01,0.7,6.01,1.1));
		assertTrue(e.crosses(2,-1,8,2));
		assertTrue(e.crosses(7,7,7,1));
		assertFalse(e.crosses(0,0,0,0));
		assertFalse(e.crosses(1,1,100,100));
		assertFalse(e.crosses(1,1,3.99,1));
		assertFalse(e.crosses(0,9,100,9));
		

		e = new Edge(7, 1, 4, 1);
		assertTrue(e.crosses(4.1, 1, 4.2, 1));
		assertTrue(e.crosses(1,1,4.2,1));
		assertTrue(e.crosses(4,0,4,9));
		assertTrue(e.crosses(4.7,2,6,0.3));
		assertTrue(e.crosses(6.01,0.7,6.01,1.1));
		assertTrue(e.crosses(2,-1,8,2));
		assertTrue(e.crosses(7,7,7,1));
		assertFalse(e.crosses(0,0,0,0));
		assertFalse(e.crosses(1,1,100,100));
		assertFalse(e.crosses(1,1,3.99,1));
		assertFalse(e.crosses(0,9,100,9));
	}
	

	@Test
	public void checkVerticalCrossings() {
		Edge e;
		
		//vertical edge
		e = new Edge(3.5,2,3.5,5);
		assertTrue(e.crosses(0,5,7,5));
		assertTrue(e.crosses(3.5,3,3.5,4));
		assertTrue(e.crosses(3.5,0,3.5,7));
		assertTrue(e.crosses(3,4,4,3));
		assertTrue(e.crosses(0,0,5,5));
		assertTrue(e.crosses(3.5,2,10,2));
		assertTrue(e.crosses(1,3,6,3));
		assertFalse(e.crosses(0,0,0,0));
		assertFalse(e.crosses(0,1,90,1));
		assertFalse(e.crosses(2,0,9,5));
		assertFalse(e.crosses(5,6,100,100));
		
		e = new Edge(3.5,5,3.5,2);
		assertTrue(e.crosses(0,5,7,5));
		assertTrue(e.crosses(3.5,3,3.5,4));
		assertTrue(e.crosses(3.5,0,3.5,7));
		assertTrue(e.crosses(3,4,4,3));
		assertTrue(e.crosses(0,0,5,5));
		assertTrue(e.crosses(3.5,2,10,2));
		assertTrue(e.crosses(1,3,6,3));
		assertFalse(e.crosses(0,0,0,0));
		assertFalse(e.crosses(0,1,90,1));
		assertFalse(e.crosses(2,0,9,5));
		assertFalse(e.crosses(5,6,100,100));
	}
	

	@Test
	public void checkDiagonalCrossings() {
		Edge e;
		
		//diagonal edge
		e = new Edge(2,1,7,3);
		assertTrue(e.crosses(2,1,7,3));
		assertTrue(e.crosses(0,2,90,2));
		assertTrue(e.crosses(6,4,8,2));
		assertTrue(e.crosses(4,0,4,3));
		assertTrue(e.crosses(0,0,4.5,2));
		assertTrue(e.crosses(4,2,5,2));
		assertTrue(e.crosses(3,1,7,4));
		assertFalse(e.crosses(0,0,0,0));
		assertFalse(e.crosses(0,1,90,100));
		assertFalse(e.crosses(4,3,5,3));
		assertFalse(e.crosses(5,6,100,100));

		e = new Edge(7,3,2,1);
		assertTrue(e.crosses(2,1,7,3));
		assertTrue(e.crosses(0,2,90,2));
		assertTrue(e.crosses(6,4,8,2));
		assertTrue(e.crosses(4,0,4,3));
		assertTrue(e.crosses(0,0,4.5,2));
		assertTrue(e.crosses(4,2,5,2));
		assertTrue(e.crosses(3,1,7,4));
		assertFalse(e.crosses(0,0,0,0));
		assertFalse(e.crosses(0,1,90,100));
		assertFalse(e.crosses(4,3,5,3));
		assertFalse(e.crosses(5,6,100,100));
	}

}
