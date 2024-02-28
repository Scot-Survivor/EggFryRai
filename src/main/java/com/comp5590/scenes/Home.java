package com.comp5590.scenes;

/**
 * Temporary will act as a home
 */

import com.comp5590.SceneManager;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class Home {

    private SceneManager main;
    private BorderPane mainPane;

    public Home (SceneManager main){
        this.main = main;

        mainPane = new BorderPane();
        mainPane.setCenter(center());
    }

    private VBox center(){
        Text welcome = new Text("Welcome");
        VBox central = new VBox(welcome);
        return central;
    }

    /**
     * Gets the root node of the given interface
     * @return BorderPane which is the root node
     */
    public BorderPane getRoot(){
        return mainPane;
    }
}
