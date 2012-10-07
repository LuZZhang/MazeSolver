package mazesolver;

import java.awt.Color;
import java.awt.Point;

public class Location {

	Maze maze;
	Point coords;
	double accumulation; double estimated; double totalCost;
	private String listStatus; // Status which can either be "open", "closed", or "none"
	Location parent;
	Color color;
	boolean traversable;
	
	/**
	 * Location constructor.
	 */
	public Location(Maze _maze, int xCoord, int yCoord) {
		maze = _maze;
		
		if (xCoord > maze.xMax || yCoord > maze.yMax)
			throw new IllegalArgumentException();

		coords = new Point(xCoord, yCoord);

		accumulation = 0.0; estimated = 0.0; totalCost = 0.0;
		listStatus = "none";
		parent = null;
		
		color = new Color(maze.getMazeImage().getRGB(coords.x, coords.y));
		Scheme scheme = maze.wallScheme;
		traversable = determine_traversability(color, scheme);
	}
	
	public void resetValues() {
		accumulation = 0.0; estimated = 0.0; totalCost = 0.0;
		listStatus = "none";
		parent = null;
	}
	
	/**
	 * Determine whether a particular color is traversable given
	 * the wall scheme chosen by the user.
	 */
	public boolean determine_traversability(Color color, Scheme scheme) {
		boolean isWhite = color.equals(Color.white);
		boolean isBlack = color.equals(Color.black);

		if (scheme == Scheme.NOWALLS)
			return true;
		else if (scheme == Scheme.MOUNTAINS)
			return !isWhite;
		else if (scheme == Scheme.RIVERS)
			return !isBlack;
		else if (scheme == Scheme.ALLWALLS)
			return !(isWhite || isBlack);
		else
			throw new IllegalArgumentException("Invalid wall scheme.");
	}
	
	public boolean isStartLocation() {
		return (this.equals(maze.getStart()));
	}
	
	public boolean isGoalLocation() {
		return (this.equals(maze.getGoal()));
	}
	
	public boolean isTraversable() {
		return traversable;
	}
	
	public boolean isOpen() {
		return (getListStatus() == "open");
	}
	
	public boolean isClosed() {
		return (getListStatus() == "closed");
	}
	
	public boolean isLoc(Location loc) {
		return (coords.equals(loc.coords));
	}
	
	public boolean isAdjacentWith(Location loc) {
		if (Math.abs(coords.x - loc.coords.x) > 1 ||
				Math.abs(coords.y - loc.coords.y) > 1)
			return false;
		
		if (this.isLoc(loc))
			return false;
					
		return true;
	}
	
	public void addToOpenSet() {
		maze.openList.add(this);
		setListStatus("open");
	}
	
	public void addToClosedSet() {
		maze.openList.remove(this);
		setListStatus("closed");
	}
	
	public boolean blocked() {
		return (!isTraversable() && !isOpen());
	}
	
	public int getX() {
		return coords.x;
	}
	
	public int getY() {
		return coords.y;
	}
	
	// Compare function for use by the open list's comparator.
	public int compareTo(Location loc) {
		if (this.totalCost < loc.totalCost)
			return -1;
		else if (loc.totalCost < this.totalCost)
			return 1;
		else
			return 0;
	}
	
	// Function for debugging purposes.
	public String printInfo() {
		return "(" + coords.x + ", " + coords.y + ")";
	}

	public double getAccumulation() {
		return accumulation;
	}

	public void setAccumulation(double accumulation) {
		this.accumulation = accumulation;
	}

	public String getListStatus() {
		return listStatus;
	}

	public void setListStatus(String listStatus) {
		this.listStatus = listStatus;
	}
	
}
