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
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.addComponentListener(new FrameListen());
	}

	public void graph(Calculator calc) {
		graph.points.clear();
		graph.drawAxis();
		f.setVisible(true);
		// Iterate through the functions
		for (int i = 0; i < calc.functions.size(); i++) {
			// check if they contain anything
			if (!calc.functions.get(i).function.equals("")) {
				// iterate through each x pixel
				for (int j = 0; j < graph.getWidth(); j++) {
					// convert the pixel to a scaled number value
					Point<Double> number = graph.pixelToNum(new Point<Integer>(j, 0));
					// set a point with that scaled number and interpret the function at that number
					Point<Double> calculatedPoint = new Point<Double>(number.x, calc.InterpretFunc(i, number.x), calc.functions.get(i).color, j!=0);
					graph.setPoint(calculatedPoint);
				}
			}
		}
		graph.repaint();
	}

	private class FrameListen implements ComponentListener {

		@Override
		public void componentResized(ComponentEvent e) {
			graph.resizeGraph(f.getWidth(), f.getHeight());
			;
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
	public java.util.List<Point<Double>> points;

	public Graph() {
		xBounds = new Point<Integer>(-20, 10);
		yBounds = new Point<Integer>(-10, 10);
		points = new ArrayList<Point<Double>>();

		setBackground(Color.BLACK);
		setSize(800, 800);
		drawAxis();
		repaint();
	}

	public void resizeGraph(int x, int y) {
		super.setSize(x, y);
		repaint();
	}
	
	public void setGraphBounds(int xl, int xh, int yl, int yh) {
		xBounds.x = xl;
		xBounds.y = xh;
		yBounds.x = yl;
		yBounds.y = yh;
	}

	public void drawAxis() {
		// zero hold the pixel position of (0,0) on the canvas and is used to draw the
		// axis
		Point<Integer> zero = numToPixel(new Point<Double>(0.0, 0.0));
		// for loop iterates through each pixel
		for (int i = 0; i < super.getSize().width; i++) {
			// convert the pixel coordinates to actual scaled number coordinates
			Point<Double> axisPoint = pixelToNum(new Point<Integer>(i, zero.y));
			// check if the current number is close enough to an increment of 1 to draw
			// markers
			if (Math.abs(axisPoint.x - (int) (double) axisPoint.x) < 0.05 * (xBounds.y - xBounds.x) / 30) {
				setPoint(pixelToNum(new Point<Integer>(i, zero.y + 1)));
				setPoint(pixelToNum(new Point<Integer>(i, zero.y - 1)));
			}
			setPoint(axisPoint);
		}

		// same as the x axis above
		for (int i = 0; i < super.getSize().height; i++) {
			Point<Double> axisPoint = pixelToNum(new Point<Integer>(zero.x, i));
			if (Math.abs(axisPoint.y - (int) (double) axisPoint.y) < 0.01 * (yBounds.y - yBounds.x) / 30) {
				setPoint(pixelToNum(new Point<Integer>(zero.x + 1, i)));
				setPoint(pixelToNum(new Point<Integer>(zero.x - 1, i)));
			}
			setPoint(axisPoint);
		}
	}

	/**
	 * takes in a point as a pixel position and converts it to a number based on the
	 * bounds of the graph
	 * @param point
	 * @return
	 */
	public Point<Double> pixelToNum(Point<Integer> point) {
		double x = (xBounds.y - xBounds.x) * ((double) point.x / super.getSize().width) + xBounds.x;
		double y = (yBounds.y - yBounds.x) * ((double) point.y / super.getSize().height) + yBounds.x;
		return new Point<Double>(x, y, point.color);
	}

	/**
	 * converts a number coordinate to a pixel coordinate
	 * 
	 * @param point
	 * @return
	 */
	public Point<Integer> numToPixel(Point<Double> point) {
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
		// System.out.println("%x: " + points.get(points.size() - 1).x + "\t%y: " +
		// points.get(points.size() - 1).y);
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
		points.add(new Point<Double>(scaledX, scaledY, point.color, point.isLine));
		// System.out.println("%x: " + points.get(points.size() - 1).x + "\t%y: " +
		// points.get(points.size() - 1).y);
	}

	/**
	 * 
	 * @param g
	 * @param point a point with an xValue between 0 and 1
	 */
	@Override
	public void paint(Graphics g) {
		//keeps the last pixel in memory to draw lines
		Point<Integer> lastPixel = new Point<Integer>(0, 0);
		//Iterate through each of the calculated points
		for (int i = 0; i < points.size(); i++) {
			Point<Double> point = points.get(i);
			//convert the points from number coordinates to pixel coordinates			
			Point<Integer> pixel = new Point<Integer>((int) (point.x * super.getSize().width + 0.5),
					super.getSize().height - (int) (point.y * super.getHeight() + 0.5), point.color, point.isLine);
			
			g.setColor(pixel.color);
			if (pixel.isLine && lastPixel.isLine) {
				g.drawLine(pixel.x, pixel.y, lastPixel.x, lastPixel.y);
			} else {
				g.drawRect(pixel.x, pixel.y, 1, 1);
			}
			lastPixel = pixel;
		}
	}

}
