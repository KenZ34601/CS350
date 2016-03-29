import java.util.LinkedList;

public class MM1System extends SimulatedSystem {
	static double IO_lambda = 6;		//type 2
	static double IO_Ts = 0.010;
	
	static double CPU_lambda = 3;		//type 1
	static double CPU_Ts = 0.300;
	
	public static double simulationTime = 200;		//Usually set to 200 for steady state, and LogEvent only logs for last 100 seconds
	public static double quantum = 0.100;
	public static LinkedList<Request> requestSchedule = new LinkedList<>();
	public static int scheduleType = 4;	//1 = FCFS
										//2 = Shortest Time Remaining
										//3 = Round Robin
										//4 = Highest Slowdown Next
	
	private static double checkTime;
	private static Event checkEvent = new Event();
	private static Request checkReq = new Request();
	private static int size;
	
	public static void initSchedule(){
		Controller.schedule.add(new BirthEvent(2));
		Event newCPUBirthEvent = new BirthEvent(1);
		insertNewEvent(newCPUBirthEvent);
	}
	
	public static int insertNewEvent(Event newEvent){
		size = Controller.schedule.size();
		double eventReqTime;		// time of execution of event? Sorta. Basically what I'm using to compare to other events
		if ((newEvent instanceof BirthEvent) || (newEvent instanceof LogEvent))
			eventReqTime = newEvent.request.getArrTime();		//Use arrivalTime to compare if newEvent is a "Birth" event
		else {
			eventReqTime = newEvent.request.getEndTime();		//Use endTime to compare if newEvent is a "Death" event...
			if (scheduleType==3 || scheduleType==4)				//...unless it's Round Robin or Highest Slowdown Next
				if (newEvent.request.getEndTime()>newEvent.request.getStartTime()+quantum){		//Check if endTime > quantum and if so
					newEvent.eventTime = newEvent.request.getStartTime()+quantum;		//eventTime used for allowing request to run for set time (quantum)
					eventReqTime = newEvent.eventTime;			//Use startTime+quantum to compare
				}
		}
		if (size==0){				//If there aren't any Events in the schedule
			Controller.schedule.add(newEvent);
		} else {
			for (int i = 0; i<size;i++) {
				checkEvent = Controller.schedule.get(i);
				if (checkEvent instanceof BirthEvent) {
					if (eventReqTime<checkEvent.request.getArrTime()){ //if newEvent arrives before LogEvent
						Controller.schedule.add(i,newEvent);	//Put newEvent before the BirthEvent
						break;
					}
				} else if (checkEvent instanceof DeathEvent) {			//Compares endTime of CPUDeathEvent and arrivalTime of newEvent
					switch (MM1System.scheduleType){
					case 3:		//For Round Robin and Highest Slowdown Next, we need to check if we're checking
					case 4:		//against checkEvent's eventTime or endTime. 
						if (checkEvent.eventTime!=0){
							if (eventReqTime<checkEvent.eventTime){		//if arrivalTime<checkEvent.request's alloted quantum time
								Controller.schedule.add(i,newEvent);	//Put newEvent before the CPUDeathEvent
								return 0;
							}
							break;
						}
						//Purposefully no break here so that it falls to default case if eventTime==0, aka if the request
						//doesn't need the allowed time (quantum)
					default:	
						if (eventReqTime<checkEvent.request.getEndTime()){	//if arrivalTime<endTime
							Controller.schedule.add(i,newEvent);	//Put newEvent before the CPUDeathEvent
							return 0;
						}
					}
				} else if (checkEvent instanceof LogEvent) {	//Compare arrivalTimes
					if (eventReqTime<checkEvent.request.getArrTime()){ //if newEvent arrives before LogEvent
						Controller.schedule.add(i,newEvent);	//Put newEvent before the LogEvent
						break;
					}
				}
				if (i==size-1){		//Sticks newEvent at the end
					Controller.schedule.add(newEvent);
				}
			}
		}
		return 0;
	}
	
	public static int insertNewRequest(Request newRequest){
		switch (scheduleType){
		case 2:		//Shortest Remaining Time will sort requests by shortest to longest time in requestSchedule
			//if there are no other requests in requestSchedule
			if (requestSchedule.isEmpty()){
				requestSchedule.add(newRequest);		//just add current request into requestSchedule
			} else {	//	if there is more than 1 request in the requestSchedule
				//we know that the first request is being served
				checkReq = requestSchedule.peekFirst();
				checkReq.setRemTime();
				//get the timeLeft for that request
				if (newRequest.getTs() < checkReq.getRemTime()){	//if (timeLeft > currentRequest.Ts)
					requestSchedule.addFirst(newRequest);
					for (int i = 0; i<Controller.schedule.size(); i++){
						checkEvent = Controller.schedule.get(i);
						if (checkEvent instanceof DeathEvent){
							checkEvent = Controller.schedule.remove(i);
							//changeReq();
							((DeathEvent)checkEvent).changeReq();
							//reschedule the deathEvent
							insertNewEvent(checkEvent);
							return 0;
						}
					}
				}
				//compare Ts of upcoming requests with current
				if (requestSchedule.size()==1){
					requestSchedule.add(newRequest);
				} else {
					for (int i = 1; i < requestSchedule.size();i++){
						checkReq = requestSchedule.get(i);
						if (checkReq.getRemTime()==0)
							checkTime = checkReq.getTs();
						else
							checkTime = checkReq.getRemTime();
						
						if (newRequest.getTs() < checkTime){
							requestSchedule.add(i, newRequest);
							break;
						}
						if (i==requestSchedule.size()-1){
							requestSchedule.add(newRequest);
							break;
						}
					}
				}
			}
			break;
		default:
			requestSchedule.add(newRequest);
			break;
		}
		return 0;
	}
}