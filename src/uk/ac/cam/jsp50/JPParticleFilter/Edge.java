package uk.ac.cam.jsp50.JPParticleFilter;

public abstract class Edge {
	
	double x1,y1,x2,y2;

	public Edge(double _x1, double _y1, double _x2, double _y2) {
		this.x1 = _x1;
		this.y1 = _y1;
		this.x2 = _x2;
		this.y2 = _y2;
	}

	public String getDescription() {
		return x1 + "," + y1 + " -> " + x2 + "," + y2;
	}

	public boolean crosses(Edge e) {
		return this.crosses(e.x1, e.y1, e.x2, e.y2);
	}
	
	public abstract boolean crosses(double o_x1, double o_y1, double o_x2, double o_y2);
	
}
