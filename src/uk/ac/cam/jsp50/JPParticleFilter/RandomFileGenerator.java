package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RandomFileGenerator {

	public static void writeRandomsToFile(int n, String pathToFile) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(pathToFile, "UTF-8");
		Random randomiser = new Random();
		for (int i = 0; i < n; i++) {
			writer.println(randomiser.nextDouble());
		}
		writer.close();
	}
	
	public static void main(String[] args) throws NumberFormatException, FileNotFoundException, UnsupportedEncodingException {
		writeRandomsToFile(Integer.parseInt(args[0]), args[1]);
	}
	
}
