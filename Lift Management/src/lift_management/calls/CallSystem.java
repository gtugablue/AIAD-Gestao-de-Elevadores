package lift_management.calls;

import java.util.ArrayList;
import java.util.List;

public abstract class CallSystem {
	private int numFloors;
	boolean calls[][];
	
	public CallSystem(int numFloors)
	{
		this.numFloors = numFloors;
		this.calls = new boolean[numFloors][2];
	}
	
	public int getNumFloors() {
		return numFloors;
	}
	
	public abstract void makeCall(Call call) throws Exception;

	public abstract void resetCall(Call call) throws Exception;
	
	public abstract Call newCall(int originFloor, int destinyFloor);
}
