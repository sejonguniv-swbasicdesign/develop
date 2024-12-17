package view.container.panel.third;

import javax.swing.*;
import java.awt.*;

public class FadePanel extends JPanel {
    private float alpha = 0f; // 투명도 (0: 완전히 투명, 1: 완전히 불투명)

    public FadePanel() {
        setOpaque(false); // 기본 배경은 투명하게
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    // 투명도 증가 메서드
    public void increaseAlpha() {
        if (alpha < 1f) {
            alpha += 0.05f;
            repaint();
        }
    }

    // 완전히 불투명한지 확인하는 메서드
    public boolean isFullyOpaque() {
        return alpha >= 1f;
    }
}
