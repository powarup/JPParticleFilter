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

		if (closed) for (int x = 0; x < bitmapWidth; x++) {
			for (int y = 0; y < bitmapHeight; y++) {
				bitmap[x][y] = OUTSIDE;
			}
		}

		// find edges
		Iterator<Edge> iterator = edges.iterator();
		Edge e;
		while (iterator.hasNext()) {
			e = iterator.next();
			drawLineInBitmap(e.x1, e.y1, e.x2, e.y2,EDGE);
		}

		// find doors
		iterator = doors.iterator();
		while (iterator.hasNext()) {
			e = iterator.next();
			drawLineInBitmap(e.x1, e.y1, e.x2, e.y2,DOOR);
		}

		// find insides
		if (closed) fillInsides();
		System.out.println(bitmapDescription());
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
