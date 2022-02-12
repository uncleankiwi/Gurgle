package jfx;

import javafx.scene.paint.Color;

public class ColourToHex {
	public static String convert(Color color) {
		return color.toString().substring(2, 8);
	}
}
