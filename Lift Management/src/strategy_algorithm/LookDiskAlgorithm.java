package strategy_algorithm;

import java.util.List;

import javafx.util.Pair;

public class LookDiskAlgorithm {
	public enum Task {PICKUP_UP, PICKUP_DOWN, STOP}
	
	public static int analyze(List<Pair<Integer, Task>> tasks, int requestedFloor, Task requestedTask, int maxBuildingFloor, int currentPosition) throws Exception{
		if(requestedTask.equals(Task.STOP)){
			throw new Exception("requestedTask cannot be STOP. Only PICKUP_UP or PICKUP_DOWN");
		}
		if(tasks.isEmpty()){
			return Math.abs(currentPosition-requestedFloor);
		}
		
		int previousStop = currentPosition;
		Task previousStopTask = Task.STOP;
		int floorsTraveled=0;
		for(Pair<Integer, Task> task : tasks){
			if( floorInBetween(previousStop, task.getKey().intValue(), requestedFloor) ){
				break;
			}
			
		}
		
		
		
		
		return floorsTraveled;
	}
	
	protected static boolean floorInBetween(int previous, int next, int n){
		return (previous <= n && n < next) || (previous >= n && n > next);
	}
}


