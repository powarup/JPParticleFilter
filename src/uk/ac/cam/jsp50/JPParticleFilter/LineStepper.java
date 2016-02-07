package uk.ac.cam.jsp50.JPParticleFilter;

public class LineStepper {
	public double cellSize, x1, y1, x2, y2, m, c;
	private final static double fuzzFactor = 0.000001;
	public boolean vertical = false;
	public int currentX, currentY, finalX, finalY;

	// essentially an extended Bresenham
	// uses quadrants, but swaps points so only need to check up and right, and down and right

	public LineStepper(double cellSize, double _x1, double _y1, double _x2, double _y2) {
		this.cellSize = cellSize;
		if (_x1 > _x2) { // swap points if going leftward
			this.x1 = _x2;
			this.y1 = _y2;
			this.x2 = _x1;
			this.y2 = _y1;
		} else {
			this.x1 = _x1;
			this.y1 = _y1;
			this.x2 = _x2;
			this.y2 = _y2;
		}

		if (this.x1 == this.x2) vertical = true;
		double fuzzyX = this.x1 + fuzzFactor;
		double fuzzyY = this.y1 + fuzzFactor;
		currentX = (int)(fuzzyX/this.cellSize);
		currentY = (int)(fuzzyY/this.cellSize);
		fuzzyX = this.x2 + fuzzFactor;
		finalX = (int)(fuzzyX/this.cellSize);
		fuzzyY = this.y2 + fuzzFactor;
		finalY = (int)(fuzzyY/this.cellSize);
		m = (this.y2-this.y1)/(this.x2-this.x1);
		c = this.y1 - m*this.x1;
	}

	public boolean canStepForward() {
		if (vertical) {
			if (y1 > y2) { // going down
				return (currentY > finalY);
			} else { // going up
				return (currentY < finalY);
			}
		} else if (m < 0) { // going down
			if (currentX == finalX) return (currentY > finalY);
			if (currentY == finalY) return (currentX < finalX);
			return (currentX < finalX && currentY > finalY);
		} else { // going up
			if (currentX == finalX) return (currentY < finalY);
			if (currentY == finalY) return (currentX < finalX);
			return (currentX < finalX && currentY < finalY);
		}
	}

	public void stepForward() {
		if (vertical) {
			if (y1 > y2) { // going down
				currentY--;
			} else { // going up
				currentY++;
			}
		} else if (m < 0) { // going down
			double bottomRightX = (currentX+1)*cellSize;
			double bottomRightY = (currentY)*cellSize;
			double lineYAtBottomRightX = m*bottomRightX + c - fuzzFactor;
			if (bottomRightY > lineYAtBottomRightX) { // bottom right is above line, so move down
				currentY--;
			} else { // bottom right is below, so move right
				currentX++;
			}
		} else { // going up
			double topRightX = (currentX+1)*cellSize;
			double topRightY = (currentY+1)*cellSize;
			double lineYAtTopRightX = m*topRightX + c + fuzzFactor; 
			if (topRightY > lineYAtTopRightX) { // top right is above line, so move right
				currentX++;
			} else { // top right is below, so move up
				currentY++;
			}
		}
	}


}
