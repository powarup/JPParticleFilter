package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.InputStream;
import java.util.Iterator;

public class PFBitmapFloorPlan extends PFFloorPlan{
	public double cellSize = 0.1;
	
	int[][] bitmap; // -1 for outside, 0 for inside, 1 for edge
	public final static int OUTSIDE = -1;
	public final static int INSIDE = 0;
	public final static int EDGE = 1;
	
	public PFBitmapFloorPlan(InputStream csvStream, Double cellSize) {
		this.cellSize = cellSize;
		getEdgesFromStream(csvStream);
		int bitmapWidth = (int)(maxX / cellSize) + 1;
		int bitmapHeight = (int)(maxY / cellSize) + 1;
		bitmap = new int[bitmapWidth][bitmapHeight];
		buildBitmap();
	}

	public PFCrossing findCrossing(double x1, double y1, double x2, double y2) {
		LineStepper lineStepper = new LineStepper(cellSize, x1, y1, x2, y2);
		if (getBitmapCell(lineStepper.currentX, lineStepper.currentY) == EDGE) return PFCrossing.CROSSING;
		while (lineStepper.canStepForward()) {
			lineStepper.stepForward();
			int step = getBitmapCell(lineStepper.currentX, lineStepper.currentY);
			if (step == EDGE || step == OUTSIDE) return PFCrossing.CROSSING;
		}
		return PFCrossing.NONE;
	}

	public boolean pointIsInsidePlan(double x, double y) {
		return (getBitmapCell(x, y) == INSIDE);
	}


	public void buildBitmap() {
		// fill with outside if closed, leave as default (inside) if open
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;
		System.out.println("Building " + (closed ? "closed " : "open ") + "bitmap floor plan");
		if (closed) for (int x = 0; x < bitmapWidth; x++) {
			for (int y = 0; y < bitmapHeight; y++) {
				bitmap[x][y] = OUTSIDE;
			}
		}

		// draw edges in bitmap
		Iterator<Edge> iterator = edges.iterator();
		Edge e;
		while (iterator.hasNext()) {
			e = iterator.next();
			drawLineInBitmap(e.x1, e.y1, e.x2, e.y2,EDGE);
		}
		System.out.println("edges drawn");

		// find insides
		if (closed) {
			plantInsideSeeds();
			for (int y = 0; y < bitmapHeight-1; y++) {
				for (int x = 0; x < bitmapWidth-1; x++) {
					if (getBitmapCell(x, y) == INSIDE) {
						propagateSeed(x, y, 0);
					}
				}
			}
		}

		System.out.println("filled");
		//System.out.println(bitmapDescription());
	}

	private void plantInsideSeeds() {
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;
		for (int y = 0; y < bitmapHeight-1; y++) {
			for (int x = 0; x < bitmapWidth-1; x++) {
				if (getBitmapCell(x, y) == EDGE) {
					if (getBitmapCell(x+1, y) == OUTSIDE && cellIsInsideByXRay(x+1, y) && cellIsInsideByYRay(x+1, y)) {
						if (getBitmapCell(x+1, y-1) == OUTSIDE) {
							setBitmapCell(x+1, y, INSIDE);
							propagateSeed(x+1, y, 0);
						}
					}
				}
			}
		}
	}

	public boolean cellIsInsideByXRay(int x, int y) {
		double xd = (x + 0.5)*cellSize;
		double yd = (y + 0.5)*cellSize;
		int crossings = 0;
		Iterator<Edge> iterator = edges.iterator();
		Edge e;
		Edge ray = new Edge(0, yd, xd, yd);
		while (iterator.hasNext()) {
			e = iterator.next();
			if (e.crosses(ray)) crossings++;
		}
		return (crossings % 2 == 1);
	}

	public boolean cellIsInsideByYRay(int x, int y) {
		double xd = (x + 0.5)*cellSize;
		double yd = (y + 0.5)*cellSize;
		int crossings = 0;
		Iterator<Edge> iterator = edges.iterator();
		Edge e;
		Edge ray = new Edge(xd, 0, xd, yd);
		while (iterator.hasNext()) {
			e = iterator.next();
			if (e.crosses(ray)) crossings++;
		}
		return (crossings % 2 == 1);
	}

	private void propagateSeed(int x, int y, int depth) {
		if (depth < 1000) {
			int bitmapWidth = bitmap.length;
			int bitmapHeight = bitmap[0].length;
			if (x > 0 && getBitmapCell(x-1, y) == OUTSIDE) {
				setBitmapCell(x-1, y, INSIDE);
				propagateSeed(x-1, y, depth + 1);
			}
			if (x < bitmapWidth -2 && getBitmapCell(x+1, y) == OUTSIDE) {
				setBitmapCell(x+1, y, INSIDE);
				propagateSeed(x+1, y, depth + 1);
			}
			if (y > 0 && getBitmapCell(x, y-1) == OUTSIDE) {
				setBitmapCell(x, y-1, INSIDE);
				propagateSeed(x, y-1, depth + 1);
			}
			if (y < bitmapHeight -2 && getBitmapCell(x, y+1) == OUTSIDE) {
				setBitmapCell(x, y+1, INSIDE);
				propagateSeed(x, y+1, depth + 1);
			}
		}
	}
	
	public void drawLineInBitmap(double x1, double y1, double x2, double y2, int value) {
		LineStepper lineStepper = new LineStepper(cellSize, x1, y1, x2, y2);
		// only draw over cell if it isn't already an edge
		if (getBitmapCell(lineStepper.currentX,lineStepper.currentY) != EDGE) setBitmapCell(lineStepper.currentX,lineStepper.currentY,value);
		while (lineStepper.canStepForward()) {
			lineStepper.stepForward();
			if (getBitmapCell(lineStepper.currentX,lineStepper.currentY) != EDGE) setBitmapCell(lineStepper.currentX,lineStepper.currentY,value);
		}
	}

	// bitmap helper methods
	
	public int getBitmapCell(int x, int y) {
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;
		if (x >= 0 && x < bitmapWidth && y >= 0 && y < bitmapHeight) {
			return bitmap[x][y];
		} else {
			return closed ? OUTSIDE : INSIDE;
		}
	}
	
	public int getBitmapCell(double x, double y) {
		int xCell = (int)(x/cellSize);
		int yCell = (int)(y/cellSize);
		return getBitmapCell(xCell, yCell);
	}
	
	public void setBitmapCell(int x, int y, int value) {
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;
		if (x >= 0 && x < bitmapWidth && y >= 0 && y < bitmapHeight && getBitmapCell(x, y) != EDGE) {
			bitmap[x][y] = value;
		}
	}
	
	public String bitmapDescription() {
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;

		String description = "";
		for (int y = 0; y < bitmapHeight; y++) {
			for (int x = 0; x < bitmapWidth; x++) {
				switch (bitmap[x][y]) {
				case OUTSIDE:
					description += "O";
					break;
				case INSIDE:
					description += "I";
					break;
				case EDGE:
					description += "E";
					break;
				default:
					description += "?";
					break;
				}
			}
			description += "\n";
		}
		return description;
	}

}
