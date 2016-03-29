import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.Semaphore;

class MyProcess extends Thread {
	private int id;
	volatile static private PrintWriter writer;
	volatile static private PrintWriter writer2;
	
	private int count[] = new int[100];
	private Random random = new Random();
	final int part = 5;	    //1 = part 3a
							//2 = part b
							//3 = part c
							//4 = part d
							//5 = Question 4
	volatile static private boolean[] flag;
	volatile static private int turn = 1;
	
	public MyProcess(int i) {
		id = i;
	}
	 
	public void run() {
		int i = id==0 ? 0:1;
		int j = id==0 ? 1:0;
		switch (part){
		case 1:
			for (int k = 1; k < 6; k++){
				System.out.println("Thread "+i+" is starting iteration "+k);
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("We hold these truths to be self-evident, that all men are created equal,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that they are endowed by their Creator with certain unalienable Rights,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that among these are Life, Liberty and the pursuit of Happiness.");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("Thread "+i+" is done with iteration "+k);
			}
			break;
		case 2:
			for (int k = 1; k < 6; k++){
			    flag[i]=true; 
			    while (flag[j]){
			       if (turn==j) { 
			         flag[i]=false;
			         while (flag[j]==true) {};
			         flag[i]=true;
			       }
			    }		    
			    System.out.println("Thread "+i+" is starting iteration "+k);
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("We hold these truths to be self-evident, that all men are created equal,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that they are endowed by their Creator with certain unalienable Rights,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that among these are Life, Liberty and the pursuit of Happiness.");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("Thread "+i+" is done with iteration "+k);
			    turn=j;
			    flag[i]=false;
			}
			break;
		case 3:
			for (int k = 1; k < 6; k++){
				flag[i]=true; 
			    while (flag[j]){
			       if (turn==j) { 
			         flag[i]=false;
			         while (flag[j]==true) {};
			         flag[i]=true;
			       }
			    }		    
			    System.out.println("Thread "+i+" is starting iteration "+k);
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("We hold these truths to be self-evident, that all men are created equal,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that they are endowed by their Creator with certain unalienable Rights,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that among these are Life, Liberty and the pursuit of Happiness.");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("Thread "+i+" is done with iteration "+k);
			    turn=j;
			    flag[i]=false;
			}
			break;
		case 4:
			for (int k = 1; k < 6; k++){
				turn=j; 
			    flag[i]=true;
			    while (flag[j] && turn==j) {};    
			    System.out.println("Thread "+i+" is starting iteration "+k);
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("We hold these truths to be self-evident, that all men are created equal,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that they are endowed by their Creator with certain unalienable Rights,");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("that among these are Life, Liberty and the pursuit of Happiness.");
				//Sleep for some random amount of time between 0 to 20 msec
				sleep(20);
				System.out.println("Thread "+i+" is done with iteration "+k);
				flag[i]=false;
			}
			break;
		case 5:
			//m is the number of runs for the multi-threaded process.
			for (int m = 0; m < 100; m++){
				for (int k = 1; k < 6; k++){
					turn=j; 
				    flag[i]=true;
				    while (flag[j] && turn==j) {count[m]++;};    
				    System.out.println("Thread "+i+" is starting iteration "+k);
					//Sleep for some random amount of time between 0 to 20 msec
					sleep(20);
					System.out.println("We hold these truths to be self-evident, that all men are created equal,");
					//Sleep for some random amount of time between 0 to 20 msec
					sleep(20);
					System.out.println("that they are endowed by their Creator with certain unalienable Rights,");
					//Sleep for some random amount of time between 0 to 20 msec
					sleep(20);
					System.out.println("that among these are Life, Liberty and the pursuit of Happiness.");
					//Sleep for some random amount of time between 0 to 20 msec
					sleep(20);
					System.out.println("Thread "+i+" is done with iteration "+k);
					flag[i]=false;
				}
				System.out.printf("count of %d: 	%d \n", m ,count[m]);
				if (id==0)
					writer.println(count[m]+"\n");
				else
					writer2.println(count[m]+"\n");
			}
			if (id==0)
				writer.close();
			else
				writer2.close();
			break;
		}
		
	}
	
	public void sleep(int dur) {
		try {Thread.sleep(random.nextInt(dur));} 
		catch(InterruptedException e) {};
	}
	public static void main(String[] args) throws IOException {
		final int N = 2;
		writer = new PrintWriter("countResults.txt");
		writer2 = new PrintWriter("countResults2.txt");
		flag = new boolean[N];
		MyProcess[] p = new MyProcess[N];
		for (int i = 0; i < N; i++) {
	        p[i] = new MyProcess(i);
	        p[i].start();
		}
	}
}