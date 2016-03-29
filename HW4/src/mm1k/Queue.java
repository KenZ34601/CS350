/*
CS350 HW4
Quan Zhou
Spring 2016
*/

package mm1k;

public class Queue {
	
	int MAX_SIZE;
	int size;
	event first;
	event last;
	int hasDeath;
	
	public Queue(){
		hasDeath = 0;
		first = null;
		last = null;
	}
	
	public Queue(int k){
		MAX_SIZE = k;
		hasDeath = 0;
		first = null;
		last = null;
	}
	
	public void push(){
		event newevent = new event();
		if(size == 0){
			first = newevent;
			last = first;
		}else{
			last.next = newevent;
			last = newevent;
		}
		
		size++;
	}
	
	public void push(double time, String eName, int bogus){
		event newevent = new event(time, eName);
		if(size == 0){
			first = newevent;
			last = first;
		}else{
			last.next = newevent;
			last = newevent;
		}
		
		size++;
	}
	
	public void push(double a, double s, double e, String eName){
		event newevent = new event(a, s, e, eName);
		if(size == 0){
			first = newevent;
			last = first;
		}else{
			last.next = newevent;
			last = newevent;
		}
		
		size++;
	}
	
	
	public int push(double time, String eName){
		event newevent = new event(time, eName);
		event tmp = first;
		
		if(size == MAX_SIZE && MAX_SIZE != 0){
			return -1;
		}
		
		if(size == 0){
			first = newevent;
			last = first;
		}else if(size == 1){
			if(time >= tmp.time){
				first.next = newevent;
				last = newevent;
			}else{
				newevent.next = first;
				first = newevent;
				last = newevent.next;
			}
		}else{
			if(time < first.time){
				newevent.next = first;
				first = newevent;
			}else{
				event nxtTmp;
				do{
					nxtTmp = tmp.next;
					if(time <= nxtTmp.time){
						newevent.next = nxtTmp;
						tmp.next = newevent;
						size++;
						return 0;
					}
					tmp = tmp.next;
				}
				while(nxtTmp.next != null);
				last.next = newevent;
				last = newevent;
			}	
		}
		size++;
		return 0;
	}
	
	public event pop(){
		if(size == 0){
			System.out.println("CAN'T POP NOTHING IN HERE");
			event shit = new event(0,"NOTHING");
			return shit;
		}
		event ret;
		ret = first;
		first = first.next;
		ret.next = null;
		size--;
		
		return ret;
	}
	
	public void printq(){
		event tmp = first;
		if(tmp == null){
			System.out.println("Queue is empty!");
			return;
		}
		
		while(tmp != null){
			System.out.print(tmp.eventName);
			System.out.print(": ");
			System.out.print(tmp.time);
			System.out.print(", ");
			tmp = tmp.next;
		}
		System.out.println();
	}
	
	public void printcq(){
		event tmp = first;
		if(tmp == null){
			System.out.println("Queue is empty!");
			return;
		}
		int i = 0;
		while(tmp != null){
			System.out.print("c ");
			System.out.print(i++);
			System.out.print(", ");
			tmp = tmp.next;
		}
		System.out.println();
	}
}