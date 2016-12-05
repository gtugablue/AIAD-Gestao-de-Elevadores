package lift_management.agents;

import sajas.core.Agent;
import sajas.core.behaviours.WakerBehaviour;

public class LiftOpenBehaviour extends WakerBehaviour {
	public static final long DURATION = 20000;
	private Lift lift;
	
	public LiftOpenBehaviour(Lift lift) {
		super(lift, 2000);
	}
	
	@Override
	public void handleElapsedTimeout() {
		// TODO
	}
}
