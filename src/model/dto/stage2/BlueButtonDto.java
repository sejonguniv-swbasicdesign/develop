package model.dto.stage2;

import javax.swing.*;

public class BlueButtonDto {

    private boolean isBlueButtonPressed;
    private JLabel floorButton;
    private JLabel tigerPlayer;
    private JLabel bearPlayer;
    private JLabel wall;

    public BlueButtonDto(boolean isBlueButtonPressed, JLabel floorButton, JLabel wall,JLabel tigerPlayer, JLabel bearPlayer) {
        this.isBlueButtonPressed = isBlueButtonPressed;
        this.floorButton = floorButton;
        this.wall = wall;
        this.tigerPlayer = tigerPlayer;
        this.bearPlayer = bearPlayer;
    }
    public boolean isBlueButtonPressed() {
        return isBlueButtonPressed;
    }

    public JLabel getFloorButton() {
        return floorButton;
    }
    public JLabel getWall() {
        return wall;
    }
    public JLabel getTigerPlayer() {
        return tigerPlayer;
    }
    public JLabel getBearPlayer() {
        return bearPlayer;
    }
}
