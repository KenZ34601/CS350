package problem3;

public class DiskDeathEvent extends Event {
	Event newBirthEvent;
	
	public DiskDeathEvent() {
		eventTime = SysNetwork.genNorm(SysNetwork.DiskMean,SysNetwork.DiskStdDev);		//eventTime  = Ts
		while (eventTime<=0)								//Makes sure eventTime isn't negative in the rare chance
			eventTime = SysNetwork.genNorm(SysNetwork.DiskMean,SysNetwork.DiskStdDev);	
		//eventTime =SysNetwork.genExp(1/0.100);				///TESTWITHHW3 TESTWITHHW3 TESTWITHHW3
		request = SysNetwork.DiskRequests.remove();			//Takes the first request waiting from request list
		request.setTs(eventTime);							//Gives it back its Ts
		SysNetwork.DiskRequests.addFirst(request);			//Puts the request back into the list
	}
	
	public void setReqSerTime(double time){					//Sets the time at which the request is scheduled
		request = SysNetwork.DiskRequests.remove();
		request.setSerTime(time);
		SysNetwork.DiskRequests.addFirst(request);
	}
	
	public void run(){
		//System.out.println("This is to tell you DeathEvent started. time= "+Controller.	time);
		
		// remove the record of request from schedule
		if (SysNetwork.DiskRequests.size()>1){
			Controller.DiskState--;
		}
		request = SysNetwork.DiskRequests.removeFirst();
		
		//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
		if (Controller.time>request.getEndTime())
			System.out.println("OH NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		Controller.time = request.getEndTime();
		
		/*Start determining what the next course of action of request is*/
		double probability = SysNetwork.genUnif(0, 1); //Generate number to use as probability of request going to each destination
		if (probability <=0.5){
			newBirthEvent = new BirthEvent(Controller.time,request);
			//System.out.println("newBirthEvent with arrTime = "+newBirthEvent.request.getArrTime()+" from DiskDeathEvent.");
		} else {
			newBirthEvent = new ArrToNet(request);
			//System.out.println("newArtToNet with arrTime = "+newBirthEvent.request.getArrTime()+" from DiskDeathEvent.");
		}
		
		SysNetwork.insertNewEvent(newBirthEvent);
		/*End determining what the next course of action of request is*/	
		
		// Check if other requests are pending in the queue
		if (SysNetwork.DiskRequests.size()>0) {
			//System.out.println("DiskDeathEvent making a new DiskDeathEvent...");
			Event newDeathEvent = new DiskDeathEvent();
			//Set new DeathEvent's service time to the max of previous request's end time and arrivalTime of new DeathEvent
			((DiskDeathEvent) newDeathEvent).setReqSerTime(Math.max(request.getEndTime(), newDeathEvent.request.getArrTime())); 
			SysNetwork.insertNewEvent(newDeathEvent);
		}
	}
}