package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.ArrayList;

/* Particle store - data structure for particles
 * should mediate individual particle operations: insert, remove, reweight (remove when reweight to 0?)
 * ideally want to resample in place
 * 
 * Here have implemented as hashset of particle objects
 * - particle is essentially tuple of x,y,w,lastx,lasty
 * - array may be cheaper in space, provides indexing
 * If implement weights as separate array can make normalisation and resampling cheaper - possible overhead for linking to particle?
 */

public class ObjectParticleStore extends ParticleStore {

	private class Particle {
		public double x,y,w;
		
		public Particle(double _x, double _y, double _w) {
			this.x = _x;
			this.y = _y;
			this.w = _w;
		}
		
		public void displace(StepVector s) {
			double dx = s.length * Math.sin(Math.toRadians(s.angle));
			double dy = s.length * Math.cos(Math.toRadians(s.angle));;
			
			this.x += dx;
			this.y += dy;
		}
	}
	
	private ArrayList<Particle> particles;

	private class ObjectParticleManager extends ParticleManager {
		
		private Particle p;
		
		@Override
		public void load(int n) {
			p = particles.get(n);
		}

		@Override
		public void displace(StepVector s) {
			p.displace(s);
		}

		@Override
		public void setWeight(double w) {
			setWeightatIndex(n, w);
		}

		@Override
		public double getX() {
			return p.x;
		}

		@Override
		public double getY() {
			return p.y;
		}

		@Override
		public double getWeight() {
			return p.w;
		}
		
	}

	public ObjectParticleStore() {
		this.particles = new ArrayList<Particle>();
	}
	
	public ObjectParticleStore(int size) {
		this.particles = new ArrayList<Particle>(size);
	}

	@Override
	public void addParticle(double x, double y, double w) {
		Particle p = new Particle(x, y, w);
		greatestIndexAssigned++;
		particles.add(greatestIndexAssigned, p);
	}

	@Override
	public double getXatIndex(int n) {
		return particles.get(n).x;
	}

	@Override
	public double getYatIndex(int n) {
		return particles.get(n).y;
	}

	@Override
	public double getWeightatIndex(int n) {
		Particle particle = particles.get(n);
		if (particle == null) return 0;
		return particle.w;
	}

	@Override
	public void setXatIndex(int n, double _x) {
		Particle p = particles.get(n);
		p.x = _x;
	}

	@Override
	public void setYatIndex(int n, double _y) {
		Particle p = particles.get(n);
		p.y = _y;
	}

	@Override
	public void setWeightatIndex(int n, double _w) {
		if (_w == 0) {
			particles.set(n, null);
		} else {
			Particle p = particles.get(n);
			p.w = _w;
		}
	}

	@Override
	public ParticleManager getParticleManager() {
		return new ObjectParticleManager();
	}

	@Override
	public void displaceParticleAtIndex(int n, StepVector s) {
		Particle p = particles.get(n);
		p.displace(s);
	}

	@Override
	public int getParticleNo() {
		return greatestIndexAssigned + 1;
	}

	@Override
	public ParticleStore getFreshParticleStoreInstance() {
		return new ObjectParticleStore();
	}

	@Override
	public void cleanForReuse() {
		greatestIndexAssigned = -1;
	}
	
}