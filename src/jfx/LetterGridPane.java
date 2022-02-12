package jfx;

import exceptions.InputNotAllowedException;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import logic.LetterGrade;
import logic.Round;

public class LetterGridPane extends GridPane {
	private static final double GAP_SIZE = 5d;

	private Round round = null;
	private LetterPane[][] letterPanes;
	private Integer currentRow = null;
	private Integer currentCol = null;

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

	public void bounceWinningRow() {
		for (int col = 0; col < round.getLength(); col++) {
			letterPanes[col][round.getCurrentAttempts() - 1].bounce(80 * col);	//ugh
		}
	}

	//wag input row upon pressing enter
	public void shakeInputRow() {
		if (currentRow != null) {
			for (int col = 0; col < round.getLength(); col++) {
				letterPanes[col][currentRow].shake();	//ugh
			}
		}
	}

	//flip the input row such that it now shows LetterGrades
	//then enables the next row of LetterPanes for entry, if applicable
	public void flipAndNextRow() {
		if (currentRow != null && round != null) {
			//flipping
			for (int col = 0; col < round.getLength(); col++) {
				letterPanes[col][currentRow].flip(round.grades[currentRow][col], 80 * col);	//ugh.
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
		currentCol = 0;
		currentRow = 0;
		getChildren().clear();
		letterPanes = new LetterPane[round.getLength()][Round.MAX_ATTEMPTS];
		for (int row = 0; row < Round.MAX_ATTEMPTS; row++) {
			for (int col = 0; col < round.getLength(); col++) {
				letterPanes[col][row] = new LetterPane();
				add(letterPanes[col][row], col, row);
			}
		}
		setHgap(GAP_SIZE);
		setVgap(GAP_SIZE);
		setAlignment(Pos.CENTER);

		//this.setStyle("-fx-border-color: green; -fx-border-width: 1px");
	}

	private static class LetterPane extends HBox {
		Label lblLetter = new Label();
		static final double SIDE_LENGTH = 60d;
		private boolean animating = false;

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
			this.setStyle("-fx-background-color: #" + ColourToHex.convert(JFXApp.BACKGROUND_BLACK));
			this.setStyle("-fx-border-width: 2px; -fx-border-color: #" + ColourToHex.convert(JFXApp.LIGHT_GRAY));
		}

		void shake() {
			if (animating) return;
			animating = true;
			int[] offsets = new int[]{8, -6, 4, -3, 2};
			TranslateTransition[] translation = new TranslateTransition[offsets.length];
			for (int i = 0; i < offsets.length; i++) {
				translation[i] = new TranslateTransition(Duration.millis(50), this);
				translation[i].setByX(offsets[i]);
				translation[i].setCycleCount(2);
				translation[i].setAutoReverse(true);
				if (i >= offsets.length - 1) {
					translation[i].setOnFinished(e -> animating = false);
				}
			}
			SequentialTransition sequence = new SequentialTransition();
			sequence.getChildren().addAll(translation);
			sequence.play();
		}

		void bounce(double delay) {
			if (animating) return;
			animating = true;

			TranslateTransition delayDummy = new TranslateTransition(Duration.millis(delay), this);
			delayDummy.setByY(0);
			delayDummy.setCycleCount(0);
			delayDummy.setAutoReverse(true);

			TranslateTransition translation = new TranslateTransition(Duration.millis(60), this);
			translation.setByY(-20);
			translation.setCycleCount(2);
			translation.setAutoReverse(true);
			translation.setOnFinished(e -> animating = false);

			SequentialTransition sequence = new SequentialTransition();
			sequence.getChildren().addAll(delayDummy, translation);
			sequence.play();
		}

		void flip(LetterGrade letterGrade, double delay) {
			//it won't conflict with the translation animations, so we won't check for it here.
			//they can occur concurrently.

			TranslateTransition delayDummy = new TranslateTransition(Duration.millis(delay), this);
			delayDummy.setByY(0);
			delayDummy.setCycleCount(0);
			delayDummy.setAutoReverse(false);

			ScaleTransition scalingAway = new ScaleTransition(Duration.millis(60), this);
			scalingAway.setByY(-1);
			scalingAway.setCycleCount(1);
			scalingAway.setOnFinished(e -> {
				switch (letterGrade) {
					case CORRECT:
						this.setStyle("-fx-border-width: 0px; -fx-background-color: #" + ColourToHex.convert(JFXApp.GREEN));
						break;
					case RIGHT_LETTER:
						this.setStyle("-fx-border-width: 0px; -fx-background-color: #" + ColourToHex.convert(JFXApp.OCHRE));
						break;
					case WRONG:
						this.setStyle("-fx-border-width: 0px; -fx-background-color: #" + ColourToHex.convert(JFXApp.DARK_GRAY));
						break;
					default:
						this.setStyle("; -fx-background-color: #" + ColourToHex.convert(JFXApp.BACKGROUND_BLACK) +
								"-fx-border-width: 2px; -fx-border-color: #" + ColourToHex.convert(JFXApp.LIGHT_GRAY));
				}
			});

			ScaleTransition scalingBack = new ScaleTransition(Duration.millis(60), this);
			scalingBack.setByY(1);
			scalingBack.setCycleCount(1);
			scalingBack.setAutoReverse(false);

			SequentialTransition sequence = new SequentialTransition();
			sequence.getChildren().addAll(delayDummy, scalingAway, scalingBack);
			sequence.play();
		}
	}
}
