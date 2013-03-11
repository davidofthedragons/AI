package aStar;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

public class NodePanel extends JPanel {

	Node[][]  nodes;
	ArrayList<Node> openList = new ArrayList<Node>();
	ArrayList<Node> closedList = new ArrayList<Node>();

	//private Node best = null;
	private Node s;
	private Node g;
	
	private int iterations = 0;
	
	private Color openColor = Color.pink;
	private Color closedColor = Color.green;
	private Color pathColor = Color.blue;
	private Color startColor = Color.cyan;
	private Color goalColor = Color.yellow;
	
	private static final int OFFX = 0;
	private static final int OFFY = 0;
	
	public boolean diagonals = false;
	public boolean stepCost = false;
	public boolean distanceCost = true;
	public static int distMethod = AStarMain.EUCLIDIAN;
	public static int finderMethod = AStarMain.ITERATIVE;
	
	public NodePanel() {
		init();
		setPreferredSize(new Dimension(AStarMain.NODESCREEN, AStarMain.NODESCREEN));
		setFocusable(true);
		setVisible(true);
		addMouseListener(new ML());
	}
	
	public void init() {
		nodes = new Node[AStarMain.NODES][AStarMain.NODES];
		for(int i=0; i<AStarMain.NODES; i++) {
			for(int j=0; j<AStarMain.NODES; j++) {
				nodes[i][j] = new Node(i, j);
			}
		}
		repaint();
	}
	public void clearNodes() {
		for(int i=0; i<AStarMain.NODES; i++) {
			for(int j=0; j<AStarMain.NODES; j++) {
				nodes[i][j].reset();
				if (nodes[i][j].isEnabled()) {
					nodes[i][j].setColor(Color.white);
				}
			}
		}
		if(s!=null && g!=null) {
			s.setColor(startColor); g.setColor(goalColor);
		}
		repaint();
	}
	
	public void reset() {
		for(int i=0; i<AStarMain.NODES; i++) {
			for(int j=0; j<AStarMain.NODES; j++) {
				nodes[i][j].setEnabled(true);
			}
		}
		repaint();
	}
	
	public void setStart(int x, int y) {s = nodes[x][y]; clearNodes();}
	public void setGoal(int x, int y)  {g = nodes[x][y]; clearNodes();}
	
	public ArrayList<Node> doSearch() {
		openList.clear();
		closedList.clear();
		iterations = 0;
		
		clearNodes();
		ArrayList<Node> path = new ArrayList<Node>();
		try {
			Node n;
			if (finderMethod == AStarMain.RECURSIVE) {
				n = findPath(s, g);
			}
			else {
				n = findPathI(s, g);
			}
			path.add(n);
			Node p = null;
			while(p != s) {
				p = n.getParent();
				path.add(p);
				n = p;
			}
			for(int i=0; i<path.size(); i++) {
				path.get(i).setColor(pathColor);
			}
			AStarMain.updateStatus("Solved in " + iterations + " iterations");
		} catch (StackOverflowError e) {
			AStarMain.updateStatus("Stack Overflow");
		} catch (NullPointerException e) {
			AStarMain.updateStatus("Could not find path");
		}
		s.setColor(startColor);
		g.setColor(goalColor);
		repaint();
		return path;
	}
	
	private void addToOpenList(Node n) {
		n.setColor(openColor);
		openList.add(n);
	}
	private void addToClosedList(Node n) {
		n.setColor(closedColor);
		closedList.add(n);
		openList.remove(n);
	}
	
	private Node findPath(Node start, Node end) {
		if(start == end) return end;
		Node best = null;
		explore(start);
		if(openList.isEmpty()) return start;
		for(int i=0; i<openList.size(); i++) {
			openList.get(i).calcF(s, g, stepCost, distanceCost);
			if(best == null || openList.get(i).getF() < best.getF()) best = openList.get(i);
		}
		addToClosedList(best);
		//System.out.println("Best Node: " + best.toString());
		iterations++;
		repaint();
		return findPath(best, end);
	}
	private Node findPathI(Node start, Node goal) {
		Node n = start;
		while(!closedList.contains(goal)) {
			explore(n);
			if(openList.isEmpty()) return null;
			Node best = null;
			for(int i=0; i<openList.size(); i++) {
				openList.get(i).calcF(start, goal, stepCost, distanceCost);
				if(best == null || openList.get(i).getF() < best.getF()) best = openList.get(i);
			}
			addToClosedList(best);
			n = best;
			repaint();
			iterations++;
			if(best == goal) return best;
		}
		return null;
	}
	
