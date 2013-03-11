package aStar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class AStarMain extends JFrame implements ActionListener {
	
	public static int SIZE = 10;  //size of each cell
	public static int NODESCREEN = 600;  //size of the screen
	public static int NODES = NODESCREEN/SIZE;  //number of cells
	private static NodePanel np = new NodePanel();
	
	private JCheckBox costBox;
	private JCheckBox distCostBox;

	private String help = "A* Search Implementation, (C) David Gardner 2013\n" +
			"The A* search is defined by the function F = G + H where F \nis the 'fitness' of " +
			"each node, G is the step cost or distance from the start node, \nand H is the hueristic value " +
			"(or in this case the distance to the goal node). \n" +
			"Two methods of distance are accomidated: Manhattan distance, \nwhich is defined as (x2-x1)+(y2-y1)" +
			" and Euclidian distance, which is the straight line distance \nbetween the nodes. \n" +
			"Two different algorithms are also provided: a recursive implementation and an iterative implementation. \n" +
			"The recursive implementation tends to cause a stack overflow when the map \nis too complex due to too many function calls. " +
			"Two different methods of calculating G are available: \nthe step cost, which simply sums all of the costs \n" +
			"of nodes in the path; and the distance cost, which uses the selected distance method to calculate the \ndistance from the start.\n" +
			"The Solve button runs the algorithm, the Clear button \nclears the path, and the Reset button re-enables all nodes.\n" +
			"To disable a node, click on it, or drag the mouse to draw a line of disabled nodes. \n" +
			"To move the start and goal nodes, drag them with the mouse. ";
	
	private static String status = "Ready";
	private static JLabel statusField = new JLabel(status);
	
	private static final long serialVersionUID = 1L;
	
	public AStarMain() {
		super("A* Search Implementation");
		setSize(700, 665);
		
		createGUI();
		
		np.setStart(5, 30);
		np.setGoal(54, 30);
		
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void createGUI() {
		setLayout(new BorderLayout());
		add(np, BorderLayout.CENTER);
		
		JPanel north = new JPanel();
		//north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
		JButton solveButton = new JButton("Solve");
		solveButton.setActionCommand("Solve");
		solveButton.addActionListener(this);
		north.add(solveButton);
		JButton clearButton = new JButton("Clear");
		clearButton.setActionCommand("Clear");
		clearButton.addActionListener(this);
		north.add(clearButton);
		JButton resetButton = new JButton("Reset");
		resetButton.setActionCommand("Reset");
		resetButton.addActionListener(this);
		north.add(resetButton);
		JButton helpButton = new JButton("Help");
		helpButton.setActionCommand("Help");
		helpButton.addActionListener(this);
		north.add(helpButton);
		JLabel gLabel = new JLabel("G Calculation:");
		north.add(gLabel);
		costBox = new JCheckBox("Step Cost");
		costBox.setActionCommand("Step Cost");
		costBox.addActionListener(this);
		north.add(costBox);
		distCostBox = new JCheckBox("Distance Cost");
		distCostBox.setSelected(true);
		distCostBox.setActionCommand("Distance Cost");
		distCostBox.addActionListener(this);
		north.add(distCostBox);
		
		JPanel south = new JPanel();
		JLabel statusLabel = new JLabel("Status:");
		south.add(statusLabel);
		statusField.setEnabled(false);
		statusField.setText(status);
		south.add(statusField);
		
		JPanel east = new JPanel();
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		JCheckBox diagBox = new JCheckBox("Diagonals");
		diagBox.setActionCommand("Diagonals");
		diagBox.setSelected(np.diagonals);
		diagBox.addActionListener(this);
		east.add(diagBox);
		JLabel distLabel = new JLabel("|   Distance:");
		east.add(distLabel);
		JRadioButton mButton = new JRadioButton("Manhattan");
		mButton.setActionCommand("Manhattan");
		mButton.addActionListener(this);
		east.add(mButton);
		JRadioButton eButton = new JRadioButton("Euclidian");
		eButton.setSelected(true);
		eButton.setActionCommand("Euclidian");
		eButton.addActionListener(this);
		east.add(eButton);
		ButtonGroup distGroup = new ButtonGroup();
		distGroup.add(mButton);
		distGroup.add(eButton);
		JLabel finderLabel = new JLabel("|   Algorithm:");
		east.add(finderLabel);
		JRadioButton iterButton = new JRadioButton("Iterative");
		iterButton.setActionCommand("Iterative");
		iterButton.addActionListener(this);
		iterButton.setSelected(true);
		east.add(iterButton);
		JRadioButton recuButton = new JRadioButton("Recursive");
		recuButton.setActionCommand("Recursive");
		recuButton.addActionListener(this);
		east.add(recuButton);
		ButtonGroup finderGroup = new ButtonGroup();
		finderGroup.add(iterButton);
		finderGroup.add(recuButton);
		
		add(north, BorderLayout.NORTH);
		add(east, BorderLayout.EAST);
		add(south, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Solve")) {
			updateStatus("Solving...");
			ArrayList<Node> path = np.doSearch();
			for(int i=0; i<path.size(); i++) {
				System.out.println(path.get(i).toString());
			}
		}
		else if(e.getActionCommand().equals("Diagonals")) {
			if(np.diagonals) np.diagonals = false;
			else np.diagonals = true;
		}
		else if(e.getActionCommand().equals("Manhattan")) {
			NodePanel.distMethod = MANHATTAN;
		}
		else if(e.getActionCommand().equals("Euclidian")) {
			NodePanel.distMethod = EUCLIDIAN;
		}
		else if(e.getActionCommand().equals("Iterative")) {
			NodePanel.finderMethod = ITERATIVE;
		}
		else if(e.getActionCommand().equals("Recursive")) {
			NodePanel.finderMethod = RECURSIVE;
		}
		else if(e.getActionCommand().equals("Clear")) {
			np.clearNodes();
			updateStatus("Ready");
		}
		else if(e.getActionCommand().equals("Reset")){
			np.reset();
			updateStatus("Ready");
		}
		else if(e.getActionCommand().equals("Help")) {
			JOptionPane.showMessageDialog(this, help, "Help", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getActionCommand().equals("Step Cost")) {
			np.stepCost = (np.stepCost)? false : true;
		}
		else if(e.getActionCommand().equals("Distance Cost")) {
			np.distanceCost = (np.distanceCost)? false : true;
			/*if(!np.stepCost && !np.distanceCost) {
				np.distanceCost = true;
				distCostBox.setSelected(true);
			}*/
		}
	}
	
	public static void updateStatus(String s) {
		status = s;
		statusField.setText(status);
	}
	
	public static void main(String[] args) {
		new AStarMain();
	}
	
	
	//Utils:
	
	public static final int EUCLIDIAN = 0;
	public static final int MANHATTAN = 1;
	
	public static final int ITERATIVE = 0;
	public static final int RECURSIVE = 1;
	
	public static double distance(int type, Node n1, Node n2) {
		if(type == EUCLIDIAN) {
			return Math.abs(Math.sqrt(Math.pow((n2.getX()-n1.getX()), 2) + Math.pow((n2.getY()-n1.getY()), 2)));
		}
		else if (type == MANHATTAN) {
			return Math.abs((n2.getX() - n1.getX()) + (n2.getY()-n1.getY()));
		}
		return -50;
	}
	
	public static int round(int num, int place) {
		return (int) Math.round((double) num/place)*place;
	}

	

}
