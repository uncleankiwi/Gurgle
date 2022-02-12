package jfx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import logic.Keys;
import logic.LetterGrade;
import logic.Round;

import java.util.HashSet;
import java.util.Set;

public class KeyboardPane extends VBox {
	private final Set<Key> keys = new HashSet<>();
	private static final double GAP_SIZE = 5d;

	private static final double DEFAULT_WIDTH = 44;
	private static final double WIDE_WIDTH = 65;
	private static final double DEFAULT_HEIGHT = 60;

	public KeyboardPane() {
		HBox topRow = new HBox();
		for (char c : Keys.topRow) {
			Key key = new Key(c);
			keys.add(key);
			topRow.getChildren().add(key);
		}
		topRow.setAlignment(Pos.CENTER);
		HBox middleRow = new HBox();
		for (char c : Keys.midRow) {
			Key key = new Key(c);
			keys.add(key);
			middleRow.getChildren().add(key);
		}
		middleRow.setAlignment(Pos.CENTER);
		HBox bottomRow = new HBox();
		bottomRow.getChildren().add(new Key("ENTER", true));
		for (char c : Keys.bottomRow) {
			Key key = new Key(c);
			keys.add(key);
			bottomRow.getChildren().add(key);
		}
		bottomRow.getChildren().add(new Key("<<", true));
		bottomRow.setAlignment(Pos.CENTER);

		getChildren().addAll(topRow, middleRow, bottomRow);
		setAlignment(Pos.CENTER);
		setSpacing(GAP_SIZE);

		setPadding(new Insets(5));

//		setStyle("-fx-border-color: red; -fx-border-width: 1px;");
	}

	public void refresh(Round round) {
		for (Key key : keys) {
			key.flip(round.letterGradeMap.get(key.letter));
		}
	}

	private static class Key extends HBox {
		Label lblLetter = new Label();
		char letter;

		Key(char c) {
			this(String.valueOf(c).toUpperCase(),false);
			this.letter = c;
		}

		Key(String text, boolean wider) {
			setMinHeight(DEFAULT_HEIGHT);
			if (wider) {
				lblLetter.setMinWidth(WIDE_WIDTH);
				setMinWidth(WIDE_WIDTH + GAP_SIZE);
			}
			else {
				lblLetter.setMinWidth(DEFAULT_WIDTH);
				setMinWidth(DEFAULT_WIDTH + GAP_SIZE);
			}
			lblLetter.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.LIGHT_GRAY));
			lblLetter.setText(text);
			lblLetter.setTextAlignment(TextAlignment.CENTER);
			lblLetter.setMinHeight(DEFAULT_HEIGHT);
			lblLetter.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
			lblLetter.setTextFill(JFXApp.OFF_WHITE);
			lblLetter.setAlignment(Pos.CENTER);
			setAlignment(Pos.CENTER);
			setPadding(new Insets(0, GAP_SIZE / 2, 0, GAP_SIZE / 2));
			getChildren().add(lblLetter);
		}

		void flip(LetterGrade letterGrade) {
			switch (letterGrade) {
				case CORRECT:
					lblLetter.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.GREEN));
					break;
				case RIGHT_LETTER:
					lblLetter.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.OCHRE));
					break;
				case WRONG:
					lblLetter.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.DARK_GRAY));
					break;
				default:
					lblLetter.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.LIGHT_GRAY));
			}
		}

	}
}
