package problem2;

public class BirthEvent extends Event {

	public BirthEvent(int type) {
		switch (type){
		case 1:
			eventTime = MM1System.genExp(MM1System.CPU_lambda);		//eventTime = IAT
			request.setTs(MM1System.genExp(1/MM1System.CPU_Ts));	//Gives request the Ts
			break;
		case 2:
			eventTime = MM1System.genExp(MM1System.IO_lambda);		//eventTime = IAT
			request.setTs(MM1System.genExp(1/MM1System.IO_Ts));		//Gives request the Ts
			break;
		}
		request.setReqType(type);		//Gives request its type
		request.setIAT(eventTime);		//Gives request the IAT time of BirthEvent
		request.setArrTime(Controller.time+eventTime);	//Gives request the arrivalTime of BirthEvent
	}
	
	public void run() {
		//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
		if (Controller.time>this.request.getArrTime())		
			System.out.println("SIMULATION ERROR! Check events are in proper order.");
		Controller.time = this.request.getArrTime();
		// add a newly born request to the schedule of requests
		MM1System.insertNewRequest(request);
		//if new request is the only one in the system 
		// 	ie server was idle upon its birth,
		if ((MM1System.requestSchedule.size()==1)){	
			Event newDeathEvent = new DeathEvent();	
			//we start service for that request	
			request = MM1System.requestSchedule.remove();
			
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime())		
				System.out.println("SIMULATION ERROR! Check events are in proper order.");
			Controller.time = this.request.getArrTime();
			
			this.request.setSerTime(Controller.time);	//Set startTime to now since we're servicing it now
			//So that we can predict when service is done
			//and schedule death event
			MM1System.requestSchedule.addFirst(request);
			MM1System.insertNewEvent(newDeathEvent);
		}
		else if (MM1System.requestSchedule.size()>1){	//if there are more than 1 request in system
			Controller.state++;		//increase the w
			//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
			if (Controller.time>this.request.getArrTime())
				System.out.println("SIMULATION ERROR! Check events are in proper order.");
			Controller.time = this.request.getArrTime();
		}
		//Schedule the next BirthEvent
		Event newBirthEvent = new BirthEvent(request.getReqType());
		MM1System.insertNewEvent(newBirthEvent);
	}
}