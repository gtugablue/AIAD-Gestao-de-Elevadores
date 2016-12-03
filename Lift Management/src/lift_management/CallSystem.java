package lift_management;

public abstract class CallSystem {
	private int numFloors;
	
	public CallSystem(int numFloors)
	{
		this.numFloors = numFloors;
	}
	
	public int getNumFloors() {
		return numFloors;
	}
	
	public abstract void callFloor(int originFloor, int destinyFloor);
}
