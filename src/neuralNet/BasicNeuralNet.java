package neuralNet;

import java.util.Random;

public class BasicNeuralNet {

	private double hWeights[][];
	private double oWeights[][];
	private double in[];
	private double out[];
	private int layers;
	private int inSize, hidSize, outSize;
	private Random rand = new Random();
	
	public BasicNeuralNet(int layers, int inN, int hidN, int outN) {
		this.layers = layers; 
		inSize=inN; hidSize=hidN; outSize=outN;
		hWeights = new double[inSize][hidSize];
		oWeights = new double[hidSize][outSize];
	}
	
	public void randomInit(double lBound, double rBound) {
		for(int i=0; i<inSize; i++) {
			for(int j=0; j<hidSize; j++) {
				hWeights[i][j] = map(0d, 1d, lBound, rBound, rand.nextDouble());
			}
		}
		for(int i=0; i<hidSize; i++) {
			for(int j=0; j<outSize; j++) {
				hWeights[i][j] = map(0d, 1d, lBound, rBound, rand.nextDouble());
			}
		}
	}
	
	public void setHiddenWeighs(double weights[][]) {
		hWeights = weights;
	}
	public void setOutWeigths(double weights[][]) {
		oWeights = weights;
	}
	
	public int calculate() {
		
		
		return max(out);
	}
	
	private double sigmoid(double n) {
		return n;
	}
	
	private int max(double vect[]) {
		int max=0;
		for(int i=0; i<vect.length; i++) {
			if(vect[i]>vect[max]) max=i;
		}
		return max;
	}
	
	public double map(double in_min, double in_max, double out_min, double out_max, double x) {
		 return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
}
