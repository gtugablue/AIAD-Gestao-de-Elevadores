package lift_management;

import repast.simphony.parameter.IllegalParameterException;
import repast.simphony.parameter.Parameters;

public class Config {
	public final int numFloors;
	public final int numLifts;
	
	public Config(Parameters p) throws IllegalParameterException {
		this.numFloors = (int) p.getValue("numFloors");
		this.numLifts = (int) p.getValue("numLifts");
	}
}
