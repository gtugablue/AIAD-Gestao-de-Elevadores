package lift_management;

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
	
	@Override
	public String toString() {
		return origin + " " + (this.up ? "UP" : "DOWN");
	}
}
