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
		//TODO add call to the system
	}

	@Override
	public void resetCall(Call call) throws Exception {
		if (!(call != null && call instanceof DestinationDispatchCall)) {
			throw new Exception("Expected DirectionalCall object instead got " + call.getClass());
		}
		//TODO remove call from the system
	}

	@Override
	public Call newCall(int originFloor, int destinyFloor) {
		return new DestinationDispatchCall(originFloor, destinyFloor);
	}
	
	

}
