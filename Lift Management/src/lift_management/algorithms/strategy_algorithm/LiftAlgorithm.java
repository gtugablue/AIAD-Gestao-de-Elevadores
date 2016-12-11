package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;
import lift_management.models.Task;

public abstract class LiftAlgorithm<T>{
	
	
	/**
	 * Dada a lista de tarafas de um elevador, a sua posição actual, o número de pisos do edifício e um pedido , analisa qual é o custo de atender o pedido.
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedDestiny
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return Retorna a quantidade de pisos percorridos até parar o requestedFloor e atender o pedido do Human
	 * @throws Exception 
	 */
	public abstract int evaluate(List<Task<T>> tasks, int requestedFloor, T requestedDestiny, int maxBuildingFloor, int currentPosition) throws Exception;
	
	
	/**
	 * Altera a lista de tasks passada como parâmetro e insere o novo pedido na lista ordenada de tasks
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedDestiny
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return posição onde foi inserida a nova task
	 * @throws Exception
	 */
	public abstract int addNewTask(List<Task<T>> tasks, int requestedFloor, T requestedDestiny, int maxBuildingFloor, int currentPosition) throws Exception;

	
	/**
	 * Executa o atendimento de uma paragem adicionando a paragem na lista de tarefas. Prioritiza o atendimento da tarefa.
	 * @param tasks
	 * @param requestedFloor
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return retorna a posição onde foi colocado
	 */
	public abstract int attendRequest(List<Task<T>> tasks, int requestedFloor, int maxBuildingFloor, int currentPosition);
	
	protected static boolean floorInBetween(int previous, int next, int n){
		return (previous < n && n <= next) || (previous > n && n >= next);
	}

	public static Direction getDirection(int previousStop, int nextStop) {
		Direction direction;
		
		if(previousStop < nextStop) {
			direction = Direction.UP;
		}else if(previousStop > nextStop){
			direction = Direction.DOWN;
		}else{
			direction = Direction.STOP;
		}
		
		return direction;
	}


	protected abstract Direction getDirection(List<Task<T>> tasks, int i, int previousStop);

}
