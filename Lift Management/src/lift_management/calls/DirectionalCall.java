package lift_management.calls;

import lift_management.agents.Lift.Direction;

public class DirectionalCall extends Call<Direction> {
	protected boolean ascending;
	
	public DirectionalCall(){
		
	}
	
	public DirectionalCall(int origin, boolean up) {
		super(origin, up ? Direction.UP : Direction.DOWN);
		this.ascending = up;
	}
	
	public boolean isAscending() {
		return ascending;
	}
	
	public boolean isDescending() {
		return !ascending;
	}
	
	@Override
	public String toString() {
		return origin + " " + (this.ascending ? "^" : "v");
	}

	public void setAscending() {
		try {
			setDestiny(Direction.DOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDescending() {
		try {
			setDestiny(Direction.DOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setAscending(boolean ascending) {
		try {
			if(ascending){
					setDestiny(Direction.UP);
			}else{
				 setDestiny(Direction.DOWN);
			}
		}
		 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Direction getDestiny() {
		
		if(isAscending()){
			return Direction.UP;
		}else{
			return Direction.DOWN;
		}
	}

	@Override
	public void setDestiny(Direction destiny) throws Exception {
		if(destiny.equals(Direction.STOP)){
			throw new Exception("Invalid Direction.STOP as destiny");
		}
		
		this.destiny = destiny;
		
		if(destiny.equals(Direction.UP)){
			ascending = true;
		}else{
			ascending = false;
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (ascending ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DirectionalCall other = (DirectionalCall) obj;
		if (ascending != other.ascending)
			return false;
		return true;
	}
}
