package lift_management.agents;
import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import lift_management.Human;
import sajas.core.Agent;
 

public class HumanGenerator extends Agent {
	
	/**
	 * ALL UNITS IN lbs
	 */
	public static final double standardVariance = 30.73;
	public static final double mean = 163.72;
	public static final double minimumWeight = 64.50; 
	public static final double maximumWeight = 227.30;
	
	public static Random random = new Random();
	
	public static final double conversionRate = 0.45359237; // one pound is equal to 0.45359237 kg
	
	
	/**
	 * 
	 * @param weight in kg
	 * @return weight in lbs
	 */
	protected static double getKgToLbs(double weight){
		return weight / conversionRate;
	}
	
	protected static double generateWeigth(){
		double weight = random.nextGaussian()*standardVariance+mean;
		
		return getKgToLbs(weight);
	}
	
	protected static int generateFloor(int maxBuildingFloor){
		double rate = 1.25;
		double n = maxBuildingFloor + 1;
		
		RealMatrix coefficients =
			    new Array2DRowRealMatrix(new double[][] { { (rate)/(n-1) + 1, 0}, { 1/(n-1), 1} },
			                       false);
		DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
		RealVector constants = new ArrayRealVector(new double[] { rate/(n-1), 1/(n-1)}, false);
		RealVector solution = solver.solve(constants);
		
		double zeroFloorRate = solution.getEntry(0);
		double nFloorRate = solution.getEntry(1);
		double x = random.nextDouble();
		
		/*
		System.out.println("a: "+solution.getEntry(0));
		System.out.println("b: "+solution.getEntry(1));
		System.out.println("x: "+x);
		*/
		
		
		if(x <= zeroFloorRate){
			return 0;
		}else{
			return (int) Math.ceil((x-zeroFloorRate)/nFloorRate);
		}
		
	}
	
	public static Human generateRandomHuman(int maxBuildingFloor){
		double weight = generateWeigth();
		int originFloor = generateFloor(maxBuildingFloor);
		int destinyFloor = 0;
		return new Human(weight,originFloor, destinyFloor);
	}
	
	public static void main(String[] args){
		System.out.println("Floor: "+generateFloor(5));
		
		
		
	}
}
