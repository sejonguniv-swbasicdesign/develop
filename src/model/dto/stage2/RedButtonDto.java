package model.dto.stage2;

import model.Storage;

import javax.swing.*;

public class RedButtonDto {

    private JLabel floorButton;
    private JLabel tigerPlayer;
    private JLabel bearPlayer;
    private JLabel rock1, rock2;
    private boolean isButtonPressed;
    private JLabel step;
    private Storage storage;

    public RedButtonDto(Storage storage, JLabel floorButton, JLabel tigerPlayer, JLabel bearPlayer, JLabel rock1, JLabel rock2 , JLabel step, boolean isButtonPressed) {
        this.storage = storage;
        this.floorButton = floorButton;
        this.tigerPlayer = tigerPlayer;
        this.bearPlayer = bearPlayer;
        this.rock1 = rock1;
        this.rock2 = rock2;
        this.step = step;
        this.isButtonPressed = isButtonPressed;
    }

    public JLabel getFloorButton() {
        return floorButton;
    }
    public JLabel getTigerPlayer() {
        return tigerPlayer;
    }
    public JLabel getBearPlayer() {
        return bearPlayer;
    }
    public JLabel getRock1() {
        return rock1;
    }
    public JLabel getRock2() {
        return rock2;
    }
    public JLabel getStep() {
        return step;
    }
    public boolean isButtonPressed() {
        return isButtonPressed;
    }
    public Storage getStorage() {
        return storage;
    }
}
