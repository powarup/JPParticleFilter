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
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

		PFRecorder recorder = PFController.recorder;
		for (int j = 0; j < recorder.recordings.length && j < recorder.currentRecordingIndex; j++) {
			PFRecording recording = recorder.recordings[j];
			//System.out.println(recording.endMemory);
			writer.write(String.valueOf(recording.endMemory) + "\n");
		}
		
		writer.close();
	}
	
}
