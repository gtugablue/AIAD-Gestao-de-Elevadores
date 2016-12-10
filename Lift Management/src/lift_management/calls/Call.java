package lift_management.calls;

public abstract class Call<T> {
	protected int origin;
	protected T destiny;
	public Call() {
		
	}
	
	public Call(int origin, T destiny) {
		this.origin = origin;
		this.destiny = destiny;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}
	
	public abstract T getDestiny();
	
	public abstract void setDestiny(T Destiny) throws Exception;
}
