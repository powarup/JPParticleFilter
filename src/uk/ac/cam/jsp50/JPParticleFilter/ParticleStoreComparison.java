package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParticleStoreComparison extends PFComparison {

	public static void runAndGenerateData(String randomFilePath, String floorPlanPath, String stepVectorFilePath, int initialParticleNo, int maxParticleNo, int degeneracyLimit, String testName) throws IOException {
		boolean visualise = false;
		InputStream floorPlanStream;
		int nGenerations = 100;
		String prefix = "comparisonResults/particle store comparison - " + maxParticleNo + " particles - " + testName + " - ";
		FloorPlanType floorPlanType = FloorPlanType.COMPLEX_BITMAP;

		// object
		// memory run
		System.out.println("OBJECT: (memory) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(nGenerations,prefix + "object memory.csv");
		System.out.println("DONE. \n");
		// time run
		System.out.println("OBJECT: (time) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.OBJECT, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(nGenerations,prefix + "object time.csv");
		System.out.println("DONE. \n");


		// array
		// memory run
		System.out.println("ARRAY: (memory) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100,prefix + "array memory.csv");
		System.out.println("DONE. \n");
		// time run
		System.out.println("ARRAY: (time) " + testName);
		System.gc();
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100,prefix + "array time.csv");
		System.out.println("DONE. \n");
	}

	public static void main(String[] args) throws IOException {
		int initialParticleNo, maxParticleNo, degeneracyLimit;
		String randomFilePath;
		String stepVectorFilePath = null;
		String floorPlanPath;

		floorPlanPath = "comparisonData/onepoint.csv";


		// 100 particles - simple
		initialParticleNo = 100;
		maxParticleNo = 100;
		degeneracyLimit = 50;
		for (int i = 1; i <= 10; i++) {
			randomFilePath = "comparisonData/randoms/particlerandoms" + i + ".txt";
			int runNo = i % 10;
			if (runNo == 0) runNo = 10;
			runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "no collisions complex " + runNo);
		}


		// 1000 particles - simple
		initialParticleNo = 1000;
		maxParticleNo = 1000;
		degeneracyLimit = 500;
		for (int i = 11; i <= 20; i++) {
			randomFilePath = "comparisonData/randoms/particlerandoms" + i + ".txt";
			int runNo = i % 10;
			if (runNo == 0) runNo = 10;
			runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "no collisions complex " + runNo);
		}

		// 10000 particles - simple
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		for (int i = 21; i <= 30; i++) {
			randomFilePath = "comparisonData/randoms/particlerandoms" + i + ".txt";
			int runNo = i % 10;
			if (runNo == 0) runNo = 10;
			runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "no collisions complex " + runNo);
		}

		// 100000 particles - simple
		initialParticleNo = 100000;
		maxParticleNo = 100000;
		degeneracyLimit = 50000;
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "no collisions complex");

		// 100000 particles - polygon1
		floorPlanPath = "polygon1.csv";
		initialParticleNo = 100000;
		maxParticleNo = 100000;
		degeneracyLimit = 50000;
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "polygon1");

		// wgb_second run 1 - cut number
		floorPlanPath = "wgb_second.csv";
		initialParticleNo = 50000;
		maxParticleNo = 3000;
		degeneracyLimit = 1000;
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "wgb_second");

		// wgb_second run 1 - constant particle no
		floorPlanPath = "wgb_second.csv";
		initialParticleNo = 50000;
		maxParticleNo = 50000;
		degeneracyLimit = 10000;
		randomFilePath = "comparisonData/bigparticlerandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "wgb_second nocut");

	}


}
