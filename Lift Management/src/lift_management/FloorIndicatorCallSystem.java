package lift_management;

public class FloorIndicatorCallSystem extends CallSystem {

	public FloorIndicatorCallSystem(int numFloors) {
		super(numFloors);
	}

	@Override
	public void callFloor(Call call) throws Exception {
		if (!(call != null && call instanceof FloorIndicatorCall)) {
			throw new Exception("Expected DirectionalCall object instaed got " + call.getClass());
		}
		//TODO add call to the system
	}

}
