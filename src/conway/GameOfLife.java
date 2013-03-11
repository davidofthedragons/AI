package conway;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GameOfLife extends JFrame {

	private static final long serialVersionUID = 1L;
	Container container;
	Timer timer;
	Random rand;
	
	static int CELLSIZE = 10;
	static final int SCREENX = 800;
	static final int SCREENY = 700;
	static int CELLSX = SCREENX/CELLSIZE;
	static int CELLSY = SCREENY/CELLSIZE;
	
	//static final int TICK = 500;
	static final boolean RANDOM = false;
	
	int speed = 500;
	int generation = 0;
	int population = 0;
	
	JButton startStop;
	JLabel genLabel;
	String genText;
	JLabel popLabel;
	String popText = "Population: ";
	
	public Cell cells[][];
	private Cell changes[][];
	public CellPanel cPanel;
	
	public GameOfLife() {
		super("The Game Of Life");
		setSize(CELLSX*CELLSIZE, CELLSY*CELLSIZE);
		
		container = getContentPane();
		
		
		
		cPanel = new CellPanel();
		this.add(cPanel, BorderLayout.CENTER);
		timer = new Timer(speed, cPanel);
		
		container.add(createGUI(), BorderLayout.NORTH);
		init();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void init() {
		population = 0;
		popLabel.setText(popText + population);
		cells = new Cell[CELLSX][CELLSY];
		for (int i=0; i<CELLSX; i++) {
			for (int j=0; j<CELLSY; j++) {
				//System.out.println("Initializing cells["+i+"]["+j+"]");
				cells[i][j]=new Cell(i*CELLSIZE, j*CELLSIZE);
			}
		}
	}
	
	public void init(int sx, int sy, int side, int seed) {
		//Random rand;
		if (seed != 0) {rand = new Random(seed);}
		else {rand = new Random();}
		init();
		for(int i=sx; i<sx+side; i++) {
			for (int j=sy; j<sy+side; j++) {
				cells[i][j] = new Cell(i*CELLSIZE, j*CELLSIZE, rand.nextBoolean());
				System.out.println("Initializing cells["+i+"]["+j+"]");
			}
		}
		countPopulation();
	}
	
	public JPanel createGUI() {
		JPanel panel = new JPanel();
		genText = "Generation: " + generation;
		genLabel = new JLabel(genText);
		panel.add(genLabel);
		
		startStop = new JButton("Start");
		startStop.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						startStop();
					}
				});
		panel.add(startStop);
		JButton reset = new JButton("Reset");
		reset.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						stop();
						init();
						cPanel.repaint();
						generation = 0;
						genText = "Generation: " + generation;
						genLabel.setText(genText);
					}
					
		
		});
		panel.add(reset);
		JButton random = new JButton("Random");
		random.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						rand = new Random();
						init(CELLSX/4, CELLSY/4, rand.nextInt(CELLSX/2-5)+5,
								rand.nextInt());
						cPanel.repaint();
					}
				});
		panel.add(random);
		JLabel speedLabel = new JLabel("Speed:");
		panel.add(speedLabel);
		JTextField speedField = new JTextField(Integer.toString(1000/speed), 4);
		speedField.setEditable(true);
		speedField.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String sp = e.getActionCommand();
						speed = 1000/Integer.parseInt(sp);
						timer.setDelay(speed);
					}
					
				}
				);
		panel.add(speedField);
		
		JTextField sizeField = new JTextField(Integer.toString(CELLSIZE), 6);
		sizeField.setEditable(true);
		sizeField.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String sp = e.getActionCommand();
						CELLSIZE = Integer.parseInt(sp);
						CELLSX = SCREENX/CELLSIZE;
						CELLSY = SCREENY/CELLSIZE;
						init();
						cPanel.repaint();
					}
					
				}
				);
		JLabel sizeLabel = new JLabel("Cell Size:");
		panel.add(sizeLabel);
		panel.add(sizeField);
		
		popLabel = new JLabel(popText + 0);
		panel.add(popLabel);
		
		return panel;
	}
	
	public void startStop() {
		if(timer.isRunning()) {
			timer.stop();
			startStop.setText("Start");
			return;
		}
		timer.start();
		startStop.setText("Stop");
		//System.out.println("startStop(): Changed text to 'Stop'");
	}
	public void stop() {
		timer.stop();
		startStop.setText("Start");
		//System.out.println("stop(): Changed text to 'Start'");
	}
	
	public boolean countPopulation() {
		int newPop = 0;
		for (int i=0; i<CELLSX; i++) {
			for (int j=0; j<CELLSY; j++) {
				if(cells[i][j].isAlive()) {newPop++;}
			}
		}
		popLabel.setText(popText + newPop);
		population = newPop;
		if(population == 0) {
			stop();
			return false;
		}
		return true;
	}
	
	public boolean toggleCell(boolean state) {
		if(state) {
			//state = false;
			return false;
		}
		//state = true;
		return true;
	}
	
	public void rules() {
		boolean changed = false;
		//boolean changes[][] = new boolean[CELLSX][CELLSY];
		changes= new Cell[CELLSX][CELLSY];
		for (int i=0; i<CELLSX; i++) {
			for (int j=0; j<CELLSY; j++) {
				changes[i][j] = new Cell(cells[i][j].state);
			}
		}
		for (int i=0; i<CELLSX; i++) {
			for (int j=0; j<CELLSY; j++) {
				//Check Neighbors
				int neighbors = 0;
				if (i<CELLSX-1) {
					if (cells[i + 1][j].isAlive())
						neighbors++;
				}
				if (j<CELLSY-1) {
					if (cells[i][j + 1].isAlive())
						neighbors++;
				}
				if (i<CELLSX-1 && j<CELLSY-1) {
					if(cells[i+1][j+1].isAlive())
						neighbors++;
				}
				if (i!=0) {
					if(cells[i-1][j].isAlive())
						neighbors++;
				}
				if (i!=0 && j<CELLSY-1) {
					if (cells[i-1][j+1].isAlive())
						neighbors++;
				}
				if (j!=0) {
					if (cells[i][j - 1].isAlive())
						neighbors++;
				}
				if (i<CELLSX-1 && j!=0) {
					if (cells[i+1][j-1].isAlive())
						neighbors++;
				}
				if (i!=0 && j!=0) {
					if (cells[i - 1][j - 1].isAlive())
						neighbors++;
				}
				//Rule 1: if less than 2, dies
				if(neighbors<2 && cells[i][j].isAlive()) {
					changes[i][j].toggle();
					changed = true;
					//boolean b=cells[i][j].isAlive();
					//b = toggleCell(b);
					//changes[i][j].setAlive(b);
				}
				//Rule 2 applies to everything else
				//Rule 3: if more than 3, dies
				if(neighbors>3 && cells[i][j].isAlive()) {
					changes[i][j].toggle();
					changed = true;
					//boolean b = cells[i][j].isAlive();
					//b = toggleCell(b);
					//changes[i][j].setAlive(b);
				}
				//Rule 4: dead with 3, lives
				if(neighbors==3 && !cells[i][j].isAlive()) {
					changes[i][j].toggle();
					changed = true;
					//boolean b = cells[i][j].isAlive();
					//b = toggleCell(b);
					//changes[i][j].setAlive(b);
				}
			}
		}
		for (int i=0; i<CELLSX; i++) {
			for (int j=0; j<CELLSY; j++) {
				cells[i][j].setAlive(changes[i][j].isAlive());
			}
		}
		if(!changed) {
			//System.out.println("rules(): Stopping timer");
			stop();
		}
	}
	
	private class CellPanel extends JPanel implements ActionListener {
		
		private static final long serialVersionUID = 1L;

		public CellPanel() {
			addMouseListener(new ML());
			setVisible(true);
			setFocusable(true);
			repaint();
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			for (int i=0; i<CELLSX; i++) {
				for (int j=0; j<CELLSY; j++) {
					cells[i][j].draw(g);
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			rules();
			if(countPopulation()) {
				generation++;
				genText = "Generation: " + generation;
				genLabel.setText(genText);
			}
			repaint();
			//System.out.println("CellPanel.actionPerformed(ActionEvent e);");
		}
	}
	
	private class ML extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			//System.out.println("Clicked");
			Point point = e.getPoint();
			boolean b=false; //If it found the point
			for (int i=0; i<CELLSX; i++) {
				for (int j=0; j<CELLSY; j++) {
					if (cells[i][j].getBounds().contains(point)) {
						point.x=i; point.y=j;
						b=true;
						break;
					}
				}
				if(b)break;
			}
			if(!b)return;
			//int cellx = round(point.x, CELLSIZE)/CELLSIZE;
			//int celly = round(point.y, CELLSIZE)/CELLSIZE;
			cells[point.x][point.y].toggle();
			//System.out.println("(" + point.x + "," + point.y + ")");
			repaint();
			countPopulation();
		}
	}
	
	private class Cell {
		boolean state=false;
		int x, y;
		
		private Cell(int x, int y) {
			this.x=x; this.y=y;
		}
		private Cell(boolean alive) {
			state = alive;
			x=0;y=0;
		}
		private Cell(int x, int y, boolean alive) {
			this.x=x; this.y=y; state=alive;
		}
		
		public void draw (Graphics g) {
			if (state) {
				g.fillRect(x, y, CELLSIZE, CELLSIZE);
			}
			else {
				g.drawRect(x, y, CELLSIZE, CELLSIZE);
			}
		}
		public Rectangle getBounds() {
			return new Rectangle(x, y, CELLSIZE, CELLSIZE);
		}
		public boolean isAlive() {
			return state;
		}
		public void toggle() {
			if(state) {
				state=false;
			}
			else {
				state=true;
			}
		}
		public void setAlive(boolean a) {
			state=a;
		}
	}
	
	public static void main(String[] args) {
		new GameOfLife();
	}

	
	public int round(int num, int place) {
		return (int) Math.round((double) num/place)*place;
	}
}
