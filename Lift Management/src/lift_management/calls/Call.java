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
	
	public abstract boolean isAscending();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destiny == null) ? 0 : destiny.hashCode());
		result = prime * result + origin;
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
		Call other = (Call) obj;
		if (destiny == null) {
			if (other.destiny != null)
				return false;
		} else if (!destiny.equals(other.destiny))
			return false;
		if (origin != other.origin)
			return false;
		return true;
	}
	
	
}
