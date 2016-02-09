package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileNotFoundException;
import java.io.IOException;

import uk.ac.cam.jsp50.JPParticleFilter.PFRecorder.Step;

public abstract class PFView {

	public static PFView getView() throws FileNotFoundException, IOException {
		//return new PFGifGeneratingView();
		return new PFSwingView();
	}

	public abstract void setPFCanvasSize(double maxX, double maxY);

	public abstract void drawStep(Step s);
	
	public abstract void drawViolation(Step s);

	public abstract void drawParticle(double x, double y);
	
	public abstract void drawPosition(double x, double y, double stdev);
	
	public abstract void drawFPEdge(Edge e);

	public abstract void clearParticles();
	
	public abstract void redraw();


}
