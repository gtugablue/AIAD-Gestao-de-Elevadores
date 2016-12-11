package lift_management;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import lift_management.gui.StatisticsPanel;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import lift_management.agents.Building;
import lift_management.agents.Lift;
import lift_management.agents.Lift.Direction;
import lift_management.algorithms.ClosestAttendsAlgorithm;
import lift_management.algorithms.DestinationDispatchAlgorithm;
import lift_management.algorithms.LiftAlgorithm;
import lift_management.algorithms.LookDiskAlgorithm;
import lift_management.calls.CallSystem;
import lift_management.calls.DestinationDispatchCallSystem;
import lift_management.calls.DirectionalCallSystem;
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
import repast.simphony.ui.RSApplication;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization3D.Display3D;
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
    private CallSystem callSystem;
    private LiftAlgorithm algorithm;
    
    public enum Algorithm{DestinationDispatch, LookDisk, ClosestAttends}
    
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
        
        StatisticsPanel.getInstance().run(lifts);
    }
    
    private void launchAgents() throws StaleProxyException {
    	for (int i = 0; i < lifts.size(); i++)
    		mainContainer.acceptNewAgent("Lift " + i, lifts.get(i)).start();
    	mainContainer.acceptNewAgent("Building", building).start();
	}
    
    @Override
    public Context build(Context<Object> context) {
    	try {
    		config = new Config(RunEnvironment.getInstance().getParameters());
    		ContinuousAdder<Object> adder = new SimpleCartesianAdder<Object>();
        	PointTranslator translator = new StrictBorders();
        	ContinuousSpaceFactory factory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
        	ContinuousSpace<Object> space = factory.createContinuousSpace("space", context, adder, translator, config.numLifts + 1, Building.floorHeight * config.numFloors);
        	setupAlgorithm(config.algorithm, config.numFloors);
        	God god = new God(config.numFloors, config.callFrequency, this.callSystem);
        	building = new Building(god, config, this.callSystem);
        	context.add(building);
        	space.moveTo(building, 0, 0);
        	lifts = createLifts(god, config.numLifts, space, context, this.algorithm);
        	return super.build(context);
    	} catch (IllegalParameterException e) {
    		e.printStackTrace();
    		return null;
    	} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    private List<Lift> createLifts(God god, int numLifts, ContinuousSpace<Object> space, Context<Object> context, LiftAlgorithm algorithm) {
    	ArrayList<Lift> lifts = new ArrayList<Lift>();
    	for (int i = 0; i < numLifts; i++) {
    		Class<?> algClass = this.getAlgorithmClass();
    		
    		Lift lift =  new Lift(i, god, space, config.numFloors, config.maxWeights[i], algorithm);
    		lifts.add(lift);
    		context.add(lift);
    		space.moveTo(lift, i + 1, 0);
    	}
    	return lifts;
    }
    
    public void setupAlgorithm(String algorithm, int numFloors) throws Exception{
    	setupAlgorithm(Algorithm.valueOf(algorithm),numFloors);
    }
    
    public void setupAlgorithm(Algorithm algorithm, int numFloors) throws Exception{
    	switch(algorithm){
			case DestinationDispatch:
				this.callSystem = new DestinationDispatchCallSystem(numFloors);
				this.algorithm = new DestinationDispatchAlgorithm();
				break;
			case LookDisk:
				this.callSystem = new DirectionalCallSystem(numFloors);
				this.algorithm = new LookDiskAlgorithm();
				break;
			case ClosestAttends:
				this.callSystem = new DirectionalCallSystem(numFloors);
				this.algorithm = new ClosestAttendsAlgorithm();
				break;
			default:
				throw new Exception ("Unknown algorithm");
		}
    }
    
    public Class<?> getAlgorithmClass(){
    	if(algorithm.getClass().equals(DestinationDispatchAlgorithm.class)){
    		return Integer.class;
    	}else{
    		return Direction.class;
    	}
    }
	
}