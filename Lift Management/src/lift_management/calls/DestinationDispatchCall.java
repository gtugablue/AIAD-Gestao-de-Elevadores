package lift_management.calls;

public class DestinationDispatchCall extends Call<Integer> {
	
	public DestinationDispatchCall() {
		
	}
	
	public DestinationDispatchCall(int origin, Integer destiny) {
		super(origin, destiny);
	}

	public DestinationDispatchCall(int origin, int destiny) {
		super(origin, Integer.valueOf(destiny));
	}
	
	public Integer getDestiny() {
		return destiny;
	}

	@Override
	public void setDestiny(Integer destiny) throws Exception {
		if(destiny.intValue()==origin){
			throw new Exception("Destiny cannot be the same as the origin");
		}
		
		this.destiny = destiny;
	}
	
	public void setDestiny(int destiny) throws Exception{
		setDestiny(Integer.valueOf(destiny));
	}

	@Override
	public boolean isAscending() {
		return this.origin < this.destiny;
	}
}
