package jfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
			Key key = new Key(KeyCode.getKeyCode(String.valueOf(c).toUpperCase()));
			keys.add(key);
			topRow.getChildren().add(key);
			setHandler(key);
		}
		topRow.setAlignment(Pos.CENTER);
		HBox middleRow = new HBox();
		for (char c : Keys.midRow) {
			Key key = new Key(KeyCode.getKeyCode(String.valueOf(c).toUpperCase()));
			keys.add(key);
			middleRow.getChildren().add(key);
			setHandler(key);
		}
		middleRow.setAlignment(Pos.CENTER);
		HBox bottomRow = new HBox();
		Key enterKey = new Key(KeyCode.ENTER);
		bottomRow.getChildren().add(enterKey);
		setHandler(enterKey);
		for (char c : Keys.bottomRow) {
			Key key = new Key(KeyCode.getKeyCode(String.valueOf(c).toUpperCase()));
			keys.add(key);
			bottomRow.getChildren().add(key);
			setHandler(key);
		}
		Key backspaceKey = new Key(KeyCode.BACK_SPACE);
		bottomRow.getChildren().add(backspaceKey);
		setHandler(backspaceKey);
		bottomRow.setAlignment(Pos.CENTER);

		getChildren().addAll(topRow, middleRow, bottomRow);
		setAlignment(Pos.CENTER);
		setSpacing(GAP_SIZE);

		setPadding(new Insets(5));

//		setStyle("-fx-border-color: red; -fx-border-width: 1px;");
	}

	//getting mouse clicks (interpreted as key presses) from individual keys
	//then passing them on upwards
	ObjectProperty<EventHandler<? super KeyEvent>> onActionProperty() { return onAction; }
	public void setOnAction(EventHandler<? super KeyEvent> value) { onActionProperty().set(value); }
	private final ObjectProperty<EventHandler<? super KeyEvent>> onAction = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() {
		@Override protected void invalidated() {
			setEventHandler(KeyEvent.KEY_PRESSED, get());
		}

		@Override
		public Object getBean() {
			return KeyboardPane.this;
		}

		@Override
		public String getName() {
			return "onAction";
		}
	};

	private void setHandler(Key key) {
		key.setOnAction(
				e -> this.setOnAction(
						event -> new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
				key.keyCode, false, false, false, false)));
	}

	public void refresh(Round round) {
		for (Key key : keys) {
			key.flip(round.letterGradeMap.get(key.getLetter()));
		}
	}

	private static class Key extends HBox {
		Label lblLetter = new Label();
		KeyCode keyCode;

		Key(KeyCode keyCode) {
			this.keyCode = keyCode;
			setMinHeight(DEFAULT_HEIGHT);
			if (keyCode == KeyCode.ENTER || keyCode == KeyCode.BACK_SPACE) {
				lblLetter.setMinWidth(WIDE_WIDTH);
				setMinWidth(WIDE_WIDTH + GAP_SIZE);
			}
			else {
				lblLetter.setMinWidth(DEFAULT_WIDTH);
				setMinWidth(DEFAULT_WIDTH + GAP_SIZE);
			}

			if (keyCode == KeyCode.ENTER) {
				lblLetter.setText("ENTER");
			}
			else if (keyCode == KeyCode.BACK_SPACE) {
				lblLetter.setText("<<");
			}
			else {
				lblLetter.setText(keyCode.getName());
			}
			setLabelColour(JFXApp.LIGHT_GRAY);
			lblLetter.setTextAlignment(TextAlignment.CENTER);
			lblLetter.setMinHeight(DEFAULT_HEIGHT);
			lblLetter.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
			lblLetter.setTextFill(JFXApp.OFF_WHITE);
			lblLetter.setAlignment(Pos.CENTER);
			setAlignment(Pos.CENTER);
			setPadding(new Insets(0, GAP_SIZE / 2, 0, GAP_SIZE / 2));
			//transmuting a click into a KeyEvent of a particular keycode
			lblLetter.setOnKeyPressed(
					e -> this.setOnAction(
					event -> new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
							keyCode, false, false, false, false)));
			getChildren().add(lblLetter);
		}

		char getLetter() {
			return this.keyCode.getName().toLowerCase().toCharArray()[0];
		}

		void flip(LetterGrade letterGrade) {
			switch (letterGrade) {
				case CORRECT:
					setLabelColour(JFXApp.GREEN);
					break;
				case RIGHT_LETTER:
					setLabelColour(JFXApp.OCHRE);
					break;
				case WRONG:
					setLabelColour(JFXApp.DARK_GRAY);
					break;
				default:
					setLabelColour(JFXApp.LIGHT_GRAY);
			}
		}

		private void setLabelColour(Color colour) {
			lblLetter.setStyle("-fx-background-radius: 4;" +
					"-fx-background-color: #" + ColourToHex.convert(colour));

		}

		//mouse clicks are treated like specific key presses
		ObjectProperty<EventHandler<? super KeyEvent>> onActionProperty() { return onAction; }
		public void setOnAction(EventHandler<? super KeyEvent> value) { onActionProperty().set(value); }
		private final ObjectProperty<EventHandler<? super KeyEvent>> onAction = new ObjectPropertyBase<EventHandler<? super KeyEvent>>() {
			@Override protected void invalidated() {
				setEventHandler(KeyEvent.KEY_PRESSED, get());
			}

			@Override
			public Object getBean() {
				return Key.this;
			}

			@Override
			public String getName() {
				return "onAction";
			}
		};

	}
}
