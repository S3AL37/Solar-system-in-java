package sim;

import java.awt.Color;
import java.awt.Graphics;

public class CelestialBody {
	public CelestialBody(double mass, double size, double posX, double posY, double vx, double vy, Color c){
		this.mass=mass;
		this.size=size;
		this.posX=posX;
		this.posY=posY;
		this.vx = vx;
		this.vy = vy;
		this.c = c;
	}
	double mass;
	double size;
	double posX;
	double posY;
	double vx;
	double vy;
	Color c;
	
	public void paint(Graphics g) {
		g.setColor(Color.BLUE);
		
	}
	
	public double getMass() {
		return mass;
	}
	public double getSize() {
		return size;
	}
	public double getX() {
		return posX;
	}
	public double getY() {
		return posY;
	}
	public double getVx() {
		return vx;
	}
	public double getVy() {
		return vy;
	}
	public Color getColor() {
		return c;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public void setX(double posX) {
		this.posX = posX;
	}
	public void setY(double posY) {
		this.posY = posY;
	}
	public void setVx(double vx) {
		this.vx = vx;
	}
	public void setVy(double vy) {
		this.vy = vy;
	}
	public void setColor(Color color) {
		this.c = color;
	}
}
