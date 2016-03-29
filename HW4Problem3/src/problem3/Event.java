package problem3;

import java.util.LinkedList;

public class Event {
	public double eventTime = 0;				//Used differently for each type of event
	public Request request = new Request();		//Temp request for each Event object
	public Event() {
		
	}
	
	public void run(LinkedList<Event> schedule, int state, double time){
		System.out.println("You're using the Event class run() function. You shouldn't be.");
	}
}