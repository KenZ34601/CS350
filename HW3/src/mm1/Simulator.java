package mm1;

import java.io.*;

public class Simulator { 

    public static void main(String[] args) { 
    	
        File dir = new File("logs");
        if (!dir.exists()) {
        	dir.mkdir();
        }
        
        File dir2 = new File("logs/mm1");
        if (!dir2.exists()) {
        	dir2.mkdir();
        }
    	//Simulation 1: Lambda = 60 and Ts = 0.015 and simulation time = 100
        double lambda = 60;
        double ts = 0.015;
        double length = 1000;        
        mm1queue c1 = new mm1queue(lambda,ts,length);       
        
        File file = new File("logs/mm1/sim1_log.txt"); 
        PrintWriter out;
		try {
			out = new PrintWriter(file);
			System.out.println("SIMULATION 1: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length);
	    	out.println("SIMULATION 1: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length + "\n");
	    	out.println("LOG STATISTICS\n");
	        c1.run(out);
	        System.out.println("DONE.");
	        System.out.println("Log: logs/mm1/sim1_log.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
/*        //Simulation 2: Lambda = 50 and Ts = 0.015 and simulation time = 100
		lambda = 50;
        mm1queue c2 = new mm1queue(50,0.015,100);
        
        file = new File("logs/mm1/sim2_log.txt"); 
		try {
			out = new PrintWriter(file);
			System.out.println("SIMULATION 2: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length);
	    	out.println("SIMULATION 2: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length + "\n");
	    	out.println("LOG STATISTICS\n");
	       c2.run(out);
	        System.out.println("DONE.");
	        System.out.println("Log: logs/mm1/sim2_log.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
        //Simulation 3: Lambda = 65 and Ts = 0.015 and simulation time = 100
		//double lambda = 65;
        lambda = 65;
		mm1queue c3 = new mm1queue(65,0.015,100);
        
        file = new File("logs/mm1/sim3_log.txt");
        //PrintWriter out;
		try {
			out = new PrintWriter(file);
			System.out.println("SIMULATION 3: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length);
	    	out.println("SIMULATION 3: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length + "\n");
	    	out.println("LOG STATISTICS\n");
	        c3.run(out);
	        System.out.println("DONE.");
	        System.out.println("Log: logs/mm1/sim3_log.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        //Simulation 4: Lambda = 65 and Ts = 0.020 and simulation time = 100
		ts = 0.020;
        mm1queue c4 = new mm1queue(65,0.020,100);
        
        file = new File("logs/mm1/sim4_log.txt"); 
		try {
			out = new PrintWriter(file);
			System.out.println("SIMULATION 4: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length);
	    	out.println("SIMULATION 4: lambda = " + lambda + ", Ts = " + ts + ", Sim length = " + length + "\n");
	    	out.println("LOG STATISTICS\n");
	       c4.run(out);
	        System.out.println("DONE.");
	        System.out.println("Log: logs/mm1/sim4_log.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		*/
	}
}
