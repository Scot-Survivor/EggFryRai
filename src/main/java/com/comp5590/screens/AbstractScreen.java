package com.comp5590.screens;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;

@Getter
public abstract class AbstractScreen {

    private final App app;
    private final DatabaseManager databaseManager;
    private final ScreenManager screenManager;
    private final SessionFactory sessionFactory;

    @Setter
    private Pane rootPane;

    /**
     * The path to the CSS file for the screen
     */
    protected String cssPath;

    public AbstractScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.app = App.getInstance();
        this.databaseManager = DatabaseManager.getInstance();
        this.sessionFactory = this.databaseManager.getSessionFactory();
        this.setup();
    }

    /**
     * This method is called when the screen is created
     */
    public abstract void setup();

    /**
     * This method is called when the screen is shown, to ensure that the logged in user has access to the screen
     * @return true if the user has access to the screen
     */
    public boolean canAccess() {
        return true;
    }

    /**
     * This method is called when the screen is shown, to ensure that the logged in user has access to the screen
     * @param user object
     * @return true if the user has access to the screen
     */
    public boolean canAccess(User user) {
        return true;
    }
}
