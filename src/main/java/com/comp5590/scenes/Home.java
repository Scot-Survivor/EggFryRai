package com.comp5590.scenes;

/**
 * Temporary will act as a home
 */

import com.comp5590.managers.SceneManager;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class Home extends AbstractScene {

    public Home (SceneManager main){
        super(main);
    }

    @Override
    public void setup() {
        setRootPane(new BorderPane());
        // We know that root is always going to be a BorderPane so its safe to cast without checking.
        ((BorderPane) getRootPane()).setCenter(center());
    }

    private VBox center(){
        Text welcome = new Text("Welcome");
        VBox central = new VBox(welcome);
        return central;
    }
}
