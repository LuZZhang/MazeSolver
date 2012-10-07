package tests;

import static org.junit.Assert.*;
import mazesolver.*;
import mazesolverGUI.MazeSolverGUI;

import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Point;

public class AllTests {
	
	/* For Solution Test */
	File testMaze = new File("images/allblack.bmp");
	int xStart = 0; int yStart = 0;
	int xGoal = 511; int yGoal = 127;
	ScalingFactors factors = new ScalingFactors(150, 60);
	Func heuristic = Func.EUCLIDEAN;
	Scheme wallScheme = Scheme.MOUNTAINS;

	/* Other Test Inputs */
	File[] files = new File("images").listFiles();
	File noWallMaze = new File("images/allblack.bmp");
	File invalidMaze = new File("images/invalid.png");
	
	/* Possible Heuristic options */
	public static int MANHATTAN = 0;
	public static int DIAGONAL = 1;
	public static int EUCLIDEAN = 2;
	public static int EUCLIDEAN_SQUARED = 3;
	public static int MANHATTAN_TIE = 4;
	
	/* Possible wall scheme options */
	public static int NOWALLS = 0;
	public static int MOUNTAINS = 1;
	public static int RIVERS = 2;
	public static int ALLWALLS = 3;
	
	@Test
	public void test_solving()
		throws InvalidMazeException, NoSolutionException, IOException
	{	
		Point start = new Point(0, 0);
		Point goal = new Point (1, 1);
		Maze maze = new Maze(testMaze, start, goal, heuristic, factors, wallScheme);
		maze.setStart(xStart, yStart);
		maze.setGoal(xGoal, yGoal);
		BufferedImage solution_image = maze.solve();

		assertNotNull(solution_image);
		
	    File outputfile = new File("saved.bmp");
	    ImageIO.write(solution_image, "bmp", outputfile);
	}

	@Test
	public void test_image_loading() 
		throws InvalidMazeException
	{
			if (testMaze.getName().endsWith(".bmp")) {
				Maze maze = new Maze(testMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		    	assertNotNull(maze.getMazeImage());
			}
	}
	
	@Test
	public void test_parameter_reading() 
		throws InvalidMazeException 
	{
			if (testMaze.getName().endsWith(".bmp")) {
				Maze maze = new Maze(testMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
				MazeRunner runner = new MazeRunner(maze);
				assertNotNull(maze.getMazeImage());
				assertNotNull(maze.getCoords());
				assertNotNull(runner.getMaze());
				assertNotNull(runner.getCurrentLoc());
			}
	}
	
	@Test
	public void test_traversable_functionality() 
		throws InvalidMazeException 
	{
			if (testMaze.getName().endsWith(".bmp")) {
				Maze maze = new Maze(testMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
				BufferedImage image;
				try { image = ImageIO.read(testMaze); } 
					catch (IOException e) { image = null; e.printStackTrace(); }
		
				int type = BufferedImage.TYPE_INT_RGB;
				image = Maze.changeImageType(image, type);
				
				int width = image.getWidth();
				int height = image.getHeight();

				Color color;
				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++) {
						color = new Color(image.getRGB(x, y));
						assertTrue(!(color.equals(Color.white)) == 
								   maze.getLocation(x, y).isTraversable());
				}
			}
	}
	
	@Test
	public void test_accumulation_and_cost() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		
		int xCenter = maze.getMazeImage().getWidth() / 2;
		int yCenter = maze.getMazeImage().getHeight() / 2;
		maze.setStart(maze.getLocation(xCenter, yCenter));
		maze.setGoal(maze.getLocation(0, 0));
		
		final MazeRunner runner = new MazeRunner(maze);
		analyze_and_move(runner, "North", 100);
		analyze_and_move(runner, "East", 200);
		analyze_and_move(runner, "NE", 341);
		analyze_and_move(runner, "South", 441);
		analyze_and_move(runner, "West", 200);
		analyze_and_move(runner, "NW", 341);
		analyze_and_move(runner, "SW", 482);
		analyze_and_move(runner, "SE", 0);
	}
	
	public void analyze_and_move(MazeRunner runner, String dir, int expected) {
		runner.analyzeDirection(dir);
		runner.moveTo(dir);
		assertTrue(runner.getCurrentLoc().getAccumulation() == expected);
	}
	
	@Test
	public void test_open_closed() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		Location loc = maze.getLocation(0, 0);
		
		loc.addToOpenSet();
		assertTrue(loc.getListStatus() == "open");
		assertEquals(loc.isOpen(), true);
		
		loc.addToClosedSet();
		assertTrue(loc.getListStatus() == "closed");
		assertEquals(loc.isOpen(), false);
	}
	
	@Test
	public void test_location_identity() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		Location loc = maze.getLocation(0, 0);
		Location loc2 = maze.getLocation(0, 0);
		Location loc3 = maze.getLocation(0, 1);
		
