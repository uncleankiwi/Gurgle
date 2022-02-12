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

	public void refresh(Round round) {

	}

	private static class Key extends Pane {
		void flip(LetterGrade letterGrade) {
			switch (letterGrade) {
				case CORRECT:
					this.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.GREEN));
					break;
				case RIGHT_LETTER:
					this.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.OCHRE));
					break;
				case WRONG:
					this.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.DARK_GRAY));
					break;
				default:
					this.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.LIGHT_GRAY));
			}
		}

	}
}
