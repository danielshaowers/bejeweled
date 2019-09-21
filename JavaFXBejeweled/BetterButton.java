package JavaFXBejeweled;
import javafx.scene.control.Button;

public class BetterButton extends Button {
	private int red;
	private int green;
	private int blue;
	
	public BetterButton (String s, int red, int green, int blue) {
		setText(s);
		this.red = red;
		this.blue = blue;
		this.green = green;
	}
	
	public BetterButton() {
		
	}
	
	public int getRed() {
		return red;
	}
	public int getGreen() {
		return green;
	}
	public int getBlue() {
		return blue;
	}
	
	public void setColor(int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
		setStyle("-fx-base: rgb(" + r + "," + g + "," + b + ");"); 
	}
	
	public boolean equals(BetterButton b2) {
		return this.getRed() == b2.getRed() && this.getGreen() == b2.getGreen() && this.getBlue() == b2.getBlue();
	}
	
	

}
