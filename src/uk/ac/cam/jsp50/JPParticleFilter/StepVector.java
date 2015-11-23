package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.Random;

public class StepVector {
	
	public double length;
	public double angle;
	
	public StepVector (double _length, double _angle) {
		this.length = _length;
		this.angle = _angle;
	}
	
	public StepVector addNoise(Random r) {
		double noiseLength = getNoiseLength(r);
		double noiseAngle = getNoiseAngle(r);
		StepVector noisyVector = new StepVector(this.length + noiseLength, this.angle + noiseAngle);
		return noisyVector;
	}
	
	private double getNoiseLength(Random r) {
		double noiseLength = r.nextGaussian() * 0.075;
		return noiseLength;
	}
	
	private double getNoiseAngle(Random r) {
		double noiseAngle = r.nextGaussian() * 7.5;
		return noiseAngle;
	}
	
}
