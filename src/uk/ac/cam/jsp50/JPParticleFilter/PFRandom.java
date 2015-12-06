package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.Random;

public class PFRandom {
	
	public static class PFRandomInstanceAlreadyExistsException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6531475697958771134L;
		
	}
	
	private static boolean usingFile = false;
	private static Random randomiser;
	public static Queue<Double> doubles;
	
	private static PFRandom singleton;
	
	protected PFRandom() {
		randomiser = new Random();
	}
	
	protected PFRandom(String filePath) {
		usingFile = true;
		System.out.println("loading random numbers from " + filePath);
		BufferedReader br;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				doubles.add(Double.parseDouble(line));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public PFRandom(long seed) {
		randomiser = new Random(seed);
	}

	public PFRandom getInstance() {
		if (singleton == null) {
			singleton = new PFRandom();
		}
		return singleton;
	}
	
	public static PFRandom startInstanceWithSeed(long seed) throws PFRandomInstanceAlreadyExistsException {
		if (singleton == null) {
			singleton = new PFRandom(seed);
		} else throw new PFRandomInstanceAlreadyExistsException();
		return singleton;
	}
	
	public static PFRandom startInstanceWithFile(String file) throws PFRandomInstanceAlreadyExistsException {
		if (singleton == null) {
			singleton = new PFRandom(file);
		} else throw new PFRandomInstanceAlreadyExistsException();
		return singleton;
	}
	
	public double nextDouble() {
		if (!usingFile) {
			return randomiser.nextDouble();
		} else {
			return doubles.poll();
		}
	}
	
}
