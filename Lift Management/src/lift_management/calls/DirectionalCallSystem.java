package lift_management.calls;

public class DirectionalCallSystem extends CallSystem {

	public DirectionalCallSystem(int numFloors) {
		super(numFloors);
	}

	@Override
	public void makeCall(Call call) throws Exception {
		if (!(call != null && call instanceof DirectionalCall)) {
			throw new Exception("Expected DirectionalCall object instead got " + call.getClass());
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

	@Override
	public void resetCall(Call call) throws Exception {
		if (!(call != null && call instanceof DirectionalCall)) {
			throw new Exception("Expected DirectionalCall object instead got " + call.getClass());
		}
		DirectionalCall directionCall = (DirectionalCall) call;
		calls[directionCall.getOrigin()][directionCall.isAscending() ? 0 : 1] = false;
		System.out.println("Reseting: " + call);
	}

	@Override
	public Call newCall(int originFloor, int destinyFloor) {
		return new DirectionalCall(originFloor, originFloor < destinyFloor);
	}
}