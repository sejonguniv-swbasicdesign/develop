package model.characters;

import model.Storage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BearPlayer extends Characters {

    private ImageIcon leftIcon;
    private ImageIcon rightIcon;

    private String leftFilePath = "src/assets/image/characters/곰_완쪽.png";
    private String rightFilePath = "src/assets/image/characters/곰.png";
    private boolean holdingRock;


    public BearPlayer(int x, int y) throws IOException {
        super(x, y);
        this.leftIcon = new ImageIcon(ImageIO.read(new File(leftFilePath)).getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        this.rightIcon = new ImageIcon(ImageIO.read(new File(rightFilePath)).getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    }

    public void decreaseHp(int damage) {
        Storage storage = Storage.getInstance();
        for (int i = 0; i < damage; i++) {
            storage.decreaseSharedHp(); // 공유 HP 감소
            super.hp--;
        }
    }

    public ImageIcon getCurrentIcon() {
        return isFacingRight ? rightIcon : leftIcon;
    }

    public void resetBearHp() {
        resetHp();
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;

        if (x > 0) {
            isFacingRight = true; // 오른쪽으로 이동 중
        } else if (x < 0) {
            isFacingRight = false; // 왼쪽으로 이동 중
        }

        if (this.x < 0) {
            this.x -= x;
        }

        if (this.y < 0) {
            this.y -= y;
        }
    }

    public boolean isHoldingRock() {
        return holdingRock;
    }

    public void pickUpRock() {
        this.holdingRock = true;
    }

    public void throwRock() {
        this.holdingRock = false;
    }

}