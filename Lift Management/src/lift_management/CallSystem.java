package lift_management;

import java.util.ArrayList;
import java.util.List;

public abstract class CallSystem {
	private int numFloors;
	
	public CallSystem(int numFloors)
	{
		this.numFloors = numFloors;
	}
	
	public int getNumFloors() {
		return numFloors;
	}
	
	public abstract void makeCall(Call call) throws Exception;

	public abstract void resetCall(Call call) throws Exception;
}
