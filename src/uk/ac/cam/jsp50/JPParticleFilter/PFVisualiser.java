package uk.ac.cam.jsp50.JPParticleFilter;

import java.util.HashSet;
import java.util.Iterator;

import uk.ac.cam.jsp50.JPParticleFilter.PFRecorder.Position;
import uk.ac.cam.jsp50.JPParticleFilter.PFRecorder.Step;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public class PFVisualiser {
	
	public PFFloorPlan floorPlan;
	PFView view;
	PFRecorder recorder;
	
	public PFVisualiser(PFFloorPlan _floorPlan) {
		this.floorPlan = _floorPlan;
		this.view = PFView.getView();
		this.view.setPFCanvasSize(floorPlan.maxX,floorPlan.maxY);
		recorder = PFController.recorder;
		drawFP();
	}
	
	
	public void update(boolean showStep) {
				
		ParticleManager particleManager = recorder.getParticles().getParticleManager();
		view.clearParticles();
		
		HashSet<Step> currentSteps = recorder.lastSteps;
		
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
		
		while (particleManager.hasNextActiveParticle()) try {
			particleManager.nextActiveParticle();
			view.drawParticle(particleManager.getX(), particleManager.getY());
		} catch (ParticleNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		Position position = recorder.getPosition();
		view.drawPosition(position.x, position.y, position.stdev);
				
		view.redraw();
		
	}
	
	private void drawFP() {
		for (Edge e : floorPlan.edges) {
			view.drawFPEdge(e);
		}
	}
	
}
