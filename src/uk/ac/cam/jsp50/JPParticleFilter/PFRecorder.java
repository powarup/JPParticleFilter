package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.HashSet;

import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public class PFRecorder {

	public enum PFRecordingType {
		PROPAGATE,RESAMPLE,INIT;
	}
	
	public class PFRecording {
		public PFRecordingType type;
		public Position position;
		public long startTime, endTime; // times in ms
		public long startMemory, endMemory;
		public int startParticleNo, endParticleNo;

		public String summary() {
			String summary = "";
			switch (type) {
			case PROPAGATE:
				summary += "Propagated " + startParticleNo + " particles to " + endParticleNo + ", ";
				break;
			case RESAMPLE:
				summary += "Resampled " + startParticleNo + " particles to " + endParticleNo + ", ";
				break;
			case INIT:
				summary += "Initialised " + endParticleNo + " particles, ";
				break;

			default:
				break;
			}
			summary += "took " + getDuration() + "ms,\n";
			summary += "started with " + (startMemory/(1024*1024)) + "MB, ended with " + (endMemory/(1024*1024)) + "MB";
			if (position != null) summary += ", position is (" + position.x + "," + position.y + ") with stdev " + position.stdev;
			return summary;
		}

		public long getDuration() {
			return endTime - startTime;
		}

		public String fullDetails() {
			String details = "";
			switch (type) {
			case PROPAGATE:
				details += "Propagate";
				break;
			case RESAMPLE:
				details += "Resample";
				break;
			case INIT:
				details += "Init";
				break;

			default:
				break;
			}
			details += "\nStart particle #: " + startParticleNo;
			details += "\nEnd particle #: " + endParticleNo;
			details += "\nStart time: " + startTime;
			details += "\nEnd time: " + endTime;
			details += "\nStart heap size (bytes): " + startMemory;
			details += "\nEnd heap size (bytes): " + endMemory;
			return details;
		}
	}

	public class Position {
		double x,y,stdev;

		public Position(double x, double y, double stdev) {
			this.x = x;
			this.y = y;
			this.stdev = stdev;
		}
	}

	public final boolean collectingMemoryStatistics, collectingTimeStatistics, collectingSteps, collectingPosition;
	public final PFBacktrackingScheme backtrackingScheme;
	public final int maxRecordingNo;

	private int currentGeneration = 1;
	private int totalSteps = 0;

	public PFRecording[] recordings;
	private HashSet<Step> currentSteps;
	public HashSet<Step> lastSteps;

	public boolean recordingPropagate = false, recordingResample = false, recordingInit = false;
	public int currentRecordingIndex = 0; // points to current recording, or if no recording is in progress, next available

	public PFRecorder(boolean collectingMemoryStatistics, boolean collectingTimeStatistics, boolean collectingSteps, boolean collectingPosition, PFBacktrackingScheme backtrackingScheme, int maxRecordingNo, String randomFilePath, String stepVectorFilePath) {
		this.collectingMemoryStatistics = collectingMemoryStatistics;
		this.collectingTimeStatistics = collectingTimeStatistics;
		this.collectingSteps = collectingSteps;
		this.collectingPosition = collectingPosition;
		this.maxRecordingNo = maxRecordingNo;
		this.backtrackingScheme = backtrackingScheme;

		if (collectingMemoryStatistics || collectingTimeStatistics) { // initialise recording array so recording occurs in place
			recordings = new PFRecording[maxRecordingNo];
			for (int i = 0; i < maxRecordingNo; i++) {
				recordings[i] = new PFRecording();
			}
		}

		if (collectingSteps) {
			currentSteps = new HashSet<Step>();
		}
	}

	// basic statistics getters

	public Position getPosition() {
		ParticleManager particleManager = getParticles().getParticleManager();
		double x = 0;
		double y = 0;
		double w;
		double variance = 0;
		int numberOfParticles = getActiveParticleNo();
		
		while (particleManager.hasNextActiveParticle()) try {
			particleManager.nextActiveParticle();
			w = particleManager.getWeight();
			x += particleManager.getX() * w;
			y += particleManager.getY() * w;
		} catch (ParticleNotFoundException e) {
			e.printStackTrace();
		}
		double totalWeight = getParticles().getTotalWeight();
		x /= totalWeight;
		y /= totalWeight;
		
		//calculate stdev
		double deltax, deltay, delta;
		particleManager = getParticles().getParticleManager();
		while (particleManager.hasNextActiveParticle()) try {
			particleManager.nextActiveParticle();
			deltax = x - particleManager.getX();
			deltay = y - particleManager.getY();
			delta = deltax*deltax + deltay*deltay;
			variance += delta;
		} catch (ParticleNotFoundException e) {
			e.printStackTrace();
		}
		
		variance /= numberOfParticles;
		double stdev = Math.sqrt(variance);
		System.out.println("position estimate from " + numberOfParticles + " particles:" + x + "," + y + " : " + stdev);

		return new Position(x, y, stdev);
	}
	
	public int getActiveParticleNo() {
		switch (backtrackingScheme) {
		case NONE:
			return PFController.activeParticles;
		case NAIVE:
			return PFNaiveBacktrackingController.activeParticles;
		case STRINGY:
			return PFStringyParticleBacktrackingController.activeParticles;
		case ACTIVE_PRUNING:
			return PFActivePruningBacktrackingController.activeParticles;
		default:
			return PFController.activeParticles;
		}
	}

	public int getMaxParticleNo() {
		switch (backtrackingScheme) {
		case NONE:
			return PFController.maxParticleNo;
		case NAIVE:
			return PFNaiveBacktrackingController.maxParticleNo;
		case STRINGY:
			return PFStringyParticleBacktrackingController.maxParticleNo;
		case ACTIVE_PRUNING:
			return PFActivePruningBacktrackingController.maxParticleNo;
		default:
			return PFController.maxParticleNo;
		}
	}

	public int getDegeneracyLimit() {
		switch (backtrackingScheme) {
		case NONE:
			return PFController.degeneracyLimit;
		case NAIVE:
			return PFNaiveBacktrackingController.degeneracyLimit;
		case STRINGY:
			return PFStringyParticleBacktrackingController.degeneracyLimit;
		case ACTIVE_PRUNING:
			return PFActivePruningBacktrackingController.degeneracyLimit;
		default:
			return PFController.degeneracyLimit;
		}
	}

	public ParticleStore getParticles() {
		switch (backtrackingScheme) {
		case NONE:
			return PFController.particleStore;
		case NAIVE:
			return PFNaiveBacktrackingController.particleStore;
		case STRINGY:
			return PFStringyParticleBacktrackingController.particleStore;
		case ACTIVE_PRUNING:
			return PFActivePruningBacktrackingController.particleStore;
		default:
			return PFController.particleStore;
		}
	}

	// Statistics recording methods
	// endRecordingPropagate MUST be called when collecting steps, to keep lastSteps in line with most recent generation

	public void startRecordingPropagate() {
		if (collectingMemoryStatistics || collectingTimeStatistics) {
			if (recordingPropagate) endRecordingPropagate();
			if (recordingResample) endRecordingResample();
			recordingPropagate = true;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.type = PFRecordingType.PROPAGATE;
			currentRecording.startParticleNo = getActiveParticleNo();
			if (collectingMemoryStatistics) currentRecording.startMemory = measureMemory();
			if (collectingTimeStatistics) currentRecording.startTime = System.currentTimeMillis();
		}
	}

	public void endRecordingPropagate() {
		if (collectingSteps) {
			lastSteps = currentSteps;
			currentSteps = new HashSet<Step>();
		}

		if (recordingPropagate) {
			recordingPropagate = false;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.endParticleNo = PFController.activeParticles;
			if (collectingTimeStatistics) currentRecording.endTime = System.currentTimeMillis();
			if (collectingMemoryStatistics) currentRecording.endMemory = measureMemory();
			if (collectingPosition) currentRecording.position = getPosition();
			incrementCurrentRecordingIndex();
		}

		totalSteps++;
	}

	public void startRecordingResample() {
		if (collectingMemoryStatistics || collectingTimeStatistics) {
			if (recordingPropagate) endRecordingPropagate();
			if (recordingResample) endRecordingResample();
			recordingResample = true;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.type = PFRecordingType.RESAMPLE;
			currentRecording.startParticleNo = getActiveParticleNo();
			if (collectingMemoryStatistics) currentRecording.startMemory = measureMemory();
			if (collectingTimeStatistics) currentRecording.startTime = System.currentTimeMillis();
		}
	}

	public void endRecordingResample() {
		if (recordingResample) {
			recordingResample = false;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.endParticleNo = PFController.activeParticles;
			if (collectingTimeStatistics) currentRecording.endTime = System.currentTimeMillis();
			if (collectingMemoryStatistics) currentRecording.endMemory = measureMemory();
			incrementCurrentRecordingIndex();
		}

		currentGeneration++;
	}
	
	public void startRecordingInit() {
		if (collectingMemoryStatistics || collectingTimeStatistics) {
			if (recordingPropagate) endRecordingPropagate();
			if (recordingResample) endRecordingResample();
			recordingInit = true;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.type = PFRecordingType.INIT;
			currentRecording.startParticleNo = getActiveParticleNo();
			if (collectingMemoryStatistics) currentRecording.startMemory = measureMemory();
			if (collectingTimeStatistics) currentRecording.startTime = System.currentTimeMillis();
		}
	}

	public void endRecordingInit() {
		if (recordingInit) {
			recordingInit = false;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.endParticleNo = PFController.activeParticles;
			if (collectingTimeStatistics) currentRecording.endTime = System.currentTimeMillis();
			if (collectingMemoryStatistics) currentRecording.endMemory = measureMemory();
			incrementCurrentRecordingIndex();
		}

		currentGeneration++;
	}

	public void incrementCurrentRecordingIndex() {
		currentRecordingIndex++;
		if (currentRecordingIndex == maxRecordingNo) {
			System.err.println("Out of space for recordings");
			System.exit(0);
		}
	}

	public long measureMemory() {
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		//runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		return memory;
	}

	// Step recording methods

	public void addStep(double x1, double y1, double x2, double y2, boolean violation) {
		if (collectingSteps) {
			currentSteps.add(new Step(x1, y1, x2, y2,violation));
		}
	}



}
