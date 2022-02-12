package jfx;

import exceptions.InputNotAllowedException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Gurgle;
import logic.Round;

public class JFXApp extends Application {
	public static final Color LIGHT_GRAY = Color.rgb(129,131,132);
	public static final Color DARK_GRAY = Color.rgb(58, 58, 60);
	public static final Color OCHRE = Color.rgb(181,159,59);
	public static final Color GREEN = Color.rgb(83,141,78);

	private final Button btnBeginRound = new Button("_Begin a new round (alt + b)");
	private final Button btnBeginRandomRound = new Button("Begin a round with a _random random (alt + r)");
	private final LetterGridPane letterGridPane = new LetterGridPane();
	private final KeyboardPane keyboardPane = new KeyboardPane();

	private Round round = null;


	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) {
		VBox uiWrapper = new VBox();

		btnBeginRound.setOnAction(e -> {
			this.round = new Round(5);
			letterGridPane.setRound(this.round);
		});
		btnBeginRound.setFocusTraversable(false);
		btnBeginRandomRound.setOnAction(e -> {
			System.out.println("Random length round begins.");
		});
		btnBeginRandomRound.setFocusTraversable(false);

		uiWrapper.getChildren().addAll(btnBeginRound, btnBeginRandomRound, letterGridPane, keyboardPane);
		Scene scene = new Scene(uiWrapper);
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				try {
					String attempt = letterGridPane.enter();
					this.round.grade(attempt);
				} catch (InputNotAllowedException inputNotAllowedException) {
					//do nothing
				}

			}
			else if (e.getCode() == KeyCode.BACK_SPACE) {
				letterGridPane.backspace();
			}
			else if (e.getCode().isLetterKey() && !e.isAltDown()) {
				letterGridPane.letter(e.getText());
			}
		});

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Gurgle");
		primaryStage.setHeight(800d);
		primaryStage.show();
	}

	private void submitAnswer() {

	}
}
