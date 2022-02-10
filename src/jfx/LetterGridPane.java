package jfx;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import logic.LetterGrade;

public class LetterGridPane extends GridPane {


	public void keyPress(KeyEvent keyEvent) {

	}

	private static class Letter extends Pane {
		final String text;
		LetterGrade letterGrade = LetterGrade.DEFAULT;



		Letter(String text) {
			this.text = text;
		}
	}
}
