package lift_management.algorithms.strategy_algorithm;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import lift_management.TravelTimes;

public class LookDiskAlgorithm {
	public enum Task {UP, DOWN, STOP}
	
	/**
	 * Dada a lista de tarafas de um elevador, a sua posição actual, o número de pisos do edifício e um pedido , analisa qual é o custo de atender o pedido.
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedTask
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return Retorna a estimação de tempo que vai demorar até atender o pedido
	 * @throws Exception 
	 */
	public static int evaluate(List<Pair<Integer, Task>> tasks, int requestedFloor, Task requestedTask, int maxBuildingFloor, int currentPosition) throws Exception{
		if(requestedTask.equals(Task.STOP)){
			throw new Exception("requestedTask cannot be STOP. Only UP or DOWN");
		}
		if(tasks.isEmpty()){
			return Math.abs(currentPosition-requestedFloor);
		}
		
		int previousStop = currentPosition;
		Task previousTask = Task.STOP;
		int floorsTraveled=0;
		int numStops = 0;
		boolean assigned = false;
		
		int nextStop;
		Task nextTask;
		Task direction;
		
		
		//Percorrer todo o caminho do elevador e tentar encaixar a paragem no caminho
		for(Pair<Integer, Task> task : tasks){
			nextStop = task.getKey().intValue();
			nextTask = task.getValue();
			direction = getDirection(tasks,tasks.indexOf(task), previousStop);
			
			if( floorInBetween(previousStop, nextStop, requestedFloor) && (direction.equals(requestedTask))){
				
				if(!(previousTask.equals(direction) || previousTask.equals(Task.STOP))){	//Se a direção do elevador é não for igual à direção de próxima tarefa e não for STOP, quer dizer que o elevador vai fazer uma paragem entre esta tarefa e a próxima tarefa que involve andar no sentido oposto.													
					int estimatedDestiny = getEstimatedDestiny(previousStop, previousTask, maxBuildingFloor);
					numStops++;
					floorsTraveled += Math.abs( estimatedDestiny - previousStop) + Math.abs(estimatedDestiny + requestedFloor); //O custo será então igual a fazer uma viagem que satisfaça o pedido anterior mais o de ir dessa paragem para o requestedFloor
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
			nextTask = previousTask;
		}
				
		//Percorreu toda a lista de tasks e ainda assim não foi atribuido a uma posição
		if(assigned == false){		
			int estimatedDestiny = getEstimatedDestiny(previousStop, previousTask, maxBuildingFloor);
			if((previousTask.equals(requestedTask) && floorInBetween(previousStop, estimatedDestiny, requestedFloor)) || previousTask.equals(Task.STOP)){	//Se a direção do elevador é igual ao requestedTask e o requestedFloor está entre a última paragem e o destino quer dizer que o elevador pode parar no caminho para apanhar.	
				floorsTraveled += Math.abs(previousStop - requestedFloor);
			}else{
				estimatedDestiny = getEstimatedDestiny(previousStop, previousTask, maxBuildingFloor);
				numStops++;
				floorsTraveled += Math.abs( estimatedDestiny - previousStop) + Math.abs(estimatedDestiny + requestedFloor); //O custo será então igual a fazer uma viagem que satisfaça o pedido anterior mais o de ir dessa paragem para o requestedFloor
			}	
		}
		
		return floorsTraveled*TravelTimes.FLOOR+TravelTimes.getStopsExtraTime(numStops); 
	}
	
	protected static int getEstimatedDestiny(int previousStop, Task previousTask, int maxBuildingFloor) {
		if(previousTask.equals(Task.DOWN)){
			return 0;
		}else if(previousTask.equals(Task.UP)){
			return (int) Math.ceil((maxBuildingFloor+1 - previousStop)/2);
		}else{
			return previousStop;
		}
		
	}

	protected static boolean floorInBetween(int previous, int next, int n){
		return (previous < n && n <= next) || (previous > n && n >= next);
	}
	
	protected static Task getDirection(List<Pair<Integer, Task>> tasks,int i, int previousStop){		
		Task direction;
		
		while(previousStop == tasks.get(i).getKey().intValue() && i < tasks.size()){
			i++;
		}
		
		int nextStop = tasks.get(i).getKey().intValue();
		if(previousStop > nextStop) {
			direction = Task.UP;
		}else if(previousStop < nextStop){
			direction = Task.DOWN;
		}else{
			direction = tasks.get(i).getValue();
		}
		
		return direction;
	}
	
	protected static Task getDirection(int previousStop, int nextStop){
		Task direction;
		
		if(previousStop > nextStop) {
			direction = Task.UP;
		}else if(previousStop < nextStop){
			direction = Task.DOWN;
		}else{
			direction = Task.STOP;
		}
		
		return direction;
	}
	
	/**
	 * Altera a lista de tasks passada como parâmetro e insere o novo pedido na lista ordenada de tasks
	 * 
	 * @param tasks
	 * @param requestedFloor
	 * @param requestedTask
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return position of the new task
	 * @throws Exception
	 */
	protected static int addNewTask(List<Pair<Integer, Task>> tasks, int requestedFloor, Task requestedTask, int maxBuildingFloor, int currentPosition) throws Exception{
		if(requestedTask.equals(Task.STOP)){
			throw new Exception("requestedTask cannot be STOP. Only UP or DOWN");
		}
		
		int previousStop = currentPosition;
		Task previousTask = Task.STOP;
		
		int nextStop;
		Task nextTask;
		Task direction;
		
		//Percorrer todo o caminho do elevador e tentar encaixar a paragem no caminho
		for(Pair<Integer, Task> task : tasks){
			nextStop = task.getKey().intValue();
			nextTask = task.getValue();
			direction = getDirection(tasks,tasks.indexOf(task), previousStop);
			
			if( floorInBetween(previousStop, nextStop, requestedFloor) && (direction.equals(requestedTask))){
				int i = tasks.indexOf(task);
				tasks.add(i, new Pair<Integer, Task>(requestedFloor, requestedTask));
				return i;
			}
			
			previousStop = nextStop;
			nextTask = previousTask;
		}
		
		
		//Percorreu toda a lista de tasks e ainda assim não foi atribuido a uma posição				
		tasks.add(new Pair<Integer, Task>(requestedFloor, requestedTask));
		return tasks.size()-1;
	}
	
	/**
	 * Executa o atendimento de uma paragem adicionando a paragem na lista de tarefas. Prioritiza o atendimento da tarefa.
	 * @param tasks
	 * @param requestedFloor
	 * @param maxBuildingFloor
	 * @param currentPosition
	 * @return retorna a posição onde foi colocado
	 */
	public static int attendRequest(List<Pair<Integer, Task>> tasks, int requestedFloor, int maxBuildingFloor, int currentPosition){
		Pair<Integer,Task> newTask = new Pair<Integer, Task>(requestedFloor, Task.STOP); 
		if(tasks.size()==0){
			tasks.add(newTask);
			return 0;
		}
		
		int previousStop = currentPosition;
		Task previousTask = Task.STOP;
		
		int nextStop;
		Task nextTask;
		Task direction;
		
		//Percorrer todo o caminho do elevador e tentar encaixar o maior número de paragens no caminho entre a posição inicial e nova paragem
		for(Pair<Integer, Task> task : tasks){
			nextStop = task.getKey().intValue();
			nextTask = task.getValue();
			direction = getDirection(previousStop, requestedFloor);
			
			if(!floorInBetween(previousStop, requestedFloor, nextStop) || !(direction.equals(nextTask))){
				int i = tasks.indexOf(task);
				tasks.add(i, newTask);
				return i;
			}
			
			previousStop = nextStop;
			nextTask = previousTask;
		}
		
		//Percorreu toda a lista de tasks e ainda assim não foi atribuido a uma posição				
		tasks.add(newTask);
		return tasks.size()-1;
	}
}


