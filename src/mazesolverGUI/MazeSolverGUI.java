package mazesolverGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
 
import javax.imageio.ImageIO;
import javax.swing.*;

import mazesolver.*;
import mazesolverController.*;
 
@SuppressWarnings("serial")
public class MazeSolverGUI extends JFrame {
	
	/* Control Variable */
	public Controller controller = new Controller();
		
	/*View Variables */
	BufferedImage myPicture;
	Vector<BufferedImage> images = new Vector<BufferedImage>();
	
	JPanel buttonPane = new JPanel();
    JPanel imagePane = new JPanel();
    JPanel inputPane = new JPanel();
    Color bgcolor = Color.ORANGE;
    
    JButton loadButton;
    JButton solvableButton;
	JButton solveButton;
	JButton optionsButton;
	JButton startButton;
	JButton goalButton;
	JButton heuristicButton;
	JButton factorsButton;
	JButton colorButton;
	JButton thresholdButton;
	JButton mergeButton;
	boolean optionsVisible = false;
	
	JTextField pathnameField;
	JTextField xStartField;
	int xStart; int yStart;
	JTextField yStartField;
	JTextField xGoalField;
	int xGoal; int yGoal;
	JTextField yGoalField;
	JPanel radioPanel;
	JTextField ascendingField;
	int ascending; int descending;
	JTextField descendingField;
	JPanel colorPanel;
	JTextField minThresholdField;
	int minThreshold; int maxThreshold;
	JTextField maxThresholdField;
	JTextField mergeField;
	JInternalFrame pathnameFrame;
	JInternalFrame xStartFrame;
	JInternalFrame yStartFrame;
	JInternalFrame xGoalFrame;
	JInternalFrame yGoalFrame;
	JInternalFrame heuristicFrame;
	JInternalFrame ascendingFrame;
	JInternalFrame descendingFrame;
	JInternalFrame colorFrame;
	JInternalFrame minThresholdFrame;
	JInternalFrame maxThresholdFrame;
	JInternalFrame mergeFrame;
	
    public static void main(String[] args) {   
        new MazeSolverGUI();
    }
    
