package lift_management.agents;

import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import sajas.core.Agent;

/**
 * Created by Gustavo on 06/10/2016.
 */
public class Lift extends Agent {
	private ContinuousSpace<Object> space;
	public enum DoorState {
		OPEN,
		CLOSED
	};
	private DoorState doorState = DoorState.CLOSED;
	
	public Lift(ContinuousSpace<Object> space) {
		this.space = space;
	}
	
	@Override
	protected void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("Lift");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	public DoorState getDoorState() {
		return doorState;
	}
	
	public NdPoint getPosition() {
		return space.getLocation(this);
	}
}