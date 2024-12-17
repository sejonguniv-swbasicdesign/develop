package view.container.panel.third;

import javax.swing.*;

public class BossPanel {

    private JLabel bossLabel;
    private ImageIcon bossImage;

    public JLabel getBossPanel() {
        bossImage = new ImageIcon("src/assets/image/characters/천신_기본.png");
        bossLabel = new JLabel(bossImage);

        int width = bossImage.getIconWidth();
        int height = bossImage.getIconHeight();
        bossLabel.setBounds(500, 10, width, height);

        return bossLabel;
    }
}