    /**
     * Sets up the GUI display.
     */
    public MazeSolverGUI() {
        setupBackground();        
        
        createButtons();
        addButtons();

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Adds an image panel, input panel, and button panel.
     */
	private void setupBackground() {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch(Exception e) {
        }

        setSize(1000, 1000);
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
        
        getContentPane().add(imagePane, BorderLayout.WEST);
        getContentPane().add(inputPane, BorderLayout.CENTER);
        getContentPane().add(buttonPane, BorderLayout.EAST);

        getContentPane().setBackground(bgcolor);
        imagePane.setBackground(bgcolor);    
        inputPane.setBackground(bgcolor);
        buttonPane.setBackground(bgcolor);
        
        displayPicture(convertPathToImage("images/MazeSolver.bmp"));
	}

	/**
	 * Adds buttons and their respective action listeners and commands.
	 */
	private void createButtons() {
		loadButton = new JButton("       Load Maze       ");
        loadButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		loadMaze();
        	}
        });
        
        mergeButton = new JButton("      Merge Maze      ");
        mergeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		loadAnotherMaze();
        	}
        });
        mergeButton.setEnabled(false);
        
        solvableButton = new JButton("        Solvable?        ");
        solvableButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		determineSolvability();
        	}
        });
        solvableButton.setEnabled(false);
        
        solveButton = new JButton("       Solve Maze      ");
        solveButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
    				solve();
    			} catch (IOException e1) {
    				e1.printStackTrace();
    			}        	
        	}
        });
        solveButton.setEnabled(false);
        
        optionsButton = new JButton("     Maze Options    ");
        optionsButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		toggleOptions();	
        	}
        });
        optionsButton.setEnabled(false);
        
        startButton = new JButton("         Set Start        ");
        startButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setStartX();	
        	}
        });
        startButton.setVisible(false);
        startButton.setEnabled(false);

        goalButton = new JButton("         Set Goal        ");
        goalButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setGoalX();	
        	}
        });
        goalButton.setVisible(false);
        goalButton.setEnabled(false);

        heuristicButton = new JButton("     Set Heuristic     ");
        heuristicButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setHeuristic();	
        	}
        });
        heuristicButton.setVisible(false);
        heuristicButton.setEnabled(false);

        factorsButton = new JButton("       Set Factors      ");
        factorsButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setAscending();	
        	}
        });
        factorsButton.setVisible(false);
        factorsButton.setEnabled(false);

        colorButton = new JButton("    Set Path Color    ");
        colorButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setColor();	
        	}
        });
        colorButton.setVisible(false);
        colorButton.setEnabled(false);

        thresholdButton = new JButton("    Set Threshold     ");
        thresholdButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setMinThreshold();	
        	}
        });
        thresholdButton.setVisible(false);
        thresholdButton.setEnabled(false);
	}
	
	/**
	 * Adds button icons to button panel.
	 */
	private void addButtons() {
		buttonPane.add(loadButton);
        buttonPane.add(mergeButton);
        buttonPane.add(solvableButton);
        buttonPane.add(solveButton);
        buttonPane.add(optionsButton);
        buttonPane.add(startButton);
        buttonPane.add(goalButton);
        buttonPane.add(heuristicButton);
        buttonPane.add(factorsButton);
        buttonPane.add(colorButton);
        buttonPane.add(thresholdButton);
	}
    
	/** 
	 * Reads a pathname input by the user, and loads its
	 * corresponding image onto the image panel.
	 */
    public void loadMaze() {
        pathnameFrame = new JInternalFrame("Enter Pathname", true, true);

    	pathnameField = new JTextField();
        pathnameField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		pathnameFrame.setVisible(false);
        		String pathname = pathnameField.getText();
        		int successfulLoad = loadImage(pathname);
        		if (successfulLoad == 0)
        			printSuccessMsg();     	
        	}
        });
        
        show(pathnameFrame, pathnameField, 300, 75);
    } 
    
    /**
     * Converts the given pathname to its corresponding image,
     * clears any previous images loaded already, and displays the image.
     */
    public int loadImage(String pathname) {
    	BufferedImage image = convertPathToImage(pathname);
    	if (image == null)
    		return -1;
    	
    	images.clear();
    	images.add(image);
		displayPicture(image);
		
		File imageFile = new File(pathname);
		controller.constructMaze(imageFile);
		
		return 0;
    }
    
    /**
     * Uses ImageIO to create a new image from the given pathname.
     */
    public BufferedImage convertPathToImage(String pathname) {
    	BufferedImage newImage = null;
    	        
		try {
			newImage = ImageIO.read(new File(pathname));
		} catch (IOException e) {
			printMessage("Invalid pathname.", "CRITICAL ERROR");
	    	return null;
		}
		
    	return newImage;
    }
    
    /**
     * Displays the given image on the image panel.
     */
    public void displayPicture(BufferedImage image) {
    	imagePane.removeAll();
		JLabel picLabel = new JLabel(new ImageIcon(image));
        imagePane.add(picLabel);
        imagePane.validate();
    }
    
    public void printSuccessMsg() {
    	JOptionPane.showMessageDialog(null,
                "Maze successfully loaded with default values: MOUNTAINS scheme, MANHATTAN heuristic, " +
                "start: NW corner, goal: SE corner, 150/60 ascending/descending factors.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    	
    	solveButton.setEnabled(false);
    	solvableButton.setEnabled(true);
    	optionsButton.setEnabled(true);
    	startButton.setEnabled(true);
    	goalButton.setEnabled(true);
    	heuristicButton.setEnabled(true);
        factorsButton.setEnabled(true);
    	colorButton.setEnabled(true);
    	thresholdButton.setEnabled(true);
    	mergeButton.setEnabled(true);
    }
    
    public void printMessage(String msg, String title) {
    	JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void printSetMsg(String set) {
    	JOptionPane.showMessageDialog(null,
                "Successfully set " + set + ".",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Displays on the input panel an internal frame which contains a component field.
     */
    public void show(JInternalFrame frame, Component field, int width, int height) {
    	frame.add(field);
    	frame.setPreferredSize(new Dimension(width, height));
        inputPane.add(frame, BorderLayout.SOUTH);        
        frame.setVisible(true);
    }
    
    /** 
     * Loads another maze to merge with the current collection.
     */
    public void loadAnotherMaze() {
        mergeFrame = new JInternalFrame("Enter Pathname", true, true);

    	mergeField = new JTextField();
        mergeField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mergeFrame.setVisible(false);
        		String pathname = mergeField.getText();
        		addImageToMerge(pathname);
        	}
        });

        show(mergeFrame, mergeField, 300, 75);
    } 
    
    /**
     * Converts a pathname input by the user to its corresponding image,
     * merges the image with current image collection, and displays the
     * resulting merged image.
     */
    public void addImageToMerge(String pathname) {
		BufferedImage newImage = convertPathToImage(pathname);
		if (newImage == null)
			return;
		images.add(newImage);
		
		BufferedImage merged_image = mergeImages();
	    displayPicture(merged_image);
	    
	    File outputfile = new File("merged.bmp");
	    try {
			ImageIO.write(merged_image, "bmp", outputfile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}   
	    controller.constructMaze(outputfile);
	    
	    printMessage("Successfully merged " + images.size() + " images.", "Success");
    }
    
    /** 
     * Merges images in collection. Uses the largest image, by area,
     * as its base, then merges the smaller images with the base image
     * by combining pixel colors at each of the corresponding coordinates.
     */
    public BufferedImage mergeImages() {
    	int baseIndex = getLargestImageIndex();
    	BufferedImage baseImage = images.get(baseIndex);
    	controller.changeImageType(baseImage, BufferedImage.TYPE_INT_RGB);
    	
    	int width = baseImage.getWidth();
    	int height = baseImage.getHeight();
    	BufferedImage currImage;
    	for (int x = 0; x < width; x++)
    		for (int y = 0; y < height; y++) {
    			int rgbSum = baseImage.getRGB(x, y);
    			int colorsMerged = 1;
    			for (int index = 0; index < images.size(); index++) {
    				if (index != baseIndex) {
    					currImage = images.get(index);
    					if (x < currImage.getWidth() && y < currImage.getHeight()) {
    						rgbSum += images.get(index).getRGB(x, y);
    						colorsMerged++;
    					}
    				}
    			}
    			int mergedRGB = rgbSum / colorsMerged;
    			baseImage.setRGB(x, y, mergedRGB);
    		}
    	return baseImage;
    }
    
    /** 
     * Searches image collection for largest image by area
     * and returns its index.
     */
    public int getLargestImageIndex() {
    	BufferedImage largestImage = images.get(0);
    	int maxArea = getArea(largestImage);
    	int returnIndex = 0;

    	BufferedImage currentImage; int imageArea; 
    	for (int index = 1; index < images.size(); index++) {
    		currentImage = images.get(index);
    		imageArea = getArea(currentImage);
    		if (imageArea > maxArea) {
    			largestImage = currentImage;
    			maxArea = imageArea;
    			returnIndex = index;
    		}
    	}
    	return returnIndex;  	
    }
    	
    /** 
     * Computes and returns the area of the given image.
     */
    public int getArea(BufferedImage image) {
    	return image.getHeight() * image.getWidth();
    }
    
    /**
     * Determines the solvability of the maze image
     * by running the algorithm on the representative maze
     * and outputting its result.
     */
    public void determineSolvability() {
    	controller.solutionImage = controller.solveMaze();
    	String solvable;
    	
    	if (controller.solutionImage == null) {
    		solvable = "No!";
    		solveButton.setEnabled(false);
    	}
    	else {
    		solvable = "Yes!";
    		solvableButton.setEnabled(false);
    		solveButton.setEnabled(true);
    	}
    	
    	printMessage(solvable, "Is it solvable?");
    }
    
    /**
     * Displays the solution image on the image panel.
     */
    public void solve() throws IOException {
	    File outputfile = new File("saved.bmp");
	    ImageIO.write(controller.solutionImage, "bmp", outputfile);
	    
	    displayPicture(convertPathToImage("saved.bmp"));
    	solvableButton.setEnabled(true);
    }
    
    /** 
     * Shows or hides the extraneous options of the maze solver.
     */
    public void toggleOptions() {
		boolean show;
		
		if (!optionsVisible) {
			show = true;
			optionsVisible = true;
			optionsButton.setText("     Hide Options    ");
		}
		else {
			show = false;
			optionsVisible = false;
			optionsButton.setText("     Maze Options    ");
		}
		
		startButton.setVisible(show);
		goalButton.setVisible(show);
		heuristicButton.setVisible(show);
		factorsButton.setVisible(show);
		colorButton.setVisible(show);
		thresholdButton.setVisible(show);
    }
    
    /**
     * Reads in the user's input for the starting x-coordinate.
     */
    public void setStartX() {
        xStartFrame = new JInternalFrame("Enter Starting X", true, true);

    	xStartField = new JTextField();
        xStartField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		xStartFrame.setVisible(false);
        		String xStartIn = xStartField.getText();
        		try {
        			xStart = Integer.parseInt(xStartIn);
        		}
        		catch(Exception ex) {
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		setStartY();     	
        	}
        });
        
        show(xStartFrame, xStartField, 300, 75);
    }
    
    /**
     * Reads in the user's input for the starting y-coordinate and
     * sets the start location of the maze.
     */
    public void setStartY() {
        yStartFrame = new JInternalFrame("Enter Starting Y", true, true);

    	yStartField = new JTextField();
        yStartField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		yStartFrame.setVisible(false);
        		String yStartIn = yStartField.getText();
        		try {
        			yStart = Integer.parseInt(yStartIn);
        		}
        		catch(Exception ex) {
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}        		
        		try {
        			controller.setStart(xStart, yStart);
        		}
        		catch(Exception ex) {
        			printMessage("Index out of bounds.", "CRITICAL ERROR");
        			return;
        		}
        		printSetMsg("starting coordinates");    	
        	}
        });
                
        show(yStartFrame, yStartField, 300, 75);
    }
    
    /**
     * Reads in the user's input for the goal x-coordinate.
     */
    public void setGoalX() {
        xGoalFrame = new JInternalFrame("Enter Goal X", true, true);

    	xGoalField = new JTextField();
        xGoalField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		xGoalFrame.setVisible(false);
        		String xGoalIn = xGoalField.getText();
        		try {
        			xStart = Integer.parseInt(xGoalIn);
        		}
        		catch(Exception ex) {
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}
        		xGoal = Integer.parseInt(xGoalIn);
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		setGoalY();   
        	}
        });
        
        show(xGoalFrame, xGoalField, 300, 75);
    }
    
    /**
     * Reads in the user's input for the goal y-coordinate and
     * sets the goal location of the maze.
     */
    public void setGoalY() {
        yGoalFrame = new JInternalFrame("Enter Goal Y", true, true);

    	yGoalField = new JTextField();
        yGoalField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		yGoalFrame.setVisible(false);
        		String yGoalIn = yGoalField.getText();
        		try {
        			yGoal = Integer.parseInt(yGoalIn);
        		}
        		catch(Exception ex) {
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}        		
        		try {
        			controller.setGoal(xGoal, yGoal);
        		}
        		catch(Exception ex) {
        			printMessage("Index out of bounds.", "CRITICAL ERROR");
        			return;
        		}
        		printSetMsg("goal coordinates");    	
        	}
        });
        
        show(yGoalFrame, yGoalField, 300, 75);
    }
    
    /** 
     * Creates and displays a radio panel, from which the user can select
     * the heuristic used by the maze-solving algorithm.
     */
    public void setHeuristic() {
        heuristicFrame = new JInternalFrame("Select Heuristic", true, true);
        
        radioPanel = new JPanel(new GridLayout(0, 1));
        
        JRadioButton manhattanButton = new JRadioButton("Manhattan");
        manhattanButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                heuristicFrame.setVisible(false);
                controller.setHeuristic(Func.MANHATTAN);
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		printSetMsg("heuristic");  	
        	}
        });
        
        JRadioButton diagonalButton = new JRadioButton("Diagonal");
        diagonalButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                heuristicFrame.setVisible(false);
                controller.setHeuristic(Func.DIAGONAL);
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		printSetMsg("heuristic");  	
        	}
        });
        
        JRadioButton euclideanButton = new JRadioButton("Euclidean");
        euclideanButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                heuristicFrame.setVisible(false);
                controller.setHeuristic(Func.EUCLIDEAN);
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		printSetMsg("heuristic");  	
        	}
        });
        
        JRadioButton euclideanSquaredButton = new JRadioButton("Euclidean Squared");
        euclideanSquaredButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                heuristicFrame.setVisible(false);
                controller.setHeuristic(Func.EUCLIDEAN_SQUARED);
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		printSetMsg("heuristic");  	
        	}
        });
        
        JRadioButton manhattanTieButton = new JRadioButton("Manhattan Tie");
        manhattanTieButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                heuristicFrame.setVisible(false);
                controller.setHeuristic(Func.MANHATTAN_TIE);
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		printSetMsg("heuristic");  	
        	}
        });
        
        radioPanel.add(manhattanButton);
        radioPanel.add(diagonalButton);
        radioPanel.add(euclideanButton);
        radioPanel.add(euclideanSquaredButton);
        radioPanel.add(manhattanTieButton);

        show(heuristicFrame, radioPanel, 250, 300);
    }
    
    /**
     * Reads in the user's input for the ascending factor of the maze.
     */
    public void setAscending() {
        ascendingFrame = new JInternalFrame("Enter Ascending Factor", true, true);

    	ascendingField = new JTextField();
        ascendingField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ascendingFrame.setVisible(false);
        		String ascendingIn = ascendingField.getText();
        		try {
        			ascending = Integer.parseInt(ascendingIn);
        		}
        		catch (Exception ex){
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		setDescending(); 	
        	}
        });
                
        show(ascendingFrame, ascendingField, 300, 75);
    }
    
    /**
     * Reads in the user's input for the descending factor of the maze
     * and sets the factors of the maze.
     * 
     */
    public void setDescending() {
        descendingFrame = new JInternalFrame("Enter Descending Factor", true, true);

    	descendingField = new JTextField();
        descendingField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		descendingFrame.setVisible(false);
        		String descendingIn = descendingField.getText();
        		try {
        			descending = Integer.parseInt(descendingIn);
        		}
        		catch (Exception ex) {
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}
        		controller.setFactors(ascending, descending);
        		printSetMsg("scaling factors");	
        	}
        });
                
        show(descendingFrame, descendingField, 300, 75);
    }
    
    /**
     * Creates and displays a radio panel from which the user can choose
     * one of six colors for the path to be overlaid on the solution image.
     */
    public void setColor() {
        colorFrame = new JInternalFrame("Select Path Color", true, true);
        
        colorPanel = new JPanel(new GridLayout(0, 1));
        
        JRadioButton redButton = new JRadioButton("Red");
        redButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
           		colorFrame.setVisible(false);
           		controller.setPathColor(Color.red);
        		printSetMsg("path color");   	
        	}
        });
        
        JRadioButton orangeButton = new JRadioButton("Orange");
        orangeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
           		colorFrame.setVisible(false);
           		controller.setPathColor(Color.orange);
        		printSetMsg("path color");   	
        	}
        });
        
        JRadioButton yellowButton = new JRadioButton("Yellow");
        yellowButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
           		colorFrame.setVisible(false);
           		controller.setPathColor(Color.yellow);
        		printSetMsg("path color");   	
        	}
        });
        
        JRadioButton greenButton = new JRadioButton("Green");
        greenButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
           		colorFrame.setVisible(false);
           		controller.setPathColor(Color.green);
        		printSetMsg("path color");   	
        	}
        });
        
        JRadioButton blueButton = new JRadioButton("Blue");
        blueButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
           		colorFrame.setVisible(false);
           		controller.setPathColor(Color.blue);
        		printSetMsg("path color");   	
        	}
        });
        
        JRadioButton purpleButton = new JRadioButton("Purple");
        purpleButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
           		colorFrame.setVisible(false);
           		controller.setPathColor(Color.magenta);
        		printSetMsg("path color");   	
        	}
        });
        
        colorPanel.add(redButton);
        colorPanel.add(orangeButton);
        colorPanel.add(yellowButton);
        colorPanel.add(greenButton);
        colorPanel.add(blueButton);
        colorPanel.add(purpleButton);
        
        show(colorFrame, colorPanel, 250, 300);
    }
    
    /**
     * Reads in the input of the user for the minimum
     * grayscale threshold value required for image traversability.
     */
    public void setMinThreshold() {
        minThresholdFrame = new JInternalFrame("Enter Minimum Threshold", true, true);

    	minThresholdField = new JTextField();
        minThresholdField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		minThresholdFrame.setVisible(false);
        		String minThresholdIn = minThresholdField.getText();
        		try {
        			minThreshold = Integer.parseInt(minThresholdIn);
        		}
        		catch (Exception ex) {
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}
        		solvableButton.setEnabled(true);
        		solveButton.setEnabled(false);
        		setMaxThreshold();  	
        	}
        });        
        
        show(minThresholdFrame, minThresholdField, 300, 75);
    }
    
    /**
     * Reads in the input of the user for the maximum
     * grayscale threshold value required for image traversability,
     * then sets the maze's grayscale threshold range.
     */
    public void setMaxThreshold() {
        maxThresholdFrame = new JInternalFrame("Enter Maximum Threshold", true, true);

    	maxThresholdField = new JTextField();
        maxThresholdField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		maxThresholdFrame.setVisible(false);
        		String maxThresholdIn = maxThresholdField.getText();
        		try {
        			maxThreshold = Integer.parseInt(maxThresholdIn);
        		}
        		catch (Exception ex) {
        			printMessage("Invalid input.", "CRITICAL ERROR");
        			return;
        		}
        		controller.setAndApplyThreshold(minThreshold, maxThreshold);
        		printSetMsg("threshold values"); 	
        	}
        });         
       
        show(maxThresholdFrame, maxThresholdField, 300, 75);
    }
   
}