package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FloorPlanComparison extends PFComparison {

	public static void runAndGenerateData(String randomFilePath, String floorPlanPath, String stepVectorFilePath, int initialParticleNo, int maxParticleNo, int degeneracyLimit, String testName) throws IOException {
		boolean visualise = false;
		InputStream floorPlanStream;
		int nGenerations = 100;
		String prefix = "comparisonResults/fp/floor plan comparison - " + maxParticleNo + " particles - " + testName + " - ";
		FloorPlanType floorPlanType;

		// NAIVE runs
		floorPlanType = FloorPlanType.NAIVE;
		// run for memory
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(nGenerations, prefix + "naive memory.csv");
		System.out.println("DONE. \n");
		// run for time
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(nGenerations, prefix + "naive time.csv");
		System.out.println("DONE. \n");


		// SIMPLE_BITMAP runs
		floorPlanType = FloorPlanType.SIMPLE_BITMAP;
		// run for memory
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(nGenerations, prefix + "simplebitmap memory.csv");
		System.out.println("DONE. \n");
		// run for time
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(nGenerations, prefix + "simplebitmap time.csv");
		System.out.println("DONE. \n");

		// COMPLEX_BITMAP runs
		floorPlanType = FloorPlanType.COMPLEX_BITMAP;
		// run for memory
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(nGenerations, prefix + "complexbitmap memory.csv");
		System.out.println("DONE. \n");
		// run for time
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, floorPlanType, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(nGenerations, prefix + "complexbitmap time.csv");
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
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "1 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "1 points 2");

		// ten points
		floorPlanPath = "comparisonData/tenPoints.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "10 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "10 points 2");

		// fifty points
		floorPlanPath = "comparisonData/fiftyPoints.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "50 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "50 points 2");

		// hundred points
		floorPlanPath = "comparisonData/hundredpoints.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "100 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "100 points 2");

		// five hundred points
		floorPlanPath = "comparisonData/fivehundredpoints.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "500 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "500 points 2");

		// thousand points
		floorPlanPath = "comparisonData/thousandpoints.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "1000 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "1000 points 2");

		// five thousand points
		floorPlanPath = "comparisonData/fivethousandpoints.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "5000 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "5000 points 2");


		// ten thousand points
		floorPlanPath = "comparisonData/tenthousandpoints.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "10000 points 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "10000 points 2");


		// polygon1
		floorPlanPath = "polygon1.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "polygon1 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "polygon1 2");


		// wgb_second
		initialParticleNo = 25000;
		maxParticleNo = 3000;
		degeneracyLimit = 1000;
		floorPlanPath = "wgb_second.csv";

		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "wgb_second 1");

		randomFilePath = "comparisonData/floorplanrandoms2.txt";
		runAndGenerateData(randomFilePath, floorPlanPath, stepVectorFilePath, initialParticleNo, maxParticleNo, degeneracyLimit, "wgb_second 2");



	}

}
