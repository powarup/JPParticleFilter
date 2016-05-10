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
		if (args.length > 2 && args[2] != null) {
			int n = Integer.parseInt(args[2]);
			String path;
			for (int i = 1; i <= n; i++) {
				path = args[1] + String.valueOf(i) + ".txt";
				System.out.println(path);
				writeRandomsToFile(Integer.parseInt(args[0]), path);
			}
		} else {
			writeRandomsToFile(Integer.parseInt(args[0]), args[1]);
		}
	}
	
}