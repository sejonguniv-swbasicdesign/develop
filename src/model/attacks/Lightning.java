package model.attacks;

import javax.swing.*;
import java.awt.*;

public class Lightning extends JLabel {
    private int x, y; // 번개의 현재 위치
    private int dx, dy; // 번개의 이동 방향
    private String imagePath;

    public Lightning(int startX, int startY, int dx, int dy) {
        this.x = startX;
        this.y = startY;
        this.dx = dx;
        this.dy = dy;

        imagePath = "src/assets/image/icon/lightning.png";

        // 번개 이미지 설정
        setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        setBounds(x, y, 50, 50);
    }

    public void move() {
        x += dx;
        y += dy;
        setLocation(x, y);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}
