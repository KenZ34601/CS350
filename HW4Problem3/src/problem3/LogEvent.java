package problem3;

public class LogEvent extends Event {
	private static int runCount = 0;
	private static double Tq = 0;
	private static double Tw = 0;
	private static double cumulTq = 0;		//E[Tq]
	private static double cumulTw = 0;
	private static double cumulQ_cpu = 0;	//E[q]
	private static double cumulQ_disk = 0;
	private static double cumulQ_net = 0;
	private static double cumulW_cpu = 0;
	private static double cumulW_disk = 0;
	private static double cumulW_net = 0;
	private static double cumulQ_sq = 0;	//E[q^2]
	private static double cumulTq_sq = 0;	//E[Tq^2]
	private static double stdDevQ = 0;
	private static double stdDevTq = 0;
	private static int i = 0;
	
	public LogEvent(double nextTime){
		eventTime = SysNetwork.genExp(SysNetwork.lambda);
		request.setArrTime(nextTime+eventTime);
		System.out.println("LogEvent's arrival time is " + request.getArrTime());
	}
	
	public static void setTqTw(double responseTime, double serviceTime){
		Tq = responseTime;
		Tw = Tq - serviceTime;
		cumulTq += Tq;
		cumulTw += Tw;
		cumulTq_sq += Math.pow(Tq, 2);
	}
	
	public void run(){
		// Increase runCount
		runCount++;
		//Print out and log Tw, Tq, q, and w
		//System.out.println("Tw: "+Tw+"\tTq: "+Tq+"\tq: "+q+"\tw: "+w);
		cumulW_cpu += Controller.CPUState;
		cumulQ_cpu += SysNetwork.CPURequests.size();
		cumulQ_sq += Math.pow(SysNetwork.CPURequests.size(), 2);
		cumulW_disk += Controller.DiskState;
		cumulQ_disk += SysNetwork.DiskRequests.size();
		cumulW_net += Controller.NetState;
		cumulQ_net += SysNetwork.NetRequests.size();
		//Schedule next LogEvent
		Event newLogEvent = new LogEvent(this.request.getArrTime());
		SysNetwork.insertNewEvent(newLogEvent);
		
		// Write to file the # of requests waiting in CPU queue
		
		if (Controller.time>=100+i){
			Controller.writer.write((Controller.time-100)+"\t"+Controller.CPUState+"\n");
			i++;
		}
	}
	
	public static double getE(double stdDev){
		double Z_val = 1.96; //Z = 1.96 for 95th percentile
		double Z_val2 = 2.32; //Z = 2.32 for 98th percentile
		System.out.println("98th Confidence level E = "+Z_val2*(stdDev/Math.sqrt(4000)));
		return Z_val*(stdDev/Math.sqrt(4000));
	}
	
	public static void getData(){
		stdDevQ = Math.sqrt(cumulQ_sq - Math.pow(cumulQ_cpu/runCount, 2));
		stdDevTq = Math.sqrt(cumulTq_sq - Math.pow(cumulTq/runCount, 2));
		
		double E_Q = getE(stdDevQ);
		double E_Tq = getE(stdDevTq);
		
		Controller.writer2.write("Final Results of simulation: \n"	//Write results to file
				+ "\tTw: "+cumulTw/runCount+"\n"
				+ "\tTq: "+cumulTq/runCount+"\n"
				+ "\tw_cpu: "+cumulW_cpu/runCount+"\n"
				+ "\tq_cpu: "+cumulQ_cpu/runCount+"\n"
				+ "\tw_disk: "+cumulW_disk/runCount+"\n"
				+ "\tq_disk: "+cumulQ_disk/runCount+"\n"
				+"\tw_net: "+cumulW_net/runCount+"\n"
				+ "\tq_net: "+cumulQ_net/runCount+"\n"
				+ "\tConfidence level of q: ["+cumulQ_cpu/runCount+"-"+E_Q
				+ ", "+cumulQ_cpu/runCount+"+"+E_Q+"]\n"
			+ "\tConfidence level of Tq: ["+cumulTq/runCount+"-"+E_Tq
				+ ", "+cumulTq/runCount+"+"+E_Tq+"]");
		System.out.println("Final Results of simulation: \n"		//Also outputs results
				+ "\tTw: "+cumulTw/runCount+"\n"
				+ "\tTq: "+cumulTq/runCount+"\n"
				+ "\tw_cpu: "+cumulW_cpu/runCount+"\n"
				+ "\tq_cpu: "+cumulQ_cpu/runCount+"\n"
				+ "\tw_disk: "+cumulW_disk/runCount+"\n"
				+ "\tq_disk: "+cumulQ_disk/runCount+"\n"
				+"\tw_net: "+cumulW_net/runCount+"\n"
				+ "\tq_net: "+cumulQ_net/runCount+"\n"
				+ "\tConfidence level of q: ["+cumulQ_cpu/runCount+"-"+E_Q
				+ ", "+cumulQ_cpu/runCount+"+"+E_Q+"]\n"
			+ "\tConfidence level of Tq: ["+cumulTq/runCount+"-"+E_Tq
				+ ", "+cumulTq/runCount+"+"+E_Tq+"]");
	}
	
}