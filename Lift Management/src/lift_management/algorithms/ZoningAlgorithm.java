package lift_management.algorithms;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;
import lift_management.models.Task;

public class ZoningAlgorithm extends LiftAlgorithm<Direction> {
	private List<List<Integer>> liftFloors;
	
	public ZoningAlgorithm(List<List<Integer>> liftFloors) {
		this.liftFloors = new ArrayList<List<Integer>>();
	}
	
	@Override
	public int evaluate(List<Task<Direction>> tasks, int requestedFloor, Direction requestedTask, int maxBuildingFloor, int currentPosition, int liftID) throws Exception {
		if (!liftFloors.get(liftID).contains(requestedFloor))
			return Integer.MAX_VALUE;
		else
			return new LookDiskAlgorithm().evaluate(tasks, requestedFloor, requestedTask, maxBuildingFloor, currentPosition, liftID);
	}

	@Override
	public int addNewTask(List<Task<Direction>> tasks, int requestedFloor, Direction requestedDirection, int maxBuildingFloor, int currentPosition) throws Exception {
		return new LookDiskAlgorithm().addNewTask(tasks, requestedFloor, requestedDirection, maxBuildingFloor, currentPosition);
	}

	@Override
	public int attendRequest(List<Task<Direction>> tasks, int requestedFloor, int maxBuildingFloor, int currentPosition) {
		return new LookDiskAlgorithm().attendRequest(tasks, requestedFloor, maxBuildingFloor, currentPosition);
	}

	@Override
	protected Direction getDirection(List<Task<Direction>> tasks, int i, int previousStop) {
		return new LookDiskAlgorithm().getDirection(tasks, i, previousStop);
	}

}
