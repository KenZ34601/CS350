package problem3;
import java.util.LinkedList;
import java.util.Random;

public class SysNetwork extends SimulatedSystem {
	static double lambda = 50;
	static double CPULow = 0.001;
	static double CPUHigh = 0.039;
	static double DiskMean = 0.100;
	static double DiskStdDev = 0.030;
	static double NetTs = 0.025;
	static Random rand = new Random();
	public static double simulationTime = 100;
	
	public static LinkedList<Request> CPURequests = new LinkedList<>();		// CPU queue
	public static LinkedList<Request> DiskRequests = new LinkedList<>();	// Disk queue
	public static LinkedList<Request> NetRequests = new LinkedList<>();		// Network queue
	
	private static Event checkEvent = new Event();
	private static int size;

	// Function used to generate an exponential Random Variable
	public static double genExp(double lamb) {
		double uRV = rand.nextDouble();
		double eRV = -(Math.log(1-uRV))/lamb;
		//double eRVCDF = 1-Math.exp(-lamb*uRV);
		//System.out.println(uRV+"\t"+eRV);
		return eRV;
	}
	
	// Function used to generate a uniform RV between low and high 
	public static double genUnif(double low, double high) {
		double uRV = rand.nextDouble();
		uRV = ((high-low)*uRV)+low;
		return uRV;
	}
	
	// Function used to generate normal distribution with mean and std dev
	public static double genNorm(double mean, double stdDev) {
		double nRV = rand.nextGaussian();	//normalRV from N(0,1)
		nRV = stdDev*nRV+mean;	//Linear trans to (mean,std dev)
		return nRV;
	}
	
	//Universal function for all events to use to insert a new event into schedule
	public static void insertNewEvent(Event newEvent){
		size = Controller.schedule.size();
		double eventReqTime;		// time of execution of event? Sorta. Basically what I'm using to compare to other events
		if ((newEvent instanceof BirthEvent) || (newEvent instanceof LogEvent) || 
				(newEvent instanceof ArrToNet) || (newEvent instanceof ArrToDisk))
			eventReqTime = newEvent.request.getArrTime();		//Use arrivalTime to compare if newEvent is a "Birth" event
		else 
			eventReqTime = newEvent.request.getEndTime();		//Use endTime to compare if enwEvent is a "Death" event
		if (size==0){
			Controller.schedule.add(newEvent);
		} else{
			for (int i = 0; i<size;i++) {
				checkEvent = Controller.schedule.get(i);
				if (checkEvent instanceof BirthEvent) {
					if (eventReqTime<checkEvent.request.getArrTime()){ //if newEvent arrives before LogEvent
						Controller.schedule.add(i,newEvent);	//Put newEvent before the BirthEvent
						break;
					}
				} else if (checkEvent instanceof ArrToDisk) {				//Compares endTime of CPUDeathEvent and arrivalTime of newEvent
					if (eventReqTime<checkEvent.request.getArrTime()){ //if newEvent arrives before LogEvent
						Controller.schedule.add(i,newEvent);	//Put newEvent before the ArrToDisk
						break;
					}
				} else if (checkEvent instanceof ArrToNet) {				//Compares endTime of CPUDeathEvent and arrivalTime of newEvent
					if (eventReqTime<checkEvent.request.getArrTime()){ //if newEvent arrives before LogEvent
						Controller.schedule.add(i,newEvent);	//Put newEvent before the ArrToNet
						break;
					}
				} else if (checkEvent instanceof CPUDeathEvent) {				//Compares endTime of CPUDeathEvent and arrivalTime of newEvent
					if (eventReqTime<checkEvent.request.getEndTime()){	//if arrivalTime<endTime
						Controller.schedule.add(i,newEvent);	//Put newEvent before the CPUDeathEvent
						break;
					}
				} else if (checkEvent instanceof DiskDeathEvent) {		//Compares endTime of DiskDeathEvent and arrivalTime of newEvent
					if (eventReqTime<checkEvent.request.getEndTime()){	//if arrivalTime<endTime
						Controller.schedule.add(i,newEvent);	//Put newEvent before the DiskDeathEvent
						break;
					}
				} else if (checkEvent instanceof NetDeathEvent) {		//Compares endTime of NetDeathEvent and arrivalTime of newEvent
					if (eventReqTime<checkEvent.request.getEndTime()){	//if arrivalTime<endTime
						Controller.schedule.add(i,newEvent);	//Put newEvent before the NetDeathEvent
						break;
					}
				}else if (checkEvent instanceof LogEvent) {	//Compare arrivalTimes
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
	}
}