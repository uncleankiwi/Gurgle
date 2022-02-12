package jfx;

import exceptions.InputNotAllowedException;
import javafx.scene.control.Label;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
			for (LetterPane letterPane : letterPanes[currentRow]) {
				builder.append(letterPane.getText());
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
	private void flipInputRow() {
		if (currentRow != null) {
			for (int col = 0; col < round.getLength(); col++) {
				letterPanes[currentRow][col].flip(round.grades[currentRow][col]);
			}
			for (LetterPane letterPane : letterPanes[currentRow]) {
				letterPane.shake();
			}

			if (round.getCurrentAttempts() < Round.MAX_ATTEMPTS - 1) {
				currentRow++;
				currentCol = 0;
			}
			else {
				currentRow = null;
				currentCol = null;
			}
		}
	}

	public void refreshRound(Round round) {
		currentCol = 0;
	}

	public void setRound(Round round) {
		this.round = round;
		this.letterPanes = new LetterPane[round.getLength()][Round.MAX_ATTEMPTS];
		for (int row = 0; row < Round.MAX_ATTEMPTS; row++) {
			for (int col = 0; col < round.getLength(); col++) {
				this.letterPanes[col][row] = new LetterPane();
				this.add(this.letterPanes[col][row], col, row);

				this.letterPanes[col][row].setText(row + ":" + col);		//todo
			}
		}

		this.setStyle("-fx-border-color: green; -fx-border-width: 1px"); //todo

	}

	private static class LetterPane extends Pane {
		Label lblLetter = new Label();
		static final double SIDE_LENGTH = 48d;

		void setText(String str) {
			this.lblLetter.setText(str);
		}

		String getText() {
			return this.lblLetter.getText();
		}

		LetterPane() {
			this.setStyle("-fx-border-color: red; -fx-border-width: 1px");	//todo
			this.getChildren().add(lblLetter);
			this.lblLetter.setTextAlignment(TextAlignment.CENTER);
			this.lblLetter.minWidth(SIDE_LENGTH);
			this.lblLetter.minHeight(SIDE_LENGTH);
			this.lblLetter.setFont(Font.font("Calibri", FontWeight.BOLD, 25));
			this.lblLetter.setStyle("-fx-border-color: blue; -fx-border-width: 5px");	//todo
			this.setMinWidth(SIDE_LENGTH);
			this.setMinHeight(SIDE_LENGTH);
			this.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.LIGHT_GRAY));
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
