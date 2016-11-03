package lift_management;

import java.util.HashMap;

import agents.Lift;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.Contexts;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.continuous.StrictBorders;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

/**
 * Created by Gustavo on 06/10/2016.
 */
public class LiftManagementLauncher extends RepastSLauncher {
    private ContainerController mainContainer;

    public static void main(String[] args) {
        return;
    }

    public String getName() {
        return null;
    }

    protected void launchJADE() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        mainContainer = rt.createMainContainer(p1);
        
        try {
			launchAgents();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
    }
    
    private void launchAgents() throws StaleProxyException {
    	Context<Object> context = Contexts.createContext(Object.class, "context");
    	ContinuousAdder<Object> adder = new SimpleCartesianAdder<Object>();
    	PointTranslator translator = new StrictBorders();
    	ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
    	ContinuousSpace<Object> space = factory.createContinuousSpace("Building", context, adder, translator, 100.0, 100.0);
    	Lift lift = new Lift(space);
		mainContainer.acceptNewAgent("Lift", lift).start();
	}
}