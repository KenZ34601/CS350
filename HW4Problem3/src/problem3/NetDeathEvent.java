package problem3;

public class NetDeathEvent extends Event {
	Event newBirthEvent;
	
	public NetDeathEvent() {
		eventTime = SysNetwork.NetTs;					//eventTime  = Ts
		//eventTime =SysNetwork.genExp(1/0.025);				///TESTWITHHW3 TESTWITHHW3 TESTWITHHW3
		request = SysNetwork.NetRequests.remove();		//Takes the first request waiting from request list
		request.setTs(eventTime);						//Gives it its Ts
		SysNetwork.NetRequests.addFirst(request);		//Puts the request back into the list
	}
	
	public void setReqSerTime(double time){				//Sets the time at which the request is scheduled
		request = SysNetwork.NetRequests.remove();
		request.setSerTime(time);
		SysNetwork.NetRequests.addFirst(request);
	}
	
	public void run(){
		//System.out.println("This is to tell you DeathEvent started. time= "+Controller.	time);
		
		// remove the record of request from schedule
		if (SysNetwork.NetRequests.size()>1){
			Controller.NetState--;
		}
		request = SysNetwork.NetRequests.removeFirst();
		//System.out.println("NetDeathrequest.Tq = "+request.getTq());
		
		//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
		if (Controller.time>request.getEndTime())
			System.out.println("OH NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		Controller.time = request.getEndTime();
		//End debug statement
		
		/*Start determining what the next course of action of request is*/
		//Network always goes back to CPU
		newBirthEvent = new BirthEvent(Controller.time, request);
		//System.out.println("newBirthEvent with arrTime = "+newBirthEvent.request.getArrTime()+" from NetDeathEvent.");
		SysNetwork.insertNewEvent(newBirthEvent);
		/*End determining what the next course of action of request is*/	
		
		// Check if other requests are pending in the queue
		if (SysNetwork.NetRequests.size()>0) {
			Event newDeathEvent = new NetDeathEvent();
			
			//Set new DeathEvent's service time to the max of previous request's end time and arrivalTime of new DeathEvent
			//System.out.println("NetDeathEvent making a new NetDeathEvent...");
			((NetDeathEvent) newDeathEvent).setReqSerTime(Math.max(request.getEndTime(), newDeathEvent.request.getArrTime())); 
			SysNetwork.insertNewEvent(newDeathEvent);
		}
	}
}