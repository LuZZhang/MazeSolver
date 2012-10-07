package mazesolverController;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import mazesolver.Func;
import mazesolver.Maze;

public class Controller {
	public Maze maze;
	public BufferedImage solutionImage;

    public void constructMaze(File file) {
    	maze = new Maze(file);
    }
    
    public BufferedImage changeImageType(BufferedImage image, int type) {
    	return Maze.changeImageType(image, type);
    }
    
    public BufferedImage solveMaze() {
    	return maze.solve();
    }
    
    public void setStart(int xStart, int yStart) {
    	maze.setStart(xStart, yStart);
    }
    
    public void setGoal(int xGoal, int yGoal) {
    	maze.setGoal(xGoal, yGoal);
    }
    
    public void setHeuristic(Func heuristic) {
    	maze.setHeuristic(heuristic);
    }
    
    public void setFactors(int ascending, int descending) {
    	maze.setFactors(ascending, descending);
    }
    
    public void setPathColor(Color color) {
    	maze.setPathColor(color);
    }
    
    public void setAndApplyThreshold(int min, int max) {
    	maze.setThreshold(min, max);
    	maze.applyThreshold();
    }
}
