package uk.ac.cam.jsp50.JPParticleFilter;

public abstract class BacktrackingParticle {
	public double x,y,w;
	public int nChildren = 0;
	public BacktrackingParticle parent;

	public void displace(StepVector s) {
		double dx = s.length * Math.sin(Math.toRadians(s.angle));
		double dy = s.length * Math.cos(Math.toRadians(s.angle));;

		this.x += dx;
		this.y += dy;
	}
	
	public abstract BacktrackingParticle generateChild();
}
