package lift_management;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import lift_management.agents.Building;
import sajas.core.behaviours.TickerBehaviour;
 

public class HumanGenerator extends TickerBehaviour {
	private static final long serialVersionUID = 6602617123027622789L;
	private Building building;
	public HumanGenerator(Building building) {
		this(building, 1);
	}
	
	public HumanGenerator(Building building, long period) {
		super(building, period);
		this.building = building;
	}

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
	
	protected static int generateFloor(int numFloors){
		double rate = 1.25;
		double n = numFloors;
		
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
		return generateRandomHumans(maxBuildingFloor, 1).get(0);
	}
	
	public static List<Human> generateRandomHumans(int maxBuildingFloor, int numHumans) {
		int originFloor = generateFloor(maxBuildingFloor);
		int destinyFloor = generateFloor(maxBuildingFloor);
		ArrayList<Human> humans = new ArrayList<Human>();
		for (int i = 0; i < numHumans; i++) {
			double weight = generateWeigth();
			Human human = new Human(weight, originFloor, destinyFloor);
			humans.add(human);
		}
		return humans;
	}
	
	public static void main(String[] args){
		System.out.println("Floor: "+generateFloor(5));
	}

	@Override
	protected void onTick() {
		List<Human> humans = generateRandomHumans(building.getNumFloors(), generateGroupSize());
		for (int i = 0; i < humans.size(); i++) {
			Human human = humans.get(i);
			//TODO the type of call depends the algorithm
			building.addCall(new DirectionalCall(human.getOriginFloor(), human.getOriginFloor() < human.getDestinyFloor()));
		}
		reset(generateRandomTime(building.getNumFloors()));
	}
	
	private static int generateGroupSize() {
		  return (int) Math.ceil(Math.pow(2 * Math.random(), 2));
	}
	
	private static long generateRandomTime(int numFloors) {
		return (long) Math.ceil(10000 * Math.random() / numFloors);
	}
}
