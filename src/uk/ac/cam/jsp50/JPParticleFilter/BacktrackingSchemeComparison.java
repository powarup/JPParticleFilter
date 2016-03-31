package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;

import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public class BacktrackingSchemeComparison extends PFComparison {

	static FileInputStream floorPlanStream;
	static int initialParticleNo;
	static int maxParticleNo;
	static int degeneracyLimit;
	static boolean visualise = false;
	static String randomFilePath;
	static String stepVectorFilePath;
	static String floorPlanPath;
	static FloorPlanType fpType = FloorPlanType.COMPLEX_BITMAP;
	static int steps;

	public static void main(String[] args) throws IOException {
		initialParticleNo = 30000;
		maxParticleNo = 30000;
		degeneracyLimit = 10000;
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		stepVectorFilePath = "1KRight.csv";
		floorPlanPath = "0m_1Point.csv";
		steps = 500;

		runAndGenerateData("rightSteps");

		initialParticleNo = 30000;
		maxParticleNo = 30000;
		degeneracyLimit = 10000;
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		stepVectorFilePath = "1KRandomSteps.csv";
		floorPlanPath = "polygon1.csv";

		runAndGenerateData("polygon1");
		
		initialParticleNo = 30000;
		maxParticleNo = 3000;
		degeneracyLimit = 1000;
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		stepVectorFilePath = "wgb walk 1.csv";
		floorPlanPath = "wgb_second.csv";
		steps = 247;
		
		runAndGenerateData("wgb_second");
	}

	public static void runAndGenerateData(String runName) throws IOException {
		// =========== NAIVE ======================

		floorPlanStream = new FileInputStream(floorPlanPath);
		PFNaiveBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);

		StepVectorGenerator s = StepVectorGenerator.getInstance();
		for (int i = 0; i < steps; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFNaiveBacktrackingController.propagate(s.next());
			if (PFNaiveBacktrackingController.activeParticles < PFNaiveBacktrackingController.degeneracyLimit)
				try {
					PFNaiveBacktrackingController.resample();
					Runtime.getRuntime().gc();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}

		export("backtrackingComparisonResults/naive " + runName + " x" + steps + " memory.csv", PFNaiveBacktrackingController.recorder);
		
		PFNaiveBacktrackingController.resetFilter();
		System.gc();
		
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFNaiveBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		
		s = StepVectorGenerator.getInstance();
		for (int i = 0; i < steps; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFNaiveBacktrackingController.propagate(s.next());
			if (PFNaiveBacktrackingController.activeParticles < PFNaiveBacktrackingController.degeneracyLimit)
				try {
					PFNaiveBacktrackingController.resample();
					Runtime.getRuntime().gc();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}

		export("backtrackingComparisonResults/naive " + runName + " x" + steps + " time.csv", PFNaiveBacktrackingController.recorder);

		PFNaiveBacktrackingController.resetFilter();
		System.gc();
		
		// =========== STRINGY =====================

		floorPlanStream = new FileInputStream(floorPlanPath);
		PFStringyParticleBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);

		s = StepVectorGenerator.getInstance();
		for (int i = 0; i < steps; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFStringyParticleBacktrackingController.propagate(s.next());
			if (PFStringyParticleBacktrackingController.activeParticles < PFStringyParticleBacktrackingController.degeneracyLimit)
				try {
					PFStringyParticleBacktrackingController.resample();
					Runtime.getRuntime().gc();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}

		export("backtrackingComparisonResults/stringy " + runName + " x" + steps + " memory.csv", PFStringyParticleBacktrackingController.recorder);
		
		PFStringyParticleBacktrackingController.resetFilter();
		System.gc();
		
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFStringyParticleBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);

		s = StepVectorGenerator.getInstance();
		for (int i = 0; i < steps; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFStringyParticleBacktrackingController.propagate(s.next());
			if (PFStringyParticleBacktrackingController.activeParticles < PFStringyParticleBacktrackingController.degeneracyLimit)
				try {
					PFStringyParticleBacktrackingController.resample();
					Runtime.getRuntime().gc();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}

		export("backtrackingComparisonResults/stringy " + runName + " x" + steps + " time.csv", PFStringyParticleBacktrackingController.recorder);

		PFStringyParticleBacktrackingController.resetFilter();
		System.gc();
		
		// =========== ACTIVE =====================

		floorPlanStream = new FileInputStream(floorPlanPath);
		PFActivePruningBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);

		s = StepVectorGenerator.getInstance();
		for (int i = 0; i < steps; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFActivePruningBacktrackingController.propagate(s.next());
			if (PFActivePruningBacktrackingController.activeParticles < PFActivePruningBacktrackingController.degeneracyLimit)
				try {
					PFActivePruningBacktrackingController.resample();
					Runtime.getRuntime().gc();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}

		export("backtrackingComparisonResults/activePruning " + runName + " x" + steps + " memory.csv", PFActivePruningBacktrackingController.recorder);
		
		PFActivePruningBacktrackingController.resetFilter();
		System.gc();
		
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFActivePruningBacktrackingController.setupFilter(floorPlanStream, fpType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);

		s = StepVectorGenerator.getInstance();
		for (int i = 0; i < steps; i++) {
			if (i % 20 == 0) System.out.println(i);
			PFActivePruningBacktrackingController.propagate(s.next());
			if (PFActivePruningBacktrackingController.activeParticles < PFActivePruningBacktrackingController.degeneracyLimit)
				try {
					PFActivePruningBacktrackingController.resample();
					Runtime.getRuntime().gc();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}

		export("backtrackingComparisonResults/activePruning " + runName + " x" + steps + " time.csv", PFActivePruningBacktrackingController.recorder);

	}
}
