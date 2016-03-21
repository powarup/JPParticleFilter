package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.ArrayList;

public abstract class BacktrackingParticleStore extends ParticleStore {

	public class BacktrackingParticleManager extends ParticleManager {

		private BacktrackingParticle p;
		private int generation;
		private ArrayList<BacktrackingParticle> particles;

		public BacktrackingParticleManager(int generation) {
			this.generation = generation;
			particles = generations.get(generation);
		}

		public BacktrackingParticle getParticle() {
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

	protected ArrayList<ArrayList<BacktrackingParticle>> generations;
	public int currentGeneration = -1;

	public BacktrackingParticleStore() {
		generations = new ArrayList<ArrayList<BacktrackingParticle>>();
		generations.add(new ArrayList<BacktrackingParticle>());
		currentGeneration = 0;
	}

	public BacktrackingParticleStore(int size) {
		generations = new ArrayList<ArrayList<BacktrackingParticle>>();
		generations.add(new ArrayList<BacktrackingParticle>(size));
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
		ArrayList<BacktrackingParticle> particles = generations.get(generation);
		return particles.get(n).x;
	}

	@Override
	public double getYatIndex(int n) {
		return getYatIndexatGeneration(n, currentGeneration);
	}

	public double getYatIndexatGeneration(int n, int generation) {
		ArrayList<BacktrackingParticle> particles = generations.get(generation);
		return particles.get(n).y;
	}

	@Override
	public double getWeightatIndex(int n) {
		return getWeightatIndexatGeneration(n, currentGeneration);
	}

	public double getWeightatIndexatGeneration(int n, int generation) {
		ArrayList<BacktrackingParticle> particles = generations.get(generation);
		BacktrackingParticle particle = particles.get(n);
		if (particle == null) return 0;
		else return particle.w;
	}

	@Override
	public void setXatIndex(int n, double _x) {
		setXatIndexatGeneration(n, currentGeneration, _x);
	}

	public void setXatIndexatGeneration(int n, int generation, double _x) {
		ArrayList<BacktrackingParticle> particles = generations.get(generation);
		BacktrackingParticle p = particles.get(n);
		p.x = _x;
	}

	@Override
	public void setYatIndex(int n, double _y) {
		setYatIndexatGeneration(n, currentGeneration, _y);
	}

	public void setYatIndexatGeneration(int n, int generation, double _y) {
		ArrayList<BacktrackingParticle> particles = generations.get(generation);
		BacktrackingParticle p = particles.get(n);
		p.y = _y;
	}

	@Override
	public void setWeightatIndex(int n, double _w) {
		setWeightatIndexatGeneration(n, currentGeneration, _w);
	}

	public void setWeightatIndexatGeneration(int n, int generation, double _w) {
		ArrayList<BacktrackingParticle> particles = generations.get(generation);
		BacktrackingParticle p = particles.get(n);
		p.w = _w;
	}

	@Override
	public ParticleManager getParticleManager() {
		return getParticleManagerForGeneration(currentGeneration);
	}

	public ParticleManager getParticleManagerForGeneration(int generation) {
		return new BacktrackingParticleManager(generation);
	}

	@Override
	public void displaceParticleAtIndex(int n, StepVector s) {
		displaceParticleAtIndexAtGeneration(n, currentGeneration, s);
	}

	public void displaceParticleAtIndexAtGeneration(int n, int generation, StepVector s) {
		ArrayList<BacktrackingParticle> particles = generations.get(generation);
		BacktrackingParticle p = particles.get(n);
		p.displace(s);
	}

	@Override
	public int getParticleNo() {
		return greatestIndexAssigned + 1;
	}

	@Override
	public void cleanForReuse() {
		greatestIndexAssigned = -1;
	}

	@Override
	public void addParticle(double x, double y, double w) {
		addParticle(x,y,w,null);
	}

	public abstract void addParticle(double x, double y, double w, BacktrackingParticle parent);

	public void addParticle(BacktrackingParticle particle) {
		ArrayList<BacktrackingParticle> particles = generations.get(currentGeneration);
		particles.add(particle);
		greatestIndexAssigned++;
	}

	public void removeParticle(BacktrackingParticle p) {
		removeParticleFromGeneration(p, currentGeneration);
	}

	public void removeParticleFromGeneration(BacktrackingParticle p, int generation) {
		System.out.println("removing particle from generation " + generation);
		int index = generations.get(generation).indexOf(p);
		generations.get(generation).set(index, null);
		if (p.parent != null) {
			p.parent.nChildren--;
			if (p.parent.nChildren == 0) {
				removeParticleFromGeneration(p.parent, generation - 1);
			}
		}
	}

	public void nextGeneration() {
		generations.add(new ArrayList<BacktrackingParticle>());
		greatestIndexAssigned = -1;
		currentGeneration++;
	}

}
