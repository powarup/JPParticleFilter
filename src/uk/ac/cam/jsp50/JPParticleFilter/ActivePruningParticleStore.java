package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.ArrayList;

public class ActivePruningParticleStore extends BacktrackingParticleStore {

	public class ActivePruningBacktrackingParticle extends BacktrackingParticle {
		public int generation;
		public int nChildren = 0;

		public ActivePruningBacktrackingParticle(double _x, double _y, double _w, BacktrackingParticle _parent) {
			this.x = _x;
			this.y = _y;
			this.w = _w;
			this.parent = _parent;
			this.generation = currentGeneration;
		}

		public ActivePruningBacktrackingParticle(double _x, double _y, double _w, BacktrackingParticle _parent, int _generation) {
			this.x = _x;
			this.y = _y;
			this.w = _w;
			this.parent = _parent;
			this.generation = _generation;
		}

		public BacktrackingParticle generateChild() {
			nChildren++;
			return new ActivePruningBacktrackingParticle(x, y, w, this);
		}
	}

	public void addParticle(double x, double y, double w, BacktrackingParticle parent) {
		ActivePruningBacktrackingParticle newParticle = new ActivePruningBacktrackingParticle(x, y, w, parent);
		addParticle(newParticle);
	}

	public void addParticleAtGeneration(double x, double y, double w, BacktrackingParticle parent, int generation) {
		ActivePruningBacktrackingParticle newParticle = new ActivePruningBacktrackingParticle(x, y, w, parent);
		addParticleAtGeneration(newParticle, generation);
	}

	public void removeParticle(ActivePruningBacktrackingParticle particle) {
		ArrayList<BacktrackingParticle> generation = generations.get(particle.generation);
		int index = generation.indexOf(particle);
		generation.set(index, null);
		ActivePruningBacktrackingParticle parent = (ActivePruningBacktrackingParticle)particle.parent;
		if (parent != null) {
			parent.nChildren--;
			if (parent.nChildren == 0) removeParticle(parent);
		}
	}

	@Override
	public ParticleStore getFreshParticleStoreInstance() {
		return new ActivePruningParticleStore();
	}


}
