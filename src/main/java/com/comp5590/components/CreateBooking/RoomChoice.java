package com.comp5590.components.CreateBooking;

import com.comp5590.database.entities.Room;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.LoggerManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;

public class RoomChoice extends VBox {

    @Getter
    private final ChoiceBox<String> choiceBox;

    private final Logger logger;

    /**
     * Create the doctor choice drop down menu
     */
    public RoomChoice(DatabaseManager db, HashMap<String, Room> roomMap) {
        logger = LoggerManager.getInstance().getLogger(RoomChoice.class);
        // Create the label for the doctor choice box
        Label roomLabel = new Label("Please select a room");
        VBox.setMargin(roomLabel, new Insets(10.0));
        choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(150.0);
        choiceBox.setId("roomChoiceBox"); // give the choice box an id

        // Grab a list of doctors
        List<Room> roomList = db.getAll(Room.class);

        // Attempt to add all doctors to the drop down. If fails then just print message and don't display and doctors
        try {
            for (Room room : roomList) {
                String roomName = room.getRoomNumber();
                choiceBox.getItems().add(roomName);
                roomMap.put(roomName, room);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
        }

        // create the VBox to store these items and then return it
        VBox doctorBox = new VBox(roomLabel, choiceBox);
        doctorBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        this.getChildren().add(doctorBox);
    }
}
