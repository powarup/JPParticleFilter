package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
	
	public static PFFloorPlan floorPlan;
	public static ParticleStore particleStore, inactiveStore;
	public static int maxParticleNo;
	public static int degeneracyLimit;
	public static int activeParticles = 0;
	
	public static PFVisualiser visualiser;
	public static PFRecorder recorder;
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
						
			if (floorPlan.pointIsInsidePlan(x, y)) {
				particleStore.addParticle(x,y,weight);
				activeParticles++;
			}
		}
		System.out.println("initialised with " + activeParticles + " particles");
	}
	
	public static void propagate(StepVector s) {
		System.out.println("Propagating " + activeParticles + " particles from step vector " + s.length + "," + s.angle);
		recorder.startRecordingPropagate();
		
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
			
			
			boolean crossesBoundary = floorPlan.doesCrossBoundary(lastx,lasty,currentx,currenty);
			if (crossesBoundary) {
				particleManager.setWeight(0.0);
				activeParticles--;
				recorder.addStep(lastx, lasty, currentx, currenty, true);
			} else {
				recorder.addStep(lastx, lasty, currentx, currenty, false);
			}
		} catch (ParticleNotFoundException e) {
			System.out.print(e.getMessage());
		}
		
		recorder.endRecordingPropagate();
	}
	
	public static void resample() throws ParticleNotFoundException {
		/* Multinomial resampling
		 * normalise weights
		 * generate N U(0,1) values
		 * sort values
		 * iterate over particles and populate new
		 */
		recorder.startRecordingResample();
		
		ParticleManager particleManager = particleStore.getParticleManager();
		double totalWeight = particleStore.getTotalWeight();
		
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
		
		// iterate over random numbers
		// if weight less than current total weight seen, generate new particle
		// else get new particle, update total weight, and try again
		
		double newWeight = 1.0 / maxParticleNo;
		System.out.println("total weight by particleStore: " + totalWeight + "\nnew weights: " + newWeight);
		
		double scaledRandom;
		
		for (int j = 0; j < maxParticleNo; j++) {
			
			scaledRandom = randomN[j] * totalWeight;
			
			while (scaledRandom > weightTotal) {
				try {
					particleManager.nextActiveParticle();
					weightTotal += particleManager.getWeight();
				} catch (ParticleNotFoundException e) {
					System.out.println("no particle found, at weight " + weightTotal + ", total weight of active particles is " + totalWeight + " and current random number is " + randomN[j] + ", assuming is floating point error, so using last particle, particle to be generated is #" + (j+1));
				}
			}
			
			//System.out.println("sampling from particle #" + particleManager.currentIndex() + " " + particleManager.summary() + " using random number " + randomN[j] + " scaled to " + scaledRandom);
			double x = particleManager.getX();
			double y = particleManager.getY();
			newParticles.addParticle(x,y,newWeight);
		}
		
		inactiveStore = particleStore;
		inactiveStore.cleanForReuse();
		particleStore = newParticles;
		activeParticles = particleStore.getParticleNo();
		
		recorder.endRecordingResample();
		
	}
	
	private static void resetFilter() {
		floorPlan = null;
		particleStore = null;
		inactiveStore = null;
		visualiser = null;
		recorder = null;
		scan = null;
		maxParticleNo = 0;
		degeneracyLimit = 0;
		activeParticles = 0;
		PFRandom.clearInstance();
		StepVectorGenerator.clearInstance();
	}
	
	public static void setupFilter(InputStream floorPlanStream, ParticleStoreType storeType, int initialParticleNo, int maxParticleNo, int degeneracyLimit, String randomFilePath, String stepVectorFilePath, boolean visualise) {
		resetFilter();
		long startTime;
		long endTime;
		
		recorder = new PFRecorder(true, true, true, 1000, storeType, randomFilePath, stepVectorFilePath);
		
		PFController.maxParticleNo = maxParticleNo;
		PFController.degeneracyLimit = degeneracyLimit;
		
		floorPlan = new PFNaiveFloorPlan(floorPlanStream);
			
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
		
		startTime = System.currentTimeMillis();
		initWithParticleNo(storeType,initialParticleNo);
		endTime = System.currentTimeMillis();
		System.out.println("init took " + (endTime - startTime) + "ms");
		
		if (visualise) {
			visualiser = new PFVisualiser(floorPlan);
			visualiser.update(false);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		// args are: floor plan path; object/array for store type; initialParticleNo; maxParticleNo; degeneracyLimit; 0 if running clean, 1 if next argument is randomfile, 2 if next argument is svfile, 3 if next arguments are randomfile svfile
		
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
		
		InputStream floorPlanStream = new FileInputStream(floorPlanPath);
		
		setupFilter(floorPlanStream, storeType, initialParticleNo, _maxParticleNo, _degeneracyLimit, randomFilePath, stepVectorFilePath, true);
		
		StepVector nextStep;
		StepVectorGenerator stepGen = StepVectorGenerator.getInstance();
		scan = new Scanner(System.in);
		
		while (scan.nextLine() != null) {
			nextStep = stepGen.next();
			propagate(nextStep);
			System.out.println(recorder.recordings[recorder.currentRecordingIndex-1].summary());
			visualiser.update(true);
			if (scan.nextLine() != null) {
				visualiser.update(false);
			}
			boolean degeneracyClose = activeParticles <= degeneracyLimit;
			if (degeneracyClose) try {
				resample();
				System.out.println(recorder.recordings[recorder.currentRecordingIndex-1].summary());
			} catch (ParticleNotFoundException e) {
				System.err.println(e.getMessage());
			}
		}
		
	}

}
