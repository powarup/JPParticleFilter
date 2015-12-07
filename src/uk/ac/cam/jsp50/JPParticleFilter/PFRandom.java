package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
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
	public static int cacheSize = 1000;
	public static int cacheLimit = 10;
	private BufferedReader br;
	
	private static PFRandom singleton;
	
	protected PFRandom() {
		randomiser = new Random();
	}
	
	protected PFRandom(String filePath) {
		usingFile = true;
		doubles = new LinkedList<Double>();
		System.out.println("PFRandom:: loading random numbers from " + filePath);
		try {
			br = new BufferedReader(new FileReader(filePath));
			loadCache();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void loadCache() {
		System.out.println("PFRandom:: filling cache");
		String line = "";
		try {
			while ((line = br.readLine()) != null && doubles.size() < cacheSize) {
				doubles.add(Double.parseDouble(line));
			}
			if (line == null) {
				System.err.println("PFRandom:: run out of random numbers");
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println("PFRandom:: exception");
			System.err.println(e.getMessage());
		}
	}
	
	public PFRandom(long seed) {
		randomiser = new Random(seed);
	}

	public static PFRandom getInstance() {
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
		if (doubles.size() <= cacheLimit) loadCache();
		if (!usingFile) {
			return randomiser.nextDouble();
		} else {
			return doubles.poll();
		}
	}
	private double nextNextGaussian;
	private boolean haveNextNextGaussian = false;

	public double nextGaussian() {
		if (haveNextNextGaussian) {
			haveNextNextGaussian = false;
			return nextNextGaussian;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
				v2 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
			nextNextGaussian = v2 * multiplier;
			haveNextNextGaussian = true;
			return v1 * multiplier;
		}
	}
}
