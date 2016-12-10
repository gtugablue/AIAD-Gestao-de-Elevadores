package lift_management.behaviours;

import lift_management.agents.Lift;
import sajas.core.behaviours.WakerBehaviour;

public class LiftOpenBehaviour<T> extends WakerBehaviour {
	private static final long DURATION = 1000;
	private Lift<T> lift;
	
	public LiftOpenBehaviour(Lift<T> lift) {
		super(lift, DURATION);
		this.lift = lift;
	}
	
	@Override
	public int onEnd() {
		lift.closeDoor();
		this.reset();
		return LiftBehaviour.Transitions.PASSANGERS_ENTER_LEAVING.ordinal();
	}
}
