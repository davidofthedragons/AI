package aStar;

import java.awt.*;

public class Node {

	public static int MOVECOST = 1;
	
	private int x, y;
	private boolean enabled = true;
	Color color = Color.WHITE;
	private Node parent;
	private double cost = 1;
	private double g = 0; //Movement cost to Node
	private double h = 0; //Distance to end
	private double f = 0; //fitness value (g+h)
	
	public Node(int x, int y) {
		this.x=x; this.y=y;
	}
	
	public void reset() {
		g=0.0; h=0.0; f=0.0; cost = 1;
	}
	
	public double getG() {return g;}
	public double getH() {return h;}
	public double getF() {return f;}
	
	public Node getParent() {return parent;}
	public void setParent(Node p) {parent = p;}
	
	public void setCost(double cost) {this.cost = cost;}
	
	public int getX() {return x;}
	public int getY() {return y;}
	
	public boolean isEnabled() {return enabled;}
	public void setEnabled(boolean b) {
		setColor(Color.white);
		enabled = b;
		if(!enabled) setColor(Color.black);
	}
	public void toggleEnabled() {
		if(enabled) setEnabled(false);
		else setEnabled(true);
	}
	
	public Color getColor() {return color;}
	public void setColor(Color c) {color = c;}
	
	public void draw(Graphics g, int size) {
		g.setColor(color);
		g.fillRect(x*size, y*size, size, size);
	}
	
	public Rectangle getBounds(int size) {
		return new Rectangle(x*size, y*size, size, size);
	}
	
/*	public double calculateF(double h) { //uses step cost
		this.h = h;
		g += parent.g + 1;
		return (f = g + h);
	}*/
	public double calcF(Node start, Node goal, boolean step, boolean dist) {
		h = AStarMain.distance(NodePanel.distMethod, this, goal);
		calcG(start, goal, step, dist);
		return(f = g + h);
	}
	
	public void calcG(Node s, Node g, boolean step, boolean dist) {
		this.g = ((step)? (parent.g+cost) : (0)) +
				((dist)? AStarMain.distance(NodePanel.distMethod, s, this) : (0));
	}
	
	public String toString() {
		return "(" + x + ", " + y + ") F: " + getF();
	}
}
