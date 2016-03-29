import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;


public class Controller {
	public static LinkedList<Event> schedule = new LinkedList<>();		//Event list
	public static int state;			//state = w
	public static double time = 0;		//time of simulator
	public static PrintWriter writer = null;			//first file to write to w/ Request results
	public static PrintWriter writer2 = null;			//second file to write to w/ Final simulator results
	
	public static double tempSD;
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		boolean logFlag = false;
		//Call a function to initialize the simulated world and calendar
		state =  MM1System.initState();
		System.out.println("State initiated to "+state+"\n");
		//schedule = MM1System.initSchedule();
		MM1System.initSchedule();
		
		Event event = new Event();
		writer = new PrintWriter(new FileWriter("Q3_test_Requests.txt"));
		writer2 = new PrintWriter(new FileWriter("Q3_test_FinalResults.txt"));
		
		while (time < MM1System.simulationTime){
			if (logFlag==false&&time>=MM1System.simulationTime/2){	//if the first LogEvent hasn't been scheduled, && time>half the total sim time
				//If it's half way through simulation, start logging
				Event newLogEvent = new LogEvent(time);
				MM1System.insertNewEvent(newLogEvent);
				logFlag=true;	//it's true that the first logEvent has been scheduled
			}
			
			//get next event from the calendar
 			event = MM1System.getNextEvent(schedule);
			//call function that needs to be executed to reflect occurrence of event
			if (event instanceof BirthEvent) {
				//System.out.println("Event is an instance of BirthEvent of type "+event.request.getReqType());
				((BirthEvent)event).run();
			}
			else if (event instanceof DeathEvent) {
				//System.out.println("Event is an instance of DeathEvent of type "+event.request.getReqType());
				((DeathEvent)event).run();
			}
			else if (event instanceof LogEvent) {		//I could just use an else statement...
				//System.out.println("Event is an instance of LogEvent");
				((LogEvent)event).run();
			}
			System.out.println("time = "+time+ "\t\t state = "+state+"\t\t size = "+schedule.size()+"\n");
			/*for (int i=0;i<MM1System.requestSchedule.size();i++){
				tempSD = (Controller.time-MM1System.requestSchedule.get(i).getArrTime()+
						MM1System.requestSchedule.get(i).getTs())/MM1System.requestSchedule.get(i).getTs();
				if (MM1System.requestSchedule.get(i).getRemTime()!=0){
					tempSD = (Controller.time-MM1System.requestSchedule.get(i).getArrTime()+
					MM1System.requestSchedule.get(i).getRemTime())/MM1System.requestSchedule.get(i).getTs();
				}
				System.out.println("\tRequest of type "+MM1System.requestSchedule.get(i).getReqType()
						+"\t\tSlowdown: "+tempSD+"\t\tTs: "+ MM1System.requestSchedule.get(i).getTs()+ "\t\tremainingTime: " +
						MM1System.requestSchedule.get(i).getRemTime() + "\n");
			}*/
		}
		//call function that prints the results of simulation, etc.
		LogEvent.getData();
		writer.close();			//Close files
		writer2.close();
	}
}