package com.fwcd.fructose.geometry;

public class Circle2D extends Oval2D {
	public Circle2D(Vector2D centerPos, double radius) {
		super(centerPos, radius * 2, radius * 2);
	}
	
	public double radius() {
		return width() / 2;
	}

	@Override
	public Circle2D movedTo(Vector2D pos) {
		return new Circle2D(pos, radius());
	}
}