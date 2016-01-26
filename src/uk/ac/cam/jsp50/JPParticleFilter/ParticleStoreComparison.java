package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParticleStoreComparison extends PFComparison {

	public static void main(String[] args) throws IOException {
		InputStream floorPlanStream;
		int initialParticleNo, maxParticleNo, degeneracyLimit;
		boolean visualise = false;
		String randomFilePath;
		String stepVectorFilePath = null;

		// 100 particles - simple
		initialParticleNo = 100;
		maxParticleNo = 100;
		degeneracyLimit = 50;
		randomFilePath = "comparisonData/particlerandoms1.txt";
		// object (memory run)
		floorPlanStream = new FileInputStream("polygon1.csv");
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,"comparisonResults/particleComparison object 100 simple memory.csv");
		// array (memory run)
		floorPlanStream = new FileInputStream("polygon1.csv");
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,"comparisonResults/particleComparison array 100 simple memory.csv");

		// 100 particles - complex
		// object

		// array


		// 1000 particles - simple
		initialParticleNo = 1000;
		maxParticleNo = 1000;
		degeneracyLimit = 500;
		randomFilePath = "comparisonData/particlerandoms3.txt";
		// object (memory run)
		floorPlanStream = new FileInputStream("polygon1.csv");
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,"comparisonResults/particleComparison object 1000 simple memory.csv");
		// array (memory run)
		floorPlanStream = new FileInputStream("polygon1.csv");
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,"comparisonResults/particleComparison array 1000 simple memory.csv");


		// 1000 particles - complex
		// object

		// array

		// 10000 particles - simple
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		randomFilePath = "comparisonData/particlerandoms7.txt";
		// object (memory run)
		floorPlanStream = new FileInputStream("polygon1.csv");
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,"comparisonResults/particleComparison object 10000 simple memory.csv");
		// array (memory run)
		floorPlanStream = new FileInputStream("polygon1.csv");
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,"comparisonResults/particleComparison array 10000 simple memory.csv");
	}


}
