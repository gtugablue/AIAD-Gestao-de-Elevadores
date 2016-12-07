package lift_management;

import lift_management.agents.Lift.Direction;

public class DirectionalCall extends Call {
	protected boolean up;
	
	public DirectionalCall() {
		
	}
	
	public DirectionalCall(int origin, boolean up) {
		super(origin);
		this.up = up;
	}
	public boolean isAscending() {
		return up;
	}
	
	public Direction getDirection() {
		if (up)
			return Direction.UP;
		else
			return Direction.DOWN;
	}
	
	@Override
	public String toString() {
		return origin + " " + (this.up ? "^" : "v");
	}
}
