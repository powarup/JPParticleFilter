package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

public class PFRandomFile implements PFRandom {
	
	public Queue<Double> doubles;
	
	public PFRandomFile(String pathToFile) {
		System.out.println("loading random numbers from " + pathToFile);
		BufferedReader br;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(pathToFile));
			while ((line = br.readLine()) != null) {
				doubles.add(Double.parseDouble(line));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public double nextDouble() {
		return doubles.poll();
	}

}
