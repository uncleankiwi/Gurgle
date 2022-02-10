package jfx;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.LetterGrade;

public class KeyboardPane extends VBox {
	private HBox topRow;
	private HBox middleRow;
	private HBox bottomRow;

	private static class Key extends Pane {
		LetterGrade letterGrade = LetterGrade.DEFAULT;

		void flip() {

		}

	}
}
