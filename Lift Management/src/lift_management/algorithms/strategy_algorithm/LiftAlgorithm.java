package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;

public interface LiftAlgorithm<T>{
	
	
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
	public int evaluate(List<Pair<Integer,T>> tasks, int requestedFloor, Direction requestedTask, int maxBuildingFloor, int currentPosition) throws Exception;
	
	
	/**
	 * Altera a lista de tasks passada como par�metro e insere o novo pedido na lista ordenada de tasks
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedDirection
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return posi��o onde foi
	 * @throws Exception
	 */
	public int addNewTask(List<Pair<Integer, T>> tasks, int requestedFloor, Direction requestedDirection, int maxBuildingFloor, int currentPosition) throws Exception;

	
	/**
	 * Executa o atendimento de uma paragem adicionando a paragem na lista de tarefas. Prioritiza o atendimento da tarefa.
	 * @param tasks
	 * @param requestedFloor
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return retorna a posi��o onde foi colocado
	 */
	public int attendRequest(List<Pair<Integer, T>> tasks, int requestedFloor, int maxBuildingFloor, int currentPosition);

}
