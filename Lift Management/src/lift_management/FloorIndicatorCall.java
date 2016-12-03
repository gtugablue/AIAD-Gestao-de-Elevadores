package lift_management;

public class FloorIndicatorCall extends Call {
	protected int destiny;
	
	public FloorIndicatorCall() {
		
	}
	
	public FloorIndicatorCall(int origin, int destiny) {
		super(origin);
		this.destiny = destiny;
	}

	public int getDestiny() {
		return destiny;
	}
}
