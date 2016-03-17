package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import uk.ac.cam.jsp50.JPParticleFilter.NaiveBacktrackingParticleStore.NaiveBacktrackingParticle;
import uk.ac.cam.jsp50.JPParticleFilter.NaiveBacktrackingParticleStore.NaiveBacktrackingParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.PFRandom.PFRandomInstanceAlreadyExistsException;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;
import uk.ac.cam.jsp50.JPParticleFilter.StepVectorGenerator.StepVectorGeneratorInstanceAlreadyExistsException;

public class PFNaiveBacktrackingController extends PFController {
	public static NaiveBacktrackingParticleStore particleStore;
	private static final boolean stepPruning = true;
	
	// ========== INITIALISATION ======================================================
	
	public static void init() {
		initWithParticleNo(maxParticleNo);
	}
	
	public static void initWithParticleNo(int particleNo) {
		System.out.println("initialising");
		particleStore = new NaiveBacktrackingParticleStore();
		
		double x,y;
		PFRandom randomGenerator = PFRandom.getInstance();
		
		double weight = 1.0/particleNo;
		while (activeParticles < particleNo) {
			x = randomGenerator.nextDouble() * floorPlan.maxX;
			y = randomGenerator.nextDouble() * floorPlan.maxY;
						
			if (floorPlan.pointIsInsidePlan(x, y)) {
				particleStore.addParticle(x,y,weight);
				activeParticles++;
			}
		}
		System.out.println("initialised with " + activeParticles + " particles");
	}
	
	public static void reinit() {
		init();
	}
	
	// ========== PROPAGATION ========================================================
	
	public static void propagate(StepVector s) {
		//System.out.println("Propagating " + activeParticles + " particles from step vector " + s.length + "," + s.angle);
		recorder.startRecordingPropagate();
		
		PFRandom randomGenerator = PFRandom.getInstance();
		
		NaiveBacktrackingParticleManager particleManager = (NaiveBacktrackingParticleManager)particleStore.getParticleManager();
		particleStore.nextGeneration();
		
		double lastx, lasty, currentx, currenty;
		
		while (particleManager.hasNextActiveParticle()) try {
			
			particleManager.nextActiveParticle();
			NaiveBacktrackingParticle newParticle = particleManager.getParticle().getChild();
			lastx = newParticle.x;
			lasty = newParticle.y;
			newParticle.displace(s.addNoise(randomGenerator));
			currentx = newParticle.x;
			currenty = newParticle.y;
			
			PFCrossing crossesBoundary = floorPlan.findCrossing(lastx,lasty,currentx,currenty);
			
			switch (crossesBoundary) {
			case NONE:
				recorder.addStep(lastx, lasty, currentx, currenty, false);
				particleStore.addParticle(newParticle);
				break;
			case CROSSING:
				newParticle.w = 0;
				activeParticles--;
				recorder.addStep(lastx, lasty, currentx, currenty, true);
				if (!stepPruning) particleStore.addParticle(newParticle);
				break;
			case DOOR:
				double newWeight = particleManager.getWeight() * doorModifier;
				newParticle.w = newWeight;
				recorder.addStep(lastx, lasty, currentx, currenty, false);
				particleStore.addParticle(newParticle);
				break;
			default:
				break;
			}
			
		} catch (ParticleNotFoundException e) {
			System.out.print(e.getMessage());
		}
		
		recorder.endRecordingPropagate();
		
		if (activeParticles <=0) reinit();
	}
	
	// ========== RESAMPLING =========================================================
	
	public static void resample() throws ParticleNotFoundException {
		/* Multinomial resampling
		 * normalise weights
		 * generate N U(0,1) values
		 * sort values
		 * iterate over particles and populate new
		 */
		recorder.startRecordingResample();
		
		NaiveBacktrackingParticleManager particleManager = (NaiveBacktrackingParticleManager)particleStore.getParticleManager();
		double totalWeight = particleStore.getTotalWeight();
		
		double[] randomN = new double[maxParticleNo];
		PFRandom randomiser = PFRandom.getInstance();
		for (int i = 0; i < maxParticleNo; i++) {
			randomN[i] = randomiser.nextDouble();
		}
		
		Arrays.sort(randomN);
				
		particleManager = (NaiveBacktrackingParticleManager)particleStore.getParticleManager();
		particleManager.nextActiveParticle();
		particleStore.nextGeneration();
		double weightTotal = particleManager.getWeight();
		
		// iterate over random numbers
		// if weight less than current total weight seen, generate new particle
		// else get new particle, update total weight, and try again
		
		double newWeight = 1.0 / maxParticleNo;
		//System.out.println("total weight by particleStore: " + totalWeight + "\nnew weights: " + newWeight);
		
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
			NaiveBacktrackingParticle newParticle = particleManager.getParticle().getChild();
			newParticle.w = newWeight;
			particleStore.addParticle(newParticle);
		}
		
