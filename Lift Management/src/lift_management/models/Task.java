package lift_management.models;

public class Task<T> {
	protected static int currentId = 0;
	
	protected int id = currentId++;
	protected int floor;
	T destiny;
	
	public Task(int originFloor, T destiny){
		this.floor = originFloor;
		this.destiny = destiny;
	}

	public int getFloor() {
		return floor;
	}

	public T getDestiny() {
		return destiny;
	}
	
}
