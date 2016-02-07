package uk.ac.cam.jsp50.JPParticleFilter;

public class PFMyEdge extends Edge {
	
	public PFMyEdge(double _x1, double _y1, double _x2, double _y2) {
		super(_x1, _y1, _x2, _y2);
	}

	public boolean crosses(double o_x1, double o_y1, double o_x2, double o_y2) {
		return linesIntersect(o_x1, o_y1, o_x2, o_y2, x1, y1, x2, y2);
	}
	
	/**
     * Tells whether the two line segments cross.
     * 
     * @param x1
     *            the x coordinate of the starting point of the first segment.
     * @param y1
     *            the y coordinate of the starting point of the first segment.
     * @param x2
     *            the x coordinate of the end point of the first segment.
     * @param y2
     *            the y coordinate of the end point of the first segment.
     * @param x3
     *            the x coordinate of the starting point of the second segment.
     * @param y3
     *            the y coordinate of the starting point of the second segment.
     * @param x4
     *            the x coordinate of the end point of the second segment.
     * @param y4
     *            the y coordinate of the end point of the second segment.
     * @return true, if the two line segments cross.
     */
    public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3,
            double y3, double x4, double y4) {
        /*
         * A = (x2-x1, y2-y1) B = (x3-x1, y3-y1) C = (x4-x1, y4-y1) D = (x4-x3,
         * y4-y3) = C-B E = (x1-x3, y1-y3) = -B F = (x2-x3, y2-y3) = A-B Result
         * is ((AxB) (AxC) <=0) and ((DxE) (DxF) <= 0) DxE = (C-B)x(-B) =
         * BxB-CxB = BxC DxF = (C-B)x(A-B) = CxA-CxB-BxA+BxB = AxB+BxC-AxC
         */
        x2 -= x1; // A
        y2 -= y1;
        x3 -= x1; // B
        y3 -= y1;
        x4 -= x1; // C
        y4 -= y1;
        double AvB = x2 * y3 - x3 * y2;
        double AvC = x2 * y4 - x4 * y2;
        // Online
        if (AvB == 0.0 && AvC == 0.0) {
            if (x2 != 0.0) {
                return (x4 * x3 <= 0.0)
                        || ((x3 * x2 >= 0.0) && (x2 > 0.0 ? x3 <= x2 || x4 <= x2 : x3 >= x2
                                || x4 >= x2));
            }
            if (y2 != 0.0) {
                return (y4 * y3 <= 0.0)
                        || ((y3 * y2 >= 0.0) && (y2 > 0.0 ? y3 <= y2 || y4 <= y2 : y3 >= y2
                                || y4 >= y2));
            }
            return false;
        }
        double BvC = x3 * y4 - x4 * y3;
        return (AvB * AvC <= 0.0) && (BvC * (AvB + BvC - AvC) <= 0.0);
    }
	
}