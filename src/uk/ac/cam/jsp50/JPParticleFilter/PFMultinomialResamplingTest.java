package uk.ac.cam.jsp50.JPParticleFilter;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.cam.jsp50.JPParticleFilter.PFController.ParticleStoreType;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public class PFMultinomialResamplingTest {
	
	// total weight tests
	
	@Test
	public void tenParticleTotalWeightStaysCorrect() throws ParticleNotFoundException {
		PFController.setupFilter("polygon1.csv", ParticleStoreType.OBJECT, 10, 10, 10, null, null);
		checkResampleKeepsTotalWeightCorrectly();
	}
	
	@Test
	public void hundredParticleTotalWeightStaysCorrect() throws ParticleNotFoundException {
		PFController.setupFilter("polygon1.csv", ParticleStoreType.OBJECT, 100, 100, 100, null, null);
		checkResampleKeepsTotalWeightCorrectly();
	}
	
	@Test
	public void thousandParticleTotalWeightStaysCorrect() throws ParticleNotFoundException {
		PFController.setupFilter("polygon1.csv", ParticleStoreType.OBJECT, 1000, 1000, 1000, null, null);
		checkResampleKeepsTotalWeightCorrectly();
	}

	@Test
	public void tenthousandParticleTotalWeightStaysCorrect() throws ParticleNotFoundException {
		PFController.setupFilter("polygon1.csv", ParticleStoreType.OBJECT, 10000, 10000, 10000, null, null);
		checkResampleKeepsTotalWeightCorrectly();
	}

	
	public void checkResampleKeepsTotalWeightCorrectly() throws ParticleNotFoundException {
		double totalWeight = PFController.particleStore.getTotalWeight();
		assertTrue(totalWeight <= 1);
		
		// try 100 resamples without propagation
		for (int i = 0; i < 100; i++) {
			PFController.resample();
			totalWeight = PFController.particleStore.getTotalWeight();
			assertTrue(totalWeight <= 1);
		}
		
		int n = 0;
		StepVector nextStep;
		StepVectorGenerator stepGen = StepVectorGenerator.getInstance();
		
		// iterate until particles die or 200 steps
		while (PFController.activeParticles > 0 && n < 200) {
			nextStep = stepGen.next();
			PFController.propagate(nextStep);
			assertTrue(totalWeight <= 1);
			if (PFController.activeParticles <= 0) break;
			PFController.resample();
			assertTrue(totalWeight <= 1);
		}
		
	}
	
	// same particle index choice tests
	
	@Test
	public void twoParticlesWithLowerHalfRandomsResamplesCorrectly() throws ParticleNotFoundException {
		PFController.setupFilter("wideVertical.csv", ParticleStoreType.OBJECT, 2, 2, 2, "10KRandoms_lowerhalf.txt", null); //TODO: use empty map
		checkResampleAlwaysUsesIndex(0);
	}
	
	@Test
	public void twoParticlesWithUpperHalfRandomsResamplesCorrectly() throws ParticleNotFoundException {
		PFController.setupFilter("wideVertical.csv", ParticleStoreType.OBJECT, 2, 2, 2, "10KRandoms_upperhalf.txt", null); //TODO: use empty map
		checkResampleAlwaysUsesIndex(1);
	}
	
	@Test
	public void threeParticlesWithLowerThirdRandomsResamplesCorrectly() throws ParticleNotFoundException {
		PFController.setupFilter("wideVertical.csv", ParticleStoreType.OBJECT, 3, 3, 3, "10KRandoms_lowerthird.txt", null); //TODO: use empty map
		checkResampleAlwaysUsesIndex(0);
	}
	
	public void checkResampleAlwaysUsesIndex(int index) throws ParticleNotFoundException {
		int n = 0;
		StepVector nextStep;
		StepVectorGenerator stepGen = StepVectorGenerator.getInstance();
		
		// try 100 resamples without propagation:
		for (int i = 0; i < 100; i++) {
			resampleForIndex(index);
		}
		
		// iterate until particles die
		while (PFController.activeParticles == 2) {	
			// Propagate
			nextStep = stepGen.next();
			PFController.propagate(nextStep);
			if (PFController.activeParticles < 2) break;
			
			// Resample
			resampleForIndex(index);
			n++;
		}
		
		System.out.println("successful over " + n + " iterations\n============================\n");
	}
	
	public void resampleForIndex(int i) throws ParticleNotFoundException {
		double correctParticleToResampleX = PFController.particleStore.getXatIndex(i);
		double correctParticleToResampleY = PFController.particleStore.getYatIndex(i);
		PFController.resample();
		ParticleManager manager = PFController.particleStore.getParticleManager();
		while (manager.hasNextActiveParticle()) {
			manager.nextActiveParticle();
			assertEquals(correctParticleToResampleX, manager.getX(),0.0);
			assertEquals(correctParticleToResampleY, manager.getY(),0.0);
		}
	}

	public void printAllParticles() throws ParticleNotFoundException {
		ParticleManager manager = PFController.particleStore.getParticleManager();
		while (manager.hasNextActiveParticle()) {
			manager.nextActiveParticle();
			System.out.println(manager.summary());
		}
	}
	
}
