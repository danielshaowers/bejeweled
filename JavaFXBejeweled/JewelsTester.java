package JavaFXBejeweled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.embed.swing.JFXPanel;

public class JewelsTester {
	private Jewels game = new Jewels();
	private JFXPanel a = new JFXPanel();

	public boolean containsColor(int[][] colorSet, BetterButton b) {
		boolean containsRed = false, containsGreen = false, containsBlue = false;
		for (int i = 0; i < colorSet.length && !containsRed; i++)
			containsRed = b.getRed() == colorSet[i][0];
		for (int i = 0; i < colorSet.length && !containsGreen; i++)
			containsGreen = b.getRed() == colorSet[i][0];
		for (int i = 0; i < colorSet.length && !containsBlue; i++)
			containsBlue = b.getRed() == colorSet[i][0];
		return containsRed && containsGreen && containsBlue;
	}

	public boolean allIdentical(BetterButton[][] list) {
		boolean allEqual = true;
		for (int i = 0; i < list.length && allEqual; i++) {
			for (int j = 0; j < list[i].length && allEqual; j++) {
				allEqual = list[0][0].equals(list[i][j]);
			}
		}
		return allEqual;
	}

	// turns all buttons in the array the same color
	public void makeIdentical(BetterButton[][] list) {
		for (int i = 0; i < list.length; i++) {
			for (int j = 0; j < list[i].length; j++) {
				list[i][j].setColor(10, 10, 10);
			}
		}

	}

	/**
	 * Tests the makeBoard method
	 */
	@Test
	public void makeBoardTester() {

		// test zero
		BetterButton[][] a = game.makeBoard(0, 0, 0);
		assertEquals(0, a.length);

		// test one
		BetterButton[][] b = game.makeBoard(1, 1, 1);
		int[][] colorSet = game.getColorSet();
		assertEquals(1, b.length);
		assertEquals(1, b[0].length);
		assertEquals(colorSet[0][0], b[0][0].getRed());
		assertEquals(colorSet[0][1], b[0][0].getGreen());
		assertEquals(colorSet[0][2], b[0][0].getBlue());

		// test many
		b = game.makeBoard(5, 6, 5);
		colorSet = game.getColorSet();
		assertEquals(6, b.length);
		assertEquals(5, b[0].length);

		// test first
		assertTrue(containsColor(colorSet, b[0][0]));
		// test middle
		assertTrue(containsColor(colorSet, b[3][3]));
		assertTrue(containsColor(colorSet, b[2][2]));
		// test last
		assertTrue(containsColor(colorSet, b[5][4]));

	}

	/**
	 * tests the changeBoard method, which removes jewels that are 3 in a row and
	 * shifts jewels down
	 * 
	 */
	@Test
	public void changeBoardTester() {
		game.makeBoard(3, 2, 2);

		for (int i = 0; i < game.getButtonArray().length; i++) {

			for (int j = 0; j < game.getButtonArray()[i].length; j++) {
				game.getButtonArray()[i][j].setColor(10, 10, 10);
			}
		}
		BetterButton[][] array2 = game.changeBoard(game.getButtonArray()[0][0], game.getButtonArray()[1][0]);
		// none of the buttons should have changed.
		// since the list had a length of 2 and jewels are only removed when there are 3
		// in a row
		// all buttons should have the same color
		assertTrue(allIdentical(array2)); // tests if any buttons changed when only 2 colors in a row
		// test many
		game.makeBoard(5, 5, 5);

		assertTrue(allIdentical(array2));

		// tests if the method successfully removes a column of 3 identical colors and
		// replaces it with new colors
		// test first
		array2 = game.changeBoard(game.getButtonArray()[0][0], game.getButtonArray()[1][0]);
		assertFalse(allIdentical(array2));
		assertTrue(containsColor(game.getColorSet(), array2[0][0])); // tests if the new color is in the set of possible
																		// colors

		// test middle
		makeIdentical(array2);
		array2 = game.changeBoard(game.getButtonArray()[2][3], game.getButtonArray()[3][3]);
		assertFalse(allIdentical(array2));
		assertTrue(containsColor(game.getColorSet(), array2[2][3]));

		// test last
		makeIdentical(array2);
		game.changeBoard(game.getButtonArray()[3][4], game.getButtonArray()[4][4]);
		assertFalse(allIdentical(array2));
		assertTrue(containsColor(game.getColorSet(), array2[3][4]));
	}

	/**
	 * Tests the getLocation method
	 */
	@Test
	public void getLocationTester() {
		BetterButton a = new BetterButton();
		// test zero (button not in the array)
		game.makeBoard(1, 1, 1);
		assertEquals(null, game.getLocation(a));
		// test one
		assertEquals(0, game.getLocation(game.getButtonArray()[0][0])[0]);
		assertEquals(0, game.getLocation(game.getButtonArray()[0][0])[1]);

		// test many
		game.makeBoard(10, 10, 10);
		// test first
		assertEquals(0, game.getLocation(game.getButtonArray()[0][0])[0]);
		assertEquals(0, game.getLocation(game.getButtonArray()[0][0])[1]);

		// test middle
		assertEquals(5, game.getLocation(game.getButtonArray()[5][5])[0]);
		assertEquals(5, game.getLocation(game.getButtonArray()[5][5])[1]);

		// test last
		assertEquals(9, game.getLocation(game.getButtonArray()[8][9])[0]);
		assertEquals(8, game.getLocation(game.getButtonArray()[8][9])[1]);
	}

	@Test
	public void winConditionTester() {
		//test victory
		game.makeBoard(1, 3, 3);
		game.changeBoard(game.getButtonArray()[0][0], game.getButtonArray()[0][1]);
		game.changeBoard(game.getButtonArray()[1][0], game.getButtonArray()[1][1]);
		game.changeBoard(game.getButtonArray()[2][0], game.getButtonArray()[2][1]);
		assertTrue(game.winCondition(game.getButtonArray()));
		//test failure
		game.makeBoard(1000, 2, 2);
		assertFalse(game.winCondition(game.getButtonArray()));
	}

	@Test
	public void generateRandomColorsTester() {
		int[][] a;
		// test 1 the number generated is always within 255 (allowable RGB values)
		for (int i = 0; i < 1000000; i++) {
			 a = game.generateRandomColors(1);
			assertTrue(a[0][0] <= 255);
			assertTrue(a[0][1] <= 255);
			assertTrue(a[0][1] <= 255);
		}

		// test many
		a = game.generateRandomColors(10);
		for (int b = 0; b < 1000; b++) {
			for (int i = 1; i < a.length; i++) {
				a = game.generateRandomColors(10);
					assertTrue(a[0][0] != a[i][0] || a[0][1] != a[i][1] || (a[0][2] != a[i][2]));
			}

		}
	}


}