	private void explore(Node start) {
		Node next;
		try {
			next = nodes[start.getX()+1][start.getY()];
			if(next.isEnabled()) {
				if(!openList.contains(next) && !closedList.contains(next)) {
					addToOpenList(next);
					next.setParent(start);
				}
			}
			
		} catch(ArrayIndexOutOfBoundsException e) {}
		try {
			next = nodes[start.getX()-1][start.getY()];
			if(next.isEnabled()) {
				if(!openList.contains(next) && !closedList.contains(next)) {
					addToOpenList(next);
					next.setParent(start);
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {}
		try {
			next = nodes[start.getX()][start.getY()+1];
			if(next.isEnabled()) {
				if(!openList.contains(next) && !closedList.contains(next)) {
					addToOpenList(next);
					next.setParent(start);
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {}
		try {
			next = nodes[start.getX()][start.getY()-1];
			if(next.isEnabled()) {
				if(!openList.contains(next) && !closedList.contains(next)) {
					addToOpenList(next);
					next.setParent(start);
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {}
		if (diagonals) {
			try {
				next = nodes[start.getX() + 1][start.getY()+1];
				if (next.isEnabled()) {
					if(!openList.contains(next) && !closedList.contains(next)) {
						addToOpenList(next);
						next.setParent(start);
						next.setCost(Math.sqrt(2));
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				next = nodes[start.getX() + 1][start.getY()-1];
				if (next.isEnabled()) {
					if(!openList.contains(next) && !closedList.contains(next)) {
						addToOpenList(next);
						next.setParent(start);
						next.setCost(Math.sqrt(2));
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				next = nodes[start.getX() - 1][start.getY()+1];
				if (next.isEnabled()) {
					if(!openList.contains(next) && !closedList.contains(next)) {
						addToOpenList(next);
						next.setParent(start);
						next.setCost(Math.sqrt(2));
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			try {
				next = nodes[start.getX() - 1][start.getY()-1];
				if (next.isEnabled()) {
					if(!openList.contains(next) && !closedList.contains(next)) {
						addToOpenList(next);
						next.setParent(start);
						next.setCost(Math.sqrt(2));
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
		}
	}
	
	public void paint(Graphics g) {
		//g.fillRect(100, 100, 100, 100);
		s.setColor(startColor);
		this.g.setColor(goalColor);
		for(int i=0; i<AStarMain.NODES; i++) {
			for(int j=0; j<AStarMain.NODES; j++) {
				nodes[i][j].draw(g, AStarMain.SIZE);
			}
		}
		g.setColor(Color.black);
		for(int i=0; i<AStarMain.NODES; i++) {
			g.drawLine(i*AStarMain.SIZE, 0, i*AStarMain.SIZE, AStarMain.NODESCREEN);
		}
		for(int i=0; i<AStarMain.NODES; i++) {
			g.drawLine(0, i*AStarMain.SIZE, AStarMain.NODESCREEN, i*AStarMain.SIZE);
		}
	}
	
	private class ML extends MouseAdapter {
	
		String moving = null;
		Point start = null;
		
		public void mousePressed(MouseEvent e) {
			Point p = new Point(e.getPoint().x / AStarMain.SIZE, e.getPoint().y / AStarMain.SIZE);
			if(nodes[p.x][p.y]==s) {
				moving = "s";
			}
			else if(nodes[p.x][p.y]==g) {
				moving = "g";
			}
			else {
				start = p;
			}
		}
		public void mouseReleased(MouseEvent e) {
			Point p = new Point(e.getPoint().x / AStarMain.SIZE, e.getPoint().y / AStarMain.SIZE);
			if(moving != null) {
				if(moving.equals("s") && (nodes[p.x][p.y].isEnabled())) {
					s = nodes[p.x][p.y];
				}
				if(moving.equals("g") && (nodes[p.x][p.y].isEnabled())) {
					g = nodes[p.x][p.y];
				}
				moving = null;
			}
			else {
				if(Math.abs(p.x-start.x) > Math.abs(p.y-start.y)) {
					if(p.x > start.x) {
						for(int i=start.x; i<p.x; i++) {
							nodes[i][start.y].toggleEnabled();
						}
					}
					else {
						for(int i=p.x; i<start.x; i++) {
							nodes[i][start.y].toggleEnabled();
						}
					}
				}
				else {
					if(p.y > start.y) {
						for(int i=start.y; i<p.y; i++) {
							nodes[start.x][i].toggleEnabled();
						}
					}
					else {
						for(int i=p.y; i<start.y; i++) {
							nodes[start.x][i].toggleEnabled();
						}
					}
				}
			}
			repaint();
			clearNodes();
		}
		public void mouseClicked(MouseEvent e) {
		/*	Point p = new Point(AStarMain.round(e.getPoint().x, AStarMain.SIZE),
					AStarMain.round(e.getPoint().y, AStarMain.SIZE));
					*/
			Point p = new Point(e.getPoint().x / AStarMain.SIZE, e.getPoint().y / AStarMain.SIZE);
			nodes[p.x][p.y].toggleEnabled();
			repaint();
		}
	}
}
