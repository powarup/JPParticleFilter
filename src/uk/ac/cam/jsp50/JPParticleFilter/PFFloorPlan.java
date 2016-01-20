package uk.ac.cam.jsp50.JPParticleFilter;

import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class PFFloorPlan {
	
	public enum EdgeType {
		LINE2D,JP
	}
	
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
	
	public class Line2DEdge extends Edge {
		public Line2DEdge(double _x1, double _y1, double _x2, double _y2) {
			super(_x1, _y1, _x2, _y2);
		}

		public boolean crosses(double o_x1, double o_y1, double o_x2, double o_y2) {
			return Line2D.linesIntersect(o_x1, o_y1, o_x2, o_y2, x1, y1, x2, y2);
		}
	}
	
	public class JPEdge extends Edge {
		
		public JPEdge(double _x1, double _y1, double _x2, double _y2) {
			super(_x1, _y1, _x2, _y2);
		}

		public boolean crosses(double o_x1, double o_y1, double o_x2, double o_y2) {
			// TODO write maths here
			return false;
		}
		
	}

	public double maxX, maxY;
	public ArrayList<Edge> edges;
	public EdgeType edgeType = EdgeType.LINE2D;

	public abstract boolean doesCrossBoundary(double x1, double y1, double x2, double y2); // TODO return crossing type enum, include crosses doorway

	public abstract boolean pointIsInsidePlan(double x, double y);

	public void getEdgesFromStream(InputStream csvStream) {
		System.out.println("loading floorplan from stream");
		edges = new ArrayList<Edge>();
		BufferedReader br;
		String line = "";
		String splitComma = ",";
		String[] coordinates;
		Edge newEdge;
		try {
			br = new BufferedReader(new InputStreamReader(csvStream));
			while ((line = br.readLine()) != null) {
				coordinates = line.split(splitComma);
				//got coordinates, store in data structure, here an arraylist of Edges
				double x1 = Double.parseDouble(coordinates[0]);
				double y1 = Double.parseDouble(coordinates[1]);
				double x2 = Double.parseDouble(coordinates[2]);
				double y2 = Double.parseDouble(coordinates[3]);
				switch (edgeType) {
				case LINE2D:
					newEdge = new Line2DEdge(x1, y1, x2, y2);
					break;
				case JP:
					newEdge = new JPEdge(x1, y1, x2, y2);
					break;
				default:
					newEdge = new Line2DEdge(x1, y1, x2, y2);
					break;
				}
				edges.add(newEdge);
				if (x1 > maxX) maxX = x1;
				if (x2 > maxX) maxX = x2;
				if (y1 > maxY) maxY = x1;
				if (y2 > maxY) maxY = x2;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
