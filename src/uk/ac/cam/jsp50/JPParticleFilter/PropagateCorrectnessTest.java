package uk.ac.cam.jsp50.JPParticleFilter;

import static org.junit.Assert.*;
import org.junit.Test;

import uk.ac.cam.jsp50.JPParticleFilter.PFController.ParticleStoreType;

public class PropagateCorrectnessTest {

	@Test
	public void propagatingInEmptyFPConservesNumber() {
		int n = 10000;
		int nSteps = 100;
		PFController.setupFilter("10m_1Point.csv", ParticleStoreType.OBJECT, n, n, 0, null, null);
		StepVectorGenerator stepVectorGenerator = StepVectorGenerator.getInstance();
		for (int i = 0; i < nSteps; i++) {
			PFController.propagate(stepVectorGenerator.next());
			assertEquals(n, PFController.activeParticles);
		}
	}

}
