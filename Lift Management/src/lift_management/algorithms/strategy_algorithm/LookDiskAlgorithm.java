package lift_management.algorithms.strategy_algorithm;

import java.util.List;

import javafx.util.Pair;
import lift_management.agents.Lift.Direction;

public class LookDiskAlgorithm implements LiftAlgorithm {

	@Override
	public int evaluate(List<Pair<Integer, Direction>> tasks, int requestedFloor, Direction requestedTask, int maxBuildingFloor, double currentPosition) throws Exception {
		if(requestedTask.equals(Direction.STOP)){
			throw new Exception("requestedTask cannot be STOP. Only UP or DOWN");
		}
		if(tasks.isEmpty()){
			return Math.abs((int)Math.round(currentPosition)-requestedFloor);
		}
		
		int previousStop = (int)Math.round(currentPosition);
		Direction previousTask = Direction.STOP;
		int floorsTraveled=0;
		boolean assigned = false;
		
		int nextStop;
		Direction nextTask;
		Direction direction;
		
		//Percorrer todo o caminho do elevador e tentar encaixar a paragem no caminho
		for(Pair<Integer, Direction> task : tasks){
			nextStop = task.getKey().intValue();
			nextTask = task.getValue();
			direction = getDirection(tasks,tasks.indexOf(task), previousStop);
			
			if( floorInBetween(previousStop, nextStop, requestedFloor) && direction.equals(requestedTask) ){
				
				if(!previousTask.equals(direction)){	//Se a direção do elevador é diferente da direção de próxima tarefa, quer dizer que o elevador vai fazer uma paragem entre esta tarefa e a próxima tarefa que involve andar no sentido oposto.													
					int estimatedDestiny = getEstimatedDestiny(previousStop, previousTask, maxBuildingFloor);
					floorsTraveled += Math.abs( estimatedDestiny - previousStop) + Math.abs(estimatedDestiny + requestedFloor); //O custo será então igual a fazer uma viagem que satisfaça o pedido anterior mais o de ir dessa paragem para o requestedFloor
				}
				else{
					floorsTraveled += Math.abs(previousStop - requestedFloor);
				}
				assigned = true;
				break;
			}
			
			floorsTraveled += Math.abs(nextStop-previousStop);
			previousStop = nextStop;
			nextTask = previousTask;
		}
		
		
		//Percorreu toda a lista de tasks e ainda assim não foi atribuido a uma posição
		if(assigned == false){		
			int estimatedDestiny = getEstimatedDestiny(previousStop, previousTask, maxBuildingFloor);
			if(previousTask.equals(requestedTask) && floorInBetween(previousStop, estimatedDestiny, requestedFloor)){	//Se a direção do elevador é igual ao requestedTask e o requestedFloor está entre a última paragem e o destino quer dizer que o elevador pode parar no caminho para apanhar.	
				floorsTraveled += Math.abs(previousStop - requestedFloor);
			}else{
				estimatedDestiny = getEstimatedDestiny(previousStop, previousTask, maxBuildingFloor);
				floorsTraveled += Math.abs( estimatedDestiny - previousStop) + Math.abs(estimatedDestiny + requestedFloor); //O custo será então igual a fazer uma viagem que satisfaça o pedido anterior mais o de ir dessa paragem para o requestedFloor
			}	
		}
		
		return floorsTraveled; 
	}
	
	protected static int getEstimatedDestiny(int previousStop, Direction previousTask, int maxBuildingFloor) {
		if(previousTask.equals(Direction.DOWN)){
			return 0;
		}else{
			return (int) Math.ceil((maxBuildingFloor+1 - previousStop)/2);
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
}


