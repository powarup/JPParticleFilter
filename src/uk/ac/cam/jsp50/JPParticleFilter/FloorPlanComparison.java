package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FloorPlanComparison extends PFComparison {

	public static void main(String[] args) throws IOException {
		InputStream floorPlanStream;
		int initialParticleNo, maxParticleNo, degeneracyLimit;
		boolean visualise = false;
		String randomFilePath, floorPlanPath;
		String stepVectorFilePath = null;

		// one point
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/onePoint.csv";

		// one point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - one point - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - one point - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - one point - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - one point - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------

		// ten points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/tenPoints.csv";


		// ten point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten points - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten points - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten points - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten points - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------

		// fifty points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/fiftyPoints.csv";


		// fifty point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - fifty points - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - fifty points - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - fifty points - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - fifty points - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------

		// hundred points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/hundredpoints.csv";


		// hundred point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - hundred points - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - hundred points - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - hundred points - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - hundred points - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------

		// five hundred points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/fivehundredpoints.csv";


		// five hundred point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five hundred points - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five hundred points - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five hundred points - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five hundred points - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------

		// thousand points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/thousandpoints.csv";


		// thousand point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - thousand points - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - thousand points - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - thousand points - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - thousand points - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------

		// five thousand points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/fivethousandpoints.csv";


		// five thousand point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five thousand points - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five thousand points - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five thousand points - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - five thousand points - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------

		// ten thousand points
		randomFilePath = "comparisonData/floorplanrandoms1.txt";
		floorPlanPath = "comparisonData/tenthousandpoints.csv";


		// ten thousand point no collisions 10000 particles
		initialParticleNo = 10000;
		maxParticleNo = 10000;
		degeneracyLimit = 5000;
		// NAIVE
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten thousand points - 10000 - naive memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.NAIVE, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten thousand points - 10000 - naive time.csv");
		System.out.println("DONE. \n");
		//				

		// BITMAP
		// memory run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, true, false, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten thousand points - 10000 - bitmap memory.csv");
		System.out.println("DONE. \n");
		// time run
		floorPlanStream = new FileInputStream(floorPlanPath);
		PFController.setupFilter(floorPlanStream, ParticleStoreType.ARRAY, FloorPlanType.SIMPLE_BITMAP, initialParticleNo, maxParticleNo, degeneracyLimit, randomFilePath, stepVectorFilePath, visualise, false, true, false, false);
		runAndExport(100, "comparisonResults/floor plan comparison - ten thousand points - 10000 - bitmap time.csv");
		System.out.println("DONE. \n");

		// --------------------------------------












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
