package uk.ac.cam.jsp50.JPParticleFilter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class PFGifGeneratingView extends PFView {

	private BufferedImage currentFrame;
	private GifSequenceWriter writer;
	private ImageOutputStream outputStream;
	private final int imageType = BufferedImage.TYPE_INT_RGB;
	private final int timeBetweenFramesMS = 500;
	private int height, width;
	private int scale = 100;
	
	public PFGifGeneratingView() throws FileNotFoundException, IOException {
		outputStream = new FileImageOutputStream(new File("run.gif"));
		writer = new GifSequenceWriter(outputStream, imageType, timeBetweenFramesMS, true);
		//currentFrame = new BufferedImage(width, height, imageType);
	}
	
	@Override
	public void setPFCanvasSize(double maxX, double maxY) {
		width = ((int)maxX + 1)*scale;
		height = ((int)maxY + 1)*scale;
		currentFrame = new BufferedImage(width, height, imageType);
		Graphics2D drawer = currentFrame.createGraphics() ;
		drawer.setBackground(Color.WHITE);
		drawer.clearRect(0,0,width,height);
	}

	@Override
	public void drawStep(Step s) {
		Graphics2D g2d = currentFrame.createGraphics();
		Line2D stepLine = new Line2D.Double();
		stepLine.setLine(s.x1*scale, s.y1*scale, s.x2*scale, s.y2*scale);
		g2d.setColor(Color.green);
		g2d.draw(stepLine);
	}

	@Override
	public void drawViolation(Step s) {
		Graphics2D g2d = currentFrame.createGraphics();
		Line2D stepLine = new Line2D.Double();
		stepLine.setLine(s.x1*scale, s.y1*scale, s.x2*scale, s.y2*scale);
		g2d.setColor(Color.red);
		g2d.draw(stepLine);
	}

	@Override
	public void drawPastStep(Step s) {
		Graphics2D g2d = currentFrame.createGraphics();
		Line2D stepLine = new Line2D.Double();
		stepLine.setLine(s.x1*scale, s.y1*scale, s.x2*scale, s.y2*scale);
		g2d.setColor(getColorForAge(s.age));
		g2d.draw(stepLine);
	}

	private Color getColorForAge(int age) {
		return Color.yellow;
	}
	
	@Override
	public void drawParticle(double x, double y) {
		Graphics2D g2d = currentFrame.createGraphics();
		g2d.setStroke(new BasicStroke(3));
		Line2D stepLine = new Line2D.Double();
		stepLine.setLine(x*scale, y*scale, x*scale, y*scale);
		g2d.setColor(Color.black);
		g2d.draw(stepLine);
	}

	@Override
	public void drawPosition(double x, double y, double stdev) {
		Graphics2D g2d = currentFrame.createGraphics();
		g2d.setStroke(new BasicStroke(4));
		Line2D stepLine = new Line2D.Double();
		stepLine.setLine(x*scale, y*scale, x*scale, y*scale);
		g2d.setColor(Color.red);
		g2d.draw(stepLine);
	}

	@Override
	public void drawFPEdge(Edge e) {
		Graphics2D g2d = currentFrame.createGraphics();
		Line2D stepLine = new Line2D.Double();
		stepLine.setLine(e.x1*scale, e.y1*scale, e.x2*scale, e.y2*scale);
		g2d.setColor(Color.blue);
		g2d.draw(stepLine);
	}

	@Override
	public void drawFPDoor(Edge e) {
		Graphics2D g2d = currentFrame.createGraphics();
		Line2D stepLine = new Line2D.Double();
		stepLine.setLine(e.x1*scale, e.y1*scale, e.x2*scale, e.y2*scale);
		g2d.setColor(Color.green);
		g2d.draw(stepLine);
	}

	@Override
	public void clearParticles() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redraw() { // write frame to image and make new
		try {
			writer.writeToSequence(currentFrame);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentFrame = new BufferedImage(width, height, imageType);
		Graphics2D drawer = currentFrame.createGraphics() ;
		drawer.setBackground(Color.WHITE);
		drawer.clearRect(0,0,width,height);
	}

	public void endGif() throws IOException {
		redraw();
		writer.close();
		outputStream.close();
	}

}
