package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;

public interface LiftAlgorithm {
	
	/**
	 * Dada a lista de tarafas de um elevador, a sua posição actual, o número de pisos do edifício e um pedido , analisa qual é o custo de atender o pedido.
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedTask
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return Retorna a quantidade de pisos percorridos até parar o requestedFloor e atender o pedido do Human
	 * @throws Exception 
	 */
	public int evaluate(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedTask, int maxBuildingFloor, double currentPosition) throws Exception;
}
