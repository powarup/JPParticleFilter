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
	public static ParticleStore particleStore;
	public static final int maxParticleNo = 300;
	public static final int degeneracyLimit = 30;
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
			System.out.println("Making " + particleNo + " particles. Current #active particles: " + activeParticles);
			x = randomGenerator.nextDouble() * floorPlan.maxX;
			y = randomGenerator.nextDouble() * floorPlan.maxY;
						
			if (floorPlan.pointIsInsideRoom(x, y)) {
				particleStore.addParticle(x,y,weight);
				activeParticles++;
			}
		}
		System.out.println("initialised with " + particleNo + " particles");
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
		
		ParticleStore newParticles = particleStore.getFreshParticleStoreInstance();
		
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
	
	public static void main(String[] args) {
		
		long startTime;
		long endTime;
		
		// argument taken is location of floor plan csv, initialise floorPlan data structure
		String floorPlanPath = args[0];
		floorPlan = new FloorPlan(floorPlanPath);
		
		try {
			PFRandom.startInstanceWithFile("1MRandoms.txt");
		} catch (PFRandomInstanceAlreadyExistsException e1) {
			System.out.println("could not instantiate PFRandom from file");
		}
		
		showMemory();
		startTime = System.currentTimeMillis();
		initWithParticleNo(ParticleStoreType.OBJECT,1);
		endTime = System.currentTimeMillis();
		System.out.println("init took " + (endTime - startTime) + "ms");
		showMemory();
		
		StepVectorGenerator stepGen;
		try {
			stepGen = StepVectorGenerator.startGeneratorFromFile("1KRight.csv");
		} catch (StepVectorGeneratorInstanceAlreadyExistsException e1) {
			System.out.println("could not instantiate StepVectorGenerator from file");
			stepGen = StepVectorGenerator.getInstance();
		}
		
		StepVector nextStep;
		scan = new Scanner(System.in);
		
		visualiser = new PFVisualiser(particleStore, floorPlan);
		visualiser.update(false);
		
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
