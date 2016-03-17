package uk.ac.cam.jsp50.JPParticleFilter;

public class Step {
	double x1, y1, x2, y2;
	int age = 0;
	boolean violation = false;

	public Step(double x1, double y1, double x2, double y2, boolean violation, int age) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.violation = violation;
		this.age = age;
	}
	
	public Step(double x1, double y1, double x2, double y2, boolean violation) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.violation = violation;
	}
}