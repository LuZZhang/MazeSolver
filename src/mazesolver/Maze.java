package mazesolver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Point;

public class Maze {

	private BufferedImage mazeImage;
	private Location[][] coords;
	public int xMax;
	public int yMax;
	private Location start; private Location goal;
	Comparator<Location> c = new TotalCostComparator();
	PriorityQueue<Location> openList = new PriorityQueue<Location>(10, c);
	private Heuristic heuristic;
	int ascendingFactor;
	int descendingFactor;
	Scheme wallScheme;
	Color pathColor;
	int minThreshold;
	int maxThreshold;
	
	/**
	 * Maze constructor.
	 */
	public Maze(File imageFile, Func heuristicType, ScalingFactors factors, Scheme scheme) 
		throws InvalidMazeException {
		initialize_image(imageFile);
		wallScheme = scheme;
		initialize_2DGrid();
		initialize_start_location();
		initialize_goal_location();
		setHeuristic(new Heuristic(heuristicType));
		ascendingFactor = factors.ascendingFactor;
		descendingFactor = factors.descendingFactor;
		pathColor = Color.red;
	}
	
	/**
	 * Maze constructor which sets start and goal locations.
	 */
	public Maze(File imageFile, Point _start, Point _goal, 
				Func heuristicType, ScalingFactors factors, Scheme scheme) {
			initialize_image(imageFile);
			wallScheme = scheme;
			initialize_2DGrid();
			setStart(getLocation(_start.x, _start.y));
			setGoal(getLocation(_goal.x, _goal.y));
			setHeuristic(new Heuristic(heuristicType));
			ascendingFactor = factors.ascendingFactor;
			descendingFactor = factors.descendingFactor; 
			pathColor = Color.red;
	}
	
	/**
	 * Maze constructor with default settings.
	 */
	public Maze(File imageFile) {
			initialize_image(imageFile);
			wallScheme = Scheme.MOUNTAINS;
			initialize_2DGrid();
			setStart(getLocation(0, 0));
			setGoal(getLocation(xMax, yMax));
			setHeuristic(new Heuristic(Func.MANHATTAN));
			ascendingFactor = 150;
			descendingFactor = 60;
			pathColor = Color.red;
	}
	
	/**
	 * Converts bmp file into an RGB BufferedImage.
	 */
	public void initialize_image(File input) {
		if (!input.getPath().endsWith(".bmp"))
			throw new IllegalArgumentException();

		try {
			setMazeImage(ImageIO.read(input));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	
		int type = BufferedImage.TYPE_INT_RGB;
		mazeImage = changeImageType(getMazeImage(), type);
	}
	
	/**
	 * Convert a BufferedImage to a specified type.
	 */
	public static BufferedImage changeImageType(BufferedImage image, int type) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage tempImage = new BufferedImage(width, height, type);
		
        tempImage.createGraphics().drawImage(image, 0, 0, null);
        
        return tempImage;
	}
	
	/** 
	 * Initialize 2D construct that represents pixels of the image.
	 */
	public void initialize_2DGrid() {
		int width = mazeImage.getWidth();
		int height = mazeImage.getHeight();
		setCoords(new Location[width][height]);
		xMax = width - 1; yMax = height - 1;
		
		for (int xCoord = 0; xCoord <= xMax; xCoord++)
			for (int yCoord = 0; yCoord <= yMax; yCoord++) {
				coords[xCoord][yCoord] = new Location(this, xCoord, yCoord);
			}
	}
	
	/** 
	 * Searches each of the maze's walls for a black pixel, 	 
	 * which will be the start location of the maze.
	 */
	public void initialize_start_location() 
		throws InvalidMazeException {
		setStart(search_west_wall());
		if (getStart() == null) setStart(search_south_wall());
		if (getStart() == null) setStart(search_north_wall());
		if (getStart() == null) setStart(search_east_wall());
		
		if (getStart() == null)
			throw new InvalidMazeException("No starting pixel found on wall");
	}
	
	/** 
	 * Searches each of the maze's walls for a black pixel, 	 
	 * which will be the goal location of the maze.
	 */
	public void initialize_goal_location() 
		throws InvalidMazeException {
		setGoal(search_east_wall());
		if (getGoal() == null) setGoal(search_north_wall());
		if (getGoal() == null) setGoal(search_south_wall());
		if (getGoal() == null) setGoal(search_west_wall());
		
		if (getGoal() == null)
			throw new InvalidMazeException("Invalid maze.");
	}
	
	public Location search_north_wall() {
		Color pixel;
		for (int x = xMax; x >= 0; x--) {
			pixel = new Color(getMazeImage().getRGB(x, yMax));
			if (pixel.equals(Color.black))
				return getCoords()[x][yMax];
		}
		return null;
	}
	
	public Location search_east_wall() {
		Color pixel;
		for (int y = yMax; y >= 0; y--) {
			pixel = new Color(getMazeImage().getRGB(xMax, y));
			if (pixel.equals(Color.black))
				return getCoords()[xMax][y];
		}
		return null;
	}
	
	public Location search_south_wall() {
		Color pixel;
		for (int x = 0; x <= xMax; x++) {
			pixel = new Color(getMazeImage().getRGB(x, 0));
			if (pixel.equals(Color.black))
				return getCoords()[x][0];
		}
		return null;
	}
	
