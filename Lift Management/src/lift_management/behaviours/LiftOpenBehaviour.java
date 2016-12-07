package lift_management.behaviours;

import lift_management.agents.Lift;
import sajas.core.behaviours.WakerBehaviour;

public class LiftOpenBehaviour extends WakerBehaviour {
	private static final long DURATION = 500;
	
	public LiftOpenBehaviour(Lift lift) {
		super(lift, DURATION);
	}
	
	@Override
	public int onEnd() {
		return LiftBehaviour.Transitions.PASSANGERS_ENTER_LEAVING.ordinal();
	}
}
