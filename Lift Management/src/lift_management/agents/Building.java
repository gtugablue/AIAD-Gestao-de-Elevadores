package lift_management.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
import lift_management.Call;
import lift_management.CallSystem;
import lift_management.DirectionCallSystem;
import lift_management.DirectionalCall;
import lift_management.HumanGenerator;
import lift_management.onto.ServiceOntology;
import lift_management.onto.ServiceProposal;
import lift_management.onto.ServiceProposalRequest;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.WakerBehaviour;
import sajas.domain.DFService;
import sajas.proto.ContractNetInitiator;
import sajas.proto.SubscriptionInitiator;

public class Building extends Agent {
	public static float floorHeight = 1f;
	private int numLifts;
	private int numFloors;
	private CallSystem callSystem;
	private ACLMessage myCfp;
	private Codec codec;
	private Ontology serviceOntology;
	private Map<String, Integer> liftCurrentOpenDoors;
	private Map<String, WakerBehaviour> liftDoorCloserBehaviours; 
	public Building(int numLifts, int numFloors) {
		this.numLifts = numLifts;
		this.numFloors = numFloors;
		this.callSystem = new DirectionCallSystem(this.numFloors);
		this.liftCurrentOpenDoors = new HashMap<String, Integer>();
		this.liftDoorCloserBehaviours = new HashMap<String, WakerBehaviour>();
	}

	public int getNumLifts() {
		return numLifts;
	}

	public int getNumFloors() {
		return numFloors;
	}

	public CallSystem getCallSystem() {
		return callSystem;
	}

	@Override
	public void setup() {
		register();
		subscribeDf();
		prepareCfpMessage();

		//addBehaviour(new CNetInit(this, myCfp));
		addBehaviour(new HumanGenerator(this));
	}

	private void prepareCfpMessage() {
		myCfp = new ACLMessage(ACLMessage.CFP);
		myCfp.setLanguage(codec.getName());
		myCfp.setOntology(serviceOntology.getName());
		myCfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
	}

	private void subscribeDf() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("building");
		template.addServices(sd);
		addBehaviour(new DFSubscInit(this, template));
	}

	/**
	 * Register Language and ontology
	 */
	private void register() {
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);
	}

	public void addCall(Call call) {
		try {
			getCallSystem().makeCall(call);

			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			cfp.setLanguage(codec.getName());
			cfp.setOntology(serviceOntology.getName());
			cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

			addBehaviour(new CNetInit(this, cfp, call));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class DFSubscInit extends SubscriptionInitiator {

		private static final long serialVersionUID = 1L;

		DFSubscInit(Agent agent, DFAgentDescription dfad) {
			super(agent, DFService.createSubscriptionMessage(agent, getDefaultDF(), dfad, null));
		}

		protected void handleInform(ACLMessage inform) {
			// TODO
		}

	}

	private class CNetInit extends ContractNetInitiator {

		private static final long serialVersionUID = 1L;
		private Call call;

		public CNetInit(Agent owner, ACLMessage cfp, Call call) {
			super(owner, cfp);
			this.call = call;
		}

		@Override
		public Vector prepareCfps(ACLMessage cfp) {
			// search provider
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("lift");
			template.addServices(sd);
			try {
				getContentManager().fillContent(cfp, new ServiceProposalRequest("attend-request", call));

				DFAgentDescription[] dfads = null;
				try {
					dfads = DFService.search(myAgent, template);
				} catch (FIPAException e) {
					e.printStackTrace();
				}
				if(dfads != null && dfads.length > 0) {
					for (int i = 0; i < dfads.length; i++) {
						AID aid = (AID) dfads[i].getName();
						cfp.addReceiver(aid);
					}
				}
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
			return super.prepareCfps(cfp);
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
		}

		@Override
		protected void handlePropose(ACLMessage propose, Vector acceptances) {
			//System.out.println(propose);
		}

		@Override
		protected void handleAllResponses(Vector responses, Vector acceptances) {
			double servicePrice;
			double bestServicePrice = Double.MAX_VALUE;
			ACLMessage response;
			ACLMessage bestServiceProposalMessage = null;
			for(Object obj : responses) {
				response = (ACLMessage) obj;
				if (response.getPerformative() == ACLMessage.PROPOSE) {

					try {
						servicePrice = ((ServiceProposal) getContentManager().extractContent(response)).getPrice();
						if(servicePrice < bestServicePrice) {
							// new best proposal
							if(bestServiceProposalMessage != null) {
								// reject previous best
								ACLMessage reject = bestServiceProposalMessage.createReply();
								reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
								acceptances.add(reject);
							}
							// update best
							bestServicePrice = servicePrice;
							bestServiceProposalMessage = response;
						} else {
							// reject proposal
							ACLMessage reject = response.createReply();
							reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
							acceptances.add(reject);
						}
					} catch (CodecException | OntologyException e) {
						e.printStackTrace();
					}
				}
			}

			if(bestServiceProposalMessage != null) {
				// accept winner
				ACLMessage accept = bestServiceProposalMessage.createReply();
				accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				acceptances.add(accept);
			} else {
				System.err.println(myAgent.getLocalName() + ": no proposal received");
			}
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			// TODO
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			System.out.println("INFORM " + call);
			try {
				((Building)getAgent()).getCallSystem().resetCall(call);
				((Building)getAgent()).setLiftDoorState(inform.getSender().getLocalName(), Lift.DoorState.OPEN, call.getOrigin());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void handleAllResultNotifications(Vector resultNotifications) {
		}

	}

	public void setLiftDoorState(String liftName, Lift.DoorState doorState, int floor) {
		Map<String, Integer> liftCurrentOpenDoors = this.liftCurrentOpenDoors;
		if (doorState.equals(Lift.DoorState.OPEN)) {
			Map<String, WakerBehaviour> liftDoorCloserBehaviours = this.liftDoorCloserBehaviours;
			liftCurrentOpenDoors.put(liftName, floor);
			if (liftDoorCloserBehaviours.containsKey(liftName)) {
				this.removeBehaviour(this.liftDoorCloserBehaviours.get(liftName));
				liftDoorCloserBehaviours.remove(liftName);
			}
			WakerBehaviour b = new WakerBehaviour(this, Lift.DOOR_OPEN_TIME) {
				@Override
				protected void onWake() {
					liftCurrentOpenDoors.remove(liftName);
					liftDoorCloserBehaviours.remove(this);
				}
			};
			this.addBehaviour(b);
			this.liftDoorCloserBehaviours.put(liftName, b);
		}
	}

	public Integer getOpenLiftDoor(String liftName) {
		return this.liftCurrentOpenDoors.get(liftName);
	}
}