		assertEquals(loc.isLoc(loc2), true);
		assertEquals(loc.isLoc(loc3), false);
	}
	
	@Test
	public void test_heuristics() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		int xTest = 256;
		int yTest = 64;

		Location loc = maze.getLocation(0, 0);
		Location goal = maze.getLocation(xTest, yTest);
		
		double actual; double expected;
		Heuristic heuristic = maze.getHeuristic();
		
		heuristic.setType(Func.MANHATTAN);
		actual = heuristic.compute(loc, goal);
		expected = 32000.0;
		assertTrue(actual == expected);
		
		heuristic.setType(Func.DIAGONAL);
		actual = heuristic.compute(loc, goal);
		expected = 28224.0;
		assertTrue(actual == expected);
		
		heuristic.setType(Func.EUCLIDEAN);
		actual = heuristic.compute(loc, goal);
		expected = (100.0 * Math.sqrt(Math.pow(256, 2) + Math.pow(64, 2)));
		assertTrue(actual == expected);
		
		heuristic.setType(Func.EUCLIDEAN_SQUARED);
		actual = heuristic.compute(loc, goal);
		expected = Math.pow(expected, 2);
		assertTrue(actual == expected);
		
		heuristic.setType(Func.MANHATTAN_TIE);
		maze.setStart(5, 5);
		actual = heuristic.compute(loc, goal);
		expected = 32000 + 0.001 * Math.abs(256 * 59 - 64 * 251);
		assertTrue(actual == expected);
	}
	
	@Test
	public void test_isStart_isGoal() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		int xCenter = maze.getMazeImage().getWidth() / 2;
		int yCenter = maze.getMazeImage().getHeight() / 2;
		
		maze.setStart(maze.getLocation(0, 0));
		maze.setGoal(maze.getLocation(xCenter, yCenter));
		
		assertTrue(maze.getStart().isStartLocation());
		assertTrue(!maze.getStart().isGoalLocation());
		assertTrue(maze.getGoal().isGoalLocation());
		assertTrue(!maze.getGoal().isStartLocation());
	}
	
	@Test
	public void test_reached_goal() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		maze.setStart(maze.setGoal(maze.getLocation(0, 0)));
		MazeRunner runner = new MazeRunner(maze);
		
		runner.setCurrentLoc(maze.getLocation(1, 1));
		assertTrue(!runner.reachedGoal());
		
		runner.setCurrentLoc(maze.getLocation(0, 0));
		assertTrue(runner.reachedGoal());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_invalidArg_MazeConstructor() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(invalidMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		maze.setStart(maze.getLocation(0, 0));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_invalidArg_LocationConstructor() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		int xOOB = maze.xMax + 1;
		int yOOB = -1;
		
		Location oobLoc = new Location(maze, xOOB, yOOB);
		oobLoc.setAccumulation(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_invalidArg_analyzeDirection() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		MazeRunner runner = new MazeRunner(maze);
		runner.analyzeDirection("Invalid String");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_invalidArg_moveTo() 
		throws InvalidMazeException 
	{
		Maze maze = new Maze(noWallMaze, Func.MANHATTAN, factors, Scheme.MOUNTAINS);
		MazeRunner runner = new MazeRunner(maze);
		runner.moveTo("Invalid String");
	}
	
	/* GUI Controller Tests */
	
	@Test
	public void test_GUI_constructMaze() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		assertNotNull(gui.controller.maze);
	}
	
	@Test
	public void test_GUI_changeImageType() {
		MazeSolverGUI gui = new MazeSolverGUI();
		BufferedImage testImg = gui.convertPathToImage("images/allblack.bmp");
		testImg = gui.controller.changeImageType(testImg, BufferedImage.TYPE_INT_RGB);
		assertTrue(testImg.getType() == BufferedImage.TYPE_INT_RGB);
	}
	
	@Test
	public void test_GUI_solveMaze() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		BufferedImage testSolution = gui.controller.solveMaze();
		assertNotNull(testSolution);
	}
	
	@Test
	public void test_GUI_setStart() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		gui.controller.setStart(7, 5);
		assertTrue(gui.controller.maze.getStart().getX() == 7);
		assertTrue(gui.controller.maze.getStart().getY() == 5);
	}
	
	@Test
	public void test_GUI_setGoal() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		gui.controller.setGoal(19, 90);
		assertTrue(gui.controller.maze.getGoal().getX() == 19);
		assertTrue(gui.controller.maze.getGoal().getY() == 90);
	}
	
	@Test
	public void test_GUI_setHeuristic() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		gui.controller.setHeuristic(Func.EUCLIDEAN);
		assertTrue(gui.controller.maze.getHeuristic().type == Func.EUCLIDEAN);
	}
	
	@Test
	public void test_GUI_setFactors() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		gui.controller.setFactors(201, 2);
		assertTrue(gui.controller.maze.getAscendingFactor() == 201);
		assertTrue(gui.controller.maze.getDescendingFactor() == 2);
	}

	@Test
	public void test_GUI_setPathColor() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		gui.controller.setPathColor(Color.orange);
		assertEquals(Color.orange, gui.controller.maze.getPathColor());
	}
	
	@Test
	public void test_GUI_setThreshold() {
		MazeSolverGUI gui = new MazeSolverGUI();
		gui.controller.constructMaze(testMaze);
		gui.controller.setAndApplyThreshold(34, 100);
		assertTrue(gui.controller.maze.getMinThreshold() == 34);
		assertTrue(gui.controller.maze.getMaxThreshold() == 100);
	}
	
}