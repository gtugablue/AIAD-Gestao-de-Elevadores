package lift_management;

public class FloorIndicatorCallSystem extends CallSystem {

	public FloorIndicatorCallSystem(int numFloors) {
		super(numFloors);
	}

	@Override
	public void makeCall(Call call) throws Exception {
		if (!(call != null && call instanceof DestinyDispatchCall)) {
			throw new Exception("Expected DirectionalCall object instead got " + call.getClass());
		}
		//TODO add call to the system
	}

	@Override
	public void resetCall(Call call) throws Exception {
		if (!(call != null && call instanceof DestinyDispatchCall)) {
			throw new Exception("Expected DirectionalCall object instead got " + call.getClass());
		}
		//TODO remove call from the system
	}

}
