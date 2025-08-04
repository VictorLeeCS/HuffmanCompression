/*  Student information for assignment:
 *
 *  On OUR honor, Victor Lee and Peter Hwang, 
 *  this programming assignment is OUR own work
 *  and WE have not provided this code to any other student.
 *
 *  Number of slip days used:0
 *
 *  Student 1 (Student whose Canvas account is being used)
 *  UTEID:VCL363   
 *  email address: victorleecs@utexas.edu
 *  TA name:Nina
 *  
 *  Student 2 
 *  UTEID: sh49687  
 *  email address: petersh0317@utexas.edu
 */
import java.util.LinkedList;
import java.util.ListIterator;

public class PriorityQueue<E extends Comparable<? super E>> {
	private LinkedList<E> con;
	
	//Creates a new LinkedList object and sets it to con
	public PriorityQueue() {
		con = new LinkedList<>();
	}
	
	
	//precondition: value != null
	//puts the value in the right spot in the linkedList
	public void enqueue(E value) {
		if (value == null) {
			throw new IllegalArgumentException("Violation of precondition."); 
		}
		if (con.size() == 0) {
			con.add(value);
		} else {
			boolean added = false;
			ListIterator<E> it = con.listIterator();
			while (it.hasNext() && !added) {
				if (it.next().compareTo(value) > 0) {
				    
				    //insert at the node before current
					it.previous();
					it.add(value);
					added = true;
				}
			}
			
			//if value has not been added in yet, add it to the end
			if (!added) {
				con.addLast(value);
			}
		}
	}
	
	//Precondtion : !con.isEmpty()
	//remove and returns the first value in the list
	public E dequeue() {
		if (con.isEmpty()) {
			throw new IllegalStateException("Queue is empty.");
		} else {
			return con.removeFirst();
		}
	}
	

	//returns the size of the list
	public int size() {
		return con.size();
	}
}