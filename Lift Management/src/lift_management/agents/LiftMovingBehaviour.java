package lift_management.agents;

import sajas.core.Agent;
import sajas.core.behaviours.TickerBehaviour;

public class LiftMovingBehaviour extends TickerBehaviour {
	private Lift lift;
	
	public LiftMovingBehaviour(Lift lift, long period) {
		super(lift, period);
		this.lift = lift;
	}
	
	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		
	}
}
