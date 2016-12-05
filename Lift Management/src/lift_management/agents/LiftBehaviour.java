package lift_management.agents;

import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.FSMBehaviour;
import sajas.core.behaviours.TickerBehaviour;
import sajas.core.behaviours.WakerBehaviour;

public class LiftBehaviour extends FSMBehaviour {
	public static final String MOVING = "Moving";
	public static final String OPEN = "Open";
	public static final String STOP = "Stop";
	private Lift lift;
	
	public LiftBehaviour(Lift lift) {
		super(lift);
		this.lift = lift;
		
		Behaviour b;
		
		b = new LiftMovingBehaviour(lift, 17);
		b = new LiftOpenBehaviour(lift);
		
	}
}
