package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.HashSet;
import java.util.Iterator;

import uk.ac.cam.jsp50.JPParticleFilter.FloorPlan.Edge;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public class PFVisualiser {
	
	public ParticleStore particles;
	public FloorPlan floorPlan;
	PFView view;
	HashSet<Step> currentSteps;
	
	public PFVisualiser(ParticleStore _particles, FloorPlan _floorPlan) {
		this.particles = _particles;
		this.floorPlan = _floorPlan;
		this.view = PFView.getView();
		this.view.setPFCanvasSize(floorPlan.maxX,floorPlan.maxY);
		drawFP();
		currentSteps = new HashSet<Step>();
	}
	
	public class Step {
		double x1, y1, x2, y2;
		boolean violation;
		public Step(double x1, double y1, double x2, double y2, boolean violation) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.violation = violation;
		}
	}
	
	public void addStep(double x1, double y1, double x2, double y2, boolean violation) {
		currentSteps.add(new Step(x1, y1, x2, y2,violation));
	}
	
	public void update(boolean showStep) {
				
		ParticleManager particleManager = particles.getParticleManager();
		view.clearParticles();
		
		if (showStep) {
			Step step;
			Iterator<Step> stepIterator = currentSteps.iterator();
			while (stepIterator.hasNext()) {
				step = stepIterator.next();
				if (step.violation) {
					view.drawViolation(step);
				} else view.drawStep(step);
			}
		}
		
		currentSteps = new HashSet<Step>();
		
		int i = 0;
		while (particleManager.hasNextActiveParticle()) try {
			i++;
			particleManager.nextActiveParticle();
			view.drawParticle(particleManager.getX(), particleManager.getY());
		} catch (ParticleNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		//System.out.println("Passed view " + i + " particles.");
		
		view.redraw();
		
//		Iterator<Particle> it = particles.getIterator();
//		Particle p;
//		view.clearParticles();
//		while (it.hasNext()) {
//			p = it.next();
//			if (showStep) {
//				view.drawStep(p);
//				if (p.w > 0) view.drawParticle(p);
//			} else {
//				view.drawParticle(p);
//			}
//		}
//		view.redraw();
	}
	
	private void drawFP() {
		for (Edge e : floorPlan.edges) {
			view.drawFPEdge(e);
		}
	}
	
}
