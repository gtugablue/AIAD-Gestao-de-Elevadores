package lift_management;

public class Human {
	public static int currentId = 0;
	protected double weight=0;
	protected int destinyFloor=0;
	protected int originFloor=0;
	protected final int id = ++currentId;
	
	
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
}
