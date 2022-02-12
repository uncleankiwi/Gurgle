package jfx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Toast {
	public Toast(Stage stage, String msg, long fadeInTime, long displayTime, long fadeOutTime) {
		Stage childStage = new Stage();
		childStage.initOwner(stage);
		childStage.setResizable(false);
		childStage.initStyle(StageStyle.TRANSPARENT);

		Label label = new Label(msg);
		label.setTextFill(Color.BLACK);
		label.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		label.setPadding(new Insets(5));
		HBox hBox = new HBox();
		hBox.setStyle("-fx-background-color: #eeeeeeff; -fx-background-radius: 5;");
		hBox.getChildren().add(label);
		Scene scene = new Scene(hBox);
		scene.setFill(Color.TRANSPARENT);
		childStage.setScene(scene);
		childStage.show();
		stage.requestFocus();

		Timeline timeline = new Timeline();
		KeyFrame fadeInFrame = new KeyFrame(Duration.millis(fadeInTime),
				new KeyValue(childStage.getScene().getRoot().opacityProperty(), 1));
		KeyFrame unchangingFrame = new KeyFrame(Duration.millis(fadeInTime + displayTime),
				new KeyValue(childStage.getScene().getRoot().opacityProperty(), 1));
		KeyFrame fadeOutFrame = new KeyFrame(Duration.millis(fadeInTime + displayTime + fadeOutTime),
				new KeyValue(childStage.getScene().getRoot().opacityProperty(), 0));
		timeline.getKeyFrames().add(fadeInFrame);
		timeline.getKeyFrames().add(fadeOutFrame);
		timeline.getKeyFrames().add(unchangingFrame);
		timeline.setOnFinished(e -> childStage.close());
		timeline.play();

	}
}
