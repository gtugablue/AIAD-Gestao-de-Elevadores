package lift_management;

import java.util.Scanner;

import repast.simphony.parameter.IllegalParameterException;
import repast.simphony.parameter.Parameters;

public class Config {
	public final int numFloors;
	public final int numLifts;
	public final int[] maxWeights;
	public final int callFrequency;
	public final String algorithm;
	public final long maxNumTicks;
	public final float velocity;
	
	
	public Config(Parameters p) throws IllegalParameterException {
		this.numFloors = (int) p.getValue("numFloors");
		this.numLifts = (int) p.getValue("numLifts");
		this.algorithm = p.getValueAsString("algorithm");
		this.maxNumTicks = p.getLong("maxNumTicks");
		String maxWeightsString = (String) p.getValue("maxWeights");
		this.maxWeights = new int[this.numLifts];
		this.velocity = p.getFloat("velocity");
		Scanner s = new Scanner(maxWeightsString);
		for (int i = 0; i < this.numLifts; i++) {
			if (!s.hasNextInt())
				throw new IllegalParameterException("Expected " + this.numLifts + " maximum weight values but found " + i + ".");
			this.maxWeights[i] = s.nextInt();
		}
		if (s.hasNextInt())
			throw new IllegalParameterException("Excepted " + this.numLifts + " maximum weight values but found more.");
		s.close();
		this.callFrequency = (int) p.getValue("callFrequency");
	}
}
