package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//this floorplan data structure handles line intersection by checking the line input against all individual edges.
//this is naive and terrible
//can be achieved better using a sweep-line algorithm
//see Bentley-Ottmann algorithm

/*	floor plan data structure needs to expose:
 *  	initialisation from file
 *  	boundary crossing detection
 * 		point is inside polygon
 * 
 * PROBLEM: common vertices count as on both lines - breaks polygon check
 */

public class FloorPlan {
	public double maxX = 0L;
	public double maxY = 0L;
	
	public class Edge {
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
		
//		private boolean doPointsStraddle(double n1, double n2, double pivot) {
//			return ((pivot - n1) * (pivot - n2) <= 0);
//		}
		
		public boolean crosses(double o_x1, double o_y1, double o_x2, double o_y2) {
			if (x1 == x2 && y1 == y2) return false;
			//System.out.println("Checking (" + o_x1 + "," + o_y1 + ") -> (" + o_x2 + "," + o_y2 + ") against (" + this.x1 + "," + this.y1 + ") -> (" + this.x2 + "," + this.y2 + ")");
			
			// SPECIAL CASE: infinite gradient
			// if difference between x values and x of infinite-m line changes sign, then crossing
			// if difference between y values and y of zero-m line changes sign, then crossing
			/*
			if (this.x1 == this.x2) {
				System.out.println("straight vertical line (1) at x=" + this.x1);
				if (doPointsStraddle(o_x1, o_x2, this.x1)) {
					if (doPointsStraddle(this.y1, this.y2, o_y1)) return true;
					
				}
				return false;
			}
			
			if (o_x1 == o_x2) {
				System.out.println("straight vertical line (2) at x=" + o_x1);
				
			}
			
			if (this.y1 == this.y2) {
				System.out.println("straight horizontal line (1) at y=" + this.y1);
				
			}
			
			if (o_y1 == o_y2) {
				System.out.println("straight horizontal line (2) at y=" + o_y1);
				
			}
			*/
			// calculate gradient of this line and other line
			double t_m = (this.y1 - this.y2) / (this.x1 - this.x2);
			double o_m = (o_y1 - o_y2) / (o_x1 - o_x2);
			
			// calculate intersection with y axis, i.e. c in y = mx + c
			
			double t_c = this.y1 - t_m*this.x1;
			double o_c = o_y1 - o_m*o_x1;
			
			
			//System.out.println("Lines are y = " + o_m + "x + " + o_c + " and y = " + t_m + "x + " + t_c);
			
			double side1;
			double side2;
			
			//check if other edge endpoints are on same side of this endpoints (no sign change when plug into y - mx - c)
			side1 = o_y1 - (t_m*o_x1) - t_c;
			side2 = o_y2 - (t_m*o_x2) - t_c;
			boolean o_same = (side1 * side2) > 0;
			
			//check if this edge endpoints are on same side of other endpoints (similarly)
			side1 = this.y1 - (o_m*this.x1) - o_c;
			side2 = this.y2 - (o_m*this.x2) - o_c;
			boolean t_same = (side1 * side2) > 0;
			
			// if both booleans are false, then there is an intersection
			
			boolean intersection = (t_same == false) & (o_same == false);
			
			/* if (intersection) {
			 *	System.out.println("crosses");
			}*/
			
			return intersection;
		}
	}
	
	public ArrayList<Edge> edges;
	
	public FloorPlan(InputStream csvStream) {
		System.out.println("loading floorplan from stream");
		edges = new ArrayList<Edge>();
		BufferedReader br;
		String line = "";
		String splitComma = ",";
		String[] coordinates;
		try {
			br = new BufferedReader(new InputStreamReader(csvStream));
			while ((line = br.readLine()) != null) {
				coordinates = line.split(splitComma);
				//got coordinates, store in data structure, here an arraylist of Edges
				double x1 = Double.parseDouble(coordinates[0]);
				double y1 = Double.parseDouble(coordinates[1]);
				double x2 = Double.parseDouble(coordinates[2]);
				double y2 = Double.parseDouble(coordinates[3]);
				edges.add(new Edge(x1,y1,x2,y2));
				if (x1 > maxX) maxX = x1;
				if (x2 > maxX) maxX = x2;
				if (y1 > maxY) maxY = x1;
				if (y2 > maxY) maxY = x2;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public int findEdgeCrossings(double x1, double y1, double x2, double y2) {
		int noCrossings = 0;
		for (Edge e : edges) {
			if (e.crosses(x1, y1, x2, y2)) {
				noCrossings++;
				//System.out.println(x1 + "," + y1 + " -> " + x2 + "," + y2 + " crosses " + e.getDescription());
			}
			//else System.out.println(x1 + "," + y1 + " -> " + x2 + "," + y2 + " does not cross " + e.getDescription());
		}
		return noCrossings;
	}
	
	public boolean didCrossBoundary(double x1, double y1, double x2, double y2) {
		int noCrossings = findEdgeCrossings(x1,y1,x2,y2);
		return (noCrossings > 0);
	}
	
	public boolean pointIsInsideRoom(double x, double y) { // essentially ray casting algorithm, horizontal ray fixed at y then fixed at x
		
		if (edges.size() <= 2) return true;
		
		boolean isInsideX = false;
		boolean isInsideY = false;
		int noCrossings;
		
		noCrossings = findEdgeCrossings(0, y, x, y);
		if (noCrossings > 0 && (noCrossings % 2) == 1) {
			return true;
		}
		
		/*
		noCrossings = findEdgeCrossings(0,y,maxX,y);
		if (noCrossings > 0 && (noCrossings % 2) == 0) {
			isInsideX = true;
			System.out.println("inside X, crosses polygon " + noCrossings + " times");
		} else {
			//System.out.println("not inside X: 0," + y + " -> " + maxX + "," + y + " crosses polygon " + noCrossings + " times");
		}
		
		noCrossings = findEdgeCrossings(x,0,x,maxY);
		if (noCrossings > 0 && (noCrossings % 2) == 0) {
			isInsideY = true;
			System.out.println("inside Y, crosses polygon " + noCrossings + " times");
		}
		*/
		return (isInsideX&isInsideY);
	}
}
