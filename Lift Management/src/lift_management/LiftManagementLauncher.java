package lift_management;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import lift_management.agents.Building;
import lift_management.agents.Lift;
import lift_management.gui.ConfigFrame;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.IllegalParameterException;
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
    private Building building;
    private List<Lift> lifts;
    private Config config;
    
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
    	mainContainer.acceptNewAgent("Building", building).start();
    	for (int i = 0; i < lifts.size(); i++)
    		mainContainer.acceptNewAgent("Lift " + i, lifts.get(i)).start();
	}
    
    @Override
    public Context build(Context<Object> context) {
    	try {
    		config = new Config(RunEnvironment.getInstance().getParameters());
    		ContinuousAdder<Object> adder = new SimpleCartesianAdder<Object>();
        	PointTranslator translator = new StrictBorders();
        	ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
        	ContinuousSpace<Object> space = factory.createContinuousSpace("space", context, adder, translator, config.numLifts + 1, Building.floorHeight * config.numFloors);
        	building = new Building(config.numLifts, config.numFloors);
        	context.add(building);
        	space.moveTo(building, 0, 0);
        	lifts = createLifts(config.numLifts, space, context);
        	return super.build(context);
    	} catch (IllegalParameterException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    private List<Lift> createLifts(int numLifts, ContinuousSpace<Object> space, Context<Object> context) {
    	ArrayList<Lift> lifts = new ArrayList<Lift>();
    	for (int i = 0; i < numLifts; i++) {
    		Lift lift = new Lift(space, config.maxWeights[i], config.numFloors);
    		lifts.add(lift);
    		context.add(lift);
    		space.moveTo(lift, i + 1, 0);
    	}
    	return lifts;
    }
}