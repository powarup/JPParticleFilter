package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.InputStream;
import java.util.Iterator;

public class PFComplexBitmapFloorPlan extends PFBitmapFloorPlan {

	static {
		supportsDoors = true;
	}

	public final static int DOOR = 2;

	public PFComplexBitmapFloorPlan(InputStream csvStream, Double cellSize) {
		super(csvStream, cellSize);
	}

	@Override
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

		// find doors
		iterator = doors.iterator();
		while (iterator.hasNext()) {
			e = iterator.next();
			drawLineInBitmap(e.x1, e.y1, e.x2, e.y2,DOOR);
		}
		System.out.println("doors drawn");

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
				if (getBitmapCell(x, y) == EDGE || getBitmapCell(x, y) == DOOR) {
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

	@Override
	public void setBitmapCell(int x, int y, int value) {
		int bitmapWidth = bitmap.length;
		int bitmapHeight = bitmap[0].length;
		if (x >= 0 && x < bitmapWidth && y >= 0 && y < bitmapHeight && (!(value == OUTSIDE || value == INSIDE) || (bitmap[x][y] == OUTSIDE || bitmap[x][y] == INSIDE))) {
			bitmap[x][y] = value;
		}
	}

	@Override
	public PFCrossing findCrossing(double x1, double y1, double x2, double y2) {
		LineStepper lineStepper = new LineStepper(cellSize, x1, y1, x2, y2);
		if (getBitmapCell(lineStepper.currentX, lineStepper.currentY) == EDGE) return PFCrossing.CROSSING;
		else if (getBitmapCell(lineStepper.currentX, lineStepper.currentY) == DOOR) return PFCrossing.DOOR;
		while (lineStepper.canStepForward()) {
			lineStepper.stepForward();
			if (getBitmapCell(lineStepper.currentX, lineStepper.currentY) == EDGE) return PFCrossing.CROSSING;
			else if (getBitmapCell(lineStepper.currentX, lineStepper.currentY) == DOOR) return PFCrossing.DOOR;
		}
		return PFCrossing.NONE;
	}

	@Override
	public boolean pointIsInsidePlan(double x, double y) {
		int cell = getBitmapCell(x, y);
		return (cell == INSIDE) || (cell == DOOR);
	}

	@Override
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
				case DOOR:
					description += "D";
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
