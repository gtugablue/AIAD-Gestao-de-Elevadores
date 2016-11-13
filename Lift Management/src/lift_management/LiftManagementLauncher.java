package lift_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

/**
 * Created by Gustavo on 06/10/2016.
 */
public class LiftManagementLauncher extends RepastSLauncher {
    private ContainerController mainContainer;
    private List<Lift> lifts = new ArrayList<Lift>();

    public static void main(String[] args) {
        return;
    }

    @Override
    public String getName() {
        return "Lift Management";
    }

    @Override
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
    	for (Lift a : lifts)
    		mainContainer.acceptNewAgent("Lift", a).start();
	}
    
    @Override
    public Context build(Context<Object> context) {
    	ContinuousAdder<Object> adder = new SimpleCartesianAdder<Object>();
    	PointTranslator translator = new StrictBorders();
    	ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
    	ContinuousSpace<Object> space = factory.createContinuousSpace("space", context, adder, translator, 10.0, 10.0);
    	Lift lift = new Lift(space);
    	lifts.add(lift);
    	context.add(lift);
    	space.moveTo(lift, 5, 5);
    	return super.build(context);
    }
}