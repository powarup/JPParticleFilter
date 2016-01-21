package uk.ac.cam.jsp50.JPParticleFilter;

import java.awt.geom.Line2D;

public class PFLine2DEdge extends Edge {
	public PFLine2DEdge(double _x1, double _y1, double _x2, double _y2) {
		super(_x1, _y1, _x2, _y2);
	}

	public boolean crosses(double o_x1, double o_y1, double o_x2, double o_y2) {
		return Line2D.linesIntersect(o_x1, o_y1, o_x2, o_y2, x1, y1, x2, y2);
	}
}