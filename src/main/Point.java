package main;

import java.awt.Color;

class Point <T>{
	public T x, y;
	public Color color;
	public Point(T x, T y) {
		this.x = x;
		this.y = y;
		color = Color.WHITE;
	}
	
	public Point(T x, T y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", color=" + color + "]";
	}
		
}