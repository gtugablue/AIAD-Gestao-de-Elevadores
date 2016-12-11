package lift_management.agents;

import java.util.List;
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
import lift_management.Config;
import lift_management.God;
import lift_management.Human;
import lift_management.LiftManagementLauncher;
import lift_management.gui.StatisticsPanel;
import lift_management.calls.Call;
import lift_management.calls.CallSystem;
import lift_management.onto.ServiceOntology;
import lift_management.onto.ServiceProposal;
import lift_management.onto.ServiceProposalRequest;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.WakerBehaviour;
import sajas.domain.DFService;
import sajas.proto.ContractNetInitiator;
import sajas.proto.SubscriptionInitiator;

public class Building extends Agent {
	public static long START_DELAY = 1000;
	public static float floorHeight = 1f;
	private int numLifts;
	private int numFloors;
	private CallSystem callSystem;
	private ACLMessage myCfp;
	private Codec codec;
	private Ontology serviceOntology;
	private God god;
	private Config config;

	public Building(God god, Config config, CallSystem callSystem) {
		this.god = god;
		this.numLifts = config.numLifts;
		this.numFloors = config.numFloors;
		this.callSystem = callSystem;
		this.config = config;
	}

	public God getGod() {
		return god;
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
		Building building = this;
		addBehaviour(new WakerBehaviour(this, START_DELAY) {
			@Override
			public void onWake() {
				addBehaviour(new CyclicBehaviour(building) {
					private long ticksToNextRun;
					private long totalTicks = 0;

					@Override
					public void action() {
						totalTicks++;
						StatisticsPanel.getInstance().updateLiftTimes();
						if (ticksToNextRun > 0)
						{
							ticksToNextRun--;
							return;
						}

						List<Human> humans = god.generateNewHumanGroup();
						Call call = god.generateNewCall(humans);
						addCall(call, humans);

						ticksToNextRun = God.generateRandomTime(numFloors, config.callFrequency);
						God.setCurrentTime(totalTicks);
						StatisticsPanel.getInstance().incTick(totalTicks, god.getAvgWaitTime());
					}
				});
			}
		});
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

	public void addCall(Call call, List<Human> humans) {
		try {
			getCallSystem().makeCall(call);

			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			cfp.setLanguage(codec.getName());
			cfp.setOntology(serviceOntology.getName());
			cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

			addBehaviour(new CNetInit(this, cfp, call, humans));
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
		private List<Human> humans;

		public CNetInit(Agent owner, ACLMessage cfp, Call call, List<Human> humans) {
			super(owner, cfp);
			this.call = call;
			this.humans = humans;
		}

		@Override
		public Vector prepareCfps(ACLMessage cfp) {
			// search provider
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("lift");
			template.addServices(sd);

			try {
				getContentManager().fillContent(cfp, new ServiceProposalRequest("attend-request", call, humans));

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
				//System.err.println(myAgent.getLocalName() + ": no proposal received");
			}
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			// Reassign the task
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			cfp.setLanguage(codec.getName());
			cfp.setOntology(serviceOntology.getName());
			cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
			
			//System.out.println("Building: Reassigning call " + call + ".");
			addBehaviour(new CNetInit(getAgent(), cfp, call, humans));
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			god.removeHumans(humans);
			//System.out.println("Building: Shutting down light for call " + call + ".");
			try {
				if (god.getNumHumansWaitingForCall(call) == 0)
					((Building)getAgent()).getCallSystem().resetCall(call);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void handleAllResultNotifications(Vector resultNotifications) {
		}
	}
}