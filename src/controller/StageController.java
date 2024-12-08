package controller;

import model.PlayerPosition;
import model.Storage;
import model.monsters.Boss;
import view.container.panel.third.TransparentPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class StageController {
    private JFrame stageFrame;
    private JLayeredPane layeredPane;
    private JLabel[] hpLabels;
    private ImageIcon fullHeartIcon;
    private ImageIcon emptyHeartIcon;
    private JPanel overlayPanel;
    private JLabel countdownLabel;
    private Storage storage;
    private LightningAttackController lightningAttackController;
    private List<TornadoAttackController> tornadoControllers; // 토네이도 컨트롤러 관리
    private Boss boss;
    private JLabel bossLabel;
    private BossAttackController bossAttackController;
    private JLabel backgroundLabel;

    public StageController(JFrame stageFrame, JLayeredPane layeredPane) {
        this.stageFrame = stageFrame;
        this.layeredPane = layeredPane;
        this.storage = Storage.getInstance();
        this.tornadoControllers = new ArrayList<>();

        initializeHpDisplay();
        initializeCountdownLabel();
    }

    // HP 이미지를 초기화하고 화면에 표시
    private void initializeHpDisplay() {
        fullHeartIcon = new ImageIcon(new ImageIcon("src/assets/image/component/하트.png")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        emptyHeartIcon = new ImageIcon(new ImageIcon("src/assets/image/component/heart.jpg")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        JPanel hpPanel = new JPanel();
        hpPanel.setLayout(null);
        hpPanel.setBounds(10, 10, 150, 40);
        hpPanel.setOpaque(false);

        hpLabels = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            hpLabels[i] = new JLabel(fullHeartIcon);
            hpLabels[i].setBounds(i * 40, 0, 30, 30);
            hpPanel.add(hpLabels[i]);
        }

        layeredPane.add(hpPanel, JLayeredPane.POPUP_LAYER);
    }

    public void initializeBoss() {
        try {
            // 기존 보스 라벨 및 컨트롤러 제거
            if (bossLabel != null) {
                layeredPane.remove(bossLabel);
            }
            if (bossAttackController != null) {
                bossAttackController.stop();
            }

            // 새 보스 생성
            boss = new Boss(500, 550, -10);
            bossLabel = new JLabel(boss.getBossIcon());
            bossLabel.setBounds(boss.x, boss.y, 150, 150);
            layeredPane.add(bossLabel, JLayeredPane.PALETTE_LAYER);

            // 새로운 BossAttackController 생성
            bossAttackController = new BossAttackController(
                    boss,
                    bossLabel,
                    storage.getBear(),
                    storage.getTiger(),
                    layeredPane,
                    this
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // HP가 감소할 때 하트 이미지를 업데이트
    public void updateHpDisplay() {
        int sharedHp = storage.getSharedHp();
        for (int i = 0; i < 3; i++) {
            if (i < sharedHp) {
                hpLabels[i].setIcon(fullHeartIcon);
            } else {
                hpLabels[i].setIcon(emptyHeartIcon);
            }
        }

        if (sharedHp <= 0) {
            endStage();
        }
    }

    public void initializeBackground() {
        if (backgroundLabel != null) {
            layeredPane.remove(backgroundLabel);
        }

        backgroundLabel = new JLabel();
        ImageIcon backgroundImage = new ImageIcon("src/assets/image/component/cloud_stage.png");
        Image scaledImage = backgroundImage.getImage().getScaledInstance(1300, 800, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaledImage));
        backgroundLabel.setBounds(0, 0, 1300, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
    }

    // 스테이지 종료 처리
    public void endStage() {
        if (overlayPanel != null) return;

        // 모든 공격 컨트롤러 종료
        if (lightningAttackController != null) {
            lightningAttackController.clearAllLightnings();
        }
        if (bossAttackController != null) {
            bossAttackController.stop();
        }
        for (TornadoAttackController tornadoController : tornadoControllers) {
            tornadoController.stop();
        }
        tornadoControllers.clear();

        overlayPanel = new TransparentPanel(new Color(0, 0, 0));
        overlayPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        overlayPanel.setLayout(new GridBagLayout());

        JButton restartButton = new JButton("다시 시작하시겠습니까?");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setForeground(Color.BLACK);
        restartButton.setBackground(new Color(50, 50, 50));
        restartButton.setFocusPainted(false);

        restartButton.addActionListener(e -> restartStage());
        overlayPanel.add(restartButton);

        layeredPane.add(overlayPanel, JLayeredPane.DRAG_LAYER);
        layeredPane.repaint();
    }

    public void restartStage() {
        storage.resetSharedHp();

        // 플레이어 좌표 초기화
        storage.getBear().x = PlayerPosition.BEAR_START.getX();
        storage.getBear().y = PlayerPosition.BEAR_START.getY();
        storage.getTiger().x = PlayerPosition.TIGER_START.getX();
        storage.getTiger().y = PlayerPosition.TIGER_START.getY();

        // HP 초기화
        for (JLabel hpLabel : hpLabels) {
            hpLabel.setIcon(fullHeartIcon);
        }

        // 배경 및 보스 초기화
        initializeBackground();
        initializeBoss();

        // 오버레이 제거
        layeredPane.remove(overlayPanel);
        overlayPanel = null;
        layeredPane.repaint();
    }

    public void initializeCountdownLabel() {
        countdownLabel = new JLabel();
        countdownLabel.setBounds(layeredPane.getWidth() - 400, 10, 160, 50); // 오른쪽 상단
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countdownLabel.setOpaque(true);
        countdownLabel.setBackground(Color.BLACK);
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        countdownLabel.setVisible(true); // 기본적으로 숨김

        layeredPane.add(countdownLabel, JLayeredPane.DRAG_LAYER);
    }

    public JLabel getCountdownLabel() {
        return countdownLabel;
    }

    public void addTornadoController(TornadoAttackController controller) {
        tornadoControllers.add(controller);
    }

    public void setLightningAttackController(LightningAttackController controller) {
        this.lightningAttackController = controller;
    }

}