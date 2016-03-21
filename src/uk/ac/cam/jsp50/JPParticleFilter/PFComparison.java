package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import uk.ac.cam.jsp50.JPParticleFilter.PFRecorder.PFRecording;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public abstract class PFComparison {
	
	public static void runAndExport(int nGenerations, String filePath) throws IOException {
		StepVectorGenerator s = StepVectorGenerator.getInstance();
		for (int i = 0; i < nGenerations; i++) {
			PFController.propagate(s.next());
			if (PFController.activeParticles < PFController.degeneracyLimit)
				try {
					PFController.resample();
				} catch (ParticleNotFoundException e) {
					e.printStackTrace();
				}
		}
		export(filePath, PFController.recorder);
	}
	
	public static void export(String filePath, PFRecorder recorder) throws IOException {
		System.out.println("exporting to " + filePath);
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false));

		for (int j = 0; j < recorder.currentRecordingIndex; j++) {
			PFRecording recording = recorder.recordings[j];
			long timeTaken = recording.endTime - recording.startTime;
			writer.write(String.valueOf(recording.endMemory) + "," + String.valueOf(timeTaken) + "\n");
		}
		
		writer.close();
	}
	
}
