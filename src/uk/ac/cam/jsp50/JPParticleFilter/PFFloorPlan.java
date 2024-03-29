package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class PFFloorPlan {
	public double maxX, maxY;
	public ArrayList<Edge> edges;
	public ArrayList<Edge> doors;
	public boolean closed;

	public static boolean supportsDoors = false;

	public abstract PFCrossing findCrossing(double x1, double y1, double x2, double y2); // TODO return crossing type enum, include crosses doorway

	public abstract boolean pointIsInsidePlan(double x, double y);

	public void getEdgesFromStream(InputStream csvStream) {
		System.out.println("loading floorplan from stream" + (supportsDoors ? ", supports doors" : ""));
		edges = new ArrayList<Edge>();
		if (supportsDoors) doors = new ArrayList<Edge>();
		BufferedReader br;
		String line = "";
		String splitComma = ",";
		String[] coordinates;
		Edge newEdge;
		try {
			br = new BufferedReader(new InputStreamReader(csvStream));
			if ((line = br.readLine()) != null) closed = line.equals("1");
			while ((line = br.readLine()) != null) {
				coordinates = line.split(splitComma);

				//got coordinates, store in data structure, here an arraylist of Edges
				double x1 = Double.parseDouble(coordinates[0]);
				double y1 = Double.parseDouble(coordinates[1]);
				double x2 = Double.parseDouble(coordinates[2]);
				double y2 = Double.parseDouble(coordinates[3]);
				newEdge = new Edge(x1, y1, x2, y2);
				if (supportsDoors && coordinates.length > 4 && coordinates[4].equals("d")) {
					doors.add(newEdge);
					if (x1 > maxX) maxX = x1;
					if (x2 > maxX) maxX = x2;
					if (y1 > maxY) maxY = y1;
					if (y2 > maxY) maxY = y2;
				} else {
					//System.out.println("storing edge (" + x1 + "," + y1 + ") -> (" + x2 + "," + y2 + ")");
					edges.add(newEdge);
					if (x1 > maxX) maxX = x1;
					if (x2 > maxX) maxX = x2;
					if (y1 > maxY) maxY = y1;
					if (y2 > maxY) maxY = y2;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("loaded " + edges.size() + " edges from stream");
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
}
