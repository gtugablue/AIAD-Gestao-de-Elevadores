package lift_management.algorithms;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;
import lift_management.models.Task;

public class ZoningAlgorithm extends LiftAlgorithm<Integer> {
	private List<List<Integer>> liftFloors;
	private DestinationDispatchAlgorithm ddAlgorithm = new DestinationDispatchAlgorithm();
	public static final float ORIGIN_PENALTY = Float.MAX_VALUE/10;
	
	public ZoningAlgorithm(List<List<Integer>> liftFloors) {
		this.liftFloors = liftFloors;
	}
	
	@Override
	public float evaluate(List<Task<Integer>> tasks, int requestedFloor, Integer requestedDestiny, int maxBuildingFloor, int currentPosition, int liftID) throws Exception {
		if (!liftFloors.get(liftID).contains(requestedDestiny))
			return Float.MAX_VALUE;
		else{
			float pontuation =  ddAlgorithm.evaluate(tasks, requestedFloor, requestedDestiny, maxBuildingFloor, currentPosition, liftID);
			
			if(!liftFloors.get(liftID).contains(requestedFloor)){
				pontuation += ORIGIN_PENALTY;
			}
			
			return pontuation;
		}	
	}

	@Override
	public int addNewTask(List<Task<Integer>> tasks, int requestedFloor, Integer requestedDirection, int maxBuildingFloor, int currentPosition) throws Exception {
		return ddAlgorithm.addNewTask(tasks, requestedFloor, requestedDirection, maxBuildingFloor, currentPosition);
	}

	@Override
	public int attendRequest(List<Task<Integer>> tasks, int requestedFloor, int maxBuildingFloor, int currentPosition) {
		return ddAlgorithm.attendRequest(tasks, requestedFloor, maxBuildingFloor, currentPosition);
	}

	@Override
	protected Direction getDirection(List<Task<Integer>> tasks, int i, int previousStop) {
		return ddAlgorithm.getDirection(tasks, i, previousStop);
	}

	
	public static List<List<Integer>> generateZoningByLifts(int numLifts, int numFloors){
		List<List<Integer>> zones = new ArrayList<List<Integer>>(numLifts);
		
		for(int i = 0; i<numLifts; i++){
		    ArrayList<Integer> zone = new ArrayList<Integer>();
		    zone.add(0);
			zones.add(i, zone);
			
		}
		System.out.println(zones.toString());
		
		int nFloorsNumber = numFloors - 1;
		double division = (double) numLifts / nFloorsNumber;
		
		if(division > 1){
			int ceil = (int) Math.ceil(division);
			int lift = 0;
			for(int i = 1; i <= nFloorsNumber; i++){
				for(double j= 0; j <= ceil; j+=division){
					zones.get(lift).add(i);
					lift++;
					if(lift == zones.size()){
	    			    break;
	    			}
				}
				if(lift == zones.size()){
    			    break;
    			}
			}
		}else{
			division = 1/division;
			int ceil = (int) Math.ceil(division);
			int floor = 1;
			
			for(int lift = 0; lift < zones.size(); lift++){
			    
			    for(double j= 0; j <= ceil; j += division){
    				
    				zones.get(lift).add(floor);
        			
        			floor++;
        			if(floor > nFloorsNumber){
        			    break;
        			}
    			}
				
			}
			
			
		}
		
        System.out.println("ZONES " + zones.toString());
		
		
		return zones;
	}
}
