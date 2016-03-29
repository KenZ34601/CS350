package problem2;

//These are the "customers" of the system that are served by server
public class Request {
	private double IAT;
	private double Ts;
	private double arrivalTime;
	private double startTime;
	private double endTime;
	private double Tq;
	private double remainingTime;
	private int reqType;   //1 = CPU bound jobs, 2 = IO bound jobs
	
	//Every instance of request holds all the info for easy retrieving later
	public Request(){
		IAT = 0;
		arrivalTime = 0;
		Ts = 0;
		startTime = 0;
		endTime = 0;
		Tq = 0;
		remainingTime = 0;
		reqType = 0;
	}
	
	public void setArrTime(double time) {
		arrivalTime = time;
	}
	public void setTs(double time) {
		Ts = time;
	}	
	public void setIAT(double time) {
		IAT = time;
	}	
	public void setSerTime(double time) {
		startTime = time;
		if (remainingTime != 0) {
			endTime = startTime + remainingTime;
		} else {
			endTime = startTime + Ts;
		}
		Tq = endTime - arrivalTime;
	}
	public void setReqType(int type) {
		reqType = type;
	}	
	
	public void setRemTime(){
		//save what time is left for the request being served
		if (remainingTime == 0){
			remainingTime = Ts - (Controller.time - startTime);
		} else {
			remainingTime = remainingTime - MM1System.quantum;
		}
	}
	public double getArrTime(){
		return arrivalTime;
	}
	public double getTs(){
		return Ts;
	}
	public double getIAT(){
		return IAT;
	}
	public double getTq(){
		return Tq;
	}
	public double getStartTime(){
		return startTime;
	}
	public double getEndTime(){
		return endTime;
	}
	public int getReqType(){
		return reqType;
	}
	public double getRemTime(){
		return remainingTime;
	}

}