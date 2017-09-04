// Bongki Moon (bkmoon@snu.ac.kr)
import java.util.*;

public class Itinerary
{
	ArrayList<Flight> flist;
	boolean found;
	// constructor
	Itinerary() {
		flist = new ArrayList<Flight>();
		this.found = false;
	}

	public boolean isFound() {
		return found;
	}

	public void print() {
		if(found){
			for(int i = flist.size()-1 ; i >= 0 ; i--) {
				System.out.print('[' + flist.get(i).src + "->" + flist.get(i).dest + ':' + flist.get(i).stime + "->" + flist.get(i).dtime + ']');
			}
			System.out.println();
			
		}
		else {
			System.out.println("No Flight Schedule Found.");
		}
	}

}
