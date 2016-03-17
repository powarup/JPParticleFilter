package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.ArrayList;

public class NaiveBacktrackingParticleStore extends ParticleStore {

	public class NaiveBacktrackingParticle {
		public double x,y,w;
		public NaiveBacktrackingParticle parent;

		public NaiveBacktrackingParticle(double _x, double _y, double _w, NaiveBacktrackingParticle _parent) {
			this.x = _x;
			this.y = _y;
			this.w = _w;
			this.parent = _parent;
		}

		public void displace(StepVector s) {
			double dx = s.length * Math.sin(Math.toRadians(s.angle));
			double dy = s.length * Math.cos(Math.toRadians(s.angle));;

			this.x += dx;
			this.y += dy;
		}
		
		public NaiveBacktrackingParticle getChild() {
			return new NaiveBacktrackingParticle(x, y, w, this);
		}
	}

	public class NaiveBacktrackingParticleManager extends ParticleManager {

		private NaiveBacktrackingParticle p;
		private int generation;
		private ArrayList<NaiveBacktrackingParticle> particles;
		
		public NaiveBacktrackingParticleManager(int generation) {
			this.generation = generation;
			particles = generations.get(generation);
		}
		
		public NaiveBacktrackingParticle getParticle() {
			return p;
		}
		
		@Override
		public boolean hasNextActiveParticle() {
			int i = n + 1;
			while (existsAtGeneration(i,generation)) {
				if (getWeightatIndexatGeneration(i,generation) > 0) 
					return true; 
				else i++;
			}
			return false;
		}
		
		@Override
		public void nextActiveParticle() throws ParticleNotFoundException {
			n++;
			while (existsAtGeneration(n, generation)) {
				if (getWeightatIndexatGeneration(n, generation) > 0) break;
				n++;
			}
			if (!existsAtGeneration(n, generation)) throw new ParticleNotFoundException("No active particle found");
			this.load(n);
		}
		
		@Override
		public boolean hasNextParticle() {
			return existsAtGeneration(n+1, generation);
		}
		
		@Override
		public void nextParticle() throws ParticleNotFoundException {
			n++;
			if (!existsAtGeneration(n, generation)) throw new ParticleNotFoundException("No particle at index " + n);
			
			this.load(n);
		}
		
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
			p.w = w;
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

	private ArrayList<ArrayList<NaiveBacktrackingParticle>> generations;
	public int currentGeneration = -1;
	
	public NaiveBacktrackingParticleStore() {
		generations = new ArrayList<ArrayList<NaiveBacktrackingParticle>>();
		generations.add(new ArrayList<NaiveBacktrackingParticle>());
		currentGeneration = 0;
	}
	
	public NaiveBacktrackingParticleStore(int size) {
		generations = new ArrayList<ArrayList<NaiveBacktrackingParticle>>();
		generations.add(new ArrayList<NaiveBacktrackingParticle>(size));
		currentGeneration = 0;
	}
	
	public boolean existsAtGeneration(int n, int generation) {
		return (n>=0) && (n < generations.get(generation).size());
	}
	
	@Override
	public double getXatIndex(int n) {
		return getXatIndexatGeneration(n, currentGeneration);
	}
	
	public double getXatIndexatGeneration(int n, int generation) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(generation);
		return particles.get(n).x;
	}

	@Override
	public double getYatIndex(int n) {
		return getYatIndexatGeneration(n, currentGeneration);
	}
	
	public double getYatIndexatGeneration(int n, int generation) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(generation);
		return particles.get(n).y;
	}

	@Override
	public double getWeightatIndex(int n) {
		return getWeightatIndexatGeneration(n, currentGeneration);
	}
	
	public double getWeightatIndexatGeneration(int n, int generation) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(generation);
		return particles.get(n).w;
	}

	@Override
	public void setXatIndex(int n, double _x) {
		setXatIndexatGeneration(n, currentGeneration, _x);
	}
	
	public void setXatIndexatGeneration(int n, int generation, double _x) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(generation);
		NaiveBacktrackingParticle p = particles.get(n);
		p.x = _x;
	}

	@Override
	public void setYatIndex(int n, double _y) {
		setYatIndexatGeneration(n, currentGeneration, _y);
	}
	
	public void setYatIndexatGeneration(int n, int generation, double _y) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(generation);
		NaiveBacktrackingParticle p = particles.get(n);
		p.y = _y;
	}

	@Override
	public void setWeightatIndex(int n, double _w) {
		setWeightatIndexatGeneration(n, currentGeneration, _w);
	}
	
	public void setWeightatIndexatGeneration(int n, int generation, double _w) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(generation);
		NaiveBacktrackingParticle p = particles.get(n);
		p.w = _w;
	}

	@Override
	public ParticleManager getParticleManager() {
		return getParticleManagerForGeneration(currentGeneration);
	}
	
	public ParticleManager getParticleManagerForGeneration(int generation) {
		return new NaiveBacktrackingParticleManager(generation);
	}

	@Override
	public void displaceParticleAtIndex(int n, StepVector s) {
		displaceParticleAtIndexAtGeneration(n, currentGeneration, s);
	}
	
	public void displaceParticleAtIndexAtGeneration(int n, int generation, StepVector s) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(generation);
		NaiveBacktrackingParticle p = particles.get(n);
		p.displace(s);
	}

	@Override
	public int getParticleNo() {
		return greatestIndexAssigned + 1;
	}

	@Override
	public ParticleStore getFreshParticleStoreInstance() {
		return new NaiveBacktrackingParticleStore();
	}

	@Override
	public void cleanForReuse() {
		greatestIndexAssigned = -1;
	}

	@Override
	public void addParticle(double x, double y, double w) {
		addParticle(x,y,w,null);
	}
	
	public void addParticle(double x, double y, double w, NaiveBacktrackingParticle parent) {
		NaiveBacktrackingParticle newParticle = new NaiveBacktrackingParticle(x, y, w, parent);
		addParticle(newParticle);
	}
	
	public void addParticle(NaiveBacktrackingParticle particle) {
		ArrayList<NaiveBacktrackingParticle> particles = generations.get(currentGeneration);
		particles.add(particle);
		greatestIndexAssigned++;
	}
	
	public void nextGeneration() {
		generations.add(new ArrayList<NaiveBacktrackingParticle>());
		greatestIndexAssigned = -1;
		currentGeneration++;
	}

}
