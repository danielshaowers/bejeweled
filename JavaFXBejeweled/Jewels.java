package JavaFXBejeweled;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Scanner;

import javax.xml.datatype.DatatypeConfigurationException;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;

public class Jewels extends Application {
	/** A clickable button */
	private BetterButton click1;

	private boolean firstClick = true;
	private int[][] colorSet;
	private GridPane board = new GridPane();
	private BetterButton[][] array;
	private int count = 0;
	private JFXPanel  a = new JFXPanel();
	private Text displayText = new Text();
	public boolean getFirstClick() {
		return firstClick;
	}

	public int[][] getColorSet() {
		return colorSet;
	}


	public BetterButton[][] getButtonArray() {
		return array;
	}

	
	

	/**
	 * Overrides the start method of Application to create the GUI with one button
	 * in the center of the main window.
	 * 
	 * @param primaryStage the JavaFX main window
	 */
	public void start(Stage primaryStage) {
		Scene scene = new Scene(board); // Create a "scene" that contains this border
		primaryStage.setTitle("Pls work");

		primaryStage.setScene(scene); // Add the "scene" to the main window
		primaryStage.show(); // Display the window
	}

	/**
	 * 
	 * @param totalColors the number of total colors
	 * @param rows        the number of rows of the board
	 * @param columns     the number of columns of the game
	 */
	public BetterButton[][] makeBoard(int totalColors, int rows, int columns) {
		Random rand = new Random();
		
		array = new BetterButton[rows][columns];
		// loop creates a visual 2d array of buttons and colors them
		// defines the character of all the buttons in the array
		generateRandomColors(totalColors);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				array[i][j] = new BetterButton();
				array[i][j].setShape(new Circle(30));
				array[i][j].setMinSize(30, 30);
				array[i][j].setMaxSize(30, 30);
				int randomColor = (int) (rand.nextDouble() * totalColors); // selects a random index of the array of
																			// possible colors
				array[i][j].setColor(colorSet[randomColor][0], colorSet[randomColor][1], colorSet[randomColor][2]);
				board.add(array[i][j], i, j);
				array[i][j].setOnAction((e) -> { // makes it so all the buttons responds to clicks in the following way
					if (!winCondition(getButtonArray())) {
						if (firstClick) {
							click1 = (BetterButton) e.getSource();
							// darkens the first jewel clicked without updatings its rgb values
							click1.setStyle("-fx-base: rgb(" + (int) (click1.getRed() * 0.7) + ","
									+ (int) (click1.getGreen() * 0.7) + "," + (int) (click1.getBlue() * 0.7) + ");");
							// ((Button)e.getSource()) //get location of the button in the array somehow? i
							// need to darken the button
							firstClick = false;
						} else {
							click1.setColor(click1.getRed(), click1.getGreen(), click1.getBlue()); // undarkens first
																									// click
							BetterButton click2 = (BetterButton) e.getSource();
							swapButtons(click1, click2);
							changeBoard(click2, click1);
							firstClick = true;
							if (winCondition(getButtonArray())) {
								displayText.setText("Well done! You won in " + count + " moves");
								board.autosize();
							}
						}
					}
				});
			}
		}
		board.setHgap(2);
		board.setVgap(2);
		board.setPadding(new Insets(2, 2, 2, 2));
		return array;
	}

	/**
	 * 
	 * @param firstClick  the button that was clicked first
	 * @param secondClick the button clicked second
	 * @return the new array of buttons
	 */
	public BetterButton[][] changeBoard(BetterButton firstClick, BetterButton secondClick) {
		int row1 = getLocation(firstClick)[0];
		int col1 = getLocation(firstClick)[1];
		boolean matchMade = false;

		int leftmost = col1, rightmost = col1;
		int top = row1, bottom = row1;
		// while loop to find the index of leftmost and rightmost jewel that are the
		// same color as the jewel clicked
		for (rightmost = col1; rightmost < array.length - 1
				&& firstClick.equals(array[rightmost + 1][row1]); rightmost++);
		for (leftmost = col1; leftmost >= 1 && firstClick.equals(array[leftmost - 1][row1]); leftmost--);
		for (top = row1; top >= 1 && firstClick.equals(array[col1][top - 1]); top--);
		for (bottom = row1; bottom < array[row1].length - 1 && firstClick.equals(array[col1][bottom + 1]); bottom++);

		if ((rightmost - leftmost) >= 2) {
			matchMade = true;
			// adds a star to the leftmost to rightmost matching color
			for (int r = leftmost; r <= rightmost; r++) {
				array[r][row1].setText("*");
				for (int q = row1; q >= 0; q--) {
					if (q > 0) // makes everything the same as the top row.
						array[r][q].setColor(array[r][q - 1].getRed(), array[r][q - 1].getGreen(),
								array[r][q - 1].getBlue());
					else { //if there are no more jewels above, generate a random color
						int[] colors = randomColor(colorSet);
						array[r][q].setColor(colors[0], colors[1], colors[2]);
					}
				}
			}
		}
		if ((bottom - top) >= 2) {
			matchMade = true;
			//places a star on the removed jewels. shifts the unremoved jewels down and generates new jewels
			for (int q = bottom; q >= 0; q--) {
				if (q >= top)
					array[col1][q].setText("*");
				int newIndex = top - bottom + q - 1; // the index of the color that will fall onto the current spot
				if (newIndex >= 0) {
					array[col1][q].setColor(array[col1][newIndex].getRed(), array[col1][newIndex].getGreen(),
							array[col1][newIndex].getBlue());
				} else {
					int[] colors = randomColor(colorSet);
					array[col1][q].setColor(colors[0], colors[1], colors[2]);
				}
			}
		}
		if (matchMade)
			displayText.setText("Moves made: " + ++count);
		else
			swapButtons(firstClick, secondClick);
		return array;
	}

	public int[][] generateRandomColors(int totalColors) {
		colorSet = new int[totalColors][3];
		//places random color values into the array storing colors
		for (int x = 0; x < totalColors; x++) {
			for (int z = 0; z < 3; z++) // stores the random rgb values in an array
				colorSet[x][z] = (int) ((Math.random() * totalColors) * (int) (255 / totalColors));
		}
		return colorSet;
	}

	public int[] randomColor(int[][] colorArray) {
		int[] color = new int[3];
		int chosenColor = (int) (Math.random() * colorArray.length);
		color[0] = colorArray[chosenColor][0];
		color[1] = colorArray[chosenColor][1];
		color[2] = colorArray[chosenColor][2];
		return color;
	}

	public boolean winCondition(BetterButton[][] list) {
		//checks the entire board for if all the jewels are starred
		for (int i = 0; i < list.length; i++) {
			for (int j = 0; j < list[i].length; j++) {
				if (!list[i][j].getText().equals("*"))
					return false;
			}
		}
		return true;
	}

	public boolean swapButtons(BetterButton b1, BetterButton b2) {
		// checks if the two buttons clicked are within 1 of each other
		int row1 = getLocation(b1)[0]; // gets the location of the first button clicked
		int col1 = getLocation(b1)[1];
		int row2 = getLocation(b2)[0]; // gets the location of the second button clicked
		int col2 = getLocation(b2)[1];
		if ((Math.abs(row1 - row2) == 1 && col1 - col2 == 0) || (Math.abs(col1 - col2) == 1 && row1 - row2 == 0)) {
			BetterButton temp = new BetterButton(b1.getText(), b1.getRed(), b1.getGreen(), b1.getBlue());
			b1.setColor(b2.getRed(), b2.getGreen(), b2.getBlue());
			b1.setText(b2.getText());
			b2.setColor(temp.getRed(), temp.getGreen(), temp.getBlue());
			b2.setText(temp.getText());
			return true;
		}
		return false;
	}

	/**
	 * returns an empty array if element not found
	 * 
	 * @param buttonArray the array of buttons containing the button being searched
	 *                    for
	 * @param element     the button being searched for
	 * @return an array containing the index of the button in the buttonArray
	 */
	public int[] getLocation(BetterButton element)  {
		try {
			int[] location = new int[2];
			location[1] = GridPane.getColumnIndex(element); 
			location[0] = GridPane.getRowIndex(element);
			return location;
		}
		catch (NullPointerException e) {
			System.out.println("No such element exists");
			return null;
		}
		}
		
	@Override
	public void init() {
		try {
			displayText = new Text("Moves Made: " + count);
			if (getParameters().getRaw().isEmpty()) {
				makeBoard(4, 8, 10);
				board.add(displayText, 9, 5);
			} else {
				if (Integer.valueOf(getParameters().getRaw().get(2)) > 1000)
					throw new IllegalStateException();
				if (getParameters().getRaw().size() == 3) {
					makeBoard(Integer.valueOf(getParameters().getRaw().get(2)),
							Integer.valueOf(getParameters().getRaw().get(0)),
							Integer.valueOf(getParameters().getRaw().get(1)));
					board.add(displayText, Integer.valueOf(getParameters().getRaw().get(0)) + 1, Integer.valueOf(getParameters().getRaw().get(1) + 1));
				}
			}
		} catch (IllegalStateException e) {
		
			makeBoard(4, 8, 10);
			board.add(displayText, 9, 11);
		} catch (Exception e) {
			System.out.println("Please input \"Java Jewels\" and *optionally* "
					+ "3 integers representing the desired rows, columns, and number of colors\n...running default game");
			makeBoard(4, 8, 10);
			board.add(displayText, 9, 11);
		}

	}

	/*
	 * The method to launch the program.
	 * 
	 * @param args The command line arguments. The arguments are passed on to the
	 * JavaFX application.
	 */
	public static void main(String[] args) {
		// try { maybe i should put the scanner here

		Application.launch(args);

		// }http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=1687099
		/*
		 * catch (InvocationTargetException e) {
		 * System.out.println("please input numbers only"); }
		 */
	}
}
