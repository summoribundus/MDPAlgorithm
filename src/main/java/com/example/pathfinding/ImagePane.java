package com.example.pathfinding;
//
//import javafx.scene.image.Image;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.text.Text;
//
//import java.net.URI;
//import java.net.URL;
//
//public class ImagePane extends Pane {
//    private static final int numImages = 29;
//    private double cWidth, cHeight;
//    private Cell[][] cells;
//
//    private static final String imgAssetsFolder = "assets";
//    private static final String imgFormat = "%d.png";
//
//    public ImagePane(int r, int c, double width, double height, double marginX, double marginY, double imageGap) {
//        cWidth = width; cHeight = height;
//        cells = new Cell[r][c];
//        URL imgAssetsUrl = getClass().getResource(imgAssetsFolder);
//        assert imgAssetsUrl != null;
//
//        int imageIdx = 0;
//        for (int i = 0; i < r; i++) {
//            for (int j = 0; j < c; j++) {
//                double x = j * cWidth + marginX + j * imageGap, y = i * cHeight + marginY + i * imageGap;
//                if (imageIdx >= numImages) continue;
//                Cell cell = new Cell(x, y, cWidth, cHeight, true);
//                cell.setImage(imgAssetsUrl.toExternalForm() + imgFormat.formatted(imageIdx++));
//                cell.setImageDraggedEnableHandler();
//                getChildren().add(cell);
//                cells[i][j] = cell;
//            }
//        }
//    }
//}
