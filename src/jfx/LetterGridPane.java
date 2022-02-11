package jfx;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import logic.LetterGrade;
import logic.Round;


public class LetterGridPane extends GridPane {
	private Round round = null;
	private char[] attempt;
	private LetterPane[][] letterPanes;

	public LetterGridPane() {

	}

	public void letter(String letter) {
		System.out.println(letter + " letter pressed");
	}

	public void enter() {
		System.out.println("enter pressed");

	}

	public void backspace() {
		System.out.println("bksp pressed");

	}

	//wag input row upon pressing enter
	public void wagInputRow() {

	}

	//flip the input row such that it now shows LetterGrades
	private void flipInputRow() {

	}

	public void refreshRound(Round round) {

	}

	public void setRound(Round round) {
		this.round = round;
		this.attempt = new char[round.getLength()];
		this.letterPanes = new LetterPane[Round.MAX_ATTEMPTS][round.getLength()];
		for (int row = 0; row < Round.MAX_ATTEMPTS; row++) {
			for (int col = 0; col < round.getLength(); col++) {
				this.letterPanes[row][col] = new LetterPane();
				GridPane.setRowIndex(this.letterPanes[row][col], row);
				GridPane.setColumnIndex(this.letterPanes[row][col], col);
				this.getChildren().add(this.letterPanes[row][col]);

				this.letterPanes[row][col].setText(row + ":" + col);
			}
		}

	}

	private static class LetterPane extends Pane {
		String text = "";
		Text txt = new Text();
		LetterGrade letterGrade = LetterGrade.DEFAULT;

		void setText(String str) {
			this.text = str;
		}

		LetterPane() {
			this.getChildren().add(txt);
		}
	}
}
