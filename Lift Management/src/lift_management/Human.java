package lift_management;

public class Human {
	public static int currentId = 0;
	protected float weight=0;
	protected int destinyFloor=0;
	protected final int id = ++currentId;
	
	
	public Human(float weight, int floor){
		this.weight = weight;
		this.destinyFloor = floor;
	}
	
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public int getDestinydFloor() {
		return destinyFloor;
	}
	public void setDestinyFloor(int desiredFloor) {
		this.destinyFloor = desiredFloor;
	}
	
	public int getId(){
		return id;
	}
}
