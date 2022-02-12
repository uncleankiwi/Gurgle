package jfx;

import exceptions.InputNotAllowedException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import logic.LetterGrade;
import logic.Round;


public class LetterGridPane extends GridPane {
	private Round round = null;
	private LetterPane[][] letterPanes;
	private Integer currentRow = null;
	private Integer currentCol = null;


	public LetterGridPane() {

	}

	public void letter(String letter) {
		if (currentRow != null && currentCol != null && currentCol < round.getLength()) {
			letterPanes[currentCol][currentRow].setText(letter.toLowerCase());
			currentCol++;
		}
	}

	public String enter() throws InputNotAllowedException {
		return getAttempt();
	}

	public void backspace() {
		if (currentRow != null && currentCol != null && currentCol >= 1) {
			currentCol--;
			letterPanes[currentCol][currentRow].setText("");
		}
	}

	public String getAttempt() throws InputNotAllowedException {
		if (currentRow == null) {
			throw new InputNotAllowedException();
		}
		else {
			StringBuilder builder = new StringBuilder();
			for (int col = 0; col < round.getLength(); col++) {
				builder.append(letterPanes[col][currentRow].getText());
			}
			return builder.toString();
		}
	}

	//wag input row upon pressing enter
	public void shakeInputRow() {
		if (currentRow != null) {
			for (LetterPane letterPane : letterPanes[currentRow]) {
				letterPane.shake();
			}
		}
	}

	//flip the input row such that it now shows LetterGrades
	//then enables the next row of LetterPanes for entry, if applicable
	public void flipAndNextRow() {
		if (currentRow != null && round != null) {
			//flipping
			for (int col = 0; col < round.getLength(); col++) {
				letterPanes[col][currentRow].flip(round.grades[currentRow][col]);	//ugh
			}

			if (!round.getGameOver()) {
				currentRow++;
				currentCol = 0;
			}
			else {
				currentCol = null;
				currentRow = null;
			}
		}
	}

	public void setRound(Round round) {
		this.round = round;
		this.currentCol = 0;
		this.currentRow = 0;
		this.letterPanes = new LetterPane[round.getLength()][Round.MAX_ATTEMPTS];
		for (int row = 0; row < Round.MAX_ATTEMPTS; row++) {
			for (int col = 0; col < round.getLength(); col++) {
				this.letterPanes[col][row] = new LetterPane();
				this.add(this.letterPanes[col][row], col, row);
			}
		}

		//this.setStyle("-fx-border-color: green; -fx-border-width: 1px");

	}

	private static class LetterPane extends HBox {
		Label lblLetter = new Label();
		static final double SIDE_LENGTH = 60d;

		void setText(String str) {
			this.lblLetter.setText(str);
		}

		String getText() {
			return this.lblLetter.getText();
		}

		LetterPane() {
			getChildren().add(lblLetter);
			lblLetter.setTextAlignment(TextAlignment.CENTER);
			lblLetter.minWidth(SIDE_LENGTH);
			lblLetter.minHeight(SIDE_LENGTH);
			lblLetter.setFont(Font.font("Calibri", FontWeight.BOLD, 40));
			lblLetter.setTextFill(JFXApp.OFF_WHITE);
//			lblLetter.setStyle("-fx-border-color: blue; -fx-border-width: 5px;");
			this.setAlignment(Pos.CENTER);
			setMinWidth(SIDE_LENGTH);
			setMinHeight(SIDE_LENGTH);
			setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.LIGHT_GRAY));
		}

		void shake() {

		}

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
