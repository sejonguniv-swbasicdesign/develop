package view.container.frame.third;

import view.component.third.BossInitializerPanel;
import view.container.panel.ImagePanel;
import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;

public class InitBossStage extends JFrame {

    private Dimension frameSize;
    private JLayeredPane layeredPane;
    private ImagePanel backgroundImage;
    private ImageIcon cloudImage;
    private JLabel cloudLabel;
    private PlayerInitializerPanel playerInitializerPanel;

    public InitBossStage() {
        frameSize = new Dimension(1300, 800);
        playerInitializerPanel = new PlayerInitializerPanel();
        setFrame();
    }

    private void setFrame() {
        setTitle("Boss Stage");
        setSize(frameSize);
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setLocationRelativeTo(null);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, frameSize.width, frameSize.height);
        layeredPane.setPreferredSize(frameSize);

        setBackgroundImage();
        setCloudStage();
        setPlayerInitializer();
        setBossInitializer();

        this.add(layeredPane);
        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> playerInitializerPanel.requestFocusInWindow());
    }

    private void setBackgroundImage() {
        backgroundImage = new ImagePanel();
        backgroundImage.setImage("src/assets/image/background/boss_background.jpeg");

        backgroundImage.setBounds(0, 0, this.getWidth(), this.getHeight());
        /*
        backgroundImage.setSize(frameSize);
        backgroundImage.setPreferredSize(frameSize);
        backgroundImage.setMinimumSize(frameSize);
*/
        layeredPane.add(backgroundImage, JLayeredPane.DEFAULT_LAYER);
    }

    private void setCloudStage() {
        cloudLabel = new JLabel();
        cloudImage = new ImageIcon("src/assets/image/component/cloud_stage.png");
        Image originalImage = cloudImage.getImage();

        int newWidth = (int) (cloudImage.getIconWidth() * 2.8);
        int newHeight = (int) (cloudImage.getIconHeight() * 2.7);
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        cloudLabel.setIcon(scaledIcon);
        cloudLabel.setBounds(-128, -250, newWidth, newHeight);

        layeredPane.add(cloudLabel, JLayeredPane.PALETTE_LAYER);
    }

    private void setPlayerInitializer() {
        playerInitializerPanel.setBounds(0, 0, frameSize.width, frameSize.height);
        layeredPane.add(playerInitializerPanel, JLayeredPane.MODAL_LAYER);

        playerInitializerPanel.setFocusable(true);
        playerInitializerPanel.requestFocusInWindow();
    }

    private void setBossInitializer() {
        BossInitializerPanel bossInitializerPanel = new BossInitializerPanel();
        bossInitializerPanel.setBounds(0, 0, frameSize.width, frameSize.height);

        layeredPane.add(bossInitializerPanel, JLayeredPane.MODAL_LAYER);
    }

}
