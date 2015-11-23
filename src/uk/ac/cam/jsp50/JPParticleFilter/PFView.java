package uk.ac.cam.jsp50.JPParticleFilter;

import uk.ac.cam.jsp50.JPParticleFilter.FloorPlan.Edge;
import uk.ac.cam.jsp50.JPParticleFilter.PFVisualiser.Step;

public abstract class PFView {

	public static PFView getView() {
		
		return new PFSwingView();
	}

	public abstract void setPFCanvasSize(double maxX, double maxY);

	public abstract void drawStep(Step s);
	
	public abstract void drawViolation(Step s);

	public abstract void drawParticle(double x, double y);

	public abstract void drawFPEdge(Edge e);

	public abstract void clearParticles();
	
	public abstract void redraw();


}
