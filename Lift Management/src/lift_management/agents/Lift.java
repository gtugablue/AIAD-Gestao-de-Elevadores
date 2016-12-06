package lift_management.agents;

import sajas.domain.DFService;
import sajas.proto.ContractNetResponder;
import sajas.proto.SSContractNetResponder;
import sajas.proto.SSResponderDispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import lift_management.DirectionalCall;
import lift_management.HumanGenerator;
import lift_management.LiftManagementLauncher;
import lift_management.algorithms.strategy_algorithm.ClosestAttendsAlgorithm;
import lift_management.onto.ServiceOntology;
import lift_management.onto.ServiceProposal;
import lift_management.onto.ServiceProposalRequest;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.TickerBehaviour;

/**
 * Created by Gustavo on 06/10/2016.
 */
public class Lift extends Agent {
	private Codec codec;
	private Ontology serviceOntology;
	private ContinuousSpace<Object> space;
	private float maxWeight;
	//TODO improve data structures for stops and tasks
	private List<Integer> stops = new ArrayList<Integer>();
	private List<Pair<Integer, Direction>> tasks;
	private List<ACLMessage> accepts;
	private int numFloors;
	public enum DoorState {
		OPEN,
		CLOSED
	};
	public enum Direction {UP, DOWN, STOP};
	private DoorState doorState = DoorState.CLOSED;

	public Lift(ContinuousSpace<Object> space, int numFloors, float maxWeight) {
		this.space = space;
		this.numFloors = numFloors;
		this.maxWeight = maxWeight;
		this.tasks = new ArrayList<Pair<Integer, Direction>>();
		this.accepts = new ArrayList<ACLMessage>();
	}

	@Override
	protected void setup() {
		register();

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addProtocols(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("lift");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// behaviours
		addBehaviour(new CNetResponderDispatcher(this));
		addBehaviour(new TickHandler(this, 17));
	}

	/**
	 * Register language and ontology
	 */
	private void register() {
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	private class CNetResponderDispatcher extends SSResponderDispatcher {

		private static final long serialVersionUID = 1L;

		public CNetResponderDispatcher(Lift agent) {
			super(agent, 
					MessageTemplate.and(
							ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
							MessageTemplate.MatchOntology(ServiceOntology.ONTOLOGY_NAME)));
		}

		@Override
		protected Behaviour createResponder(ACLMessage cfp) {
			return new CNetResp((Lift)myAgent, cfp);
		}

	}


	private class CNetResp extends SSContractNetResponder {

		private static final long serialVersionUID = 1L;

		private boolean expectedSuccessfulExecution;

		public CNetResp(Lift a, ACLMessage cfp) {
			super(a, cfp);
		}

		@Override
		protected ACLMessage handleCfp(ACLMessage cfp) {
			//System.out.println(cfp);
			ACLMessage reply = cfp.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			try {
				Lift lift = (Lift)myAgent;
				DirectionalCall call = (DirectionalCall)((ServiceProposalRequest)getContentManager().extractContent(cfp)).getCall();
				int price = new ClosestAttendsAlgorithm().evaluate(lift.tasks, call.getOrigin(), call.isAscending() ? Direction.UP : Direction.DOWN, numFloors, lift.getPosition().getY());
				getContentManager().fillContent(reply, new ServiceProposal("attend-request", price));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return reply;
		}

		@Override
		protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
			//System.out.println(myAgent.getLocalName() + ": proposal accepted");
			ACLMessage result = accept.createReply();

			DirectionalCall call;
			try {
				//TODO generalize to different calls
				call = (DirectionalCall) ((ServiceProposalRequest) getContentManager().extractContent(cfp)).getCall();
				addRequest(call);
				accepts.add(accept);
				return null;
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
				result.setPerformative(ACLMessage.FAILURE);
			}

			return result;
		}
		
		private void addRequest(DirectionalCall call) {
			for (int i = 0; i < tasks.size(); i++) {
				if (tasks.get(i).getKey() == call.getOrigin() && tasks.get(i).getValue().equals(call.getDirection()))
					return;
			}
			tasks.add(new Pair<Integer, Direction>(call.getOrigin(), call.getDirection())); // TODO insert in the right place
		}

		@Override
		protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
			//System.out.println(myAgent.getLocalName() + ": proposal rejected");
		}

	}

	public class TickHandler extends TickerBehaviour {

		public TickHandler(Lift lift, long period) {
			super(lift, period);
		}

		@Override
		protected void onTick() {
			if (tasks.isEmpty() && stops.isEmpty())
				return;
			
			double delta = 0.01;
			double y = space.getLocation(myAgent).getY();
			int destinyFloor = (int) Math.round(y);

			if (!stops.isEmpty() && stops.get(0) > y - delta && stops.get(0) < y + delta) {
				stops.remove(0);
			}
			
			if (!tasks.isEmpty() && tasks.get(0).getKey() > y - delta && tasks.get(0).getKey() < y + delta) {
				tasks.remove(0);
				ACLMessage inform = accepts.get(0).createReply();
				accepts.remove(0);
				inform.setPerformative(ACLMessage.INFORM);
				getAgent().send(inform);
				System.out.println("SENT INFORM");
			}
			
			if (!stops.isEmpty()) {
				destinyFloor = stops.get(0);
			} else if (!tasks.isEmpty()) {
				destinyFloor = tasks.get(0).getKey();
			}
			
			if (destinyFloor > y - delta && destinyFloor < y + delta)
				return;

			double location = space.getLocation(myAgent).getY();
			if (destinyFloor - location > 0)
				space.moveByDisplacement(myAgent, 0, 0.01f);
			else if (destinyFloor < location)
				space.moveByDisplacement(myAgent, 0, -0.01f);

		}

	}

	public DoorState getDoorState() {
		return doorState;
	}

	public NdPoint getPosition() {
		return space.getLocation(this);
	}

	public float getMaxWeight() {
		return this.maxWeight;
	}

	public void assignTask(int originFloor, Direction direction) {
		this.tasks.add(new Pair<Integer, Direction>(originFloor, direction));
		// TODO place in the correct position
	}

	public void assignTask(int originFloor, int destinyFloor) {
		// TODO
	}
}