package jfx;

import exceptions.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Round;

import java.util.Random;

public class JFXApp extends Application {
	public static final Color BACKGROUND_BLACK = Color.rgb(18, 18, 19);
	public static final Color LIGHT_GRAY = Color.rgb(129,131,132);
	public static final Color DARK_GRAY = Color.rgb(58, 58, 60);
	public static final Color OCHRE = Color.rgb(181,159,59);
	public static final Color GREEN = Color.rgb(83,141,78);
	public static final Color OFF_WHITE = Color.rgb(215,218,220);

	private static final int DEFAULT_LENGTH = 5;

	//adding a shortcut with alt key here messes things up since it seems to get toggled
	//instead of holding it. might not be a problem for other computers?
	private final Button btnBeginRound = new NonFocusButton("Begin a new round (alt + b)");
	private final Button btnBeginRandomRound = new NonFocusButton("Begin a round with a random word length (alt + r)");
	private final LetterGridPane letterGridPane = new LetterGridPane();
	private final KeyboardPane keyboardPane = new KeyboardPane();
	private Stage stage;

	private Round round = null;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;	//for toast purposes
		VBox uiWrapper = new VBox();
		uiWrapper.setAlignment(Pos.CENTER);

		btnBeginRound.setOnAction(e -> beginRound(DEFAULT_LENGTH));
		btnBeginRandomRound.setOnAction(e -> beginRound());
		uiWrapper.setStyle("-fx-background-color: #" + ColourToHex.convert(BACKGROUND_BLACK));
		uiWrapper.getChildren().addAll(btnBeginRound, btnBeginRandomRound, letterGridPane, keyboardPane);

		Scene scene = new Scene(uiWrapper);
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				submitAnswer();
			}
			else if (e.getCode() == KeyCode.BACK_SPACE) {
				letterGridPane.backspace();
			}
			else if (e.getCode().isLetterKey()) {
				//some makeshift shortcut combinations since underscoring in buttons is wonky
				if (e.getText().equalsIgnoreCase("r") && e.isAltDown()) {
					btnBeginRandomRound.fire();
				}
				else if (e.getText().equalsIgnoreCase("b") && e.isAltDown()) {
					btnBeginRound.fire();
				}
				else {
					letterGridPane.letter(e.getText());
				}
			}
		});
		scene.setFill(BACKGROUND_BLACK);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Gurgle");
		primaryStage.setHeight(700d);
		primaryStage.setWidth(700d);
		beginRound(DEFAULT_LENGTH);
		primaryStage.show();
	}

	private void submitAnswer() {
		try {
			String attempt = letterGridPane.enter();
			this.round.grade(attempt);
			letterGridPane.flipAndNextRow();
			keyboardPane.refresh(round);
			if (round.getGameOver()) {
				if (round.getGameWon()) {
					letterGridPane.bounceWinningRow();
					showToast("You won in " + round.getCurrentAttempts() + " guesses.");
				}
				else {
					showToast("Answer was " + round.getCurrentWord());
				}
			}
		} catch (InputNotAllowedException | GameOverException inputNotAllowedException) {
			//do nothing
		} catch (NoSuchWordException | WrongGuessLengthException e) {
			letterGridPane.shakeInputRow();
			showToast(e.getMessage());
		}
	}

	@SuppressWarnings("SameParameterValue")
	private void beginRound(int length) {
		try {
			round = new Round(length);
		} catch (WrongRequestedLengthException wrongRequestedLengthException) {
			//do nothing
		} catch (NoSuchLengthException noSuchLengthException) {
			showToast(noSuchLengthException.getMessage());
		}
		letterGridPane.setRound(round);
		keyboardPane.refresh(round);
	}

	private void beginRound() {
		beginRound((int) (new Random().nextDouble() * (10 + 1 - 4)) + 4);
	}

	//message popup at the top
	private void showToast(String msg) {
		new Toast(stage, msg, 500, 1000, 500);
//		System.out.println(msg);
	}
}
