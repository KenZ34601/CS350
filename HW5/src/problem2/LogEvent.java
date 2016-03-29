package problem2;

public class LogEvent extends Event {
	private static int runCount = 0;
	private static double Tq = 0;
	private static double Tw = 0;

	private static double IO_cumulTq = 0;
	private static double IO_cumulTw = 0;
	private static double IO_slowdown = 0;
	private static double CPU_cumulTq = 0;
	private static double CPU_cumulTw = 0;
	private static double CPU_slowdown = 0;
	private static double cumulQ = 0;
	private static double cumulW = 0;
	
	
	public LogEvent(double nextTime){
		eventTime = MM1System.genExp(MM1System.IO_lambda);
		request.setArrTime(nextTime+eventTime);
		//System.out.println("LogEvent's arrival time is " + request.getArrTime());
	}
	
	public static void setTqTw(int type, double responseTime, double serviceTime){
		Tq = responseTime;
		Tw = Tq - serviceTime;

		if (type==1){
			CPU_cumulTq += Tq;
			CPU_cumulTw += Tw;
			
		} else {
			IO_cumulTq += Tq;
			IO_cumulTw += Tw;
		}
	}
	
	public void run(){
		// Increase runCount
		runCount++;
		//Print out and log Tw, Tq, q, and w
		//System.out.println("Tw: "+Tw+"\tTq: "+Tq+"\tq: "+q+"\tw: "+w);
		cumulQ += MM1System.requestSchedule.size();
		cumulW += Controller.state;
		//Schedule next LogEvent
		Event newLogEvent = new LogEvent(this.request.getArrTime());
		MM1System.insertNewEvent(newLogEvent);
	}
	
	public static void getData(){
		CPU_slowdown = CPU_cumulTq/(CPU_cumulTq-CPU_cumulTw);
		IO_slowdown = IO_cumulTq/(IO_cumulTq-IO_cumulTw);
		Controller.writer2.write("Final Results of simulation: \n"	//Write results to file
				+ "\tIO_Tw: "+IO_cumulTw/runCount+"\n"
				+ "\tIO_Tq: "+IO_cumulTq/runCount+"\n"
				+ "\tIO_Slowdown: "+IO_slowdown//*runCount*/+"\n"
				+ "\tCPU_Tw: "+CPU_cumulTw/(runCount/2)+"\n"
				+ "\tCPU_Tq: "+CPU_cumulTq/(runCount/2)+"\n"
				+ "\tCPU_Slowdown: "+CPU_slowdown//*(runCount/2)*/+"\n"
				+ "\tw: "+cumulW/runCount+"\n"
				+ "\tq: "+cumulQ/runCount);
		System.out.println("Final Results of simulation: \n"		//Also outputs results
				+ "\tIO_Tw: "+IO_cumulTw/runCount+"\n"
				+ "\tIO_Tq: "+IO_cumulTq/runCount+"\n"
				+ "\tIO_Slowdown: "+IO_slowdown/*runCount*/+"\n"
				+ "\tCPU_Tw: "+CPU_cumulTw/(runCount/2)+"\n"
				+ "\tCPU_Tq: "+CPU_cumulTq/(runCount/2)+"\n"
				+ "\tCPU_Slowdown: "+CPU_slowdown/*(runCount/2)*/+"\n"
				+ "\tw: "+cumulW/runCount+"\n"
				+ "\tq: "+cumulQ/runCount);
	}
	
}