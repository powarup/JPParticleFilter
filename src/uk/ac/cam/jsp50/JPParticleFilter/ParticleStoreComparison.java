package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParticleStoreComparison extends PFComparison {

	public static void runAndGenerateData(String randomFilePath, String floorPlanPath, String stepVectorFilePath, int initialParticleNo, int maxParticleNo, int degeneracyLimit, String testName) throws IOException {
		boolean visualise = false;
		InputStream floorPlanStream;
		int nGenerations = 100;
		String prefix = "comparisonResults/particle store comparison - " + testName + " - ";
		
		// object
		// memory run
		System.out.println("OBJECT: (memory) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(nGenerations,prefix + "object memory.csv");
		System.out.println("DONE. \n");
		// time run
		System.out.println("OBJECT: (time) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(nGenerations,prefix + "object time.csv");
		System.out.println("DONE. \n");


		// array
		// memory run
		System.out.println("ARRAY: (memory) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,prefix + "array memory.csv");
		System.out.println("DONE. \n");
		// time run
		System.out.println("ARRAY: (time) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100,prefix + "array time.csv");
		System.out.println("DONE. \n");
	}

	public static void main(String[] args) throws IOException {
//		InputStream floorPlanStream;
		int initialParticleNo, maxParticleNo, degeneracyLimit;
//		boolean visualise = false;
		String randomFilePath;
		String stepVectorFilePath = null;
		String floorPlanPath;
		
		floorPlanPath = "polygon1.csv";
		

		// 100 particles - simple
		initialParticleNo = 100;
		maxParticleNo = 100;
		degeneracyLimit = 50;
		//floorPlanPath = "comparisonData/onePoint.csv";
		for (int i = 1; i <= 10; i++) {
			randomFilePath = "comparisonData/randoms/particlerandoms" + i + ".txt";
			int runNo = i % 10;
			if (runNo == 0) runNo = 10;
			runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "100 particles no collisions " + runNo);
		}
//		randomFilePath = "comparisonData/particlerandoms1.txt";
//		// object (memory run)
//		floorPlanStream = new FileInputStream("polygon1.csv");
//		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
//		runAndExport(100,"comparisonResults/particleComparison object 100 simple memory.csv");
//		// array (memory run)
//		floorPlanStream = new FileInputStream("polygon1.csv");
//		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
//		runAndExport(100,"comparisonResults/particleComparison array 100 simple memory.csv");
//
//		// 100 particles - complex
//		// object
//
//		// array


		// 1000 particles - simple
		initialParticleNo = 1000;
		maxParticleNo = 1000;
		degeneracyLimit = 500;
		//floorPlanPath = "comparisonData/onePoint.csv";
		for (int i = 11; i <= 20; i++) {
			randomFilePath = "comparisonData/randoms/particlerandoms" + i + ".txt";
			int runNo = i % 10;
			if (runNo == 0) runNo = 10;
			runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "1000 particles no collisions " + runNo);
		}
//		// object (memory run)
//		floorPlanStream = new FileInputStream("polygon1.csv");
//		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
//		runAndExport(100,"comparisonResults/particleComparison object 1000 simple memory.csv");
//		// array (memory run)
//		floorPlanStream = new FileInputStream("polygon1.csv");
//		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
//		runAndExport(100,"comparisonResults/particleComparison array 1000 simple memory.csv");
//
//
//		// 1000 particles - complex
//		// object
//
//		// array

		// 10000 particles - simple
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		//floorPlanPath = "comparisonData/onePoint.csv";
		for (int i = 21; i <= 30; i++) {
			randomFilePath = "comparisonData/randoms/particlerandoms" + i + ".txt";
			int runNo = i % 10;
			if (runNo == 0) runNo = 10;
			runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "10000 particles no collisions " + runNo);
		}
		
		// 10000 particles - simple
		initialParticleNo = 100000;
		maxParticleNo = 100000;
		degeneracyLimit = 50000;
		//floorPlanPath = "comparisonData/onePoint.csv";
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "100000 particles no collisions");
		
//		FileInputStream floorPlanStream;
//		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
//		initialParticleNo = 100000;
//		maxParticleNo = 100000;
//		degeneracyLimit = 50000;
//		// object (memory run)
//		floorPlanStream = new FileInputStream("polygon1.csv");
//		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, false, true, false, false, false);
//		runAndExport(100,"comparisonResults/particleComparison object 100000 simple memory.csv");
//		// array (memory run)
//		floorPlanStream = new FileInputStream("polygon1.csv");
//		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, false, true, false, false, false);
//		runAndExport(100,"comparisonResults/particleComparison array 100000 simple memory.csv");
	}


}
