package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;

public class ClosestAttendsAlgorithm implements LiftAlgorithm {

	@Override
	public int evaluate(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedTask, int maxBuildingFloor, int currentPosition) throws Exception {
		return Math.abs((int)Math.round(currentPosition) - requestedFloor);
	}

	@Override
	public int addNewTask(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedDirection,
			int maxBuildingFloor, int currentPosition) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int attendRequest(List<Pair<Integer, Direction>> tasks, int requestedFloor, int maxBuildingFloor,
			int currentPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

}
