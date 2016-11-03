package agents;

import repast.simphony.space.continuous.ContinuousSpace;
import sajas.core.Agent;

/**
 * Created by Gustavo on 06/10/2016.
 */
public class Lift extends Agent {
	private ContinuousSpace<Object> space;
	
	public Lift(ContinuousSpace<Object> space) {
		this.space = space;
	}
}