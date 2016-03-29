package problem3;

public class ArrToDisk extends Event {
	private Event newDeathEvent;
	
	public ArrToDisk(Request req) {
		request = req;
		request.setArrTime(Controller.time);	//Gives request the arrivalTime to Disk
		request.setCore(0);
	}
	
	public void run() {
		
		// add a newly born request to the schedule of requests
		SysNetwork.DiskRequests.add(this.request);
		
		/*Start check if service can be instantly started*/
		if (SysNetwork.DiskRequests.size()==1){	// 	if either server was idle upon its birth
			//System.out.println("ArrToDisk making a new DiskDeathEvent...");
			newDeathEvent = new DiskDeathEvent();	
			
			//we start service for that request	
			request = SysNetwork.DiskRequests.remove();			
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime()){	
				System.out.println("SIMULATION ERROR! CHECK EVENTS ARE IN PROPER ORDER.");
			}
			Controller.time = this.request.getArrTime();
			
			this.request.setSerTime(Controller.time);	//Set startTime to now since we're servicing it now
			
			//So that we can predict when service is done
			//and schedule death event
			SysNetwork.DiskRequests.addFirst(request);
			
			SysNetwork.insertNewEvent(newDeathEvent); //Insert new DeathEvent into event schedule 
		}
		else if (SysNetwork.DiskRequests.size()>1){	//if there are more than 1 request in system
			Controller.DiskState++;		//increase the w
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime()){
				System.out.println("SIMULATION ERROR! CHECK EVENTS ARE IN PROPER ORDER.");
			}
			Controller.time = this.request.getArrTime();
		}
		/*End check if service can be instantly started*/
	}
}