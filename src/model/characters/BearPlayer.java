package model.characters;

import javax.swing.*;

public class BearPlayer extends Character {

    private ImageIcon leftIcon;
    private ImageIcon rightIcon;

    public BearPlayer() {
    }

    public BearPlayer(int x, int y) {
        super(x, y);
        this.leftIcon = new ImageIcon("../image/곰_왼쪽.png");
        this.rightIcon = new ImageIcon("../image/곰.png");
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
