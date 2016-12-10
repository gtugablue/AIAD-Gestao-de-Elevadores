package lift_management;

public class DestinyDispatchCall extends Call<Integer> {
	
	public DestinyDispatchCall() {
		
	}
	
	public DestinyDispatchCall(int origin, Integer destiny) {
		super(origin, destiny);
	}

	public DestinyDispatchCall(int origin, int destiny) {
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
}
