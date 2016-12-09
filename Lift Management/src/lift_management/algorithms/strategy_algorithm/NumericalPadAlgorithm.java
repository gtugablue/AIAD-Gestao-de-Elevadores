package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;
import lift_management.models.Task;

public class NumericalPadAlgorithm implements LiftAlgorithm<Integer>{

	@Override
	public int evaluate(List<Task<Integer>> tasks, int requestedFloor, Direction requestedTask,
			int maxBuildingFloor, int currentPosition) throws Exception {
		return 0;
	}

	@Override
	public int addNewTask(List<Task<Integer>> tasks, int requestedFloor, Direction requestedDirection,
			int maxBuildingFloor, int currentPosition) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int attendRequest(List<Task<Integer>> tasks, int requestedFloor, int maxBuildingFloor,
			int currentPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

}
