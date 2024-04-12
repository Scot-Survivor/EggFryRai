package com.comp5590.components.EditBooking;

import com.comp5590.database.entities.Room;
import com.comp5590.database.utils.EntityUtils;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class RoomChoice extends HBox {

    public RoomChoice(ChoiceBox<String> roomChoiceInput, Room defaultRoom, HashMap<String, Room> roomMap) {
        // import styling
        this.getStylesheets().add("/editBooking.css");

        // add styling
        this.getStyleClass().add("room-choice-box");

        // create label
        Label timeLabel = new Label("Please select a room");

        // grab all rooms from db
        List<Room> rooms = EntityUtils.getAllRooms();

        // create choice box
        roomChoiceInput.setId("room-choice");

        // add all rooms to the choice box
        for (Room room : rooms) {
            String roomIdentifier = String.format("%s_(%d)", room.getRoomNumber(), room.getRoomId());

            roomMap.put(roomIdentifier, room);

            roomChoiceInput.getItems().add(roomIdentifier);
        }

        // set the default value to the default room
        roomChoiceInput.setValue(String.format("%s_(%d)", defaultRoom.getRoomNumber(), defaultRoom.getRoomId()));

        // add label & choice box to the HBox
        this.getChildren().addAll(timeLabel, roomChoiceInput);
    }
}
