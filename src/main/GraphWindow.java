package main;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class GraphWindow {

	JFrame f;
	Graph graph;
	public GraphWindow() {
		f = new JFrame("Graph");
		graph = new Graph();
		f.add(graph);
		f.setLayout(null);
		f.setSize(800, 800);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.addComponentListener(new FrameListen());
	}
	
	private class FrameListen implements ComponentListener{

		@Override
		public void componentResized(ComponentEvent e) {
			graph.resizeGraph(f.getWidth(), f.getHeight());;		
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}

class Graph extends Canvas {
	public Point<Integer> xBounds;
	public Point<Integer> yBounds;
	// collection of points represented by percentages of their position on the
	// canvas
	private java.util.List<Point<Double>> points;

	public Graph() {
		xBounds = new Point<Integer>(-100, 100);
		yBounds = new Point<Integer>(-10, 10);
		points = new ArrayList<Point<Double>>();
		
		setBackground(Color.BLACK);
		setSize(800, 800);
		for (int i = 0; i < super.getWidth(); i += 1) {
			setPoint(pixelToNum(new Point<Integer>(i,i, Color.GREEN)));
		}
		setPoint(-5, -2);
		System.out.println(pixelToNum(new Point<Integer>(400, 400)));
		drawAxis();
		repaint();
	}
	
	public void resizeGraph(int x, int y) {
		super.setSize(x,y);
		repaint();
	}

	private void drawAxis() {
		Point<Integer> zero = numToPixel(new Point<Double>(0.0,0.0));
		for (int i = 0; i < super.getSize().width; i++) {			
			Point<Double> axisPoint = pixelToNum(new Point<Integer>(i, zero.y));
			if (Math.abs(axisPoint.x - (int)(double)axisPoint.x)< 0.05 * (xBounds.y - xBounds.x) /30) {
				setPoint(pixelToNum(new Point<Integer>(i, zero.y + 1)));
				setPoint(pixelToNum(new Point<Integer>(i, zero.y - 1)));
			}
			setPoint(axisPoint);
		}
		
		for (int i = 0; i < super.getSize().height; i++) {
			Point<Double> axisPoint = pixelToNum(new Point<Integer>(zero.x, i));
			if (Math.abs(axisPoint.y - (int)(double)axisPoint.y)< 0.01 * (yBounds.y - yBounds.x) /30) {
				setPoint(pixelToNum(new Point<Integer>(zero.x + 1, i)));
				setPoint(pixelToNum(new Point<Integer>(zero.x - 1, i)));
			}
			setPoint(axisPoint);
		}
	}

	/**
	 * takes in a point as a pixel position and converts it to a number based on the
	 * bounds of the graph
	 * 
	 * @param point
	 * @return
	 */
	private Point<Double> pixelToNum(Point<Integer> point) {
		double x = (xBounds.y - xBounds.x) * ((double) point.x / super.getSize().width) + xBounds.x;
		double y = (yBounds.y - yBounds.x) * ((double) point.y / super.getSize().height) + yBounds.x;
		return new Point<Double>(x, y, point.color);
	}
	
	private Point<Integer> numToPixel(Point<Double> point){
		double scaledX = (point.x - xBounds.x) / (xBounds.y - xBounds.x);
		double scaledY = (point.y - yBounds.x) / (yBounds.y - yBounds.x);
		Point<Integer> pixel = new Point<Integer>((int) (scaledX * super.getSize().width + 0.5),
				super.getSize().height - (int) (scaledY * super.getHeight() + 0.5), point.color);
		return pixel;
	}

	/**
	 * converts a point to a scaled version between 0 and 1 that describes the %
	 * distance on the canvas based on the bounds of the graph then adds it to be
	 * graphed
	 * 
	 * @param x
	 * @param y
	 */
	public void setPoint(double x, double y) {
		double scaledX = (x - xBounds.x) / (xBounds.y - xBounds.x);
		double scaledY = (y - yBounds.x) / (yBounds.y - yBounds.x);
		points.add(new Point<Double>(scaledX, scaledY));
		//System.out.println("%x: " + points.get(points.size() - 1).x + "\t%y: " + points.get(points.size() - 1).y);
	}

	/**
	 * converts a point to a scaled version between 0 and 1 that describes the %
	 * distance on the canvas based on the bounds of the graph then adds it to be
	 * graphed
	 * 
	 * @param point
	 */
	public void setPoint(Point<Double> point) {
		double scaledX = (point.x - xBounds.x) / (xBounds.y - xBounds.x);
		double scaledY = (point.y - yBounds.x) / (yBounds.y - yBounds.x);
		points.add(new Point<Double>(scaledX, scaledY, point.color));
		//System.out.println("%x: " + points.get(points.size() - 1).x + "\t%y: " + points.get(points.size() - 1).y);
	}

	/**
	 * 
	 * @param g
	 * @param point a point with an xValue between 0 and 1
	 */
	@Override
	public void paint(Graphics g) {

		for (Point<Double> point : points) {
			Point<Integer> pixel = new Point<Integer>((int) (point.x * super.getSize().width + 0.5),
					super.getSize().height - (int) (point.y * super.getHeight() + 0.5), point.color);

			// System.out.println("painting x: " + pixel.x + "\tpainting y: " + pixel.y);
			g.setColor(pixel.color);
			g.drawRect(pixel.x, pixel.y, 1, 1);
		}
	}

}
