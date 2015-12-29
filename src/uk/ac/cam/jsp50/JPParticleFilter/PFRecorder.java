package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.HashSet;

import uk.ac.cam.jsp50.JPParticleFilter.PFController.ParticleStoreType;

public class PFRecorder {

	private enum PFRecordingType {
		PROPAGATE,RESAMPLE;
	}
	
	public class PFRecording {
		public PFRecordingType type;
		public long startTime, endTime; // times in ms
		public long startMemory, endMemory;
		public int startParticleNo, endParticleNo;

		public String summary() {
			String summary = "";
			summary += (type == PFRecordingType.PROPAGATE) ? "Propagated " + startParticleNo + " particles to " + endParticleNo + ", " : "Resampled " + startParticleNo + " particles to " + endParticleNo + ", ";
			summary += "took " + getDuration() + "ms,\n";
			summary += "started with " + (startMemory/(1024*1024)) + "MB, ended with " + (endMemory/(1024*1024)) + "MB";
			return summary;
		}
		
		public long getDuration() {
			return endTime - startTime;
		}
		
		public String fullDetails() {
			String details = "";
			details += (type == PFRecordingType.PROPAGATE) ? "Propagate" : "Resample";
			details += "\nStart particle #: " + startParticleNo;
			details += "\nEnd particle #: " + endParticleNo;
			details += "\nStart time: " + startTime;
			details += "\nEnd time: " + endTime;
			details += "\nStart heap size (bytes): " + startMemory;
			details += "\nEnd heap size (bytes): " + endMemory;
			return details;
		}
	}
	
	public class Step {
		double x1, y1, x2, y2;
		boolean violation;
		public Step(double x1, double y1, double x2, double y2, boolean violation) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.violation = violation;
		}
	}
		
	public final boolean collectingStatistics, collectingSteps;
	public final int maxRecordingNo;
	
	private int currentGeneration = 1;
	private int totalSteps = 0;
	
	public PFRecording[] recordings;
	private HashSet<Step> currentSteps;
	public HashSet<Step> lastSteps;
	
	public boolean recordingPropagate = false, recordingResample = false;
	public int currentRecordingIndex = 0; // points to current recording, or if no recording is in progress, next available
	
	public PFRecorder(boolean collectingStatistics, boolean collectingSteps, int maxRecordingNo, String floorPlanPath, ParticleStoreType storeType, String randomFilePath, String stepVectorFilePath) {
		this.collectingStatistics = collectingStatistics;
		this.collectingSteps = collectingSteps;
		this.maxRecordingNo = maxRecordingNo;
		
		if (collectingStatistics) { // initialise recording array so recording occurs in place
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
	
	public int getActiveParticleNo() {
		return PFController.activeParticles;
	}
	
	public int getMaxParticleNo() {
		return PFController.maxParticleNo;
	}
	
	public int getDegeneracyLimit() {
		return PFController.degeneracyLimit;
	}
	
	public ParticleStore getParticles() {
		return PFController.particleStore;
	}
	
	// need a statistic for spread/accuracy
	// need a position estimate
	
	// Statistics recording methods
	// endRecordingPropagate MUST be called when collecting steps, to keep lastSteps in line with most recent generation
	
	public void startRecordingPropagate() {
		if (collectingStatistics) {
			if (recordingPropagate) endRecordingPropagate();
			if (recordingResample) endRecordingResample();
			recordingPropagate = true;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.type = PFRecordingType.PROPAGATE;
			currentRecording.startParticleNo = getActiveParticleNo();
			currentRecording.startMemory = measureMemory();
			currentRecording.startTime = System.currentTimeMillis();
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
			currentRecording.endTime = System.currentTimeMillis();
			currentRecording.endMemory = measureMemory();
			incrementCurrentRecordingIndex();
		}
		
		totalSteps++;
	}
	
	public void startRecordingResample() {
		if (collectingStatistics) {
			if (recordingPropagate) endRecordingPropagate();
			if (recordingResample) endRecordingResample();
			recordingResample = true;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.type = PFRecordingType.RESAMPLE;
			currentRecording.startParticleNo = getActiveParticleNo();
			currentRecording.startMemory = measureMemory();
			currentRecording.startTime = System.currentTimeMillis();
		}
	}
	
	public void endRecordingResample() {
		if (recordingResample) {
			recordingResample = false;
			PFRecording currentRecording = recordings[currentRecordingIndex];
			currentRecording.endParticleNo = PFController.activeParticles;
			currentRecording.endTime = System.currentTimeMillis();
			currentRecording.endMemory = measureMemory();
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
		runtime.gc();
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