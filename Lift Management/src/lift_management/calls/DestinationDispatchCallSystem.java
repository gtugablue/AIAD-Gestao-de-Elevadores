package lift_management.calls;

public class DestinationDispatchCallSystem extends CallSystem {

	public DestinationDispatchCallSystem(int numFloors) {
		super(numFloors);
	}

	@Override
	public void makeCall(Call call) throws Exception {
		if (!(call != null && call instanceof DestinationDispatchCall)) {
			throw new Exception("Expected DirectionalCall object instead got " + call.getClass());
		}
		DestinationDispatchCall newCall = (DestinationDispatchCall) call;
		calls[newCall.getOrigin()][newCall.isAscending() ? 0 : 1] = true;
	}

	public boolean toClimb(int floor) {
		return calls[floor][0];
	}

	public boolean toDescend(int floor) {
		return calls[floor][1];
	}

	@Override
	public void resetCall(Call call) throws Exception {
		if (!(call != null && call instanceof DestinationDispatchCall)) {
			throw new Exception("Expected DirectionalCall object instead got " + call.getClass());
		}
		DestinationDispatchCall newCall = (DestinationDispatchCall) call;
		calls[newCall.getOrigin()][newCall.isAscending() ? 0 : 1] = false;
		System.out.println("Reseting: " + call);
	}

	@Override
	public Call newCall(int originFloor, int destinyFloor) {
		return new DestinationDispatchCall(originFloor, destinyFloor);
	}
	
	

}
