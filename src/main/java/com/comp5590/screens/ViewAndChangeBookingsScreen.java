package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.components.ViewAndChangeBookingsScreen.BookingCard;
import com.comp5590.components.global.ScrollerBox;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.User;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class ViewAndChangeBookingsScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(ViewAndChangeBookingsScreen.class);

    public ViewAndChangeBookingsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        this.addCss("/viewAndChangeBookingsScreen.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane gridPane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar("View / Change Appointments");

        // grab user from session
        User user = SessionManager.getInstance().getCurrentUser();

        // get all bookings from DB for user
        List<Booking> bookings = EntityUtils.getBookingsForUser(user);

        // if no bookings, display message
        if (bookings.isEmpty()) {
            // create new label
            Title noBookingsLabel = new Title(
                "No bookings found for user " + user.getFirstName() + " " + user.getSurName()
            );
            noBookingsLabel.setId("no-bookings-label"); // for testing

            // align it both vertically and horizontally
            noBookingsLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // add it to gridpane
            gridPane.add(noBookingsLabel, 0, 1);

            return;
        }

        // sort bookings in place, by time added (latest first)
        bookings.sort((a, b) -> a.getApptTime().compareTo(b.getApptTime()));

        // create new scrollable box
        ScrollPane scrollPane = new ScrollPane();

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

        // create a booking card for each booking
        for (Booking booking : bookings) {
            // create edit and delete buttons
            Button rescheduleAppointmentBtn = new Button("Reschedule");
            Button cancelAppointmentBtn = new Button("Cancel");

            // create a new booking card
            BookingCard bookingCard = new BookingCard(booking, rescheduleAppointmentBtn, cancelAppointmentBtn);

            // bind width to parent width
            bookingCard.prefWidthProperty().bind(scrollerBox.widthProperty());

            // add card to scroller box
            scrollerBox.getChildren().add(bookingCard);

            // bind the scroller box memory locations to the buttons, for later use
            rescheduleAppointmentBtn.setUserData(booking);
            cancelAppointmentBtn.setUserData(booking);

            // bind event handlers to the buttons
            rescheduleAppointmentBtn.setOnAction(e -> onEditButtonClicked(booking));
            cancelAppointmentBtn.setOnAction(e -> onDeleteButtonClicked(booking));
        }
    }

    // rescchedule appointment
    private void onEditButtonClicked(Booking booking) {
        // put the booking into EditBookingScreen using a setter
        ((EditBookingScreen) getScreenManager().getScreenInstance(EditBookingScreen.class)).setBookingToEdit(booking);

        // navigate to the edit booking screen
        this.showScene(EditBookingScreen.class);
    }

    // cancel appointment
    private void onDeleteButtonClicked(Booking booking) {
        // delete booking from the DB
        boolean worked = EntityUtils.deleteBooking(booking);

        // if deletion worked, refresh the screen
        if (worked) {
            this.refreshScene();
        } else {
            logger.error("Failed to delete booking: " + booking.getBookingId());
        }
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
