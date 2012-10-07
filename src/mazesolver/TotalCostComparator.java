package mazesolver;

import java.util.*;

/**
 * Comparator for the maze's open list. 
 * 
 * @author Ryan
 */
public class TotalCostComparator implements Comparator<Location> {
	
	  // Locations with lower estimated total costs go earlier in the list.
	  public int compare(Location loc1, Location loc2) {
	    return loc1.compareTo(loc2);
	  }
	  
}
