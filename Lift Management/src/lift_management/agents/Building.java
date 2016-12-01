package lift_management.agents;

import repast.simphony.space.continuous.ContinuousSpace;
import sajas.core.Agent;

public class Building extends Agent {
	public static float floorHeight = 1f;
	protected int numLifts;
	protected int numFloors;
	
	public Building(int numLifts, int numFloors) {
		this.numLifts = numLifts;
		this.numFloors = numFloors;
	}
	
	public int getNumLifts() {
		return numLifts;
	}
	
	public int getNumFloors() {
		return numFloors;
	}
}
