package model.characters;

import javax.swing.*;
import java.awt.*;

public class TigerPlayer extends Character {
    private ImageIcon leftIcon;
    private ImageIcon rightIcon;

    public TigerPlayer() {
    }

    public TigerPlayer(int x, int y) {
        super(x, y);
        this.leftIcon = new ImageIcon("../image/호랑이.png");
        this.rightIcon = new ImageIcon("../image/호랑이_오른쪽.png");
    }

    public ImageIcon getCurrentIcon() {
        return isFacingRight ? rightIcon : leftIcon;
    }

    public void resetTigerHp() {
        super.resetHp();
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
