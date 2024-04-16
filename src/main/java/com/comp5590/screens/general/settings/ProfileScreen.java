package com.comp5590.screens.general.settings;

import com.comp5590.components.ProfileScreen.UserInfoCard;
import com.comp5590.database.entities.User;
import com.comp5590.events.managers.EventManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import com.comp5590.security.managers.passwords.*;
import javafx.scene.layout.GridPane;

@AuthRequired
public class ProfileScreen extends AbstractScreen {

    private final SessionManager sessionManager = SessionManager.getInstance();
    private final PasswordManager passwordManager = PasswordManager.getInstance();
    private final EventManager eventManager = EventManager.getInstance();

    public ProfileScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        GridPane pane = attachDefaultPane();
        attachHeaderAndNavBar(2, "Your Profile");

        User user = SessionManager.getInstance().getCurrentUser();

        // make card
        UserInfoCard userInfoCard = new UserInfoCard(user);

        // add card to center of gridpane
        pane.add(userInfoCard, 0, 2);
        GridPane.setHalignment(userInfoCard, javafx.geometry.HPos.CENTER);
    }

    @Override
    public void cleanup() {}
}
