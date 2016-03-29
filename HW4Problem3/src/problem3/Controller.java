package problem3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;


public class Controller {
	public static LinkedList<Event> schedule = new LinkedList<>();		//Event list
	public static int CPUState;											//state of CPU = w of CPU
	public static int DiskState;										//state of Disk = w of Disk
	public static int NetState;											//state of Network = w of Network
	public static double time = 0;										//time of simulator
	public static PrintWriter writer = null;							//first file to write to w/ Request results
	public static PrintWriter writer2 = null;							//second file to write to w/ Final simulator results
	
	/*
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {
		boolean logFlag = false;		//whether to use LogEvent
		
		/*Start initialization of the simulated world and calendar*/
		CPUState =  SysNetwork.initState();
		DiskState = SysNetwork.initState();
		NetState = SysNetwork.initState();
		schedule = SysNetwork.initSchedule();
		System.out.println("Initiated world.");
		/*End initialization of the simulated world and calendar*/
		
		Event event = new Event();
		
		writer = new PrintWriter(new FileWriter("Q2_test_Requests.txt"));
		writer2 = new PrintWriter(new FileWriter("Q2_test_FinalResults.txt"));
		
		while (time < SysNetwork.simulationTime){
			
			/*Start LogEvent Ini*/
			if (logFlag==false&&time>=SysNetwork.simulationTime/2){	//if the first LogEvent hasn't been scheduled, && time>half the total sim time
				//If it's half way through simulation, start logging
				Event newLogEvent = new LogEvent(time);
				SysNetwork.insertNewEvent(newLogEvent);
				logFlag=true;	//it's true that the first logEvent has been scheduled
			}
			/*End LogEvent Init*/
			
			// get next event from the calendar
			event = SysNetwork.getNextEvent(schedule);
			// call function that needs to be executed to reflect occurrence of event
			if (event instanceof BirthEvent) {
				//System.out.println("Event is an instance of BirthEvent");
				((BirthEvent)event).run();
			} else if (event instanceof ArrToDisk) {
				//System.out.println("Event is an instance of ArrToDisk");
				((ArrToDisk)event).run();
			} else if (event instanceof ArrToNet) {
				//System.out.println("Event is an instance of ArrToNet");
				((ArrToNet)event).run();
			} else if (event instanceof CPUDeathEvent) {
				//System.out.println("Event is an instance of CPUDeathEvent");
				((CPUDeathEvent)event).run();
			} else if (event instanceof DiskDeathEvent) {
				//System.out.println("Event is an instance of DiskDeathEvent");
				((DiskDeathEvent)event).run();
			} else if (event instanceof NetDeathEvent) {
				//System.out.println("Event is an instance of NetDeathEvent");
				((NetDeathEvent)event).run();
			} else if (event instanceof LogEvent) {		//I could just use an else statement...
				//System.out.println("Event is an instance of LogEvent");
				((LogEvent)event).run();
			}
			/*System.out.println("time = "+time+ "\t\t w = "+CPUState+"\t\t q ="+
					SysNetwork.CPURequests.size()+"\t\t size = "+schedule.size()+"\n");*/		
		}
		//call function that prints the results of simulation, etc.
		LogEvent.getData();
		writer.close();			//Close files
		writer2.close();
	}
}