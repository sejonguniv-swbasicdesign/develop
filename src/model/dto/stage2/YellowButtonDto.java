package model.dto.stage2;

import javax.swing.*;

public class YellowButtonDto {

    private boolean isButtonPressed;
    private JLabel floorButton;
    private JLabel tigerPlayer;
    private JLabel bearPlayer;
    private JLabel portal1;
    private JLabel portal2;

    public YellowButtonDto(boolean isButtonPressed, JLabel floorButton, JLabel tigerPlayer,JLabel bearPlayer, JLabel portal1, JLabel portal2) {
        this.isButtonPressed = isButtonPressed;
        this.floorButton = floorButton;
        this.tigerPlayer = tigerPlayer;
        this.bearPlayer = bearPlayer;
        this.portal1 = portal1;
        this.portal2 = portal2;
    }

    public boolean isButtonPressed() {
        return isButtonPressed;
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
    public JLabel getPortal1() {
        return portal1;
    }
    public JLabel getPortal2() {
        return portal2;
    }
}
