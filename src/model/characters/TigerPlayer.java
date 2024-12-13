package model.characters;

import model.Storage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TigerPlayer extends Characters {
    private ImageIcon leftIcon;
    private ImageIcon rightIcon;
    private ImageIcon attackIcon; // 공격 중 이미지 아이콘 추가

    private String leftFilePath = "src/assets/image/characters/호랑이.png";
    private String rightFilePath = "src/assets/image/characters/호랑이_오른쪽.png";
    private String attackFilePath = "src/assets/image/characters/호랑이_공격.png"; // 공격 이미지 파일 경로

    private JLabel tigerLabel; // 이미지 표시를 위한 JLabel

    public TigerPlayer(int x, int y) throws IOException {
        super(x, y);
        this.leftIcon = new ImageIcon(ImageIO.read(new File(leftFilePath)).getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        this.rightIcon = new ImageIcon(ImageIO.read(new File(rightFilePath)).getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        this.attackIcon = new ImageIcon(ImageIO.read(new File(rightFilePath)).getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    }

    public ImageIcon getCurrentIcon() {
        return isFacingRight ? rightIcon : leftIcon;
    }

    public void setOppositeDirection() {
        if(isFacingRight) {
            isFacingRight = false;
        } else {
            isFacingRight = true;
        }
    }

    public void setImage(String state) {
        switch (state) {
            case "normal":
                tigerLabel.setIcon(getCurrentIcon());
                break;
            case "attack":
                tigerLabel.setIcon(attackIcon);
                break;
            default:
                throw new IllegalArgumentException("Invalid state: " + state);
        }
    }

    public void decreaseHp(int damage) {
        Storage storage = Storage.getInstance();
        for (int i = 0; i < damage; i++) {
            storage.decreaseSharedHp(); // 공유 HP 감소
            super.hp--;
        }
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

    public Rectangle getBounds() {
        return new Rectangle(x, y, 100, 100);
    }

}
