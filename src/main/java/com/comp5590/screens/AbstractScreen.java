package com.comp5590.screens;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.SessionFactory;

@Getter
public abstract class AbstractScreen {

    private final App app;
    private final DatabaseManager databaseManager;
    private final ScreenManager screenManager;
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerManager.getInstance().getLogger(AbstractScreen.class, "DEBUG");

    @Setter
    private Pane rootPane;

    /**
     * The paths to the CSS file for the screen
     */
    @Getter
    private final List<String> cssPaths;

    public AbstractScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.app = App.getInstance();
        this.databaseManager = DatabaseManager.getInstance();
        this.sessionFactory = this.databaseManager.getSessionFactory();
        this.cssPaths = new ArrayList<>();
        this.setup();
    }

    /**
     * This method is called when the screen is created
     */
    public abstract void setup();

    /**
     * This method is called when the scene is changed, to clean it up
     * Every screen must implement this method
     * Examples of cleanup include removing event listeners, clearing text fields,
     * etc.
     */
    public abstract void cleanup();

    /**
     * This method is called when the screen is shown, to ensure that the logged in
     * user has access to the screen
     *
     * @return true if the user has access to the screen
     */
    public boolean canAccess() {
        return true;
    }

    /**
     * This method is called when the screen is shown, to ensure that the logged in
     * user has access to the screen
     *
     * @param user object
     * @return true if the user has access to the screen
     */
    public boolean canAccess(User user) {
        return true;
    }

    /**
     * Add new CSS file to list
     */
    public void addCss(String cssPath) {
        // Check CSS Path exists first
        if (cssPath == null || cssPath.isEmpty()) {
            logger.warn("CSS Path is empty or null");
            return;
        }
        if (getClass().getResource(cssPath) == null) {
            logger.warn("CSS Path does not exist in resource: " + cssPath);
            return;
        }
        cssPaths.add(cssPath);
    }

    /**
     * Remove CSS File from list
     */
    public void removeCss(String cssPath) {
        cssPaths.remove(cssPath);
    }

    protected void addBackAndHomeButtons(Pane previousRootPane) {
        // Create a StackPane to layer the button on top of the BorderPane
        StackPane stackPane = new StackPane();
        // make stack pane the root pane
        setRootPane(stackPane);
        // add border pane to stack pane
        stackPane.getChildren().add(previousRootPane);

        // Add back button
        addBackButton(stackPane);
        // Add home button
        addHomeButton(stackPane);

        // add CSS to stackpane
        this.addCss("/abstract.css");
        stackPane.getStyleClass().add("stackpane");
    }

    // method that adds a back button to the screen
    protected void addBackButton(StackPane stackPane) {
        // Make icon button
        ImageView img = new ImageView(new Image("/back.png"));
        img.preserveRatioProperty().set(true);
        img.setFitHeight(100);
        img.setFitWidth(100);

        // Make box for storing the image
        HBox box = new HBox(img);

        // on click, go back to the previous screen
        box.setOnMouseClicked(e -> {
            getScreenManager().goBack();
            logger.info("Back button clicked");
        });

        // Add it to the top left of the StackPane with absolute positioning (the latest
        // children are placed on top sequentially, back to front)
        StackPane.setAlignment(box, Pos.TOP_LEFT);
        stackPane.getChildren().add(box);

        // Load & Apply CSS
        this.addCss("/abstract.css");
        img.getStyleClass().add("back-button");
        box.getStyleClass().add("back-button-box");
    }

    // method that adds a home button to the screen
    protected void addHomeButton(StackPane stackPane) {
        // Make icon button
        ImageView img = new ImageView(new Image("/home.png"));
        img.preserveRatioProperty().set(true);
        img.setFitHeight(100);
        img.setFitWidth(100);

        // Make box for storing the image
        HBox box = new HBox(img);

        // on click, go back to the home screen
        box.setOnMouseClicked(e -> {
            getScreenManager().showScene(HomeScreen.class);
            logger.info("Home button clicked");
        });

        // Add it to the top right of the StackPane with absolute positioning (the
        // latest
        // children are placed on top sequentially, back to front)
        StackPane.setAlignment(box, Pos.TOP_RIGHT);
        stackPane.getChildren().add(box);

        // Load & Apply CSS
        this.addCss("/abstract.css");
        img.getStyleClass().add("home-button");
        box.getStyleClass().add("home-button-box");
    }

    protected void showScene(Class<? extends AbstractScreen> screenClass) {
        getApp().getScreenManager().showScene(screenClass);
    }

    protected void showSceneBetweenScenesThenNextScene(
        String msg,
        int waitTimeInSecs,
        Class<? extends AbstractScreen> nextScreenClass
    ) {
        try {
            // show the ScreenBetweenScreens screen
            this.showScene(ScreenBetweenScreens.class);

            // grab instance of ScreenBetweenScreens
            ScreenBetweenScreens screenBetweenScreens = (ScreenBetweenScreens) getScreenManager().getCurrentScreen();
            // run functionality after setup
            screenBetweenScreens.runFunctionalityAfterDisplayingScene(msg);

            // after N seconds of forced waiting on main thread (nothing happens), redirect
            // to whatever screen is specified
            PauseTransition pause = new PauseTransition(Duration.seconds(waitTimeInSecs));
            pause.setOnFinished(event -> {
                showScene(nextScreenClass);
            });
            pause.play();
        } catch (Exception e) {
            logger.error("Error in showSceneBetweenScenesThenNextScene: " + e.getMessage());
            logger.info("Redirecting to LoginScreen screen immediately");
            showScene(LoginScreen.class);
        }
    }
}
