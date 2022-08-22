package com.example.pathfinding;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ImageRecognizePane extends Pane {
    private Text title;
    private Cell imgDisplay;

    public ImageRecognizePane(double startX, double startY, double width, double height) {
        title = new Text("Image Recognized");
        title.setLayoutX(startX);
        title.setLayoutY(startY);
        imgDisplay = new Cell(startX, startY + 40, width, height);
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
