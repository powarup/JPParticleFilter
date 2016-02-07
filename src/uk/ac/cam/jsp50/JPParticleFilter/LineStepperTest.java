package uk.ac.cam.jsp50.JPParticleFilter;

import static org.junit.Assert.*;

import org.junit.Test;
import org.omg.PortableInterceptor.LOCATION_FORWARD;

public class LineStepperTest {

	@Test
	public void verticalTest() {
		LineStepper line1Stepper = new LineStepper(0.1, 0.417, 1.1, 0.417, -0.7);
		assertTrue(line1Stepper.vertical);
		for (int y = 11; y > -6; y--) {
			assertTrue(line1Stepper.currentX == 4);
			assertTrue(line1Stepper.currentY == y);
			assertTrue(line1Stepper.canStepForward());
			line1Stepper.stepForward();
		}
		assertTrue(line1Stepper.currentX == 4);
		assertTrue(line1Stepper.currentY == -6);
		assertFalse(line1Stepper.canStepForward());
		
		LineStepper line2Stepper = new LineStepper(0.1, 1.4, 5.4, 1.4, 2.71);
		assertTrue(line2Stepper.vertical);
		for (int y = 54; y > 27; y--) {
			assertTrue(line2Stepper.currentX == 14);
			assertTrue(line2Stepper.currentY == y);
			assertTrue(line2Stepper.canStepForward());
			line2Stepper.stepForward();
		}
		assertTrue(line2Stepper.currentX == 14);
		assertTrue(line2Stepper.currentY == 27);
		assertFalse(line2Stepper.canStepForward());
		
		LineStepper line3Stepper = new LineStepper(0.1, 0.417, -0.7, 0.417, 1.1);
		assertTrue(line3Stepper.vertical);
		for (int y = -6; y < 11; y++) {
			assertTrue(line3Stepper.currentX == 4);
			assertTrue(line3Stepper.currentY == y);
			assertTrue(line3Stepper.canStepForward());
			line3Stepper.stepForward();
		}
		assertTrue(line3Stepper.currentX == 4);
		assertTrue(line3Stepper.currentY == 11);
		assertFalse(line3Stepper.canStepForward());
		
		LineStepper line4Stepper = new LineStepper(0.1, 1.4, 2.71, 1.4, 5.4);
		assertTrue(line4Stepper.vertical);
		for (int y = 27; y < 54; y++) {
			assertTrue(line4Stepper.currentX == 14);
			assertTrue(line4Stepper.currentY == y);
			assertTrue(line4Stepper.canStepForward());
			line4Stepper.stepForward();
		}
		assertTrue(line4Stepper.currentX == 14);
		assertTrue(line4Stepper.currentY == 54);
		assertFalse(line4Stepper.canStepForward());
		
		LineStepper line5Stepper = new LineStepper(0.1,0.1,0.1,0.1,1.1);
		assertTrue(line5Stepper.vertical);
		for (int y = 1; y < 11; y++) {
			assertTrue(line5Stepper.currentX == 1);
			assertTrue(line5Stepper.currentY == y);
			assertTrue(line5Stepper.canStepForward());
			line5Stepper.stepForward();
		}
		assertTrue(line5Stepper.currentX == 1);
		assertTrue(line5Stepper.currentY == 11);
		assertFalse(line5Stepper.canStepForward());
	}
	
	@Test
	public void goingUpTest() {
		LineStepper line1Stepper = new LineStepper(0.1, 0, 0, 0.35, 0.2);
		assertFalse(line1Stepper.vertical);
		assertTrue(line1Stepper.currentX == 0);
		assertTrue(line1Stepper.currentY == 0);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 1);
		assertTrue(line1Stepper.currentY == 0);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 1);
		assertTrue(line1Stepper.currentY == 1);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 2);
		assertTrue(line1Stepper.currentY == 1);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 3);
		assertTrue(line1Stepper.currentY == 1);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 3);
		assertTrue(line1Stepper.currentY == 2);
		assertFalse(line1Stepper.canStepForward());
		
		
		LineStepper line2Stepper = new LineStepper(0.1, 0.47, 0.06, 0.62, 0.38);
		assertFalse(line2Stepper.vertical);
		assertTrue(line2Stepper.currentX == 4);
		assertTrue(line2Stepper.currentY == 0);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 4);
		assertTrue(line2Stepper.currentY == 1);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 5);
		assertTrue(line2Stepper.currentY == 1);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 5);
		assertTrue(line2Stepper.currentY == 2);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 5);
		assertTrue(line2Stepper.currentY == 3);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 6);
		assertTrue(line2Stepper.currentY == 3);
		assertFalse(line2Stepper.canStepForward());
	}
	
	@Test
	public void goingDownTest() {
		LineStepper line1Stepper = new LineStepper(0.1, 0.05, 0.5, 0.25, 0.3);
		assertTrue(line1Stepper.currentX == 0);
		assertTrue(line1Stepper.currentY == 5);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 0);
		assertTrue(line1Stepper.currentY == 4);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 1);
		assertTrue(line1Stepper.currentY == 4);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 1);
		assertTrue(line1Stepper.currentY == 3);
		assertTrue(line1Stepper.canStepForward());
		line1Stepper.stepForward();
		assertTrue(line1Stepper.currentX == 2);
		assertTrue(line1Stepper.currentY == 3);
		assertFalse(line1Stepper.canStepForward());
		
		LineStepper line2Stepper = new LineStepper(0.1, 0.32, 0.36, 0.41, 0.04);
		assertTrue(line2Stepper.currentX == 3);
		assertTrue(line2Stepper.currentY == 3);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 3);
		assertTrue(line2Stepper.currentY == 2);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 3);
		assertTrue(line2Stepper.currentY == 1);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 3);
		assertTrue(line2Stepper.currentY == 0);
		assertTrue(line2Stepper.canStepForward());
		line2Stepper.stepForward();
		assertTrue(line2Stepper.currentX == 4);
		assertTrue(line2Stepper.currentY == 0);
		assertFalse(line2Stepper.canStepForward());
	}
	

}
