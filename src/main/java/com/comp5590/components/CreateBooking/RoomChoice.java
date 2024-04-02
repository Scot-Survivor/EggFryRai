package com.comp5590.components.CreateBooking;

import com.comp5590.database.entities.Room;
import com.comp5590.database.managers.DatabaseManager;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RoomChoice {

    /**
     * Create the doctor choice drop down menu
     * @return A VBox containing the required fields
     */
    public RoomChoice(DatabaseManager db, HashMap<String, Room> roomMap, VBox vbox) {
        // Create the label for the doctor choice box
        Label roomLabel = new Label("Please select a room");
        VBox.setMargin(roomLabel, new Insets(10.0));
        ChoiceBox<String> roomChoiceBox = new ChoiceBox<>();
        roomChoiceBox.setPrefWidth(150.0);
        roomChoiceBox.setId("roomChoiceBox"); // give the choice box an id

        // Grab a list of doctors
        List<Room> roomList = db.getAll(Room.class);

        // Attempt to add all doctors to the drop down. If fails then just print message and don't display and doctors
        try {
            for (Object room : roomList) {
                String roomName = ((Room) room).getRoomNumber();
                roomChoiceBox.getItems().add(roomName);
                roomMap.put(roomName, (Room) room);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // create the VBox to store these items and then return it
        VBox doctorBox = new VBox(roomLabel, roomChoiceBox);
        doctorBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        vbox.getChildren().add(doctorBox);
    }
}
