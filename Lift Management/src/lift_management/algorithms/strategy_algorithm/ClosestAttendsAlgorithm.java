package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;

public class ClosestAttendsAlgorithm implements LiftAlgorithm {

	@Override
	public int evaluate(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedTask, int maxBuildingFloor, double currentPosition) throws Exception {
		return Math.abs((int)Math.round(currentPosition) - requestedFloor);
	}

}
