package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.InputStream;

public class PFNaiveFloorPlan extends PFFloorPlan {
	
	public PFNaiveFloorPlan(InputStream csvStream) {
		this(csvStream, null);
	}
	
	public PFNaiveFloorPlan(InputStream csvStream, EdgeType edgeType) {
		this.edgeType = (edgeType == null) ? EdgeType.LINE2D : edgeType;
		getEdgesFromStream(csvStream);
	}

	public int findEdgeCrossings(double x1, double y1, double x2, double y2) {
		int noCrossings = 0;
		for (Edge e : edges) {
			if (e.crosses(x1, y1, x2, y2)) {
				noCrossings++;
			}
		}
		return noCrossings;
	}
	
	public boolean doesCrossBoundary(double x1, double y1, double x2, double y2) {
		int noCrossings = findEdgeCrossings(x1,y1,x2,y2);
		return (noCrossings > 0);
	}

	public boolean pointIsInsidePlan(double x, double y) { // essentially ray casting algorithm, horizontal ray fixed at y then fixed at x
		
		if (edges.size() <= 2) return true;
		
		boolean isInsideX = false;
		boolean isInsideY = false;
		int noCrossings;
		
		noCrossings = findEdgeCrossings(0, y, x, y);
		if (noCrossings > 0 && (noCrossings % 2) == 1) {
			return true;
		}
		
		return (isInsideX&isInsideY);
	}

}
