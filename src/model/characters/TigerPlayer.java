package model.characters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TigerPlayer extends Character {
    private ImageIcon leftIcon;
    private ImageIcon rightIcon;

    private String leftFilePath = "./src/image/호랑이.png";
    private String rightFilePath = "./src/image/호랑이_오른쪽.png";

    public TigerPlayer() {
    }

    public TigerPlayer(int x, int y) throws IOException {
        super(x, y);
        this.leftIcon = new ImageIcon(ImageIO.read(new File(leftFilePath)).getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        this.rightIcon = new ImageIcon(ImageIO.read(new File(rightFilePath)).getScaledInstance(64, 64, Image.SCALE_SMOOTH));
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
