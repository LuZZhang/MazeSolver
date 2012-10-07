package mazesolver;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class MazeRunner {

	private Maze maze;
	private Location currentLoc;
	String[] directions = {"North", "East", "South", "West",
						   "NE", "SE", "SW", "NW" };
	
	/** 
	 * MazeRunner constructor. Runner's initial location set to
	 * maze's start and immediately set to closed.
	 */
	public MazeRunner(Maze maze) {
		this.setMaze(maze); 
	    currentLoc = maze.getStart();
	    currentLoc.setListStatus("closed");
	}
	
	public boolean reachedGoal() {
		return (getCurrentLoc().isGoalLocation());
	}
	
	/**
	 *  Analyze each of the locations adjacent to the runner's location.
	 */
	public void analyzeAll() {
		for (String dir : directions)
			analyzeDirection(dir);
	}
	
	/** 
	 * Calculate the relevant scores for each of the locations adjacent
	 * to the runner's location.
	 */
	public void analyzeDirection(String dir) {
		if (dir == "North") calculateTargetValues(0, -1, 100);
		else if (dir == "East") calculateTargetValues(1, 0, 100);
		else if (dir == "South") calculateTargetValues(0, 1, 100);
		else if (dir == "West") calculateTargetValues(-1, 0, 100);
		else if (dir == "NE") calculateTargetValues(1, -1, 141);
		else if (dir == "SE") calculateTargetValues(1, 1, 141);
		else if (dir == "SW") calculateTargetValues(-1, 1, 141);
		else if (dir == "NW") calculateTargetValues(-1, -1, 141);
		else throw new IllegalArgumentException();
	}
	
	/**
	 * Computes the accumulation, estimation, and total scores of
	 * the location determined by the runner's current location and the direction
	 * specified by the parameters.
	 */
	public void calculateTargetValues(int xDelta, int yDelta, int moveCost) {
		// Compute the target's coordinates.
		int xTarget = getCurrentLoc().coords.x + xDelta;
		int yTarget = getCurrentLoc().coords.y + yDelta;	
		Point target = new Point(xTarget, yTarget);

		// Check that the target is within bounds, traversable, and not closed.
		if (outOfBounds(target))
			return;
			
		Location targetLoc = maze.getCoords()[xTarget][yTarget];

		if (!targetLoc.isTraversable() || targetLoc.isClosed())
			return;
		
		// If the target hasn't been analyzed yet, add it to the open list.
		if (targetLoc.getListStatus().equals("none"))
			targetLoc.addToOpenSet();

		computeAccumulated(targetLoc);
		computeEstimated(targetLoc);
		computeTotalCost(targetLoc);
		
		// Re-sort the open list, as the target's total cost may have changed.
		maze.openList.remove(targetLoc);
		maze.openList.add(targetLoc);
	}
	
	// Returns true if the given coordinates are out of bounds of the maze.
	public boolean outOfBounds(Point coords) {
		if (coords.x < 0 || coords.x > getMaze().xMax ||
				coords.y < 0 || coords.y > getMaze().yMax) 
				return true;
		return false;
	}

	/**
	 * Computes the current *minimum* accumulation of the target location.
	 */
	public void computeAccumulated(Location targetLoc) {
		// Start location has no need for an accumulation score.
		if (targetLoc.isStartLocation()) 
			return;
		
		// Compute the move cost of using current path to reach the target.
		double moveCost = computeMoveCost(this.getCurrentLoc(), targetLoc);
		double pathAccumulation = getCurrentLoc().getAccumulation() + moveCost;
		
		/* If the target does not have an accumulation yet, OR if the above value is 
		   less than the target's current accumulation, set the above value as the
		   new accumulation of the target */ 
		if (targetLoc.getAccumulation() == 0 ||
			pathAccumulation < targetLoc.getAccumulation()) {
			targetLoc.setAccumulation(pathAccumulation);	
			targetLoc.parent = getCurrentLoc();
		}
	}
	
	/** 
	 * Compute the move cost between *adjacent* locations curr and target.
	 */
	public double computeMoveCost(Location curr, Location target) {
		if (!curr.isAdjacentWith(target))
			throw new IllegalArgumentException("Locations aren't adjacent.");

		double flatlandCost = computeFlatlandCost(curr, target);
		double heightCost = computeHeightCost(curr, target);

		double moveCost = flatlandCost + heightCost;
		return moveCost;
	}
	
	/** 
	 * Compute the move cost between locations curr and target, which have equal height.
	 */
	public double computeFlatlandCost(Location curr, Location target) {
		double flatlandCost;
		
		// Horizontally or vertically adjacent.
		if (curr.coords.x != target.coords.x && curr.coords.y == target.coords.y)
			flatlandCost =  100;
		else if (curr.coords.x == target.coords.x && curr.coords.y != target.coords.y)
			flatlandCost =  100;
		
		// Diagonally adjacent.
		else
			flatlandCost = 141;
		
		return flatlandCost;
	}
	
	/** 
	 * Compute the move cost between locations curr and target, which have unequal height.
	 */
	public int computeHeightCost(Location curr, Location target) {
		int currHeight = maze.getGrayscaleValue(curr.color);
		int targetHeight = maze.getGrayscaleValue(target.color);
		int diffHeight = targetHeight - currHeight;

		int heightCost;
		if (diffHeight > 0)
			heightCost = diffHeight * getMaze().ascendingFactor;
		else if (diffHeight < 0)
			heightCost = diffHeight * getMaze().descendingFactor;
		else
			heightCost = 0;

		return heightCost;
	}
	
	/**
	 * Computes and sets the target location's estimated distance
	 * to the maze's goal.
	 */
	public void computeEstimated(Location targetLoc) {
		targetLoc.estimated = getMaze().getHeuristic().compute(targetLoc, getMaze().getGoal());
	}
	
	public void computeTotalCost(Location targetLoc) {
		targetLoc.totalCost = targetLoc.getAccumulation() + targetLoc.estimated;
	}
	
	public void moveTo(Location loc) {
		setCurrentLoc(loc);
		getCurrentLoc().addToClosedSet();
	}
	
	// Function for testing purposes.
	public void moveTo(String dir) {
		if (dir == "North") changeCoords(0, -1);
		else if (dir == "East") changeCoords(1, 0);
		else if (dir == "South") changeCoords(0, 1);
		else if (dir == "West") changeCoords(-1, 0);
		else if (dir == "NE") changeCoords(1, -1);
		else if (dir == "SE") changeCoords(1, 1);
		else if (dir == "SW") changeCoords(-1, 1);
		else if (dir == "NW") changeCoords(-1, -1);
		else throw new IllegalArgumentException();
		
		getCurrentLoc().setListStatus("closed");
	}

	// Function for testing purposes.
	public void changeCoords(int xDelta, int yDelta) {
		int xCurr = getCurrentLoc().coords.x;
		int yCurr = getCurrentLoc().coords.y;
		setCurrentLoc(getMaze().getCoords()[xCurr + xDelta][yCurr + yDelta]);
	}
	
	/**
	 * Finds the optimal path from start to goal utilizing the A* algorithm.
	 * Draws the path on the image representation of the maze.
	 */
	//@SuppressWarnings("static-access")
	public BufferedImage solveMaze() {
		
		// Single Path On Image 
	/*	BufferedImage original_image = maze.getMazeImage();
		int type = BufferedImage.TYPE_INT_RGB;
		BufferedImage solution_image = maze.changeImageType(original_image, type); */
		
		// Allow Multiple Paths On Image
		BufferedImage solution_image = maze.getMazeImage();

		while (true) {
			// Compute values for all 8 directions.
			analyzeAll();
			
			// If the open list is empty, then no solution exists.
			if (getMaze().openList.size() == 0)
				return null;
				//throw new NoSolutionException("Maze has no solution.");
			
			// Runner moves to the location with the lowest estimated total cost.
			moveTo(maze.openList.peek());

			// Runner reaches goal, solution found.
			if (reachedGoal()) {
				draw_solution_path(getCurrentLoc(), solution_image);
				return solution_image;
			}
		}
	}
	
	/**
	 *  Draw the solution path on the maze image in red.
	 *  Starts from the goal location and recursively works 
	 *  its way back to the start location using the locations' parents.
	 */
	public void draw_solution_path(Location goal, BufferedImage solution_image) {
		Location colorLoc = goal;

		do {
			int xColor = colorLoc.coords.x;
			int yColor = colorLoc.coords.y;
			solution_image.setRGB(xColor, yColor, maze.pathColor.getRGB());
			colorLoc = colorLoc.parent;
		} while (!colorLoc.isStartLocation());
	}

	public Location getCurrentLoc() {
		return currentLoc;
	}

	public void setCurrentLoc(Location currentLoc) {
		this.currentLoc = currentLoc;
	}

	public Maze getMaze() {
		return maze;
	}

	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
}