	public Location search_west_wall() {
		Color pixel;
		for (int y = 0; y <= yMax; y++) {
			pixel = new Color(getMazeImage().getRGB(0, y));
			if (pixel.equals(Color.black))
				return getCoords()[0][y];
		}
		return null;
	}
	
	public Location getLocation(int x, int y) {
		return getCoords()[x][y];
	}
	
	public void setStart(int xStart, int yStart) {
		if (xStart < 0 || xStart > xMax || yStart < 0 || yStart > yMax)
			throw new IllegalArgumentException("Coordinate out of bounds.");
		
		start = getLocation(xStart, yStart);
	}
	
	public void setGoal(int xGoal, int yGoal) {
		if (xGoal < 0 || xGoal > xMax || yGoal < 0 || yGoal > yMax)
			throw new IllegalArgumentException("Coordinate out of bounds.");

		goal = getLocation(xGoal, yGoal);
	}
	
	public void setStartingX(int xStart) {
		if (xStart < 0 || xStart > xMax)
			throw new IllegalArgumentException("Coordinate out of bounds.");
		setStart(getLocation(xStart, getStart().getY()));
	}
	
	public void setStartingY(int yStart) {
		if (yStart < 0 || yStart > yMax)
			throw new IllegalArgumentException("Coordinate out of bounds.");
		setStart(getLocation(getStart().getX(), yStart));
	}
	
	public void setGoalX(int xGoal) {
		if (xGoal < 0 || xGoal > xMax)
			throw new IllegalArgumentException("Coordinate out of bounds.");
		setGoal(getLocation(xGoal, getGoal().getY()));
	}
	
	public void setGoalY(int yGoal) {
		if (yGoal < 0 || yGoal > yMax)
			throw new IllegalArgumentException("Coordinate out of bounds.");
		setGoal(getLocation(getGoal().getX(), yGoal));
	}
	
	public void setHeuristic(Func type) {
		heuristic.setType(type);
	}
	
	public int getAscendingFactor() {
		return ascendingFactor;
	}
	
	public int getDescendingFactor() {
		return descendingFactor;
	}
	
	public void setFactors(int ascending, int descending) {
		setAscendingFactor(ascending);
		setDescendingFactor(descending);
	}
	
	public void setAscendingFactor(int factor) {
		ascendingFactor = factor;
	}
	
	public void setDescendingFactor(int factor) {
		descendingFactor = factor;
	}
	
	public void setWallScheme(Scheme scheme) {
		if (scheme != Scheme.MOUNTAINS &&
			scheme != Scheme.RIVERS &&
			scheme != Scheme.NOWALLS &&
			scheme != Scheme.ALLWALLS)
			throw new IllegalArgumentException("Invalid wall scheme.");
		
		wallScheme = scheme;
	}
	
	/**
	 * Finds an optimal solution path from the start to the goal of the maze,
	 * and draws it on the image representation of the maze.
	 */
	public BufferedImage solve() {
		openList.clear();
		reset_2DGrid();
		MazeRunner runner = new MazeRunner(this);
		return runner.solveMaze();
	}
	
	public void reset_2DGrid() {
		Location loc;
		for (int xCoord = 0; xCoord <= xMax; xCoord++)
			for (int yCoord = 0; yCoord <= yMax; yCoord++) {
				loc = getLocation(xCoord, yCoord);
				loc.resetValues();
			}
	}

	public Location[][] getCoords() {
		return coords;
	}

	public void setCoords(Location[][] coords) {
		this.coords = coords;
	}

	public Location getGoal() {
		return goal;
	}

	public Location setGoal(Location goal) {
		this.goal = goal;
		return goal;
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public BufferedImage getMazeImage() {
		return mazeImage;
	}

	public void setMazeImage(BufferedImage mazeImage) {
		this.mazeImage = mazeImage;
	}

	public Heuristic getHeuristic() {
		return heuristic;
	}
	
	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}
	
	public Color getPathColor() {
		return pathColor;
	}
	
	public void setPathColor(Color color) {
		pathColor = color;
	}
	
	public int getMinThreshold() {
		return minThreshold;
	}
	
	public int getMaxThreshold() {
		return maxThreshold;
	}
	
	public void setThreshold(int min, int max) {
		setMinThreshold(min);
		setMaxThreshold(max);
	}
	
	public void setMinThreshold(int min) {
		minThreshold = min;
	}
	
	public void setMaxThreshold(int max) {
		maxThreshold = max;
	}
	
	public void applyThreshold() {
		Location pixel;
		int grayscale;
		for (int xCoord = 0; xCoord <= xMax; xCoord++) 
			for (int yCoord = 0; yCoord <= yMax; yCoord++) {
				pixel = coords[xCoord][yCoord];
				grayscale = getGrayscaleValue(pixel.color);
				if (grayscale >= minThreshold && grayscale <= maxThreshold)
					pixel.traversable = true;
				else
					pixel.traversable = false;
			}
	}
	
	public int getGrayscaleValue(Color color) {
		int rgb = color.getRGB();
		int grayscale = 255 + rgb / 0x00010101;
		return grayscale;
	}
	
}
