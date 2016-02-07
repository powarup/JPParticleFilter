package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.InputStream;
import java.util.Iterator;

public class PFSimpleBitmapFloorPlan extends PFBitmapFloorPlan {

	int[][] bitmap; // -1 for outside, 0 for inside, 1 for edge
	public final static int OUTSIDE = -1;
	public final static int INSIDE = 0;
	public final static int EDGE = 1;


	public PFSimpleBitmapFloorPlan(InputStream csvStream, double cellSize) {
		this(csvStream, cellSize, null);
	}

	public PFSimpleBitmapFloorPlan(InputStream csvStream, Double cellSize, EdgeType edgeType) {
		this.cellSize = cellSize;
		this.edgeType = (edgeType == null) ? EdgeType.LINE2D : edgeType;
		getEdgesFromStream(csvStream);
		int bitmapWidth = (int)(maxX / cellSize) + 1;
		int bitmapHeight = (int)(maxY / cellSize) + 1;
		bitmap = new int[bitmapWidth][bitmapHeight];
		buildBitmap();
	}

	public boolean doesCrossBoundary(double x1, double y1, double x2, double y2) {
		LineStepper lineStepper = new LineStepper(cellSize, x1, y1, x2, y2);
		if (getBitmapCell(lineStepper.currentX, lineStepper.currentY) == EDGE) return true;
		while (lineStepper.canStepForward()) {
			lineStepper.stepForward();
			if (getBitmapCell(lineStepper.currentX, lineStepper.currentY) == EDGE) return true;
		}
		return false;
	}

	@Override
	public boolean pointIsInsidePlan(double x, double y) {
		return (getBitmapCell(x, y) == INSIDE);
	}

	public void buildBitmap() {
		// fill with outside if closed, inside if open
		int fillValue = closed ? OUTSIDE : INSIDE;
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;

		for (int x = 0; x < bitmapWidth; x++) {
			for (int y = 0; y < bitmapHeight; y++) {
				bitmap[x][y] = fillValue;
			}
		}

		// find edges

		Iterator<Edge> iterator = edges.iterator();
		Edge e;
		while (iterator.hasNext()) {
			e = iterator.next();
			drawLineInBitmap(e.x1, e.y1, e.x2, e.y2);
		}
		
		// find insides
		if (closed) {
			for (int x = 0; x < bitmapWidth; x++) {
				for (int y = 0; y < bitmapHeight; y++) {
					if (bitmap[x][y] != EDGE) {
						int up = getBitmapCell(x, y-1);
						int left = getBitmapCell(x-1, y);
						if (up != EDGE) setBitmapCell(x, y, up);
						else if (left != EDGE) setBitmapCell(x, y, left);
						else { // if can't infer from neighbourhood, ray trace
							int nXCrossings = 0;
							int nYCrossings = 0;
							for (int i = 0; i < x; i++) {
								if (getBitmapCell(i, y) == EDGE && getBitmapCell(i-1, y) != EDGE) nXCrossings++;
							}
							for (int i = 0; i < y; i++) {
								if (getBitmapCell(x, i) == EDGE && getBitmapCell(x, i-1) != EDGE) nYCrossings++;
							}
							if (nXCrossings % 2 == 1 && nYCrossings % 2 == 1) setBitmapCell(x, y, INSIDE);
						}
					}
				}
			}
		}
		System.out.println(bitmapDescription());
	}

	public void drawLineInBitmap(double x1, double y1, double x2, double y2) {
		LineStepper lineStepper = new LineStepper(cellSize, x1, y1, x2, y2);
		setBitmapCell(lineStepper.currentX,lineStepper.currentY,EDGE);
		while (lineStepper.canStepForward()) {
			lineStepper.stepForward();
			setBitmapCell(lineStepper.currentX,lineStepper.currentY,EDGE);
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
		if (x >= 0 && x < bitmapWidth && y >= 0 && y < bitmapHeight) {
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
