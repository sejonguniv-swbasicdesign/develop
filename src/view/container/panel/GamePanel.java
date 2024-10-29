package view.container.panel;

import controller.MonsterMovementController;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

//게임 패널 설정
public class GamePanel {

    private ImagePanel imagePanel;
    private Container container;
    private JPanel panel;

    private int panelWidth = 1500;
    private int panelHeight = 1000;
    private MonsterMovementController movementController;


    public GamePanel() {
        movementController = new MonsterMovementController();

    }

    public void setGamePanel(Container container) {
        this.container = container;

        imagePanel = new ImagePanel();
        imagePanel.setImage("src/assets/image/background/game_background.png");
        imagePanel.setSize(new Dimension(panelWidth, panelHeight));
        imagePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        imagePanel.setMinimumSize(new Dimension(panelWidth, panelHeight));
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(container.getSize());
        layeredPane.add(imagePanel, JLayeredPane.DEFAULT_LAYER);
        panel = new JPanel();
        panel.setSize(new Dimension(panelWidth, panelHeight));
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        panel.setMinimumSize(new Dimension(panelWidth, panelHeight));
        panel.setOpaque(false);

        movementController.setMonster(panelWidth,panelHeight,panel);
        layeredPane.add(panel, JLayeredPane.PALETTE_LAYER);
        container.add(layeredPane);

    }


}
