package lift_management;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lift_management.agents.Lift.Direction;
import repast.simphony.engine.schedule.Schedule;


public class God {
	private static final long serialVersionUID = 6602617123027622789L;
	private List<Human> humans;
	private int numFloors;
	private int callFrequency;
	private long totalCountWaits;
	private long sumWaits;
	private static double currentTime;

	public God(int numFloors, int callFrequency) {
		this.humans = Collections.synchronizedList(new ArrayList<Human>());
		this.numFloors = numFloors;
		this.callFrequency = callFrequency;
		currentTime = 0;
		totalCountWaits = 0;
		sumWaits = 0;
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
		return weight * conversionRate;
	}

	protected static double generateWeight(){
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
		ArrayList<Human> humans = new ArrayList<Human>();
		Human human;
		for (int i = 0; i < numHumans; i++) {
			double weight = generateWeight();
			human = new Human(weight, originFloor, destinyFloor, currentTime);
			humans.add(human);
		}
		System.out.println("God: generated " + numHumans + " humans (" + originFloor + "->" + destinyFloor + ").");
		return humans;
	}

	public static void main(String[] args){
		System.out.println("Floor: " + generateOriginFloor(5));
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

	public static long generateRandomTime(int numFloors, int callFrequency) {
		return (long) Math.ceil((1000000 * Math.random()) / (numFloors * callFrequency));
	}

	public void addHumans(List<Human> humans) {
		synchronized (this.humans) {
			this.humans.addAll(humans);
		}
	}

	public List<Human> attendWaitingHumans(int floor, int maxWeight, int liftID, boolean[] possibleDestinies) {
		List<Human> humans = new ArrayList<Human>();
		int currWeight = 0;
		synchronized (this.humans) {
			for (Human human : this.humans) {
				/*if (!possibleDestinies[human.getDestinyFloor()]) {
					// TODO recall
					continue; // The lift destination is different from the human destination
				}*/
				if (human.getLiftID() != null)
					continue; // Human already in a lift
				
				if (human.getOriginFloor() != floor)
					continue; // Human not in the same floor as the lift
				
				totalCountWaits++;
				sumWaits += (currentTime - human.getCallTick());
				
				currWeight += human.getWeight();
				if (currWeight > maxWeight) { // Lift is full
					break;
				}

				human.setLiftID(liftID);
				humans.add(human);
			}
		}
		System.out.println("Lift " + liftID + ": Picked up " + humans.size() + " humans on floor " + floor + ", leaving " + getNumHumansInFloor(floor) + " waiting");
		return humans;
	}

	/**
	 * 
	 * @param currFloor
	 * @return The humans that left the lift.
	 */
	public List<Human> dropoffHumans(int liftId, int currFloor) {
		List<Human> removed = new ArrayList<Human>();
		synchronized (this.humans) {
			Iterator<Human> iter = this.humans.iterator();
			while(iter.hasNext()){
				Human human = iter.next();
				Integer humanLiftId = human.getLiftID();
				if (humanLiftId != null && humanLiftId.equals(liftId) && currFloor == human.getDestinyFloor()) {
					human.setLiftID(null);
					iter.remove();
					removed.add(human);
				}
			}
		}
		System.out.println("Lift " + liftId + ": Dropped off " + removed.size() + " humans on floor " + currFloor + ".");
		return removed;
	}
	
	public int getNumHumansInLift(int liftId) {
		int n = 0;
		synchronized (this.humans) {
			for (Human human : this.humans) {
				Integer humanLiftId = human.getLiftID();
				if (humanLiftId != null && humanLiftId.equals(liftId))
					n++;
			}
		}
		return n;
	}
	
	public int getNumHumansInFloor(int floor) {
		int n = 0;
		synchronized (this.humans) {
			for (Human human : this.humans) {
				if (human.getLiftID() == null && human.getOriginFloor() == floor)
					n++;
			}
		}
		return n;
	}

	public double getAvgWaitTime() {
		if (totalCountWaits == 0) {
			return 0;
		}
		return sumWaits / totalCountWaits;
	}
	
	public static void setCurrentTime(long ticks) {
		currentTime = ticks;
	}
}
