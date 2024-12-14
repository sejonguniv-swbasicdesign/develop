package controller.movement;

import model.Storage;

import javax.swing.*;

public class MoveInteraction {

    private Storage storage;

    public MoveInteraction() {
        storage = Storage.getInstance();
    }

    public void updateCharacterPositions(JLabel bearLabel, JLabel tigerLabel) {
        bearLabel.setIcon(storage.getBear().getCurrentIcon());
        tigerLabel.setIcon(storage.getTiger().getCurrentIcon());

        bearLabel.setLocation(storage.getBear().x, storage.getBear().y);
        tigerLabel.setLocation(storage.getTiger().x, storage.getTiger().y);
    }
}
