package lift_management;

import javafx.util.Pair;

public class DirectionCallSystem extends CallSystem {
	
	boolean calls[][];

	public DirectionCallSystem(int numFloors) {
		super(numFloors);
		calls = new boolean[numFloors][2];
	}

	@Override
	public void callFloor(Call call) throws Exception {
		if (!(call != null && call instanceof DirectionalCall)) {
			throw new Exception("Expected DirectionalCall object instaed got " + call.getClass());
		}
		DirectionalCall directionCall = (DirectionalCall) call;
		calls[directionCall.getOrigin()][directionCall.isAscending() ? 0 : 1] = true;
	}

	public boolean toClimb(int floor) {
		return calls[floor][0];
	}

	public boolean toDescend(int floor) {
		return calls[floor][1];
	}
}