package main;

import java.awt.*;

public class Graph {
	public Graph() {
		Frame f = new Frame("Graph");
		f.add(new MyCanvas());
		f.setLayout(null);
		f.setSize(800, 800);
		f.setVisible(true);
	}

	public static void main(String args[]) {
		new Graph();
	}
}

class MyCanvas extends Canvas {
	public MyCanvas() {
		setBackground(Color.GRAY);
		setSize(800, 800);
	}

	public void paint(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(75, 75, 150, 75);
	}
}
