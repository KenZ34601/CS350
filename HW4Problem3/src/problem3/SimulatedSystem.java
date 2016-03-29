package problem3;
import java.util.LinkedList;

public abstract class SimulatedSystem {

	public static int initState(){
		int state = 0;
		return state;
	}

	public static LinkedList<Event> initSchedule(){
		LinkedList<Event> schedule = new LinkedList<>();
		schedule.add(new BirthEvent());
		//Event event = new Event();
		//event = schedule.peekFirst();
		//System.out.println("Schedule created, event has IAT = "+event.eventTime);
		return schedule;  
	}
	
	public static Event getNextEvent(LinkedList<Event> sch) {
		Event event = sch.remove();
		//System.out.println("Event created with IAT = "+event.eventTime);
		return event;
	}
}