package lift_management.behaviours;

import lift_management.agents.Lift;
import sajas.core.behaviours.Behaviour;

public class LiftIdleBehaviour extends Behaviour {
	private Lift lift;

	public LiftIdleBehaviour(Lift lift) {
		super(lift);
		this.lift = lift;
	}

	@Override
	public void action() {
		//System.out.println(lift.getLocalName() + ": ACTION START " + lift.getTasks());
		if (lift.getTasks().isEmpty()) {
			//block();
		}
		//System.out.println(lift.getLocalName() + ": ACTION END " + lift.getTasks());
	}

	@Override
	public boolean done() {
		return !lift.getTasks().isEmpty();
	}
	
	@Override
	public int onEnd() {
		if (lift.getTasks().get(0).getFloor() == (int) Math.round(lift.getPosition().getY())) {
			lift.handleTaskComplete();
			return LiftBehaviour.Transitions.TASK_SAME_FLOOR.ordinal();
		} else
			return LiftBehaviour.Transitions.TASK_DIFF_FLOOR.ordinal();
	}
}