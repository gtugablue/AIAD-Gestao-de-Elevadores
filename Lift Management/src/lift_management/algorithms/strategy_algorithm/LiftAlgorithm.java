package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;

public interface LiftAlgorithm {
	
	/**
	 * Dada a lista de tarafas de um elevador, a sua posi��o actual, o n�mero de pisos do edif�cio e um pedido , analisa qual � o custo de atender o pedido.
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedTask
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return Retorna a quantidade de pisos percorridos at� parar o requestedFloor e atender o pedido do Human
	 * @throws Exception 
	 */
	public int evaluate(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedTask, int maxBuildingFloor, double currentPosition) throws Exception;
}
