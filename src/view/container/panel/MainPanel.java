package view.container.panel;

import view.component.DesignedButton;
import view.container.frame.GameFrame;
import view.container.frame.Stage1Frame;

import javax.swing.*;
import java.awt.*;

public class MainPanel {

    private ImagePanel imagePanel;
    private Container container;
    private SettingPanel settingPanel;
    private JLayeredPane layeredPane;

    public MainPanel(SettingPanel settingPanel) {
        this.settingPanel = settingPanel;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void setMainPanel() {
        setImagePanel();
        setTitle();
        setButtons();
        container.add(layeredPane);
        container.revalidate();
        container.repaint();
    }

    private void setTitle(){
        JLabel title = new JLabel();
        title.setText("호남과 웅녀");
        title.setSize(500,70);
        title.setBounds(270,80, 500,70);

        title.setForeground(Color.WHITE);

        title.setFont(new Font("", Font.BOLD, 45));

        imagePanel.add(title);
    }

    private void setButtons(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10));
        buttonPanel.setOpaque(false);

        DesignedButton startButton = new DesignedButton();
        DesignedButton settingButton = new DesignedButton();
        startButton.setText("Start");
        settingButton.setText("Setting");

        startButton.setSize(60,40);
        settingButton.setSize(60,40);

        startButton.addActionListener(e->{
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(container);
            currentFrame.dispose();

            Stage1Frame stage1Frame = new Stage1Frame();
            stage1Frame.setStage1Frame();
//            GameFrame gameFrame = new GameFrame();
//            gameFrame.setVisible(true);
        });
        settingButton.addActionListener(e -> {
            container.removeAll();
            settingPanel.addSettingPanel();

        });

        buttonPanel.add(startButton);
        buttonPanel.add(settingButton);

        imagePanel.setLayout(null);

        buttonPanel.setSize(200, 100);
        int x = (imagePanel.getWidth() - buttonPanel.getWidth()) / 2;
        int y = (imagePanel.getHeight() - buttonPanel.getHeight()) / 2;
        buttonPanel.setBounds(x, y, 200, 100);

        layeredPane.add(buttonPanel);
        layeredPane.setComponentZOrder(buttonPanel,0);
    }

    private void setImagePanel() {
        this.imagePanel = new ImagePanel();
        imagePanel.setImage("src/assets/image/background/cave_background.jpg");

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(container.getSize());

        imagePanel.setBounds(0, 0, container.getWidth(), container.getHeight());

        imagePanel.setSize(new Dimension(800,630));
        imagePanel.setPreferredSize(new Dimension(800, 630));
        imagePanel.setMinimumSize(new Dimension(800, 630));
        layeredPane.add(imagePanel, JLayeredPane.DEFAULT_LAYER);

        JLabel bearTigerLabel = new JLabel();
        ImageIcon bearTigerIcon = new ImageIcon("src/assets/image/background/bear_tiger.png");

        int imageWidth = bearTigerIcon.getIconWidth();
        int imageHeight = bearTigerIcon.getIconHeight();
        bearTigerLabel.setIcon(bearTigerIcon);
        bearTigerLabel.setBounds(230, 200, imageWidth, imageHeight);

        layeredPane.add(bearTigerLabel, JLayeredPane.PALETTE_LAYER);

    }
}
