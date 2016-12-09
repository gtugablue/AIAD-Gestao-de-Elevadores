package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.TravelTimes;
import lift_management.agents.Lift.Direction;
import lift_management.models.Task;

public class DestinationDispatchAlgorithm extends LiftAlgorithm<Integer>{
	public static final int STOP = -1;
	@Override
	public int evaluate(List<Task<Integer>> tasks, int requestedFloor, Integer requestedDestiny, int maxBuildingFloor, int currentPosition) throws Exception {
		if(requestedDestiny == -1 || requestedDestiny > maxBuildingFloor){
			throw new Exception("Invalid requestedDestiny");
		}
		if(tasks.isEmpty()){
			return Math.abs(currentPosition-requestedFloor);
		}
		
		int previousStop = currentPosition;
		Integer previousDestiny = -1;
		int floorsTraveled=0;
		int numStops = 0;
		boolean assigned = false;
		
		int nextStop;
		Integer nextDestiny;
		Direction direction;
		
		
		//Percorrer todo o caminho do elevador e tentar encaixar a paragem no caminho
		for(Task<Integer> task : tasks){
			nextStop = task.getFloor();
			nextDestiny = task.getDestiny();
			direction = getDirection(tasks,tasks.indexOf(task), previousStop);
			
			if( floorInBetween(previousStop, nextStop, requestedFloor) && (direction.equals(requestedDestiny))){
				
				if(!(floorInBetween(previousStop, previousDestiny, nextStop) || previousDestiny == STOP)){	//Se a direção do elevador é não for igual à direção de próxima tarefa e não for STOP, quer dizer que o elevador vai fazer uma paragem entre esta tarefa e a próxima tarefa que involve andar no sentido oposto.													
					numStops++;
					floorsTraveled += Math.abs( previousDestiny - previousStop) + Math.abs(previousDestiny - requestedFloor); //O custo será então igual a fazer uma viagem que satisfaça o pedido anterior mais o de ir dessa paragem para o requestedFloor
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
			previousDestiny = nextDestiny;
		}
				
		//Percorreu toda a lista de tasks e ainda assim não foi atribuido a uma posição
		if(assigned == false){		
			if(floorInBetween(previousStop, previousDestiny, requestedFloor) || previousDestiny.equals(STOP)){	//Se a direção do elevador é igual ao requestedTask e o requestedFloor está entre a última paragem e o destino quer dizer que o elevador pode parar no caminho para apanhar.	
				floorsTraveled += Math.abs(previousStop - requestedFloor);
			}else{
				numStops++;
				floorsTraveled += Math.abs( previousDestiny - previousStop) + Math.abs(previousDestiny - requestedFloor); //O custo será então igual a fazer uma viagem que satisfaça o pedido anterior mais o de ir dessa paragem para o requestedFloor
			}	
		}
		
		return floorsTraveled*TravelTimes.FLOOR+TravelTimes.getStopsExtraTime(numStops);
	}

	protected static Direction getDirection(List<Task<Integer>> tasks,int i, int previousStop){		
		Direction direction;
		
		while(i < tasks.size() - 1 && previousStop == tasks.get(i).getFloor()){
			i++;
		}
		
		int nextStop = tasks.get(i).getFloor();
		if(previousStop > nextStop) {
			direction = Direction.UP;
		}else if(previousStop < nextStop){
			direction = Direction.DOWN;
		}else if(tasks.get(i).getDestiny() == -1){
			direction = Direction.STOP;
		}else{
			direction = tasks.get(i).getDestiny() < nextStop? Direction.DOWN : Direction.UP;
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
	
	@Override
	public int addNewTask(List<Task<Integer>> tasks, int requestedFloor, Integer requestedDestiny,
			int maxBuildingFloor, int currentPosition) throws Exception {
		if(requestedDestiny.equals(-1) || requestedDestiny > maxBuildingFloor){
			throw new Exception("requestedDestiny must be between 0 and maxBuildingFloor:" + maxBuildingFloor);
		}
		
		int previousStop = currentPosition;
		
		int nextStop;
		Integer nextDestiny;
		Direction direction;
		
		//Percorrer todo o caminho do elevador e tentar encaixar a paragem no caminho
		for(Task<Integer> task : tasks){
			nextStop = task.getFloor();
			nextDestiny = task.getDestiny();
			direction = getDirection(tasks,tasks.indexOf(task), previousStop);
			
			if( floorInBetween(previousStop, nextStop, requestedFloor) && (direction.equals(requestedDestiny))){
				int i = tasks.indexOf(task);
				tasks.add(i, new Task<Integer>(requestedFloor, requestedDestiny));
				return i;
			}
			
			previousStop = nextStop;
		}
		
		
		//Percorreu toda a lista de tasks e ainda assim não foi atribuido a uma posição				
		tasks.add(new Task<Integer>(requestedFloor, requestedDestiny));
		return tasks.size()-1;
	}

	@Override
	public int attendRequest(List<Task<Integer>> tasks, int requestedFloor, int maxBuildingFloor,
			int currentPosition) {		
		Task<Integer> newTask = new Task<Integer>(requestedFloor, STOP); 
		if(tasks.size()==0){
			tasks.add(newTask);
			return 0;
		}
		
		int previousStop = currentPosition;
		
		int nextStop;
		Integer nextDirection;
		Direction direction;
		
		//Percorrer todo o caminho do elevador e tentar encaixar o maior número de paragens no caminho entre a posição inicial e nova paragem
		for(Task<Integer> task : tasks){
			nextStop = task.getFloor();
			nextDirection = task.getDestiny();
			direction = getDirection(previousStop, requestedFloor);
			
			if(!(floorInBetween(previousStop, requestedFloor, nextStop) && (floorInBetween(previousStop, nextStop,requestedFloor)))){
				int i = tasks.indexOf(task);
				tasks.add(i, newTask);
				return i;
			}
			
			previousStop = nextStop;
		}
		
		//Percorreu toda a lista de tasks e ainda assim não foi atribuido a uma posição				
		tasks.add(newTask);
		return tasks.size()-1;
	}

}
