package main;

import java.awt.Color;

class Point <T>{
	public T x, y;
	public Color color;
	public boolean isLine;
	public Point(T x, T y) {
		this.x = x;
		this.y = y;
		color = Color.WHITE;
		isLine = false;
	}
	
	public Point(T x, T y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
		isLine = false;
	}
	
	public Point(T x, T y, Color color, boolean line) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.isLine = line;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", color=" + color + ", line=" + isLine + "]";
	}		
		
}