package uk.ac.cam.jsp50.JPParticleFilter;

public class NaiveBacktrackingParticleStore extends BacktrackingParticleStore {
	
	public class NaiveBacktrackingParticle extends BacktrackingParticle {

		public NaiveBacktrackingParticle(double _x, double _y, double _w, BacktrackingParticle _parent) {
			this.x = _x;
			this.y = _y;
			this.w = _w;
			this.parent = _parent;
		}
		
		public BacktrackingParticle generateChild() {
			nChildren++;
			return new NaiveBacktrackingParticle(x, y, w, this);
		}
	}
	
	public void addParticle(double x, double y, double w, BacktrackingParticle parent) {
		NaiveBacktrackingParticle newParticle = new NaiveBacktrackingParticle(x, y, w, parent);
		addParticle(newParticle);
	}
	
	public void addParticleAtGeneration(double x, double y, double w, BacktrackingParticle parent, int generation) {
		NaiveBacktrackingParticle newParticle = new NaiveBacktrackingParticle(x, y, w, parent);
		addParticleAtGeneration(newParticle, generation);
	}
	
	@Override
	public ParticleStore getFreshParticleStoreInstance() {
		return new NaiveBacktrackingParticleStore();
	}
	
}
