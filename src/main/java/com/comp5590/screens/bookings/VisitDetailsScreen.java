package com.comp5590.screens.bookings;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.components.VisitDetailsScreen.VisitDetailsCard;
import com.comp5590.components.global.ScrollerBox;
import com.comp5590.database.entities.User;
import com.comp5590.database.entities.VisitDetails;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class VisitDetailsScreen extends AbstractScreen {

    private Logger logger = LoggerManager.getInstance().getLogger(VisitDetailsScreen.class);

    public VisitDetailsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // load css
        this.addCss("/css/visitDetails.css");

        GridPane gridPane = attachDefaultPane();
        attachHeaderAndNavBar("Details of Past Visits");

        User user = SessionManager.getInstance().getCurrentUser();

        // grab all visit details for every booking the user has made
        List<VisitDetails> allVisitDetails = EntityUtils.getAllVisitDetailsForUser(user);

        // if no visit details, display message
        if (allVisitDetails.isEmpty()) {
            // create new label
            Title noVisitDetailsLabel = new Title(
                "No visit details found for user " + user.getFirstName() + " " + user.getSurName()
            );
            noVisitDetailsLabel.setId("no-visit-details-label");

            // align it both vertically and horizontally
            noVisitDetailsLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // add it to gridpane
            gridPane.add(noVisitDetailsLabel, 0, 1);

            return;
        }

        // sort visit details by time added (latest first)
        allVisitDetails.sort((a, b) -> b.getTimeAdded().compareTo(a.getTimeAdded()));

        // grab all prescriptions, based on VisitDetails entity
        allVisitDetails.forEach(visitDetails -> {
            visitDetails.setPrescriptions(EntityUtils.getAllPrescriptionsForVisitDetails(visitDetails));
        });

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

        // loop through all visit details and create a card for each
        for (VisitDetails visitDetails : allVisitDetails) {
            // create a new visit details card
            VisitDetailsCard visitDetailsCard = new VisitDetailsCard(visitDetails);

            // bind width to parent width
            visitDetailsCard.prefWidthProperty().bind(scrollerBox.widthProperty());

            // add card to scroller box
            scrollerBox.getChildren().add(visitDetailsCard);
        }
    }

    @Override
    public void cleanup() {}
}
