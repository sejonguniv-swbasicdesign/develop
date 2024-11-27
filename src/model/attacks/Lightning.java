package model.attacks;

import javax.swing.*;
import java.awt.*;

public class Lightning extends JLabel {
    private int dx, dy;

    public Lightning(int x, int y, int dx, int dy, int width, int height) {
        this.dx = dx;
        this.dy = dy;

        // 번개 이미지 크기 설정
        ImageIcon originalIcon = new ImageIcon("src/assets/image/icon/lightning.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));

        setBounds(x, y, width, height);
    }

    public void move() {
        setLocation(getX() + dx, getY() + dy);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
