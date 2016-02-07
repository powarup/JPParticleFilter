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
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/onePoint.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "one point");

		
		// ten points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/tenPoints.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "ten points");

		// fifty points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/fiftyPoints.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "fifty points");

		// hundred points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/hundredpoints.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "hundred points");

		// five hundred points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/fivehundredpoints.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "five hundred points");

		// thousand points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/thousandpoints.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "thousand points");

		// five thousand points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/fivethousandpoints.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "five thousand points");


		// ten thousand points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/tenthousandpoints.csv";

		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "ten thousand points");


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
