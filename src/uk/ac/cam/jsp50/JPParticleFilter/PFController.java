package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.Arrays;
import java.util.Scanner;

import uk.ac.cam.jsp50.JPParticleFilter.PFRandom.PFRandomInstanceAlreadyExistsException;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;
import uk.ac.cam.jsp50.JPParticleFilter.StepVectorGenerator.StepVectorGeneratorInstanceAlreadyExistsException;

public class PFController {

	public enum ParticleStoreType {
		OBJECT,ARRAY
	}
	
	public static FloorPlan floorPlan;
	public static ParticleStore particleStore, inactiveStore;
	public static int maxParticleNo;
	public static int degeneracyLimit;
	public static int activeParticles = 0;
	
	public static PFVisualiser visualiser;
	private static Scanner scan;
	
	public static void init(ParticleStoreType type) {
		initWithParticleNo(type,maxParticleNo);
	}
	
	public static void initWithParticleNo(ParticleStoreType type, int particleNo) {
		switch (type) {
		case OBJECT:
			particleStore = new ObjectParticleStore();
			break;
		case ARRAY:
			particleStore = new ArrayParticleStore(particleNo);
			break;
		default:
			particleStore = new ArrayParticleStore(particleNo);
			break;
		}
		double x,y;
		PFRandom randomGenerator = PFRandom.getInstance();
		
		double weight = 1.0/particleNo;
		while (activeParticles < particleNo) {
			//System.out.println("Making " + particleNo + " particles. Current #active particles: " + activeParticles);
			x = randomGenerator.nextDouble() * floorPlan.maxX;
			y = randomGenerator.nextDouble() * floorPlan.maxY;
						
			if (floorPlan.pointIsInsideRoom(x, y)) {
				particleStore.addParticle(x,y,weight);
				activeParticles++;
			}
		}
		System.out.println("initialised with " + activeParticles + " particles");
	}
	
	public static void propagate(StepVector s) {
		System.out.println("Propagating " + activeParticles + " particles from step vector " + s.length + "," + s.angle);
		
		PFRandom randomGenerator = PFRandom.getInstance();
		
		ParticleManager particleManager = particleStore.getParticleManager();
		
		double lastx, lasty, currentx, currenty;
		
		while (particleManager.hasNextActiveParticle()) try {
			particleManager.nextActiveParticle();
			lastx = particleManager.getX();
			lasty = particleManager.getY();
			particleManager.displace(s.addNoise(randomGenerator));
			currentx = particleManager.getX();
			currenty = particleManager.getY();
			
			
			boolean crossesBoundary = floorPlan.didCrossBoundary(lastx,lasty,currentx,currenty);
			if (crossesBoundary) {
				particleManager.setWeight(0.0);
				activeParticles--;
				visualiser.addStep(lastx, lasty, currentx, currenty, true);
			} else {
				visualiser.addStep(lastx, lasty, currentx, currenty, false);
			}
		} catch (ParticleNotFoundException e) {
			System.out.print(e.getMessage());
		}
		
	}
	
	public static void resample() throws ParticleNotFoundException {
		/* Multinomial resampling
		 * normalise weights
		 * generate N U(0,1) values
		 * sort values
		 * iterate over particles and populate new
		 */
		System.out.println("Resampling");
		
		ParticleManager particleManager = particleStore.getParticleManager();
		double totalWeight = 0;
		while (particleManager.hasNextActiveParticle()) {
			particleManager.nextActiveParticle();
			totalWeight += particleManager.getWeight();
		}
		
		double[] randomN = new double[maxParticleNo];
		PFRandom randomiser = PFRandom.getInstance();
		for (int i = 0; i < maxParticleNo; i++) {
			randomN[i] = randomiser.nextDouble();
		}
		
		Arrays.sort(randomN);
		
		ParticleStore newParticles = (inactiveStore == null) ? particleStore.getFreshParticleStoreInstance() : inactiveStore;
		
		particleManager = particleStore.getParticleManager();
		particleManager.nextActiveParticle();
		double weightTotal = particleManager.getWeight();
		
		System.out.println("total weight: " + totalWeight);
		
		// iterate over random numbers
		// if weight less than current total weight seen, generate new particle
		// else get new particle, update total weight, and try again
		
		for (int j = 0; j < maxParticleNo; j++) {
			while (randomN[j] > weightTotal) {
				try {
					particleManager.nextActiveParticle();
				} catch (ParticleNotFoundException e) {
					System.out.println("no particle found, at weight " + weightTotal + ", total weight of active particles is " + totalWeight + " and current random number is " + randomN[j] + ", assuming is floating point error, so using last particle");
				}
				weightTotal += particleManager.getWeight()/totalWeight;
			}
			double x = particleManager.getX();
			double y = particleManager.getY();
			double w = particleManager.getWeight() / totalWeight;
			newParticles.addParticle(x,y,w);
		}
		
		inactiveStore = particleStore;
		inactiveStore.cleanForReuse();
		particleStore = newParticles;
		visualiser.particles = particleStore;
		activeParticles = particleStore.getParticleNo();
		
	}
	
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	public static void showMemory() {
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Used memory in bytes: " + memory);
		System.out.println("Used memory in megabytes: "
				+ bytesToMegabytes(memory));
	}
	
