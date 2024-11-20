package view.container.frame.third;

import controller.LightningAttackController;
import controller.StageController;
import view.component.third.BossInitializerPanel;
import view.component.third.PlayerInitializerPanel;
import controller.BossAttackController;
import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.monsters.Boss;
import view.container.panel.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class InitBossStage extends JFrame {

    private Dimension frameSize;
    private JLayeredPane layeredPane;
    private ImagePanel backgroundImage;
    private ImageIcon cloudImage;
    private JLabel cloudLabel;
    private PlayerInitializerPanel playerInitializerPanel;
    private BossAttackController bossAttackController;
    private StageController stageController;
    private Boss boss;

    public InitBossStage() {
        frameSize = new Dimension(1300, 800);
        setLayout(null);
        initializeGameObjects();
        playerInitializerPanel = new PlayerInitializerPanel();
        layeredPane = new JLayeredPane(); // JLayeredPane 생성
        stageController = new StageController(this, layeredPane);
        setFrame();
    }

    private void initializeGameObjects() {
        try {
            boss = new Boss(500, 550, -10);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // 보스 공격 컨트롤러 초기화
        Storage storage = Storage.getInstance();
        bossAttackController = new BossAttackController(
                boss,
                storage.getBear(),
                storage.getTiger(),
                layeredPane,
                stageController
        );
    }

    private void setBackgroundImage() {
        backgroundImage = new ImagePanel();
        backgroundImage.setImage("src/assets/image/background/boss_background.jpeg");

        backgroundImage.setBounds(0, 0, this.getWidth(), this.getHeight());
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
