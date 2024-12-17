package view.container.panel.third;

import utils.constants.ComponentSize;
import view.container.panel.ImagePanel;

import javax.swing.*;
import java.awt.*;

public class Background extends JPanel {

    private ImagePanel imagePanel;
    private JLabel cloudStage;
    private ImageIcon cloudIcon;
    private JLabel bossLabel;
    private ImageIcon bossIcon;

    private JLayeredPane layeredPane;
    private Container container;

    public void setContainer(Container container) {
        this.container = container;
    }

    public void setBackgroundPanel() {
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(container.getSize());

        setBackImagePanel();
        setCloudImagePanel();
        setBossImagePanel();

        container.add(layeredPane);

        container.revalidate();
        container.repaint();
    }

    private void setCloudImagePanel() {
        cloudStage = new JLabel();
        cloudIcon = new ImageIcon("src/assets/image/components/cloud_stage.png");

        int imageWidth = cloudIcon.getIconWidth();
        int imageHeight = cloudIcon.getIconHeight();
        Image originalImage = cloudIcon.getImage();
        int newWidth = (imageWidth * 3);
        int newHeight = (int) (imageHeight * 2.5);
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        cloudStage.setIcon(scaledIcon);
        cloudStage.setBounds(-135, -200, newWidth, newHeight);

        layeredPane.add(cloudStage, JLayeredPane.PALETTE_LAYER);
    }

    private void setBackImagePanel() {
        imagePanel = new ImagePanel();

        imagePanel.setOpaque(true);
        imagePanel.setImage("src/assets/image/background/boss_stage_back.png");

        imagePanel.setBounds(0, 0, ComponentSize.BOSSSTAGEWIDTH.getSize(), ComponentSize.BOSSSTAGEHEIGHT.getSize());
        imagePanel.setSize(ComponentSize.BOSSSTAGEHEIGHT.getBossDimension());
        imagePanel.setMinimumSize(ComponentSize.BOSSSTAGEHEIGHT.getBossDimension());
        imagePanel.setPreferredSize(ComponentSize.BOSSSTAGEHEIGHT.getBossDimension());

        layeredPane.add(imagePanel, JLayeredPane.DEFAULT_LAYER);
    }

    private void setBossImagePanel() {
        bossLabel = new JLabel();
        bossIcon = new ImageIcon("src/assets/image/characters/천신_기본.png");

        int width = bossIcon.getIconWidth();
        int height = bossIcon.getIconHeight();
        Image originalImage = bossIcon.getImage();
        int newWidth = (width / 3);
        int newHeight = (height / 3);
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        bossLabel.setIcon(scaledIcon);
        bossLabel.setBounds(600, 5, newWidth, newHeight);

        layeredPane.add(bossLabel, JLayeredPane.POPUP_LAYER);
    }

}
