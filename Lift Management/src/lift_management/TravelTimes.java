package lift_management;

public class TravelTimes {
	public static int FLOOR = 20;
	
	public static int DOORS = 5;
	
	public static int STANDBY = 10;
	
	public static int getStopsExtraTime(){
		return DOORS*2 + STANDBY;
	}
	
	public static int getStopsExtraTime(int numStops){
		return numStops*getStopsExtraTime();
	}
}
