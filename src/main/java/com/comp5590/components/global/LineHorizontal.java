package com.comp5590.components.global;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class LineHorizontal extends Line {

    public LineHorizontal(Node lineLengthMetric, double offsetPerSide, double fatness) {
        // set the line's length to the length of the node
        this.setEndX(lineLengthMetric.getBoundsInLocal().getWidth());

        // apply styling to line
        this.setStroke(Paint.valueOf("#000000"));
        this.setStrokeWidth(Math.abs(fatness));

        // set initial line length values
        this.setStartX(Math.abs(offsetPerSide));
        this.setStartY(0);
        this.setEndX(lineLengthMetric.getBoundsInLocal().getWidth() - Math.abs(offsetPerSide));
        this.setEndY(0.0);

        // attach event to itself, that on lineLengthMetric change, it will update line
        // length
        lineLengthMetric
            .boundsInLocalProperty()
            .addListener((observable, oldValue, newValue) -> {
                this.setEndX(newValue.getWidth() - Math.abs(offsetPerSide));
            });
    }
}
