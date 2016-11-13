package lift_management;

import java.util.ArrayList;
import java.util.List;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import lift_management.agents.Building;
import lift_management.agents.Lift;
import repast.simphony.context.Context;
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
    private Building building;
    private List<Lift> lifts;

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
    	for (Lift a : lifts)
    		mainContainer.acceptNewAgent("Lift", a).start();
	}
    
    @Override
    public Context build(Context<Object> context) {
    	ContinuousAdder<Object> adder = new SimpleCartesianAdder<Object>();
    	PointTranslator translator = new StrictBorders();
    	ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
    	int numLifts = 4;
    	int numFloors = 15;
    	ContinuousSpace<Object> space = factory.createContinuousSpace("space", context, adder, translator, numLifts, Building.floorHeight * numFloors);
    	building = new Building(numLifts, numFloors);
    	context.add(building);
    	space.moveTo(building, 0, 0);
    	lifts = new ArrayList<Lift>();
    	Lift lift = new Lift(space);
    	lifts.add(lift);
    	context.add(lift);
    	space.moveTo(lift, 1, 0);
    	return super.build(context);
    }
}