package uk.ac.cam.jsp50.JPParticleFilter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.*;

import uk.ac.cam.jsp50.JPParticleFilter.PFRecorder.Step;

public class PFSwingView extends PFView {
	
	JFrame frame;
	PFCanvasPanel canvas;
	int xmargin = 10;
	int ymargin = 10;
	int canvas_xmax = 500;
	int canvas_ymax = 500;
	double scale;
	
	private class PFCanvasPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public Collection<Line2D> particles;
		public Collection<Line2D> FP;
		public Collection<Line2D> steps;
		public Collection<Line2D> violations;
		public Line2D position;
		
		public Collection<Line2D> next_particles;
		public Collection<Line2D> next_steps;
		public Collection<Line2D> next_violations;
		public Line2D next_position;

		public PFCanvasPanel() {
			setBorder(BorderFactory.createLineBorder(Color.black));
			setBackground(Color.white);
			this.steps = new HashSet<Line2D>();
			this.particles = new HashSet<Line2D>();
			this.violations = new HashSet<Line2D>();
		}
		
		public Dimension getPreferredSize() {
	        return new Dimension(250,200);
	    }
		
		private void drawFP(Graphics2D g) {
			g.setColor(Color.blue);
			for (Line2D l : FP) {
				g.draw(l);
			}
		}

		private void drawParticles(Graphics2D g) {
			synchronized (g) {
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.black);
				Iterator<Line2D> iterator = particles.iterator();
				while (iterator.hasNext()) {
					g.draw(iterator.next());
				}
				g.setStroke(new BasicStroke(1));
			}
		}
		
		private void drawSteps(Graphics2D g) {
			g.setColor(Color.green);
			for (Line2D l : steps) {
				g.draw(l);
			}
			
			g.setColor(Color.red);
			for (Line2D l : violations) {
				g.draw(l);
			}
		}
		
		private void drawPosition(Graphics2D g) {
			synchronized (g) {
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.red);
				g.draw(position);
				g.setStroke(new BasicStroke(1));
			}
		}
		
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);       
	        Graphics2D g2d = (Graphics2D) g;
	        
	        drawFP(g2d);
	        drawSteps(g2d);
	        drawParticles(g2d);
	        drawPosition(g2d);
	    } 
	    
	    public void loadNext() {
	    	this.particles = this.next_particles;
	    	next_particles = new HashSet<Line2D>();
	    	this.steps = this.next_steps;
	    	next_steps = new HashSet<Line2D>();
	    	this.violations = this.next_violations;
	    	next_violations = new HashSet<Line2D>();
	    	this.position = next_position;
	    }
	}
	
	public PFSwingView() {
		canvas = new PFCanvasPanel();
		canvas.FP = new HashSet<Line2D>();
		clearParticles();
		
		frame = new JFrame("Particle filter");
		frame.setBounds(0, 0, xmargin + canvas_xmax, ymargin + canvas_ymax);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void setPFCanvasSize(double maxX, double maxY) {
		double xscale = canvas_xmax / maxX;
		double yscale = canvas_ymax / maxY;
		
		scale = (xscale < yscale) ? xscale : yscale;
		if (scale == Double.NEGATIVE_INFINITY || scale == Double.POSITIVE_INFINITY || scale == Double.NaN) {
			scale = 50;
		}
		System.out.println("Scaling points by by a factor of " + scale);
		
	}

	@Override
	public void drawStep(Step s) {
		Line2D stepLine = new Line2D.Double(s.x1 * scale, s.y1 * scale, s.x2 * scale, s.y2 * scale);
		canvas.next_steps.add(stepLine);
	}

	@Override
	public void drawParticle(double x, double y) {
		Line2D particlePoint = new Line2D.Double(x * scale, y * scale, x * scale, y * scale);
		canvas.next_particles.add(particlePoint);
	}

	@Override
	public void drawFPEdge(Edge e) {
		Line2D edgeLine = new Line2D.Double(e.x1 * scale, e.y1 * scale, e.x2 * scale, e.y2 * scale);
		canvas.FP.add(edgeLine);
	}

	@Override
	public void clearParticles() {
		canvas.next_steps = new HashSet<Line2D>();
		canvas.next_violations = new HashSet<Line2D>();
		canvas.next_particles = new HashSet<Line2D>();
	}

	@Override
	public void redraw() {
		canvas.loadNext();
		canvas.repaint();
	}

	@Override
	public void drawViolation(Step s) {
		Line2D stepLine = new Line2D.Double(s.x1 * scale, s.y1 * scale, s.x2 * scale, s.y2 * scale);
		canvas.next_violations.add(stepLine);
	}

	@Override
	public void drawPosition(double x, double y, double stdev) {
		canvas.next_position = new Line2D.Double(x * scale, y * scale, x * scale, y * scale);
	}

	
}
