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
import lift_management.behaviours.LiftBehaviour;
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
	private List<Pair<Integer, Boolean>> tasks;
	public List<Pair<Integer, Boolean>> getTasks() {
		return tasks;
	}

	private List<ACLMessage> accepts;
	public enum DoorState {
		OPEN,
		CLOSED
	};
	private DoorState doorState = DoorState.CLOSED;

	public Lift(ContinuousSpace<Object> space, float maxWeight) {
		this.space = space;
		this.maxWeight = maxWeight;
		this.tasks = new ArrayList<Pair<Integer, Boolean>>();
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

		addBehaviour(new CNetResponderDispatcher(this));
		addBehaviour(new LiftBehaviour(this));
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
			//System.out.println(cfp);
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
				if (tasks.get(i).getKey() == call.getOrigin() && tasks.get(i).getValue() == call.isAscending())
					return;
			}
			tasks.add(new Pair<Integer, Boolean>(call.getOrigin(), call.isAscending()));
			// result.setPerformative(ACLMessage.INFORM); // TODO
		}

		@Override
		protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
			//System.out.println(myAgent.getLocalName() + ": proposal rejected");
		}

	}

	public void handleTaskComplete() {
		if (tasks.isEmpty())
			return;
		
		tasks.remove(0);

		ACLMessage inform = accepts.get(0).createReply();
		accepts.remove(0);
		inform.setPerformative(ACLMessage.INFORM);
		send(inform);
		System.out.println("SENT INFORM");
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

	public void ascend() {
		space.moveByDisplacement(this, 0, 0.01f);
	}
	
	public void descend() {
		space.moveByDisplacement(this, 0, -0.01f);
	}
}