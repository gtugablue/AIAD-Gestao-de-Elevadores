package lift_management.algorithms.strategy_algorithm;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import lift_management.TravelTimes;
import lift_management.agents.Lift.Direction;

public class LookDiskAlgorithm implements LiftAlgorithm{
	
	/**
	 * Dada a lista de tarafas de um elevador, a sua posi��o actual, o n�mero de pisos do edif�cio e um pedido , analisa qual � o custo de atender o pedido.
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedDirection
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return Retorna a estima��o de tempo que vai demorar at� atender o pedido
	 * @throws Exception 
	 */
	public int evaluate(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedDirection, int maxBuildingFloor, int currentPosition) throws Exception{
		if(requestedDirection.equals(Direction.STOP)){
			throw new Exception("requestedTask cannot be STOP. Only UP or DOWN");
		}
		if(tasks.isEmpty()){
			return Math.abs(currentPosition-requestedFloor);
		}
		
		int previousStop = currentPosition;
		Direction previousDirection = Direction.STOP;
		int floorsTraveled=0;
		int numStops = 0;
		boolean assigned = false;
		
		int nextStop;
		Direction nextDirection;
		Direction direction;
		
		
		//Percorrer todo o caminho do elevador e tentar encaixar a paragem no caminho
		for(Pair<Integer, Direction> task : tasks){
			nextStop = task.getKey().intValue();
			nextDirection = task.getValue();
			direction = getDirection(tasks,tasks.indexOf(task), previousStop);
			
			if( floorInBetween(previousStop, nextStop, requestedFloor) && (direction.equals(requestedDirection))){
				
				if(!(previousDirection.equals(direction) || previousDirection.equals(Direction.STOP))){	//Se a dire��o do elevador � n�o for igual � dire��o de pr�xima tarefa e n�o for STOP, quer dizer que o elevador vai fazer uma paragem entre esta tarefa e a pr�xima tarefa que involve andar no sentido oposto.													
					int estimatedDestiny = getEstimatedDestiny(previousStop, previousDirection, maxBuildingFloor);
					numStops++;
					floorsTraveled += Math.abs( estimatedDestiny - previousStop) + Math.abs(estimatedDestiny + requestedFloor); //O custo ser� ent�o igual a fazer uma viagem que satisfa�a o pedido anterior mais o de ir dessa paragem para o requestedFloor
				}
				else{
					floorsTraveled += Math.abs(previousStop - requestedFloor);
				}
				assigned = true;
				break;
			}
			
			numStops++;
			floorsTraveled += Math.abs(nextStop-previousStop);
			previousStop = nextStop;
			nextDirection = previousDirection;
		}
				
		//Percorreu toda a lista de tasks e ainda assim n�o foi atribuido a uma posi��o
		if(assigned == false){		
			int estimatedDestiny = getEstimatedDestiny(previousStop, previousDirection, maxBuildingFloor);
			if((previousDirection.equals(requestedDirection) && floorInBetween(previousStop, estimatedDestiny, requestedFloor)) || previousDirection.equals(Direction.STOP)){	//Se a dire��o do elevador � igual ao requestedTask e o requestedFloor est� entre a �ltima paragem e o destino quer dizer que o elevador pode parar no caminho para apanhar.	
				floorsTraveled += Math.abs(previousStop - requestedFloor);
			}else{
				estimatedDestiny = getEstimatedDestiny(previousStop, previousDirection, maxBuildingFloor);
				numStops++;
				floorsTraveled += Math.abs( estimatedDestiny - previousStop) + Math.abs(estimatedDestiny + requestedFloor); //O custo ser� ent�o igual a fazer uma viagem que satisfa�a o pedido anterior mais o de ir dessa paragem para o requestedFloor
			}	
		}
		
		return floorsTraveled*TravelTimes.FLOOR+TravelTimes.getStopsExtraTime(numStops); 
	}
	
	protected static int getEstimatedDestiny(int previousStop, Direction previousTask, int maxBuildingFloor) {
		if(previousTask.equals(Direction.DOWN)){
			return 0;
		}else if(previousTask.equals(Direction.UP)){
			return (int) Math.ceil((maxBuildingFloor+1 - previousStop)/2);
		}else{
			return previousStop;
		}
		
	}

