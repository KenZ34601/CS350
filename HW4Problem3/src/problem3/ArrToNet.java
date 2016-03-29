package problem3;

public class ArrToNet extends Event {
	private Event newDeathEvent;
	
	public ArrToNet(Request req) {
		request = req;
		request.setArrTime(Controller.time);	//Gives request the arrivalTime to Net
		request.setCore(0);
	}
	
	public void run() {
		
		// add a newly born request to the schedule of requests
		SysNetwork.NetRequests.add(this.request);
		
		/*Start check if service can be instantly started*/
		if (SysNetwork.NetRequests.size()==1){	// 	if server was idle upon its birth
			//System.out.println("ArrToNet making a new NetDeathEvent...");
			newDeathEvent = new NetDeathEvent();
			
			//we start service for that request	
			request = SysNetwork.NetRequests.remove();			
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime()){		
				System.out.println("SIMULATION ERROR! CHECK EVENTS ARE IN PROPER ORDER.");
			}
			Controller.time = this.request.getArrTime();
			//End debug statement
			this.request.setSerTime(Controller.time);	//Set startTime to now since we're servicing it now
			//So that we can predict when service is done
			SysNetwork.NetRequests.addFirst(request);
			
			//and schedule death event
			//System.out.println("newNetDeathEvent with endTime = "+newDeathEvent.request.getEndTime()+" from ArrToNet.");
			SysNetwork.insertNewEvent(newDeathEvent); //Insert new DeathEvent into event schedule 
		}
		else if (SysNetwork.NetRequests.size()>1){	//if there are more than 1 request in system
			Controller.NetState++;		//increase the w
			Controller.time = this.request.getArrTime();
		}
		/*End check if service can be instantly started*/
	}
}