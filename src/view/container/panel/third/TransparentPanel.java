package view.container.panel.third;

import javax.swing.*;
import java.awt.*;

public class TransparentPanel extends JPanel {
    private Color backgroundColor;

    public TransparentPanel(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setOpaque(false); // 완전 투명 방지
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // 투명도 설정 (0.0 ~ 1.0)
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
        super.paintComponent(g);
    }
}