	private static void resetFilter() {
		floorPlan = null;
		particleStore = null;
		inactiveStore = null;
		visualiser = null;
		scan = null;
		maxParticleNo = 0;
		degeneracyLimit = 0;
		activeParticles = 0;
		PFRandom.clearInstance();
		StepVectorGenerator.clearInstance();
	}
	
	public static void setupFilter(String floorPlanPath, ParticleStoreType storeType, int initialParticleNo, int maxParticleNo, int degeneracyLimit, String randomFilePath, String stepVectorFilePath) {
		resetFilter();
		long startTime;
		long endTime;
		
		PFController.maxParticleNo = maxParticleNo;
		PFController.degeneracyLimit = degeneracyLimit;
		
		floorPlan = new FloorPlan(floorPlanPath);
			
		if (randomFilePath != null) try {
			PFRandom.startInstanceWithFile(randomFilePath);
		} catch (PFRandomInstanceAlreadyExistsException e1) {
			System.out.println("could not instantiate PFRandom from file");
		}
		
		if (stepVectorFilePath != null) try {
			StepVectorGenerator.startGeneratorFromFile("1KRight.csv");
		} catch (StepVectorGeneratorInstanceAlreadyExistsException e1) {
			System.out.println("could not instantiate StepVectorGenerator from file");
		}
		
		showMemory();
		startTime = System.currentTimeMillis();
		initWithParticleNo(storeType,initialParticleNo);
		endTime = System.currentTimeMillis();
		System.out.println("init took " + (endTime - startTime) + "ms");
		showMemory();
		
		visualiser = new PFVisualiser(particleStore, floorPlan);
		visualiser.update(false);
	}
	
	public static void main(String[] args) {
		
		// args are: floor plan path; object/array for store type; initialParticleNo; maxParticleNo; degeneracyLimit; 0 if running clean, 1 if next argument is randomfile, 2 if next argument is svfile, 3 if next arguments are randomfile svfile
		
		long startTime;
		long endTime;
		
		// get setup options
		
		String floorPlanPath = args[0];
		String storeTypeOption = args[1];
		int initialParticleNo = Integer.parseInt(args[2]);
		int _maxParticleNo = Integer.parseInt(args[3]);
		int _degeneracyLimit = Integer.parseInt(args[4]);
		int seedingOption = Integer.parseInt(args[5]);
		String randomFilePath = null, stepVectorFilePath = null;
		
		ParticleStoreType storeType = ParticleStoreType.OBJECT;
		if (storeTypeOption.equals("object")) {
			storeType = ParticleStoreType.OBJECT;
		}
		if (storeTypeOption.equals("array")) {
			storeType = ParticleStoreType.ARRAY;
		}
		
		switch (seedingOption) {
		case 1:
			randomFilePath = args[6];
			break;
		case 2:
			stepVectorFilePath = args[6];
			break;
		case 3:
			randomFilePath = args[6];
			stepVectorFilePath = args[7];
			break;
		default:
			break;
		}
		
		setupFilter(floorPlanPath, storeType, initialParticleNo, _maxParticleNo, _degeneracyLimit, randomFilePath, stepVectorFilePath);
		
		StepVector nextStep;
		StepVectorGenerator stepGen = StepVectorGenerator.getInstance();
		scan = new Scanner(System.in);
		
		while (scan.nextLine() != null) {
			nextStep = stepGen.next();
			startTime = System.currentTimeMillis();
			propagate(nextStep);
			endTime = System.currentTimeMillis();
			System.out.println("propagate took " + (endTime - startTime) + "ms");
			showMemory();
			visualiser.update(true);
			if (scan.nextLine() != null) {
				visualiser.update(false);
			}
			boolean degeneracyClose = activeParticles <= degeneracyLimit;
			if (degeneracyClose) try {
				startTime = System.currentTimeMillis();
				resample();
				endTime = System.currentTimeMillis();
				System.out.println("resample took " + (endTime - startTime) + "ms");
				showMemory();
			} catch (ParticleNotFoundException e) {
				System.err.println(e.getMessage());
			}
		}
		
	}

}
