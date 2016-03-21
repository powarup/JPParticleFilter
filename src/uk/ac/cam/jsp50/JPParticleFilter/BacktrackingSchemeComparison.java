package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;

import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public class BacktrackingSchemeComparison extends PFComparison {
	
	public static void main(String[] args) throws IOException {
		FileInputStream floorPlanStream;
		int initialParticleNo = 3000;
		int maxParticleNo = 3000;
		int degeneracyLimit = 1000;
		boolean visualise = false;
		String randomFilePath = null;
		String stepVectorFilePath = "1KRight.csv";
		String floorPlanPath = "0m_1Point.csv";
		FloorPlanType fpType = FloorPlanType.SIMPLE_BITMAP;
		
		// =========== NAIVE ======================
		
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFNaiveBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		
		StepVectorGenerator s = StepVectorGenerator.getInstance();
		for (int i = 0; i < 999; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFNaiveBacktrackingController.propagate(s.next());
			if (PFNaiveBacktrackingController.activeParticles < PFNaiveBacktrackingController.degeneracyLimit)
				try {
					PFNaiveBacktrackingController.resample();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}
		
		export("backtrackingComparisonResults/naive rightSteps x999.csv", PFNaiveBacktrackingController.recorder);
	
		// =========== STRINGY =====================
		
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFStringyParticleBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		
		s = StepVectorGenerator.getInstance();
		for (int i = 0; i < 999; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFStringyParticleBacktrackingController.propagate(s.next());
			if (PFStringyParticleBacktrackingController.activeParticles < PFStringyParticleBacktrackingController.degeneracyLimit)
				try {
					PFStringyParticleBacktrackingController.resample();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}
		
		export("backtrackingComparisonResults/stringy rightSteps x999.csv", PFStringyParticleBacktrackingController.recorder);
	
	}
}
