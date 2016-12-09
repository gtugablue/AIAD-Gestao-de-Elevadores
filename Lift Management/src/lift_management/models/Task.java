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
	
	public int getId(){
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destiny == null) ? 0 : destiny.hashCode());
		result = prime * result + floor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (destiny == null) {
			if (other.destiny != null)
				return false;
		} else if (!destiny.equals(other.destiny))
			return false;
		if (floor != other.floor)
			return false;
		return true;
	}
}
