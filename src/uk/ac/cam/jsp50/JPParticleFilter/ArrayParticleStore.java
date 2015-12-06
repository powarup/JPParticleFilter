package uk.ac.cam.jsp50.JPParticleFilter;

public class ArrayParticleStore extends ParticleStore {
	
	private int n;
	private double[] xValues;
	private double[] yValues;
	private double[] weightValues;
	
 	public class ArrayParticleManager extends ParticleManager {
 		int currentIndex;
		@Override
		public void load(int _n) {
			currentIndex = _n;
		}

		@Override
		public void displace(StepVector s) {
			displaceParticleAtIndex(currentIndex, s);
		}

		@Override
		public void setWeight(double w) {
			setWeightatIndex(currentIndex, w);
		}

		@Override
		public double getX() {
			return getXatIndex(currentIndex);
		}

		@Override
		public double getY() {
			return getYatIndex(currentIndex);
		}

		@Override
		public double getWeight() {
			return getWeightatIndex(currentIndex);
		}
		
	}
	
	public ArrayParticleStore(int _n) {
		n = _n;
		xValues = new double[n];
		yValues = new double[n];
		weightValues = new double[n];
	}
	
	@Override
	public int getParticleNo() {
		return n;
	}

	@Override
	public void addParticle(double x, double y, double w) {
		greatestIndexAssigned++;
		xValues[greatestIndexAssigned] = x;
		yValues[greatestIndexAssigned] = y;
		weightValues[greatestIndexAssigned] = w;
	}

	@Override
	public double getXatIndex(int n) {
		return xValues[n];
	}

	@Override
	public double getYatIndex(int n) {
		return yValues[n];
	}

	@Override
	public double getWeightatIndex(int n) {
		return weightValues[n];
	}

	@Override
	public void setXatIndex(int n, double x) {
		xValues[n] = x;
	}

	@Override
	public void setYatIndex(int n, double y) {
		yValues[n] = y;
	}

	@Override
	public void setWeightatIndex(int n, double w) {
		weightValues[n] = w;
	}

	@Override
	public void displaceParticleAtIndex(int n, StepVector s) {
		double dx = s.length * Math.sin(Math.toRadians(s.angle));
		double dy = s.length * Math.cos(Math.toRadians(s.angle));;
		
		xValues[n] += dx;
		yValues[n] += dy;
	}

	@Override
	public ParticleManager getParticleManager() {
		return new ArrayParticleManager();
	}

	@Override
	public ParticleStore getFreshParticleStoreInstance() {
		return new ArrayParticleStore(n);
	}

}
