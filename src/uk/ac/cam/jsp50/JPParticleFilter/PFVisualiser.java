package uk.ac.cam.jsp50.JPParticleFilter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import uk.ac.cam.jsp50.JPParticleFilter.BacktrackingParticleStore.BacktrackingParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.PFRecorder.Position;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleManager;
import uk.ac.cam.jsp50.JPParticleFilter.ParticleStore.ParticleNotFoundException;
import uk.ac.cam.jsp50.JPParticleFilter.StringyBacktrackingParticleStore.StringyBacktrackingParticle;

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

		particleManager = recorder.getParticles().getParticleManager();

		while (particleManager.hasNextActiveParticle()) try {
			particleManager.nextActiveParticle();
			view.drawParticle(particleManager.getX(), particleManager.getY());

			if (recorder.backtrackingScheme != PFBacktrackingScheme.NONE) {
				BacktrackingParticleManager backtrackingParticleManager = (BacktrackingParticleManager)particleManager;
				int age = 1;

				switch (recorder.backtrackingScheme) {
				case NAIVE:
				case ACTIVE_PRUNING:
					BacktrackingParticle oldNaivePoint = backtrackingParticleManager.getParticle().parent;
					while (oldNaivePoint != null && oldNaivePoint.parent != null) {
						Step oldStep = new Step(oldNaivePoint.x, oldNaivePoint.y, oldNaivePoint.parent.x, oldNaivePoint.parent.y, false, age);
						view.drawPastStep(oldStep);
						age++;
						oldNaivePoint = oldNaivePoint.parent;
					}
					break;

				case STRINGY:
					StringyBacktrackingParticle oldParticle = (StringyBacktrackingParticle) backtrackingParticleManager.getParticle();
					Step oldStep;
					age = 0;
					double x,y, lastX = -1, lastY = -1;
					while (oldParticle != null) {
						x = oldParticle.x;
						y = oldParticle.y;
						if (age > 0) {
							oldStep = new Step(lastX, lastY, x, y, false, age);
							view.drawPastStep(oldStep);
						}
						lastX = x;
						lastY = y;
						age++;
						for (int index = oldParticle.historyX.size() -1; index >= 0; index--) {
							x = oldParticle.historyX.get(index);
							y = oldParticle.historyY.get(index);
							oldStep = new Step(lastX, lastY, x, y, false, age);
							view.drawPastStep(oldStep);
							lastX = x;
							lastY = y;
							age++;
						}
						oldParticle = (StringyBacktrackingParticle) oldParticle.parent;
					}
					break;
				default:
					break;
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
		if (floorPlan.doors != null) for (Edge e : floorPlan.doors) {
			view.drawFPDoor(e);
		}
		for (Edge e : floorPlan.edges) {
			view.drawFPEdge(e);
		}
	}

}
