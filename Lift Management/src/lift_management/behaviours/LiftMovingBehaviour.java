package lift_management.behaviours;

import lift_management.agents.Lift;
import sajas.core.behaviours.TickerBehaviour;

public class LiftMovingBehaviour extends TickerBehaviour {
	private Lift lift;
	
	public LiftMovingBehaviour(Lift lift, long period) {
		super(lift, period);
		this.lift = lift;
	}
	
	@Override
	protected void onTick() {
		double delta = 0.01;
		double y = lift.getPosition().getY();
		int targetedFloor = lift.getTasks().get(0).getKey();

		if (targetedFloor > y - delta && targetedFloor < y + delta) {
			lift.handleTaskComplete();
			stop();
			return;
		}

		if (targetedFloor > y)
			lift.ascend();
		else
			lift.descend();
	}
		
	@Override
	public int onEnd() {
		return LiftBehaviour.Transitions.ARRIVED.ordinal();
	}
}
