package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FloorPlanComparison extends PFComparison {

	public static void runAndGenerateData(String randomFilePath, String floorPlanPath, String stepVectorFilePath, int initialParticleNo, int maxParticleNo, int degeneracyLimit, String testName) throws IOException {
		boolean visualise = false;
		InputStream floorPlanStream;
		int nGenerations = 100;
		String prefix = "comparisonResults/floor plan comparison - " + testName + " - " + initialParticleNo + " - ";


		// NAIVE runs
		// run for memory
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(nGenerations, prefix + "naive memory.csv");
		System.out.println("DONE. \n");
		// run for time
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(nGenerations, prefix + "naive time.csv");
		System.out.println("DONE. \n");


		// SIMPLE_BITMAP runs
		// run for memory
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(nGenerations, prefix + "simplebitmap memory.csv");
		System.out.println("DONE. \n");
		// run for time
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(nGenerations, prefix + "simplebitmap time.csv");
		System.out.println("DONE. \n");
	}

	public static void main(String[] args) throws IOException {
		int initialParticleNo, maxParticleNo, degeneracyLimit;

		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		
		String randomFilePath, floorPlanPath;
		String stepVectorFilePath = null;

		// one point
		floorPlanPath = "comparisonData/onePoint.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "one point 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "one point 2");

		// ten points
		floorPlanPath = "comparisonData/tenPoints.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "ten points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "ten points 2");

		// fifty points
		floorPlanPath = "comparisonData/fiftyPoints.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "fifty points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "fifty points 2");

		// hundred points
		floorPlanPath = "comparisonData/hundredpoints.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "hundred points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "hundred points 2");

		// five hundred points
		floorPlanPath = "comparisonData/fivehundredpoints.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "five hundred points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "five hundred points 2");

		// thousand points
		floorPlanPath = "comparisonData/thousandpoints.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "thousand points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "thousand points 2");

		// five thousand points
		floorPlanPath = "comparisonData/fivethousandpoints.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "five thousand points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "five thousand points 2");


		// ten thousand points
		floorPlanPath = "comparisonData/tenthousandpoints.csv";
		
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "ten thousand points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "ten thousand points 2");


		// polygon1 100 particles
		//naive
		//bitmap

		// polygon1 1000 particles
		//naive
		//bitmap

		// polygon1 10000 particles
		//naive	
		//bitmap


		// complex 100 particles
		//naive
		//bitmap

		// complex 1000 particles
		//naive
		//bitmap

		// complex 10000 particles
		//naive
		//bitmap

	}

}
