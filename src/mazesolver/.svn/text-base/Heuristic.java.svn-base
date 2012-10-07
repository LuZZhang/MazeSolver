package mazesolver;

public class Heuristic {

	public Func type;
	
	/**
	 * Heuristic constructor.
	 */
	public Heuristic(Func _type) {
		type = _type;
	}
	
	public void setType(Func _type) {
		if (_type != Func.MANHATTAN &&
			_type != Func.DIAGONAL &&
			_type != Func.EUCLIDEAN &&
			_type != Func.EUCLIDEAN_SQUARED &&
			_type != Func.MANHATTAN_TIE)
			throw new IllegalArgumentException("Invalid heuristic type.");
		
		type = _type;
	}
	
	/** 
	 * Computes the estimated distance between curr and goal,
	 * which is determined by the heuristic type chosen by the user.
	 */
	public double compute(Location curr, Location goal) {
		
		if (type == Func.MANHATTAN)
			return manhattan(curr, goal);
		else if (type == Func.DIAGONAL)
			return diagonal(curr, goal);
		else if (type == Func.EUCLIDEAN)
			return euclidean(curr, goal);
		else if (type == Func.EUCLIDEAN_SQUARED)
			return euclidean_squared(curr, goal);
		else if (type == Func.MANHATTAN_TIE) {
			Location start = curr.maze.getStart();
			return manhattan_tied(curr, goal, start);	
		}
		else
			throw new IllegalArgumentException("Invalid heuristic type.");
	}
	
	public double manhattan(Location curr, Location goal) {
		int xDiff = Math.abs(curr.coords.x - goal.coords.x);
		int yDiff = Math.abs(curr.coords.y - goal.coords.y);
		
		int straightSteps =  xDiff + yDiff;
		
		double cost = 100.0 * straightSteps;
		return cost;
	}
	
	public double diagonal(Location curr, Location goal) {
		int xDiff = Math.abs(curr.coords.x - goal.coords.x);
		int yDiff = Math.abs(curr.coords.y - goal.coords.y);
		
		int diagonalSteps = Math.min(xDiff, yDiff);
		int straightSteps = xDiff + yDiff - 2 * diagonalSteps;
		
		double cost = 141.0 * diagonalSteps + 100.0 * straightSteps;
		return cost;
	}
	
	public double euclidean(Location curr, Location goal) {
		int xDiff = Math.abs(curr.coords.x - goal.coords.x);
		int yDiff = Math.abs(curr.coords.y - goal.coords.y);
		
		double cost = (100.0 * Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));
		return cost;
	}
	
	public double euclidean_squared(Location curr, Location goal) {
		double euclideanCost = euclidean(curr, goal);
		
		double cost = Math.pow(euclideanCost, 2);
		return cost;
	}
	
	public double manhattan_tied(Location curr, Location goal, Location start) {
		int dxCurrGoal = Math.abs(curr.coords.x - goal.coords.x);
		int dyCurrGoal = Math.abs(curr.coords.y - goal.coords.y);
		int dxStartGoal = Math.abs(start.coords.x - goal.coords.x);
		int dyStartGoal = Math.abs(start.coords.y - goal.coords.y);

		double cross = Math.abs(dxCurrGoal * dyStartGoal - dyCurrGoal * dxStartGoal);

		double cost = manhattan(curr, goal) + cross * 0.001;
		return cost;
	}
	
}