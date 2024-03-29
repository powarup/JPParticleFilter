package uk.ac.cam.jsp50.JPParticleFilter;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

public class PropagateCorrectnessTest {

	@Test
	public void propagatingInEmptyFPConservesNumber() throws FileNotFoundException {
		int n = 10000;
		int nSteps = 1000;
		FileInputStream floorPlanStream = new FileInputStream("10m_1Point.csv");
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, n, n, 0, null, null, false, false, false, false, false);
		StepVectorGenerator stepVectorGenerator = StepVectorGenerator.getInstance();
		for (int i = 0; i < nSteps; i++) {
			PFController.propagate(stepVectorGenerator.next());
			assertEquals(n, PFController.activeParticles);
		}
	}

}
