package uk.ac.cam.jsp50.JPParticleFilter;

public abstract class ParticleStore {
	
	public class ParticleNotFoundException extends Exception {

		public ParticleNotFoundException(String message) {
			super(message);
		}

		/**
		 * Autogenerated ID
		 */
		private static final long serialVersionUID = 2325590139173565119L;
		
	}
	
	public abstract class ParticleManager { // ParticleManager iterates over set of particles and provides proxy operations
		private int n = -1;
		
		public boolean hasNextActiveParticle() {
			int i = n + 1;
			while (exists(i)) {
				if (getWeightatIndex(i) > 0) 
					return true; 
				else i++;
			}
			return false;
		}
		
		public void nextActiveParticle() throws ParticleNotFoundException {
			n++;
			while (exists(n)) {
				if (getWeightatIndex(n) > 0) break;
				n++;
			}
			if (!exists(n)) throw new ParticleNotFoundException("No active particle found");
			this.load(n);
		}
		
		public boolean hasNextParticle() {
			return exists(n+1);
		}
		
		public void nextParticle() throws ParticleNotFoundException {
			n++;
			if (!exists(n)) throw new ParticleNotFoundException("No particle at index " + n);
			
			this.load(n);
		}
		
		public abstract void load(int n); // loads state or makes pointers to allow passthrough of operations to particle at index n
		
		public abstract void displace(StepVector s);

		public abstract void setWeight(double w);

		public abstract double getX();

		public abstract double getY();
		
		public abstract double getWeight();

		public String summary() {
			return getX() + "," + getY() + " : " + getWeight();
		}
	}
	

	protected int greatestIndexAssigned = -1;
	
	public abstract int getParticleNo();
	
	public boolean exists(int n) {
		return (n>=0) && (n <= greatestIndexAssigned);
	}
	
	public abstract void addParticle(double x, double y, double w); // implementations must update greatestIndexAssigned
	
	public abstract double getXatIndex(int n);
	public abstract double getYatIndex(int n);
	public abstract double getWeightatIndex(int n);
	
	public abstract void setXatIndex(int n, double x);
	public abstract void setYatIndex(int n, double y);
	public abstract void setWeightatIndex(int n, double w);
	
	public abstract void displaceParticleAtIndex(int n, StepVector s);
	
	public abstract ParticleManager getParticleManager();
	
	public abstract ParticleStore getFreshParticleStoreInstance();
	public abstract void cleanForReuse();
}
