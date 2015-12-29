package uk.ac.cam.jsp50.JPParticleFilter;

public class StepVector {
	
	public final double length;
	public final double angle;
	public final double max_delta_l;
	public final double delta_theta_stdev;
	
	public StepVector(double _length, double _angle, double _max_delta_l, double _delta_theta_stdev) {
		this.length = _length;
		this.angle = _angle;
		this.max_delta_l = _max_delta_l;
		this.delta_theta_stdev = _delta_theta_stdev;
	}
	
	public StepVector addNoise(PFRandom r) {
		double noiseLength = getNoiseLength(r);
		double noiseAngle = getNoiseAngle(r);
		StepVector noisyVector = new StepVector(this.length + noiseLength, this.angle + noiseAngle, max_delta_l, delta_theta_stdev);
		return noisyVector;
	}
	
	private double getNoiseLength(PFRandom r) {
		double noiseLength = r.nextGaussian() * max_delta_l;
		return noiseLength;
	}
	
	private double getNoiseAngle(PFRandom r) {
		double noiseAngle = r.nextGaussian() * delta_theta_stdev;
		return noiseAngle;
	}
	
	public String getDescription() {
		return (this.length + "," + this.angle);
	}
	
}
