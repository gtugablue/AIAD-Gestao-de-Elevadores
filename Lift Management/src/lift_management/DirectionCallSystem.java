package lift_management;

public class DirectionCallSystem extends CallSystem {
	private boolean[] ups;
	private boolean[] downs;
	public DirectionCallSystem(int numFloors) {
		super(numFloors);
		ups = new boolean[numFloors];
		downs = new boolean[numFloors];
	}
	
	public boolean toClimb(int numFloor) {
		return ups[numFloor];
	}
	
	public boolean toDescend(int numFloor) {
		return downs[numFloor];
	}
}
