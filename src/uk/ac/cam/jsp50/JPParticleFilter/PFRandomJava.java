package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.Random;

public class PFRandomJava implements PFRandom {
	
	private Random randomiser;
	
	public PFRandomJava() {
		randomiser = new Random();
	}
	
	public PFRandomJava(long seed) {
		randomiser = new Random(seed);
	}
	
	@Override
	public double nextDouble() {
		return randomiser.nextDouble();
	}
	
}
