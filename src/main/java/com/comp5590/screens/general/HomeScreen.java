package com.comp5590.screens.general;

import com.comp5590.components.HomeScreen.HugeImage;
import com.comp5590.components.LoginScreen.Title;
import com.comp5590.components.global.NotificationCard;
import com.comp5590.components.global.ScrollerBox;
import com.comp5590.database.entities.Notification;
import com.comp5590.database.entities.User;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class HomeScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(HomeScreen.class);

    public HomeScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load CSS
        this.addCss("/css/home.css");
        this.addCss("/css/global.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane gridPane = this.attachDefaultPane();

        // attach the header bar & navbar
        this.attachHeaderAndNavBar(1, "Home - GP Alpha");

        // grab user from session
        User user = SessionManager.getInstance().getCurrentUser();

        // get all notifications from DB for user
        List<Notification> notifications = EntityUtils.getAllNotificationsForUser(user);

        // if no notifications, display message
        if (notifications.isEmpty()) {
            // create new label
            Title noNotificationsLabel = new Title("No Notifications");
            HugeImage noNotificationsImage = new HugeImage("/images/homeBackground.jpg");

            // align both nodes vertically and horizontally
            noNotificationsLabel.setAlignment(javafx.geometry.Pos.TOP_CENTER);
            noNotificationsImage.setAlignment(javafx.geometry.Pos.CENTER);

            // add both nodes to gridpane
            gridPane.add(noNotificationsLabel, 0, 1);
            gridPane.add(noNotificationsImage, 0, 0);

            // set gap between nodes
            gridPane.setVgap(10);

            return;
        }

        // sort bookings in place, by time added (latest first)
        notifications.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        // create new scrollable box
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("scroll-pane");

        // add it to gridpane
        gridPane.add(scrollPane, 0, 1);
        // span 100% width and center it
        GridPane.setFillWidth(scrollPane, true);
        GridPane.setHgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        // create box for all visit details cards
        ScrollerBox scrollerBox = new ScrollerBox();
        // add style class of scroller box
        scrollerBox.getStyleClass().add("scroller-box");
        scrollerBox.setId("scroller-box");

        // bind width of scroller box to scroll pane, -10 to account for scroll bar
        scrollerBox.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));

        // add scroller box to scroll pane
        scrollPane.setContent(scrollerBox);

        // add all visit details cards to scroller box
        for (Notification notification : notifications) {
            NotificationCard notificationCard = getNotificationCard(notification);

            // add it to scroller box
            scrollerBox.getChildren().add(notificationCard);
        }
    }

    private NotificationCard getNotificationCard(Notification notification) {
        String markReadOrUnreadTxt = notification.isRead() ? "Mark Unread" : "Mark Read";

        // create 2 new buttons
        Button markReadOrUnreadButton = new Button(markReadOrUnreadTxt);
        Button deleteButton = new Button("Delete");

        // attach event handlers to buttons
        markReadOrUnreadButton.setOnAction(e -> markReadOrUnread(notification));
        deleteButton.setOnAction(e -> delete(notification));

        // create new visit details card
        return new NotificationCard(notification, markReadOrUnreadButton, deleteButton);
    }

    private void markReadOrUnread(Notification notification) {
        notification.setRead(!notification.isRead());

        EntityUtils.updateNotification(notification);

        // refresh scene
        this.refreshScene();
    }

    private void delete(Notification notification) {
        // delete notification
        EntityUtils.deleteNotification(notification);

        // refresh scene
        this.refreshScene();
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
