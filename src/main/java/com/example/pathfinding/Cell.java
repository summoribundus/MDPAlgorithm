package com.example.pathfinding;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import java.net.URL;

public class Cell extends StackPane {

    private ImageView im;
    private double width, height;
    private URL imgAssetsUrl;

    private static final String imgAssetsFolder = "assets";
    private static final String imgFormat = "%d.png";
    public Cell(double x, double y, double _width, double _height) {
        width = _width; height = _height;
        setLayoutX(x);
        setLayoutY(y);
        setPrefWidth(width);
        setPrefHeight(height);

        im = new ImageView();
        im.setFitHeight(width);
        im.setFitHeight(height);

        getStyleClass().add("cell");
        setOpacity(0.9);
        getChildren().add(im);

        imgAssetsUrl = getClass().getResource(imgAssetsFolder);
        assert imgAssetsUrl != null;
    }

    public void highlight() {
        getStyleClass().remove("cell-highlight");
        getStyleClass().add("cell-highlight");
    }

    public void unhighlight() {
        getStyleClass().remove("cell-highlight");
    }

    public void carHighlight() {
        getStyleClass().remove("cell-car-highlight");
        getStyleClass().add("cell-car-highlight");
    }

    public void unCarHighlight() {
        getStyleClass().remove("cell-car-highlight");
    }

    public void carStartHighlight() {
        getStyleClass().remove("cell-hover-highlight");
        getStyleClass().add("cell-hover-highlight");
    }

    public void unCarStartHighlight() {
        getStyleClass().remove("cell-hover-highlight");
    }

    public void setImage(String imgPath) {
        Image image = new Image(imgPath, width, height, false, false);
        setImage(image);
    }

    public void setImage(int idx) {
        String imgPath = imgAssetsUrl.toExternalForm() + imgFormat.formatted(idx);
        Image image = new Image(imgPath, width, height, false, false);
        setImage(image);
    }

    public void setImage(Image image) {
        im.setImage(image);
    }

    public void unSetImage() {
        im.setImage(null);
    }

//    public void setImageDraggedEnableHandler() {
//        setOnDragDetected(mouseEvent -> {
//            if (im.getImage() == null) {
//                mouseEvent.consume();
//                return;
//            }
//            Dragboard db = im.startDragAndDrop(TransferMode.COPY);
//            ClipboardContent cb = new ClipboardContent();
//            cb.putImage(im.getImage());
//            db.setContent(cb);
//            mouseEvent.consume();
//        });
//    }
//
//    public void setImageDraggedEventHandler() {
//        setOnDragOver(dragEvent -> {
//            if (dragEvent.getGestureSource() != this &&
//                    dragEvent.getDragboard().hasImage()) {
//                dragEvent.acceptTransferModes(TransferMode.COPY);
//            }
//            dragEvent.consume();
//        });
//
//        setOnDragEntered(dragEvent -> {
//            if (dragEvent.getGestureSource() != this
//                    && dragEvent.getDragboard().hasImage()){
//                hoverHighlight();
//            }
//            dragEvent.consume();
//        });
//
//        setOnDragExited(dragEvent -> {
//            unHoverHighlight();
//            dragEvent.consume();
//        });
//
//        setOnDragDropped(dragEvent -> {
//            boolean success = false;
//            Dragboard db = dragEvent.getDragboard();
//
//            if (db.hasImage()) {
//                this.im.setImage(db.getImage());
//                success = true;
//            }
//
//            dragEvent.setDropCompleted(success);
//            dragEvent.consume();
//        });
//    }
}