		activeParticles = particleStore.getParticleNo();
		
		recorder.endRecordingResample();
		
	}
	
	// ========== RUNNING ===========================================================
	
	public static void resetFilter() {
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

	public static void setupFilter(InputStream floorPlanStream, FloorPlanType fpType, int initialParticleNo, int maxParticleNo, int degeneracyLimit, String randomFilePath, String stepVectorFilePath, boolean visualise, boolean recorderShouldCollectMemoryStats, boolean recorderShouldCollectTimeStats, boolean recorderShouldCollectSteps, boolean recorderShouldTrackPosition) {
		resetFilter();
		long startTime;
		long endTime;
		boolean backtracking = true;
		
		recorder = new PFRecorder(recorderShouldCollectMemoryStats, recorderShouldCollectTimeStats, recorderShouldCollectSteps, recorderShouldTrackPosition, backtracking, 1000, randomFilePath, stepVectorFilePath);
		
		PFController.maxParticleNo = maxParticleNo;
		PFController.degeneracyLimit = degeneracyLimit;
		
		switch (fpType) {
		case NAIVE:
			floorPlan = new PFNaiveFloorPlan(floorPlanStream);
			break;

		case SIMPLE_BITMAP:
			floorPlan = new PFBitmapFloorPlan(floorPlanStream,0.05);
			break;
			
		case COMPLEX_BITMAP:
			floorPlan = new PFComplexBitmapFloorPlan(floorPlanStream,0.05);
			break;
			
		default:
			floorPlan = new PFNaiveFloorPlan(floorPlanStream);
			break;
		}
			
		if (randomFilePath != null) try {
			PFRandom.startInstanceWithFile(randomFilePath);
		} catch (PFRandomInstanceAlreadyExistsException e1) {
			System.out.println("could not instantiate PFRandom from file");
		}
		
		if (stepVectorFilePath != null) try {
			StepVectorGenerator.startGeneratorFromFile(stepVectorFilePath);
		} catch (StepVectorGeneratorInstanceAlreadyExistsException e1) {
			System.out.println("could not instantiate StepVectorGenerator from file");
		}
		
		startTime = System.currentTimeMillis();
		initWithParticleNo(initialParticleNo);
		endTime = System.currentTimeMillis();
		System.out.println("init took " + (endTime - startTime) + "ms");
		
		if (visualise) {
			try {
				visualiser = new PFVisualiser(floorPlan, false);
				visualiser.update(false);
			} catch (IOException e) {
				e.printStackTrace();
				visualise = false;
			}
		}
	}
	
public static void main(String[] args) throws FileNotFoundException {
		
		// args are: floor plan path; object/array for store type; initialParticleNo; maxParticleNo; degeneracyLimit; 0 if running clean, 1 if next argument is randomfile, 2 if next argument is svfile, 3 if next arguments are randomfile svfile
		
		// get setup options
		
		String floorPlanPath = args[0];
		int initialParticleNo = Integer.parseInt(args[1]);
		int _maxParticleNo = Integer.parseInt(args[2]);
		int _degeneracyLimit = Integer.parseInt(args[3]);
		int seedingOption = Integer.parseInt(args[4]);
		String randomFilePath = null, stepVectorFilePath = null;
		
		switch (seedingOption) {
		case 1:
			randomFilePath = args[5];
			break;
		case 2:
			stepVectorFilePath = args[5];
			break;
		case 3:
			randomFilePath = args[5];
			stepVectorFilePath = args[6];
			break;
		default:
			break;
		}
		
		InputStream floorPlanStream = new FileInputStream(floorPlanPath);
		
		setupFilter(floorPlanStream, FloorPlanType.COMPLEX_BITMAP, initialParticleNo, _maxParticleNo, _degeneracyLimit, randomFilePath, stepVectorFilePath, true, true, true, true, true);
		
		StepVector nextStep;
		StepVectorGenerator stepGen = StepVectorGenerator.getInstance();
		scan = new Scanner(System.in);
		
		while (scan.nextLine() != null) {
			if (scan.nextLine().equals("end")) {
				try {
					PFGifGeneratingView gifGeneratingView = (PFGifGeneratingView)visualiser.view;
					gifGeneratingView.endGif();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
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
