package ntu.mdp.pathfinding.GUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;

public class ImageRecognizePane extends Pane {
    private Text title;
    private Cell imgDisplay;

    public ImageRecognizePane(double startX, double startY, double width, double height, URL imgURL) {
        title = new Text("Image Recognized");
        title.setLayoutX(startX);
        title.setLayoutY(startY + 10);
        title.setStyle("-fx-font: 24 arial;");
        imgDisplay = new Cell(startX, startY + 30, width, height, imgURL);
        getChildren().addAll(title, imgDisplay);
    }

    public void recognize(int imgIdx) {
        imgDisplay.setImage(imgIdx);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            imgDisplay.unSetImage();
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
