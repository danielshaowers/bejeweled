package JavaFXBejeweled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.embed.swing.JFXPanel;
public class BetterButtonTester {
	private JFXPanel a = new JFXPanel();
	@Test
	public void getRGBTester() {
		BetterButton b = new BetterButton("", 10, 11, 12);
		assertEquals(10, b.getRed());
		assertEquals(11, b.getGreen());
		assertEquals(12, b.getBlue());
	}
	@Test
	public void setColorTester(){
		BetterButton b = new BetterButton("", 10, 11, 12);
		b.setColor(15, 20, 30);
		assertEquals(15, b.getRed());
		assertEquals(20, b.getGreen());
		assertEquals(30, b.getBlue());
	}
	@Test
	public void setgetTextTester() {
		BetterButton b = new BetterButton("a", 10, 11, 12);
		assertEquals("a", b.getText());
		b.setText("poop");
		assertEquals("poop", b.getText());
	}
	@Test
	public void equalsTester() {
		BetterButton b = new BetterButton("a", 10, 11, 12);
		BetterButton c = new BetterButton("b", 10, 11, 12);
		assertTrue(b.equals(c));
		c.setColor(200, 11, 12);
		assertFalse(b.equals(c));
	}
}
