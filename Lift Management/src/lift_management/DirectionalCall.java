package lift_management;

import lift_management.agents.Lift.Direction;

public class DirectionalCall extends Call {
	protected boolean ascending;
	
	public DirectionalCall() {
		
	}
	
	public DirectionalCall(int origin, boolean up) {
		super(origin);
		this.ascending = up;
	}
	
	public boolean isAscending() {
		return ascending;
	}
	
	public boolean isDescending() {
		return !ascending;
	}
	
	public Direction getDirection() {
		if (ascending)
			return Direction.UP;
		else
			return Direction.DOWN;
	}
	
	@Override
	public String toString() {
		return origin + " " + (this.ascending ? "^" : "v");
	}

	public void setAscending() {
		this.ascending = true;
	}
	
	public void setDescending() {
		this.ascending = false;
	}
	
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}
