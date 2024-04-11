package com.comp5590.screens;

import com.comp5590.managers.ScreenManager;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class AboutUsScreen extends AbstractScreen {

    public AboutUsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        this.addCss("/aboutUs.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar("About Us");

        // set main content to center
        ((BorderPane) getRootPane()).setCenter(center());
    }

    private VBox center() {
        // Info paragraphs
        Label info1 = new Label(
            "GP Alpha was a company founded in 2024. Since then we have striven to provide the " +
            "best healthcare services for all our customers, using an intuitive design to help patients of all types " +
            "easily access and set appointments. We have doctors all across the world, that are personally " +
            "vetted by us to give you the best care possible."
        );
        info1.setWrapText(true);
        Label info2 = new Label(
            "Our online prescription system makes it effortless to collect any medication from " +
            "supported pharmacies. As of 04/04/2024, we have over 100 doctors working at GP alpha, and we have " +
            "helped over 300 patients with issues ranging from mental health to spinal injuries."
        );
        info2.setWrapText(true);
        VBox infoBox = new VBox(info1, info2);
        infoBox.setId("infoBox");

        // Customer reviews
        Label review1 = new Label(
            "\"I struggled with online health, but after finding GP Alpha, its never been " + "easier!\" - Susan, 37"
        );
        Label review2 = new Label("\"This program has changed my life.\" - Moe, 20");
        Label review3 = new Label("\"GP Alpha is undoubtedly one of the best programs I've ever used.\" - Dan, 52");
        VBox reviewBox = new VBox(review1, review2, review3);
        reviewBox.setId("reviewBox");
        reviewBox.setAlignment(Pos.CENTER);

        Label bottomNote = new Label("\u00A9 2024 GP Alpha."); // Using unicode copyright doesn't work
        bottomNote.setId("bottomNote");

        // Add elements to VBox
        VBox center = new VBox(infoBox, reviewBox, bottomNote);
        center.setId("center");
        center.getStyleClass().add("custom-pane");
        center.setPrefSize(600, 400);
        // set max width to 250, and max height to fit content
        center.setMaxSize(600, 400);
        center.setAlignment(Pos.CENTER);
        return center;
    }

    @Override
    public void cleanup() {}
}
