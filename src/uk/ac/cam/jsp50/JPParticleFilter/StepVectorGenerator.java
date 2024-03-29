package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class StepVectorGenerator {
	
	public static class StepVectorGeneratorInstanceAlreadyExistsException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1331223271030883005L;
		
	}
	
	public static final double fixedLength = 0.75;
	public static final double fixedLengthNoise = 0.075;
	public static final double fixedAngleNoise = 7.5;
	
	private boolean usingFile = false;
	private Queue<StepVector> steps;
	public int cacheSize = 1000;
	public int cacheLimit = 10;
	private BufferedReader br;
	
	public PFRandom randomGenerator;
	public static StepVectorGenerator singleton;
	
	// Singleton methods
	
	public static void clearInstance() {
		singleton = null;
	}
	
	public static StepVectorGenerator getInstance() {
		if (singleton == null) {
			singleton = new StepVectorGenerator();
		}
		
		return singleton;
	}
	
	public static StepVectorGenerator startGeneratorFromFile(String pathToFile) throws StepVectorGeneratorInstanceAlreadyExistsException {
		if (singleton == null) {
			singleton = new StepVectorGenerator(pathToFile);
		} else throw new StepVectorGeneratorInstanceAlreadyExistsException();
		
		return singleton;
	}
	
	public static StepVectorGenerator startGeneratorFromInputStream(InputStream inputStream) throws StepVectorGeneratorInstanceAlreadyExistsException {
		if (singleton == null) {
			singleton = new StepVectorGenerator(inputStream);
		} else throw new StepVectorGeneratorInstanceAlreadyExistsException();
		
		return singleton;
	}
	
	// Constructors
	
	protected StepVectorGenerator() {
		if (randomGenerator == null) {
			randomGenerator = PFRandom.getInstance();
		}
	}
	
	protected StepVectorGenerator(String filePath) {
		if (randomGenerator == null) {
			randomGenerator = PFRandom.getInstance();
		}
		
		usingFile = true;
		steps = new LinkedList<StepVector>();
		System.out.println("StepVectorGenerator:: loading steps from " + filePath);
		try {
			br = new BufferedReader(new FileReader(filePath));
			loadCache();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	protected StepVectorGenerator(InputStream inputStream) {
		if (randomGenerator == null) {
			randomGenerator = PFRandom.getInstance();
		}
		
		usingFile = true;
		steps = new LinkedList<StepVector>();
		System.out.println("StepVectorGenerator:: loading steps from input stream");
		br = new BufferedReader(new InputStreamReader(inputStream));
		loadCache();
	}
	
	// object methods
	
	private void loadCache() {
		System.out.println("StepVectorGenerator:: filling cache");
		String line = "";
		String splitComma = ",";
		String[] step;
		try {
			while ((line = br.readLine()) != null && steps.size() < cacheSize) {
				step = line.split(splitComma);
				double length = Double.parseDouble(step[0]);
				double angle = Double.parseDouble(step[1]);
				steps.add(new StepVector(length, angle, fixedLengthNoise, fixedAngleNoise));
				}
			if (line == null) {
				System.err.println("StepVectorGenerator:: run out of steps, " + steps.size() + " left in cache");
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println("StepVectorGenerator:: exception");
			System.err.println(e.getMessage());
		}
	}
	
	private double generateLength() {
		return fixedLength;
	}
	
	private double generateAngle() {
		double angle = randomGenerator.nextDouble() * 360;
		if (angle == 360.0) angle = 0.0;
		return angle;
	}
	
	public StepVector next() {
		if (usingFile) return nextFromFile();
		else {
			double len = this.generateLength();
			double angle = this.generateAngle();
			StepVector nextStep = new StepVector(len,angle,fixedLengthNoise,fixedAngleNoise);
			return nextStep;
		}
	}
	
	private StepVector nextFromFile() {
		if (steps.size() <= cacheLimit) loadCache();
		return steps.poll();
	}
	
}
