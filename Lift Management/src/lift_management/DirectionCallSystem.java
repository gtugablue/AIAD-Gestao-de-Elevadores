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

	@Override
	public void callFloor(int originFloor, int destinyFloor) {
		System.out.println(originFloor + " -> " + destinyFloor);
		if (destinyFloor > originFloor)
			ups[originFloor] = true;
		else if (destinyFloor < originFloor)
			downs[originFloor] = true;
	}
}