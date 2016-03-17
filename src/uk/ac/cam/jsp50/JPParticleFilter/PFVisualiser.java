package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import uk.ac.cam.jsp50.JPParticleFilter.NaiveBacktrackingParticleStore.NaiveBacktrackingParticle;
import uk.ac.cam.jsp50.JPParticleFilter.NaiveBacktrackingParticleStore.NaiveBacktrackingParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.PFRecorder.Position;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;

public class PFVisualiser {
	
	public PFFloorPlan floorPlan;
	PFView view;
	PFRecorder recorder;
	public boolean generatingGif = false; 
	
	public PFVisualiser(PFFloorPlan _floorPlan, boolean generatingGif) throws FileNotFoundException, IOException {
		this.floorPlan = _floorPlan;
		this.generatingGif = generatingGif;
		this.view = this.generatingGif ? new PFGifGeneratingView() : new PFSwingView();
		this.view.setPFCanvasSize(floorPlan.maxX,floorPlan.maxY);
		recorder = PFController.recorder;
		drawFP();
	}
	
	
	public void update(boolean showStep) {
				
		if (generatingGif) drawFP();
		
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
			
			if (recorder.backtracking) {
				NaiveBacktrackingParticleManager backtrackingParticleManager = (NaiveBacktrackingParticleManager)particleManager;
				NaiveBacktrackingParticle oldPoint = backtrackingParticleManager.getParticle().parent;
				int age = 1;
				while (oldPoint != null && oldPoint.parent != null) {
					Step oldStep = new Step(oldPoint.x, oldPoint.y, oldPoint.parent.x, oldPoint.parent.y, false, age);
					view.drawPastStep(oldStep);
					age++;
					oldPoint = oldPoint.parent;
				}
			}
			
		} catch (ParticleNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		Position position = recorder.getPosition();
		view.drawPosition(position.x, position.y, position.stdev);
				
		view.redraw();
		
	}
	
	public void drawFP() {
		for (Edge e : floorPlan.doors) {
			view.drawFPDoor(e);
		}
		for (Edge e : floorPlan.edges) {
			view.drawFPEdge(e);
		}
	}
	
}
