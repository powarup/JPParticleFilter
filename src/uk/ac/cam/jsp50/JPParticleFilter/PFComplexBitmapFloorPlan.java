package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.InputStream;
import java.util.Iterator;

public class PFComplexBitmapFloorPlan extends PFBitmapFloorPlan {
	private CellType[][] bitmap;
	private double cellSize = 0.1;

	private enum CellType {
		OUTSIDE,INSIDE,EDGE,VERTEX,UNKNOWN
	}

	public PFComplexBitmapFloorPlan(InputStream csvStream) {
		this(csvStream, null);
	}

	public PFComplexBitmapFloorPlan(InputStream csvStream, EdgeType edgeType) {
		this.edgeType = (edgeType == null) ? EdgeType.LINE2D : edgeType;
		getEdgesFromStream(csvStream);
		int bitmapWidth = (int)(maxX / cellSize) + 1;
		int bitmapHeight = (int)(maxY / cellSize) + 1;
		bitmap = new CellType[bitmapWidth][bitmapHeight];
		buildBitmap();
	}

	public void buildBitmap() {

		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;

		for (int x = 0; x < bitmapWidth; x++) {
			for (int y = 0; y < bitmapHeight; y++) {
				bitmap[x][y] = CellType.UNKNOWN;
			}
		}

		// find unknowns

		Iterator<Edge> iterator = edges.iterator();
		Edge e;
		while (iterator.hasNext()) {
			e = iterator.next();
			drawLineInBitmap(e.x1, e.y1, e.x2, e.y2);
		}

		System.out.println("building " + (closed ? "closed" : "open") + " bitmap");
		
		for (int x = 0; x < bitmapWidth; x++) {
			if (bitmap[x][0] == CellType.UNKNOWN) bitmap[x][0] = CellType.OUTSIDE;
		}

		// check if outside or inside
		for (int x = 0; x < bitmapWidth; x++) {
			for (int y = 0; y < bitmapHeight; y++) {
				if (bitmap[x][y] == CellType.UNKNOWN) {
					if (!closed) {
						bitmap[x][y] = CellType.INSIDE;
					} else {
						System.out.println("filling cell " + x + "," + y);
						if (y > 0 && (bitmap[x][y-1] == CellType.INSIDE || bitmap[x][y-1] == CellType.OUTSIDE)) {
							bitmap[x][y] = bitmap[x][y-1];
						} else {
							String history = "";
							for (int i = 0; i < y; i++) {
								switch (bitmap[x][i]) {
								case OUTSIDE:
									history += "O";
									break;
								case INSIDE:
									history += "I";
									break;
								case EDGE:
									history += "E";
									break;
								case VERTEX:
									history += "V";
									break;
								case UNKNOWN:
									history += "?";
								default:
									break;
								}
							}
							boolean inlineWithEdge = history.contains("E");
							boolean inlineWithVertex = history.contains("V");
							if (!inlineWithEdge && !inlineWithVertex) { // if no vertices or edges on path, must be outside
								System.out.println("not vertically inline with an edge");
								bitmap[x][y] = CellType.OUTSIDE;
							} else {
								if (inlineWithEdge && !inlineWithVertex) {// if only edges, ray cast
									System.out.println("vertically inline with an edge");
									int nCrossings = findEdgeCrossings(x*cellSize, 0, x*cellSize, y*cellSize);
									if (nCrossings % 2 == 1) {
										bitmap[x][y] = CellType.INSIDE;
									} else {
										bitmap[x][y] = CellType.OUTSIDE;
									}
								} else { // otherwise say we don't know and take care of it later, because there is a vertex and that is a complex case we can tell quickly through neighbourhood
									bitmap[x][y] = CellType.UNKNOWN;
								}
							}
						}
					}
				}
			}
		}

		// clean up unknowns
		for (int x = 0; x < bitmapWidth; x++) {
			for (int y = 0; y < bitmapHeight; y++) {
				if (bitmap[x][y] == CellType.UNKNOWN) {
					if (x > 0 && bitmap[x-1][y] == CellType.INSIDE || bitmap[x-1][y] == CellType.OUTSIDE) bitmap[x][y] = bitmap[x-1][y];
					if (x < bitmapWidth - 1 && bitmap[x+1][y] == CellType.INSIDE || bitmap[x-1][y] == CellType.OUTSIDE) bitmap[x][y] = bitmap[x+1][y];
				}
			}
		}
	}

	public void drawLineInBitmap(double x1, double y1, double x2, double y2) {
		
		LineStepper lineStepper = new LineStepper(cellSize, x1, y1, x2, y2);
		setBitmapCell(lineStepper.currentX,lineStepper.currentY,CellType.VERTEX); // mark start point as vertex
		while (lineStepper.canStepForward()) {
			lineStepper.stepForward();
			setBitmapCell(lineStepper.currentX,lineStepper.currentY,CellType.EDGE);
		}
		setBitmapCell(lineStepper.currentX,lineStepper.currentY,CellType.VERTEX); // mark endpoint as vertex

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
				case VERTEX:
					description += "V";
					break;
				case UNKNOWN:
					description += "?";
					break;
				default:
					break;
				}
			}
			description += "\n";
		}
		return description;
	}

	public void setBitmapCell(int x, int y, CellType type) {
		try {
			bitmap[x][y] = type;
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	public boolean doesCrossBoundary(double x1, double y1, double x2, double y2) {
		LineStepper lineStepper = new LineStepper(cellSize, x1, y1, x2, y2);
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;
		while (true) {
			if (lineStepper.currentX >= bitmapWidth || lineStepper.currentY >= bitmapHeight || lineStepper.currentX < 0 || lineStepper.currentY < 0) break;
			System.out.println("Checking cell " + lineStepper.currentX + "," + lineStepper.currentY + " : " + bitmap[lineStepper.currentX][lineStepper.currentY]);
			CellType currentCell = bitmap[lineStepper.currentX][lineStepper.currentY];
			if (currentCell == CellType.EDGE || currentCell == CellType.VERTEX) {
				return true;
			}
			if (!lineStepper.canStepForward()) System.out.println("cell is last in line");
			if (!lineStepper.canStepForward()) break;
			else lineStepper.stepForward();
		}
		
		return false;
	}

	public boolean pointIsInsidePlan(double x, double y) {
		int cellX = (int)(x/cellSize);
		int cellY = (int)(y/cellSize);
		return bitmap[cellX][cellY] == CellType.INSIDE;
	}
	
}
