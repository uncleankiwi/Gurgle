package jfx;

import javafx.scene.control.Button;

import static jfx.JFXApp.BACKGROUND_BLACK;
import static jfx.JFXApp.OFF_WHITE;

public class NonFocusButton extends Button {
	public NonFocusButton(String text) {
		super(text);
		this.setFocusTraversable(false);
		setStyle("-fx-background-color: #" + ColourToHex.convert(BACKGROUND_BLACK));
		setTextFill(OFF_WHITE);
	}
}
