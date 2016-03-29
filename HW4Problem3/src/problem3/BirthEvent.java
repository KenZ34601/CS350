package problem3;

public class BirthEvent extends Event {
	private Event newDeathEvent;
	private boolean makeNewBirth; 
	
	public BirthEvent() {
		eventTime = SysNetwork.genExp(SysNetwork.lambda);		//eventTime = IAT
		request.setIAT(eventTime);		//Gives request the IAT time of BirthEvent
		request.setArrTime(Controller.time+eventTime);	//Gives request the arrivalTime of BirthEvent
		makeNewBirth = true;
	}
	
	public BirthEvent(double arrivalTime, Request req){
		request = req;
		request.setArrTime(arrivalTime);
		makeNewBirth = false;
	}
	
	public void run() {
		// add a newly born request to the schedule of requests
		SysNetwork.CPURequests.add(this.request);
		
		/*Start check if service can be instantly started*/
		if (/*Controller.CPUState==0&&*/SysNetwork.CPURequests.size()==1){	// 	if either server was idle upon its birth
			newDeathEvent = new CPUDeathEvent(1,0);	
			//we start service for that request	
			request = SysNetwork.CPURequests.remove();			
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime()){
				System.out.println("SIMULATION ERROR! CHECK EVENTS ARE IN PROPER ORDER.");
			}
			Controller.time = this.request.getArrTime();
			
			this.request.setSerTime(Controller.time);	//Set startTime to now since we're servicing it now
			
			//So that we can predict when service is done
			//and schedule death event
			SysNetwork.CPURequests.addFirst(request);
			//System.out.println("new DeathEvent with arrTime = "+newDeathEvent.request.getArrTime()+" from BirthEvent.");
			SysNetwork.insertNewEvent(newDeathEvent); //Insert new DeathEvent into event schedule 
		}
		else if (SysNetwork.CPURequests.size()==2){
			if (SysNetwork.CPURequests.getFirst().getCoreNum()==1){
				newDeathEvent = new CPUDeathEvent(2,1);
			}
			else {
				newDeathEvent = new CPUDeathEvent(1,1);
			}
			//((CPUDeathEvent) newDeathEvent).setReqSerTime(Math.max(request.getEndTime(), newDeathEvent.request.getArrTime()));
			
			request = SysNetwork.CPURequests.remove(1);			
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime()){
				System.out.println("SIMULATION ERROR! CHECK EVENTS ARE IN PROPER ORDER.");
			}
			Controller.time = this.request.getArrTime();
			
			this.request.setSerTime(Controller.time);	//Set startTime to now since we're servicing it now
			
			SysNetwork.CPURequests.add(1, request);
			////Check where to put the new DeathEvent in schedule
			//System.out.println("new DeathEvent with arrTime = "+newDeathEvent.request.getArrTime()+" from BirthEvent.");
			SysNetwork.insertNewEvent(newDeathEvent); //Insert new DeathEvent into event schedule 
			
		}
		else if (SysNetwork.CPURequests.size()>2){	//if there are more than 1 request in system
			Controller.CPUState++;		//increase the w
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime()){
				System.out.println("SIMULATION ERROR! CHECK EVENTS ARE IN PROPER ORDER.");
			}
			Controller.time = this.request.getArrTime();
		}
		/*End check if service can be instantly started*/
		
		//Schedule the next BirthEvent
		if (makeNewBirth == true){
			Event newBirthEvent = new BirthEvent();
			//System.out.println("newBirthEvent with arrTime = "+newBirthEvent.request.getArrTime()+" from BirthEvent.");
			SysNetwork.insertNewEvent(newBirthEvent);
		}
	}
}