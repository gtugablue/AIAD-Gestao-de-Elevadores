package serviceConsumerProvider;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import jade.wrapper.StaleProxyException;

public class Repast3ServiceConsumerProviderLauncher extends Repast3Launcher {

	private int N = 10;

	private int FILTER_SIZE = 5;
	
	private double FAILURE_PROBABILITY_GOOD_PROVIDER = 0.2;
	private double FAILURE_PROBABILITY_BAD_PROVIDER = 0.8;
	
	private int N_CONTRACTS = 100;
	
	public static final boolean USE_RESULTS_COLLECTOR = true;
	
	public static final boolean SEPARATE_CONTAINERS = false;
	private ContainerController mainContainer;
	private ContainerController agentContainer;
	
	public int getN() {
		return N;
	}

	public void setN(int N) {
		this.N = N;
	}

	public int getFILTER_SIZE() {
		return FILTER_SIZE;
	}

	public void setFILTER_SIZE(int FILTER_SIZE) {
		this.FILTER_SIZE = FILTER_SIZE;
	}

	public double getFAILURE_PROBABILITY_GOOD_PROVIDER() {
		return FAILURE_PROBABILITY_GOOD_PROVIDER;
	}

	public void setFAILURE_PROBABILITY_GOOD_PROVIDER(double FAILURE_PROBABILITY_GOOD_PROVIDER) {
		this.FAILURE_PROBABILITY_GOOD_PROVIDER = FAILURE_PROBABILITY_GOOD_PROVIDER;
	}

	public double getFAILURE_PROBABILITY_BAD_PROVIDER() {
		return FAILURE_PROBABILITY_BAD_PROVIDER;
	}

	public void setFAILURE_PROBABILITY_BAD_PROVIDER(double FAILURE_PROBABILITY_BAD_PROVIDER) {
		this.FAILURE_PROBABILITY_BAD_PROVIDER = FAILURE_PROBABILITY_BAD_PROVIDER;
	}

	public int getN_CONTRACTS() {
		return N_CONTRACTS;
	}

	public void setN_CONTRACTS(int N_CONTRACTS) {
		this.N_CONTRACTS = N_CONTRACTS;
	}

	@Override
	public String[] getInitParam() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "Service Consumer/Provider -- SAJaS Repast3 Test";
	}

	@Override
	protected void launchJADE() {
		
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);
		
		if(SEPARATE_CONTAINERS) {
			Profile p2 = new ProfileImpl();
			agentContainer = rt.createAgentContainer(p2);
		} else {
			agentContainer = mainContainer;
		}
		
		launchAgents();
	}
	
	private void launchAgents() {
		
		int N_CONSUMERS = N;
		int N_CONSUMERS_FILTERING_PROVIDERS = N;
		int N_PROVIDERS = 2*N;
		
		try {
			
			AID resultsCollectorAID = null;
			if(USE_RESULTS_COLLECTOR) {
				// create results collector
				ResultsCollector resultsCollector = new ResultsCollector(N_CONSUMERS + N_CONSUMERS_FILTERING_PROVIDERS);
				mainContainer.acceptNewAgent("ResultsCollector", resultsCollector).start();
				resultsCollectorAID = resultsCollector.getAID();
			}
			
			// create providers
			// good providers
			for (int i = 0; i < N_PROVIDERS/2; i++) {
				ProviderAgent pa = new ProviderAgent(FAILURE_PROBABILITY_GOOD_PROVIDER);
				agentContainer.acceptNewAgent("GoodProvider" + i, pa).start();
			}
			// bad providers
			for (int i = 0; i < N_PROVIDERS/2; i++) {
				ProviderAgent pa = new ProviderAgent(FAILURE_PROBABILITY_BAD_PROVIDER);
				agentContainer.acceptNewAgent("BadProvider" + i, pa).start();
			}

			// create consumers
			// consumers that use all providers
			for (int i = 0; i < N_CONSUMERS; i++) {
				ConsumerAgent ca = new ConsumerAgent(N_PROVIDERS, N_CONTRACTS, resultsCollectorAID);
				mainContainer.acceptNewAgent("Consumer" + i, ca).start();
			}
			// consumers that filter providers
			for (int i = 0; i < N_CONSUMERS_FILTERING_PROVIDERS; i++) {
				ConsumerAgent ca = new ConsumerAgent(FILTER_SIZE, N_CONTRACTS, resultsCollectorAID);
				mainContainer.acceptNewAgent("ConsumerF" + i, ca).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * Launching Repast3
	 * @param args
	 */
	public static void main(String[] args) {
		SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new Repast3ServiceConsumerProviderLauncher(), null, false);
	}

}
