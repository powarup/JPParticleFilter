package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.ArrayList;

public class StringyBacktrackingParticleStore extends BacktrackingParticleStore {

	public class StringyBacktrackingParticle extends BacktrackingParticle {
		public ArrayList<Double> historyX, historyY;
		private ArrayList<WeightRange> historyW;
		public int nChildren = 0;

		private class WeightRange { // stores weight value and last generation at which weight was stored for this particle
			public double weight;
			public int step;

			public WeightRange(double weight, int step) {
				this.weight = weight;
				this.step = step;
			}
		}

		public StringyBacktrackingParticle(double _x, double _y, double _w, BacktrackingParticle _parent) {
			this.x = _x;
			this.y = _y;
			this.w = _w;
			this.parent = _parent;
			historyX = new ArrayList<Double>();
			historyY = new ArrayList<Double>();
			historyW = new ArrayList<WeightRange>();
		}

		@Override
		public void displace(StepVector s) {
			pushToHistory();

			double dx = s.length * Math.sin(Math.toRadians(s.angle));
			double dy = s.length * Math.cos(Math.toRadians(s.angle));;

			this.x += dx;
			this.y += dy;
		}

		public void reweight(double weight) {
			if (weight == 0) {
				removeParticle(this);
			}
			WeightRange endedRange = new WeightRange(w, historyX.size());
			historyW.add(endedRange);
			w = weight;
		}

		private void pushToHistory() {
			historyX.add(x);
			historyY.add(y);
		}

		@Override
		public BacktrackingParticle generateChild() {
			nChildren++;
			return new StringyBacktrackingParticle(x, y, w, this);
		}


	}

	@Override
	public void addParticle(double x, double y, double w, BacktrackingParticle parent) {
		StringyBacktrackingParticle newParticle = new StringyBacktrackingParticle(x, y, w, parent);
		addParticle(newParticle);
	}
	
	@Override
	public void addParticleAtGeneration(double x, double y, double w, BacktrackingParticle parent, int generation) {
		StringyBacktrackingParticle newParticle = new StringyBacktrackingParticle(x, y, w, parent);
		addParticleAtGeneration(newParticle, generation);
	}
	
	public void removeParticle(BacktrackingParticle p) {
		removeParticleFromGeneration(p, currentGeneration);
	}

	public void removeParticleFromGeneration(BacktrackingParticle p, int generation) {
		//System.out.println("removing particle from generation " + generation);
		int index = generations.get(generation).indexOf(p);
		generations.get(generation).set(index, null);
		StringyBacktrackingParticle parent = (StringyBacktrackingParticle) p.parent;
		if (parent != null) {
			parent.nChildren--;
			if (parent.nChildren == 0) {
				removeParticleFromGeneration(p.parent, generation - 1);
			}
		}
	}
	
	@Override
	public ParticleStore getFreshParticleStoreInstance() {
		return new StringyBacktrackingParticleStore();
	}

}
