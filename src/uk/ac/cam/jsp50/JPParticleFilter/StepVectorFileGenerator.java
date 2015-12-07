package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class StepVectorFileGenerator {

	public static void writeStepsToFile(int n, String pathToFile) throws FileNotFoundException, UnsupportedEncodingException {
		StepVectorGenerator generator = StepVectorGenerator.getInstance();
		PrintWriter writer = new PrintWriter(pathToFile, "UTF-8");
		StepVector step;
		for (int i = 0; i < n; i++) {
			step = generator.next();
			writer.println(step.getDescription());
		}
		writer.close();
	}
	
	public static void main(String[] args) throws NumberFormatException, FileNotFoundException, UnsupportedEncodingException {
		writeStepsToFile(Integer.parseInt(args[0]), args[1]);
	}

}
