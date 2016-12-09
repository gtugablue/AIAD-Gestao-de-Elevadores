package lift_management;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class God {
	private static final long serialVersionUID = 6602617123027622789L;
	private List<Human> humans;
	private int numFloors;

	public God(int numFloors) {
		this.humans = Collections.synchronizedList(new ArrayList<Human>());
		this.numFloors = numFloors;
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

	protected static int generateOriginFloor(int numFloors){
		double n = numFloors + 1;

		double groundFloorRate = 0.4;
		double nFloorRate = (1-groundFloorRate)/(n-1);
		double x = random.nextDouble();

		if(x <= groundFloorRate){
			return 0;
		}else{
			return (int) Math.ceil((x-groundFloorRate)/nFloorRate);
		}

	}

	public static Human generateRandomHuman(int maxBuildingFloor){
		return generateRandomHumans(maxBuildingFloor, 1).get(0);
	}

	protected static int generateDestinyFloor(int originFloor, int maxBuildingFloors){
		double nFloorRate, groundFloorRate;
		double n = maxBuildingFloors + 1;

		if(originFloor == 0){
			groundFloorRate = 1/(n-1);
			nFloorRate = groundFloorRate;
		}else{
			groundFloorRate = 0.9d;
			nFloorRate = (1-groundFloorRate)/(n-2);
		}

		double x = random.nextDouble();		
		int destinyFloor;
		if(x <= groundFloorRate){
			destinyFloor = 0;
		}else{
			destinyFloor = (int) Math.ceil((x-groundFloorRate)/nFloorRate);
		}

		if(destinyFloor >= originFloor){
			destinyFloor++;
		}

		return destinyFloor;
	}
	public static List<Human> generateRandomHumans(int maxBuildingFloor, int numHumans) {
		int originFloor = generateOriginFloor(maxBuildingFloor);
		int destinyFloor = generateDestinyFloor(originFloor, maxBuildingFloor);
		System.out.println(originFloor + " -> " + destinyFloor);
		ArrayList<Human> humans = new ArrayList<Human>();
		Human human;
		for (int i = 0; i < numHumans; i++) {
			double weight = generateWeigth();
			human = new Human(weight, originFloor, destinyFloor);
			humans.add(human);
		}
		return humans;
	}

	public static void main(String[] args){
		System.out.println("Floor: "+generateOriginFloor(5));
	}

	public Call generateNewCall() {
		List<Human> humans = generateRandomHumans(numFloors - 1, generateGroupSize());
		addHumans(humans);		
		Human human = humans.get(0);

		//TODO the type of call depends the algorithm
		return new DirectionalCall(human.getOriginFloor(), human.getOriginFloor() < human.getDestinyFloor());
	}

	private static int generateGroupSize() {
		return (int) Math.ceil(Math.pow(2 * Math.random(), 2));
	}

	public static long generateRandomTime(int numFloors) {
		return (long) Math.ceil(1000 * Math.random() / numFloors);
	}

	public void addHumans(List<Human> humans) {
		synchronized (this.humans) {
			this.humans.addAll(humans);
		}
	}

	public List<Human> attendWaitingHumans(int floor, int maxWeight, int liftID) {
		List<Human> humans = new ArrayList<Human>();
		int currWeight = 0;
		synchronized (this.humans) {
			for (Human human : this.humans) {
				if (human.getLiftID() != null)
					continue; // Human already in a lift

				currWeight += human.getWeight();
				if (currWeight > maxWeight)
					break; // Lift is full

				human.setLiftID(liftID);
				humans.add(human);
			}
		}
		return humans;
	}

	/**
	 * 
	 * @param humans
	 * @param currFloor
	 * @return The weight of the humans that left the lift.
	 */
	public int dropoffHumans(List<Human> humans, int currFloor) {
		int weight = 0;
		synchronized (this.humans) {
			Iterator<Human> iter = this.humans.iterator();
			while(iter.hasNext()){
				Human human = iter.next();
				if (currFloor == human.getDestinyFloor()) {
					human.setLiftID(null);
					weight += human.getWeight();
					iter.remove();
				}
			}
		}
		return weight;
	}
}
