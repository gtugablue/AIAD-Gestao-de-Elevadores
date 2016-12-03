package lift_management.agents;

import sajas.domain.DFService;
import sajas.proto.ContractNetResponder;
import sajas.proto.SSContractNetResponder;
import sajas.proto.SSResponderDispatcher;

import java.util.ArrayList;
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
	private List<Pair<Integer, Boolean>> tasks;
	public enum DoorState {
		OPEN,
		CLOSED
	};
	private DoorState doorState = DoorState.CLOSED;

	public Lift(ContinuousSpace<Object> space, float maxWeight) {
		this.space = space;
		this.maxWeight = maxWeight;
		this.tasks = new ArrayList<Pair<Integer, Boolean>>();
	}

	@Override
	protected void setup() {
		// register language and ontology
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);

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

		public CNetResponderDispatcher(Agent agent) {
			super(agent, 
					MessageTemplate.and(
							ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
							MessageTemplate.MatchOntology(ServiceOntology.ONTOLOGY_NAME)));
		}

		@Override
		protected Behaviour createResponder(ACLMessage cfp) {
			return new CNetResp(myAgent, cfp);
		}

	}


	private class CNetResp extends SSContractNetResponder {

		private static final long serialVersionUID = 1L;

		private boolean expectedSuccessfulExecution;

		public CNetResp(Agent a, ACLMessage cfp) {
			super(a, cfp);
		}

		@Override
		protected ACLMessage handleCfp(ACLMessage cfp) {
			System.out.println(cfp);
			ACLMessage reply = cfp.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			try {
				getContentManager().fillContent(reply, new ServiceProposal("attend-request", 100));
			} catch (CodecException | OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return reply;
		}

		@Override
		protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
			System.out.println(myAgent.getLocalName() + ": proposal accepted");
			ACLMessage result = accept.createReply();

			DirectionalCall call;
			try {
				call = (DirectionalCall) ((ServiceProposalRequest) getContentManager().extractContent(cfp)).getCall();
				tasks.add(new Pair<Integer, Boolean>(call.getOrigin(), call.isAscending()));
				// result.setPerformative(ACLMessage.INFORM); // TODO
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
				result.setPerformative(ACLMessage.FAILURE);
			}

			return result;
		}

		@Override
		protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
			System.out.println(myAgent.getLocalName() + ": proposal rejected");
		}

	}

	public class TickHandler extends TickerBehaviour {

		public TickHandler(Lift lift, long period) {
			super(lift, period);
		}

		@Override
		protected void onTick() {
			if (tasks.isEmpty())
				return;

			Pair<Integer, Boolean> task = tasks.get(0);
			int destinyFloor = task.getKey();
			boolean up = task.getValue();
			double location = space.getLocation(myAgent).getY();
			System.out.println(destinyFloor + " " + location);
			if (destinyFloor > location && location < 14.989d)
				space.moveByDisplacement(myAgent, 0, 0.01f);
			else if (destinyFloor < location && location > 0.011d)
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

	public void assignTask(int originFloor, boolean up) {
		this.tasks.add(new Pair<Integer, Boolean>(originFloor, up));
		// TODO place in the correct position
	}

	public void assignTask(int originFloor, int destinyFloor) {
		// TODO
	}
}