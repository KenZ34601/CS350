package problem3;

public class CPUDeathEvent extends Event {
	private Event newDeathEvent;
	private Event newBirthEvent;
	
	public CPUDeathEvent(int core, int ReqIndex) {
		eventTime = SysNetwork.genUnif(SysNetwork.CPULow,SysNetwork.CPUHigh);		//eventTIme  = Ts
		//eventTime = SysNetwork.genExp(1/0.020);		///TESTWITHHW3 TESTWITHHW3 TESTWITHHW3
		request = SysNetwork.CPURequests.remove(ReqIndex);		//Takes the first request waiting from request list
		request.setTs(eventTime);							//Gives it its Ts
		request.setCore(core);
		SysNetwork.CPURequests.add(ReqIndex, request);		//Puts the request back into the list
	}
	
	public void setReqSerTime(double time, int ReqIndex){					//Sets the time at which the request is scheduled
		request = SysNetwork.CPURequests.remove(ReqIndex);
		request.setSerTime(time);
		SysNetwork.CPURequests.add(ReqIndex, request);
	}
	
	public void nextDest(){
		//Will be used to determine next destination of request
	}
	
	public void run(){

		// remove the record of request from schedule
		if (SysNetwork.CPURequests.size()>2){
			Controller.CPUState--;
		}
		//Makes sure to remove the correct request from request schedule
		if (SysNetwork.CPURequests.getFirst().getCoreNum()==request.getCoreNum())	
			request = SysNetwork.CPURequests.removeFirst();
		else 
			request = SysNetwork.CPURequests.remove(1);
		
		//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
		if (Controller.time>request.getEndTime())
			System.out.println("OH NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		Controller.time = request.getEndTime();

		// Check if other requests are pending in the queue
		if (SysNetwork.CPURequests.size()>=2) {		//If there is at least 2 requests in the schedule
			
			//Make a new DeathEvent using the current core
			if (SysNetwork.CPURequests.get(0).getCoreNum() == 0){
				newDeathEvent = new CPUDeathEvent(request.getCoreNum(), 0);
				((CPUDeathEvent) newDeathEvent).setReqSerTime(Math.max(request.getEndTime(), newDeathEvent.request.getArrTime()), 0);
				
			} else {
				newDeathEvent = new CPUDeathEvent(request.getCoreNum(), 1);
				((CPUDeathEvent) newDeathEvent).setReqSerTime(Math.max(request.getEndTime(), newDeathEvent.request.getArrTime()), 1);
			}			
			//System.out.println("new DeathEvent with arrTime = "+newDeathEvent.request.getArrTime()+" from DeathEvent.");
			SysNetwork.insertNewEvent(newDeathEvent);
		}
		
		/*Start determining what the next course of action of request is*/
		double probability = SysNetwork.genUnif(0, 1); //Generate number to use as probability of request going to each destination
		if (probability <= 0.5) {							//Finish request with 0.5 chance
			
			if (Controller.time>SysNetwork.simulationTime/2){
				LogEvent.setTqTw(request.getTq(),request.getTs());
			}
		} else {
			if (0.5 < probability && probability <= 0.6){	//Go to disk with 0.1 chance
				newBirthEvent = new ArrToDisk(request);				////Make a "birth event" for disk
				//System.out.println("Going to Disk next!");
			} else {										//Go to network with 0.4 chance
				newBirthEvent = new ArrToNet(request);				////Make a "birth event" for network
				//System.out.println("Going to Net next!");
			}
			//System.out.println("new BirthEvent with arrTime = "+newBirthEvent.request.getArrTime()+" from CPUDeathEvent.");
			SysNetwork.insertNewEvent(newBirthEvent);
		}
		/*End determining what the next course of action of request is*/
		
	}
}