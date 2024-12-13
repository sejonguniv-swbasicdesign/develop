package view.container.frame.third;

import controller.BossAttackController;
import controller.PlayerAttackController;
import controller.ReviveController;
import model.Storage;
import model.monsters.Boss;
import view.container.frame.third.BossHpBar;
import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;

public class InitBossStage extends JFrame {
    private Dimension frameSize;
    private JLayeredPane layeredPane;
    private BossHpBar bossHpBar;
    private JLabel bossLabel; // 보스 라벨
    private PlayerInitializerPanel playerPanel;
    private BossAttackController bossAttackController;
    private ReviveController reviveController;
    private PlayerAttackController attackController;

    public InitBossStage() {
        frameSize = new Dimension(1300, 800);
        setLayout(null);
        initializeComponents();
        setupStage();
    }

    private void initializeComponents() {
        layeredPane = new JLayeredPane(); // layeredPane 초기화
        layeredPane.setBounds(0, 0, frameSize.width, frameSize.height);

        Storage storage = Storage.getInstance(); // Storage 인스턴스 가져오기
        Boss boss = storage.getBoss(); // Boss 객체 가져오기
        boss.setPosition(600, 150); // 보스 위치를 중앙 상단에서 약간 하단으로 이동

        // 배경 이미지 설정
        JLabel backgroundLabel = new JLabel(new ImageIcon(
                new ImageIcon("src/assets/image/background/boss_background.jpeg")
                        .getImage()
                        .getScaledInstance(frameSize.width, frameSize.height, Image.SCALE_SMOOTH)
        ));
        backgroundLabel.setBounds(0, 0, frameSize.width, frameSize.height);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER); // 배경 추가

        // 구름 이미지 설정
        JLabel cloudLabel = new JLabel(new ImageIcon(
                new ImageIcon("src/assets/image/component/cloud_stage.png")
                        .getImage()
                        .getScaledInstance((int) (frameSize.width * 1.2), (int) (frameSize.height * 1.0), Image.SCALE_SMOOTH)
        ));
        cloudLabel.setBounds(-50, 250, 1400, 500); // 구름 위치와 크기 조정
        layeredPane.add(cloudLabel, JLayeredPane.PALETTE_LAYER);

        // 보스 HP 바
        bossHpBar = new BossHpBar(boss);
        bossHpBar.setBounds(10, 10, 600, 30);
        layeredPane.add(bossHpBar, JLayeredPane.PALETTE_LAYER);

        // 보스 라벨 설정 및 이미지 크기 조정
        bossLabel = new JLabel();
        updateBossLabel(boss, bossLabel); // 보스 이미지 업데이트
        layeredPane.add(bossLabel, JLayeredPane.MODAL_LAYER);

        // 플레이어 초기화 패널
        playerPanel = new PlayerInitializerPanel(layeredPane, bossLabel, cloudLabel);
        playerPanel.setBounds(0, 0, frameSize.width, frameSize.height);
        layeredPane.add(playerPanel, JLayeredPane.MODAL_LAYER);

        // 컨트롤러 초기화
        bossAttackController = new BossAttackController(boss, layeredPane, playerPanel.getBearLabel(), playerPanel.getTigerLabel(), playerPanel);
        bossAttackController.startAttacks(); // 보스 공격 활성화
        reviveController = new ReviveController();
        attackController = new PlayerAttackController(storage.getBear(), storage.getTiger(), boss, layeredPane);

        // 보스 상태 업데이트
        setupBossStateListener(boss);
    }

    private void updateBossLabel(Boss boss, JLabel bossLabel) {
        int labelWidth = 150;
        int labelHeight = 150;

        // 보스 이미지를 라벨 크기에 맞춰 조정
        Image scaledImage = boss.getCurrentIcon().getImage()
                .getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
        bossLabel.setIcon(new ImageIcon(scaledImage));
        bossLabel.setBounds(boss.getBounds()); // Boss 클래스의 getBounds 사용
    }

    private void setupBossStateListener(Boss boss) {
        // 보스 상태 변화 리스너 등록
        boss.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "hp" -> {
                    bossHpBar.updateHp(); // HP 바 업데이트
                }
                case "rageMode" -> {
                    updateBossLabel(boss, bossLabel); // 분노 모드로 변경 시 이미지 업데이트
                    System.out.println("보스가 분노 모드로 전환되었습니다!");
                }
            }
            layeredPane.repaint(); // 화면 갱신
        });
    }

    private void setupStage() {
        setTitle("Boss Stage");
        setSize(frameSize);
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        this.add(layeredPane); // layeredPane을 프레임에 추가
        pack();
        setVisible(true);
    }

}
