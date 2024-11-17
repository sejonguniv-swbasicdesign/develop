package view.container.frame.third;

import view.container.panel.ImagePanel;

import javax.swing.*;
import java.awt.*;

public class InitBossStage extends JFrame {

    private Dimension frameSize;
    private JLayeredPane layeredPane;
    private ImagePanel backgroundImage;
    private ImagePanel cloudStage;


    public InitBossStage() {
        frameSize = new Dimension(1300, 800);
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
        layeredPane.setPreferredSize(this.getSize());
        setBackgroundImage();
        setCloudStage();

        this.add(layeredPane);
        revalidate();
        repaint();
    }

    private void setBackgroundImage() {
        backgroundImage = new ImagePanel();
        backgroundImage.setImage("src/assets/image/background/boss_background.jpeg");

        backgroundImage.setBounds(0, 0, this.getWidth(), this.getHeight());
        backgroundImage.setSize(frameSize);
        backgroundImage.setPreferredSize(frameSize);
        backgroundImage.setMinimumSize(frameSize);

        layeredPane.add(backgroundImage, JLayeredPane.DEFAULT_LAYER);
    }

    private void setCloudStage() {
        JLabel cloudLabel = new JLabel();
        ImageIcon cloudImage = new ImageIcon("src/assets/image/component/cloud_stage.png");
        Image originalImage = cloudImage.getImage();

        int newWidth = (int) (cloudImage.getIconWidth() * 2.8);
        int newHeight = (int) (cloudImage.getIconHeight() * 2.7);
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        cloudLabel.setIcon(scaledIcon);
        cloudLabel.setBounds(-128, -250, newWidth, newHeight);

        layeredPane.add(cloudLabel, JLayeredPane.PALETTE_LAYER);
    }


}