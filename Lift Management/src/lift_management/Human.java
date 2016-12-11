package lift_management;

public class Human {
	public static int currentId = 0;
	protected double weight=0;
	protected int destinyFloor=0;
	protected int originFloor=0;
	protected final int id = ++currentId;
	protected Integer liftID; /** The ID of the lift the human is in, or null if still waiting for one. **/
	
	public Human(double weight, int originFloor, int destinyFloor){
		this.weight = weight;
		this.originFloor = originFloor;
		this.destinyFloor = destinyFloor;
	}
	
	public double getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public int getDestinyFloor() {
		return destinyFloor;
	}
	public void setDestinyFloor(int desiredFloor) {
		this.destinyFloor = desiredFloor;
	}
	
	public int getOriginFloor() {
		return originFloor;
	}

	public void setOriginFloor(int originFloor) {
		this.originFloor = originFloor;
	}

	public int getId(){
		return id;
	}

	public Integer getLiftID() {
		return liftID;
	}

	public void setLiftID(Integer liftID) {
		this.liftID = liftID;
	}
	
	@Override
	public String toString() {
		return getOriginFloor() + "->" + getDestinyFloor() + ((liftID == null) ? "" : (" (L" + liftID + ")"));
	}
}