	protected static boolean floorInBetween(int previous, int next, int n){
		return (previous < n && n <= next) || (previous > n && n >= next);
	}
	
	protected static Direction getDirection(List<Pair<Integer, Direction>> tasks,int i, int previousStop){		
		Direction direction;
		
		while(previousStop == tasks.get(i).getKey().intValue() && i < tasks.size()){
			i++;
		}
		
		int nextStop = tasks.get(i).getKey().intValue();
		if(previousStop > nextStop) {
			direction = Direction.UP;
		}else if(previousStop < nextStop){
			direction = Direction.DOWN;
		}else{
			direction = tasks.get(i).getValue();
		}
		
		return direction;
	}
	
	protected static Direction getDirection(int previousStop, int nextStop){
		Direction direction;
		
		if(previousStop > nextStop) {
			direction = Direction.UP;
		}else if(previousStop < nextStop){
			direction = Direction.DOWN;
		}else{
			direction = Direction.STOP;
		}
		
		return direction;
	}
	
	/**
	 * Altera a lista de tasks passada como par�metro e insere o novo pedido na lista ordenada de tasks
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedDirection
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return position of the new task
	 * @throws Exception
	 */
	public int addNewTask(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedDirection, int maxBuildingFloor, int currentPosition) throws Exception{
		if(requestedDirection.equals(Direction.STOP)){
			throw new Exception("requestedDirection cannot be STOP. Only UP or DOWN");
		}
		
		int previousStop = currentPosition;
		Direction previousDirection = Direction.STOP;
		
		int nextStop;
		Direction nextDirection;
		Direction direction;
		
		//Percorrer todo o caminho do elevador e tentar encaixar a paragem no caminho
		for(Pair<Integer, Direction> task : tasks){
			nextStop = task.getKey().intValue();
			nextDirection = task.getValue();
			direction = getDirection(tasks,tasks.indexOf(task), previousStop);
			
			if( floorInBetween(previousStop, nextStop, requestedFloor) && (direction.equals(requestedDirection))){
				int i = tasks.indexOf(task);
				tasks.add(i, new Pair<Integer, Direction>(requestedFloor, requestedDirection));
				return i;
			}
			
			previousStop = nextStop;
			nextDirection = previousDirection;
		}
		
		
		//Percorreu toda a lista de tasks e ainda assim n�o foi atribuido a uma posi��o				
		tasks.add(new Pair<Integer, Direction>(requestedFloor, requestedDirection));
		return tasks.size()-1;
	}
	
	/**
	 * Executa o atendimento de uma paragem adicionando a paragem na lista de tarefas. Prioritiza o atendimento da tarefa.
	 * @param tasks
	 * @param requestedFloor
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return retorna a posi��o onde foi colocado
	 */
	public int attendRequest(List<Pair<Integer, Direction>> tasks, int requestedFloor, int maxBuildingFloor, int currentPosition){
		Pair<Integer,Direction> newTask = new Pair<Integer, Direction>(requestedFloor, Direction.STOP); 
		if(tasks.size()==0){
			tasks.add(newTask);
			return 0;
		}
		
		int previousStop = currentPosition;
		Direction previousDirection = Direction.STOP;
		
		int nextStop;
		Direction nextDirection;
		Direction direction;
		
		//Percorrer todo o caminho do elevador e tentar encaixar o maior n�mero de paragens no caminho entre a posi��o inicial e nova paragem
		for(Pair<Integer, Direction> task : tasks){
			nextStop = task.getKey().intValue();
			nextDirection = task.getValue();
			direction = getDirection(previousStop, requestedFloor);
			
			if(!floorInBetween(previousStop, requestedFloor, nextStop) || !(direction.equals(nextDirection))){
				int i = tasks.indexOf(task);
				tasks.add(i, newTask);
				return i;
			}
			
			previousStop = nextStop;
			nextDirection = previousDirection;
		}
		
		//Percorreu toda a lista de tasks e ainda assim n�o foi atribuido a uma posi��o				
		tasks.add(newTask);
		return tasks.size()-1;
	}
}


