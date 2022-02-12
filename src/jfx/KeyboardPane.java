package jfx;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.LetterGrade;
import logic.Round;

public class KeyboardPane extends VBox {
	private Round round = null;
	private HBox topRow;
	private HBox middleRow;
	private HBox bottomRow;

	public void refreshRound(Round round) {

	}

	private static class Key extends Pane {
		LetterGrade letterGrade = LetterGrade.DEFAULT;

		void flip() {

		}

	}
}
