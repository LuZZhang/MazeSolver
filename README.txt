	My GUI program utilizes the Model-View-Controller (MVC) architecture. The Model component is represented by the Maze class and its 
		associated classes (MazeRunner, Location, Heuristic, and ScalingFactors). The View component is constructed with the Java Swing 
		toolkit, which involves utilizing JFrames, JInternalFrames, JButtons, JTextFields, JPanels, etc. Finally, the Controller is 
		employed by writing a Controller class which encapsulates every function the View needs to use from the Model. That is, in the
		Controller class, a Maze instance (the Model) is defined. When a user interacts with the various features of the GUI (the View),
		methods of the Controller are called, which manipulates the Maze instance in accordance with what the user selects on the interface. 
		These internal actions by the Controller, of course, are unseen by the user, who can only see the GUI.

	The User Interface pattern I chose to implement is the 'Extras On Demand' pattern. This pattern entails initially hiding extraneous 
		features to the user. That is, when the program is started up, only the components that implement the main functionality of the 
		program is shown. The user can access those extra features by simply clicking on a button, which proceeds to make the 
		previously-hidden components visible. Another click can hide those components if the user wishes to.

Manual Test Plan For View
1. Load the program.
2. Click 'Load Maze' and enter a valid pathname (such as 'images/Tahoe.bmp').
4. Click 'Merge Maze' and enter a valid pathname (such as 'images/mountains.bmp')
6. Click 'Maze Options'
7. Click 'Set Start' and enter coordinates (20, 20)
8. Click 'Set Goal' and enter coordinates (300, 450)
9. Click 'Set Heuristic' and click 'Euclidean'
10. Click 'Set Factors' and enter factors (120, 90)
11. Click 'Set Path Color' and click 'Orange'
12. Click 'Set Threshold and enter threshold range 1-254
13. Click 'Solvable?'
14. Click 'Solve Maze'
15. Repeat steps 4-14 with different parameters until satisfied.