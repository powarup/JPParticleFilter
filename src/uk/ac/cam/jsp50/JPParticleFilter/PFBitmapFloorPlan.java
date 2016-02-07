package uk.ac.cam.jsp50.JPParticleFilter;

public abstract class PFBitmapFloorPlan extends PFFloorPlan {
	public double cellSize = 0.1;

	public abstract void buildBitmap();
	
	public abstract void drawLineInBitmap(double x1, double y1, double x2, double y2);
	
	public abstract String bitmapDescription();
	
}
