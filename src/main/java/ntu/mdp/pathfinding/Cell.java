package ntu.mdp.pathfinding;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;

public class Cell extends StackPane {

    private ImageView im;
    private double width, height;
    private URL imgAssetsUrl;

    private static final String imgAssetsFolder = "assets";
    private static final String imgCroppedFormat = "%d-%d.png";
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

    public void carPassHighlight() {
        clearHighlight();
        getStyleClass().add("cell-highlight");
    }

    public void unCarPassHighlight() {
        getStyleClass().remove("cell-highlight");
    }

    public void carHighlight() {
        clearHighlight();
        getStyleClass().add("cell-car-highlight");
    }

    public void unCarHighlight() {
        getStyleClass().remove("cell-car-highlight");
    }

    public void carStartHighlight() {
        clearHighlight();
        getStyleClass().add("cell-hover-highlight");
    }

    public void unCarStartHighlight() {
        getStyleClass().remove("cell-hover-highlight");
    }

    public void virtualBoarderHighlight() {
        clearHighlight();
        getStyleClass().add("cell-virtualBoarder-highlight");
    }

    public void unVirtualBoarderHighlight() {
        getStyleClass().remove("cell-virtualBoarder-highlight");
    }

    public void clearHighlight() {
        unCarHighlight();
        unCarPassHighlight();
        unCarStartHighlight();
        unVirtualBoarderHighlight();
    }

    public void setImage(String imgPath) {
        Image image = new Image(imgPath, width, height, false, false);
        setImage(image);
    }

    public void setImage(int idx, int part) {
        String imgPath = imgAssetsUrl.toExternalForm() + imgCroppedFormat.formatted(idx, part);
        setImage(imgPath);
    }

    public void setImage(int idx) {
        String imgPath = imgAssetsUrl.toExternalForm() + imgFormat.formatted(idx);
        setImage(imgPath);
    }

    public void setImage(Image image) {
        im.setImage(image);
    }

    public void unSetImage() {
        im.setImage(null);
    }

    public boolean isImageSet() {
        return im.getImage() != null;
    }
}
