package lift_management.agents.behaviours;

import lift_management.agents.Lift;
import sajas.core.behaviours.FSMBehaviour;

public class LiftBehaviour extends FSMBehaviour {
	public static final String MOVING = "Moving";
	public static final String OPEN = "Open";
	public static final String STOP = "Stop";
	public static enum Transitions {
		IDLING,
		TASK_DIFF_FLOOR,
		ARRIVED,
		PASSANGERS_ENTER_LEAVING,
		TASK_SAME_FLOOR
	};
	private Lift lift;
	
	public LiftBehaviour(Lift lift) {
		super(lift);
		this.lift = lift;
		
		registerStates();
		registerTransitions();
	}

	private void registerStates() {
		registerFirstState(new LiftIdleBehaviour(lift), STOP);
		registerState(new LiftIdleBehaviour(lift), STOP);
		registerState(new LiftMovingBehaviour(lift, 17), MOVING);
		registerState(new LiftOpenBehaviour(lift), OPEN);
	}

	private void registerTransitions() {
		registerTransition(STOP, MOVING, Transitions.TASK_DIFF_FLOOR.ordinal());
		registerTransition(MOVING, OPEN, Transitions.ARRIVED.ordinal());
		registerTransition(OPEN, STOP, Transitions.PASSANGERS_ENTER_LEAVING.ordinal());
		registerTransition(STOP, OPEN, Transitions.TASK_SAME_FLOOR.ordinal());
	}
}
