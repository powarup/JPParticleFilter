package uk.ac.cam.jsp50.JPParticleFilter;

public class StepVectorGenerator {
	
	public static final double fixedLength = 0.75;
	
	public static PFRandom randomGenerator;
	
	private double generateLength() {
		return fixedLength;
	}
	
	private double generateAngle() {
		double angle = randomGenerator.nextDouble() * 360;
		if (angle == 360.0) angle = 0.0;
		return angle;
	}
	
	public StepVector next() {
		double len = this.generateLength();
		double angle = this.generateAngle();
		StepVector nextStep = new StepVector(len,angle);
		return nextStep;
	}
	
	public StepVectorGenerator() {
		if (randomGenerator == null) {
			randomGenerator = PFRandom.getInstance();
		}
	}
}
