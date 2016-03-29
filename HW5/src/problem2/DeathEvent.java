package problem2;

public class DeathEvent extends Event {
	private int ReqIndex = 0;	//index of request w/ highest slowdown
	public DeathEvent() {
		//request = MM1System.requestSchedule.remove();		//Takes the first request waiting from request list
		//MM1System.requestSchedule.addFirst(request);		//Puts the request back into the list
		changeReq();
	}
	
	public void setReqSerTime(double time){					//Sets the time at which the request is scheduled
		request = MM1System.requestSchedule.remove(ReqIndex);
		request.setSerTime(time);
		MM1System.requestSchedule.add(ReqIndex,request);
	}
	
	public void changeReq(){
		//make the deathEvent change the request it is completing
		switch (MM1System.scheduleType){
		case 4:
			double tempSD = 0;		//slowdown for iterating
			double maxSD = 0;		//max slowdown
			for (int i=0; i<MM1System.requestSchedule.size(); i++){
				//Find the request with highest slowdown as of current time
				tempSD = (Controller.time-MM1System.requestSchedule.get(i).getArrTime()+
						MM1System.requestSchedule.get(i).getTs())/MM1System.requestSchedule.get(i).getTs();
				if (MM1System.requestSchedule.get(i).getRemTime()!=0){
					tempSD = (Controller.time-MM1System.requestSchedule.get(i).getArrTime()+
					MM1System.requestSchedule.get(i).getRemTime())/MM1System.requestSchedule.get(i).getTs();
				}
						/*System.out.println("Request Type: "+MM1System.requestSchedule.get(i).getReqType()+
								"\t\tSlowdown: "+tempSD+"\t\tTs: "+MM1System.requestSchedule.get(i).getTs());*/
				if (tempSD > maxSD){
					maxSD = tempSD;
					ReqIndex = i;
				}
			}
			request = MM1System.requestSchedule.get(ReqIndex);
			break;
		default:
			request = MM1System.requestSchedule.getFirst();
			break;
		}
		
		//setReqSerTime to currentTime for the new request
		setReqSerTime(Controller.time);
	}
	public void run(){
		//System.out.println("This is to tell you DeathEvent started. time= "+Controller.	time);
		
		if (MM1System.requestSchedule.size()>1){
			Controller.state--;
		}
		// remove the record of request from schedule
		//request = MM1System.requestSchedule.removeFirst();
		request = MM1System.requestSchedule.remove(ReqIndex);
		switch (MM1System.scheduleType){
		case 4: // Highest Slowdown Next w/ timeout 100ms
			if (request.getEndTime()>request.getStartTime()+MM1System.quantum){
				//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
				if (Controller.time>eventTime)
					System.out.println("SIMULATION ERROR! Check events are in proper order.");
				Controller.time = eventTime;
				
				request.setRemTime(); 	//Set remaining time for request
				MM1System.requestSchedule.add(ReqIndex,request);	//Put request back where DeathEvent got it
				if (MM1System.requestSchedule.size()>1){
					Controller.state++;
				}
				changeReq();		//Change request to the next one in the request Schedule with the highest slowdown
				Event newDeathEvent = new DeathEvent();
				((DeathEvent) newDeathEvent).setReqSerTime(Controller.time);
				MM1System.insertNewEvent(newDeathEvent);
				break;
			}
			//No break here so that it falls to default when Ts<quantum
		case 3:	// Round Robin
			if ((MM1System.scheduleType==3) && (request.getEndTime()>request.getStartTime()+MM1System.quantum)){
				//Debugging statement to make sure events are in proper order / sim time doesn't go backwards
				if (Controller.time>eventTime)
					System.out.println("SIMULATION ERROR! Check events are in proper order.");
				Controller.time = eventTime;
				
				request.setRemTime(); 	//Set remaining time for request
				MM1System.requestSchedule.add(request);	//Put request at end
				if (MM1System.requestSchedule.size()>1){
					Controller.state++;
				}
				//request = MM1System.requestSchedule.removeFirst();	//Take first request
				Event newDeathEvent = new DeathEvent();
				((DeathEvent) newDeathEvent).setReqSerTime(Controller.time);
				MM1System.insertNewEvent(newDeathEvent);
				break;
			}			
			//No break here so that it falls to default when Ts<quantum
		default:
			//Debugging statement to make sure events are in proper order / simulation time doesn't go backwards
			if (Controller.time>request.getEndTime())
				System.out.println("SIMULATION ERROR! Check events are in proper order.");
			Controller.time = request.getEndTime();			
			/*System.out.println(this.request.getIAT()+"\t"+
				this.request.getTs()+"\t"+this.request.getArrTime()+"\t"+
				this.request.getStartTime()+"\t"+this.request.getEndTime()+"\t"+
				this.request.getTq());*/
			if (Controller.time>MM1System.simulationTime/2){
				Controller.writer.write(this.request.getIAT()+"\t"+
					this.request.getTs()+"\t"+this.request.getArrTime()+"\t"+
					this.request.getStartTime()+"\t"+this.request.getEndTime()+"\t"+
					this.request.getTq()+"\n");
				LogEvent.setTqTw(request.getReqType(),request.getTq(),request.getTs());
			}
			// Check if other requests are pending in the queue
			if (MM1System.requestSchedule.size()>0) {
				Event newDeathEvent = new DeathEvent();
				//Set new DeathEvent's service time to the max of previous request's end time and arrivalTime of new DeathEvent
				((DeathEvent) newDeathEvent).setReqSerTime(Math.max(request.getEndTime(), newDeathEvent.request.getArrTime())); 
				MM1System.insertNewEvent(newDeathEvent);			
			}
			//System.out.println("\t\t\tRequest has exited the system.");
		}
	}
}