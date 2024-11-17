package model.characters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BearPlayer extends Character {

    private ImageIcon leftIcon;
    private ImageIcon rightIcon;

    private String leftFilePath = "src/assets/image/characters/곰_완쪽.png";
    private String rightFilePath = "src/assets/image/characters/곰.png";

    public BearPlayer() {
    }

    public BearPlayer(int x, int y) {
        super(x, y);
        this.leftIcon = new ImageIcon(leftFilePath);
        this.rightIcon = new ImageIcon(rightFilePath);
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
}
