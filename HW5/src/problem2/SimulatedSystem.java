package problem2;

import java.util.LinkedList;
import java.util.Random;

public abstract class SimulatedSystem {
	static Random rand = new Random();
	
	public static int initState(){
		int state = 0;
		return state;
	}
	
	public static Event getNextEvent(LinkedList<Event> sch) {		//returns the next Event in the schedule
		Event event = sch.remove();
		//System.out.println("Event created with IAT = "+event.eventTime);
		return event;
	}

	// Function used to generate an exponential RV
	// Will be used for generating inter-arrival and service times
	public static double genExp(double lamb) {
		double uRV = rand.nextDouble();
		double eRV = -(Math.log(1-uRV))/lamb;
		return eRV;
	}
}