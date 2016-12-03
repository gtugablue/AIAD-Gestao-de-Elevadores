package lift_management;

public abstract class Call {
	protected int origin;
	
	public Call() {
		
	}
	
	public Call(int origin) {
		this.origin = origin;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}
}
