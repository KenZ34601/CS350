/*
CS350 HW4
Quan Zhou
Spring 2016
*/

package mm1k;

class event{
	double arrival;
	double startS;
	double endS;
	double time;
	String eventName;
	event next;
		
	public event(double intime, String eName){
		next = null;
		time = intime;
		eventName = eName;
	}
		
	public event(double arr, double start,double end, String eName){
		next = null;
		arrival = arr;
		startS = start;
		endS = end;
		eventName = eName;
	}
		
	public event(){
		next = null;
		eventName = "";
	}
		
	public void setNext(event nxt){
		next = nxt;
	}
}