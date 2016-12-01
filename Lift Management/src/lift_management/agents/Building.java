package lift_management.agents;

import java.util.Vector;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lift_management.onto.ServiceOntology;
import sajas.core.Agent;
import sajas.domain.DFService;
import sajas.proto.ContractNetInitiator;
import sajas.proto.SubscriptionInitiator;

public class Building extends Agent {
	public static float floorHeight = 1f;
	protected int numLifts;
	protected int numFloors;
	protected ACLMessage myCfp;

	private Codec codec;
	private Ontology serviceOntology;

	public Building(int numLifts, int numFloors) {
		this.numLifts = numLifts;
		this.numFloors = numFloors;
	}

	public int getNumLifts() {
		return numLifts;
	}

	public int getNumFloors() {
		return numFloors;
	}

	@Override
	public void setup() {
		// register language and ontology
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);

		// subscribe DF
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("building");
		template.addServices(sd);
		addBehaviour(new DFSubscInit(this, template));

		// prepare cfp message
		myCfp = new ACLMessage(ACLMessage.CFP);
		myCfp.setLanguage(codec.getName());
		myCfp.setOntology(serviceOntology.getName());
		myCfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

		addBehaviour(new CNetInit(this, myCfp));
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

		public CNetInit(Agent owner, ACLMessage cfp) {
			super(owner, cfp);
		}

		@Override
		public Vector prepareCfps(ACLMessage cfp) {
			// TODO
			return super.prepareCfps(cfp);
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
		}

		@Override
		protected void handlePropose(ACLMessage propose, Vector acceptances) {
		}

		@Override
		protected void handleAllResponses(Vector responses, Vector acceptances) {

			ACLMessage response;
			for(Object obj : responses) {
				response = (ACLMessage) obj;
				if (response.getPerformative() == ACLMessage.PROPOSE) {
					// TODO
				}
			}
			// TODO
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			// TODO
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			// TODO
		}

		@Override
		protected void handleAllResultNotifications(Vector resultNotifications) {
		}

	}
}