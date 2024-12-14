package controller;

import controller.ReviveController;
import controller.TornadoAttackController;
import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.monsters.Boss;
import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BossAttackController {
    private final Boss boss;
    private final JLayeredPane layeredPane;
    private final Storage storage;
    private final Random random;
    private JLabel bearLabel;
    private JLabel tigerLabel;
    private ReviveController reviveController;
    private PlayerInitializerPanel playerPanel;

    public BossAttackController(Boss boss, JLayeredPane layeredPane, JLabel bearLabel, JLabel tigerLabel, PlayerInitializerPanel playerPanel) {
        this.boss = boss;
        this.layeredPane = layeredPane;
        this.storage = Storage.getInstance();
        this.bearLabel = bearLabel;
        this.tigerLabel = tigerLabel;
        this.random = new Random();
        this.reviveController = new ReviveController();
        this.playerPanel = playerPanel;
    }

    public void startAttacks() {
        // 공격 타이머 설정 (2초 간격으로 실행)
        Timer attackTimer = new Timer(2000, e -> executeRandomAttack());
        attackTimer.start(); // 타이머 시작
    }

    private void executeRandomAttack() {
        int attackType = random.nextInt(3); // 0: 번개, 1: 토네이도, 2: 먹구름
        ssk(attackType);
    }

    private void ssk(int attackType) {
        switch (attackType) {
            case 0 -> lightningAttack(); // 번개 공격
            case 1 -> tornadoAttack();   // 토네이도 공격
            case 2 -> {
                if (boss.isRageMode()) { // 분노 모드에서만 실행
                    shadowCloudAttack();
                } else {
                    ssk(random.nextInt(2));
                }
            }
        }
    }

    private void tornadoAttack() {
        int startX = 50; // 토네이도 시작 X 좌표
        int startY = 100; // 토네이도 시작 Y 좌표

        // 토네이도 생성
        new TornadoAttackController(
                startX,
                startY,
                bearLabel,
                tigerLabel,
                layeredPane,
                15, // 토네이도 이동 속도
                reviveController,
                playerPanel
        );
    }

    private void lightningAttack() {
        int lightningCount = 7; // 번개의 개수
        int minX = 280; // 번개 시작 X 좌표 최소값
        int maxX = 1000; // 번개 시작 X 좌표 최대값
        int startY = 0; // 번개 시작 Y 좌표

        JLabel[] lightningLabels = new JLabel[lightningCount];
        Timer lightningTimer = new Timer(40, null);

        // 번개 생성
        for (int i = 0; i < lightningCount; i++) {
            int lightningX = minX + random.nextInt(maxX - minX + 1);

            JLabel lightning = new JLabel(new ImageIcon(
                    new ImageIcon("src/assets/image/icon/lightning.png")
                            .getImage()
                            .getScaledInstance(30, 100, Image.SCALE_SMOOTH)
            ));
            lightning.setBounds(lightningX - 15, startY, 30, 100);
            layeredPane.add(lightning, JLayeredPane.MODAL_LAYER);
            lightningLabels[i] = lightning;
        }

        // 번개 이동 및 충돌 체크
        lightningTimer.addActionListener(e -> {
            boolean allRemoved = true; // 모든 번개가 제거되었는지 확인
            for (int i = 0; i < lightningLabels.length; i++) {
                JLabel lightning = lightningLabels[i];
                if (lightning != null) {
                    lightning.setLocation(lightning.getX(), lightning.getY() + 10); // 번개를 아래로 이동

                    // 충돌 체크
                    if (checkCollision(lightning, bearLabel)) {
                        handlePlayerFaint(storage.getBear(), bearLabel, tigerLabel);
                        layeredPane.remove(lightning);
                        lightningLabels[i] = null;
                    } else if (checkCollision(lightning, tigerLabel)) {
                        handlePlayerFaint(storage.getTiger(), tigerLabel, bearLabel);
                        layeredPane.remove(lightning);
                        lightningLabels[i] = null;
                    }

                    // 번개가 화면을 벗어나면 제거
                    if (lightning.getY() > layeredPane.getHeight()) {
                        layeredPane.remove(lightning);
                        lightningLabels[i] = null;
                    }

                    if (lightningLabels[i] != null) {
                        allRemoved = false;
                    }
                }
            }

            // 모든 번개가 제거되었으면 타이머 중지
            if (allRemoved) {
                lightningTimer.stop();
            }

            layeredPane.repaint(); // 화면 갱신
        });

        lightningTimer.start();
    }

    private void shadowCloudAttack() {
        JLabel shadowCloud = new JLabel();
        shadowCloud.setBounds(boss.getBounds().x - 50, boss.getBounds().y, 200, 200);
        shadowCloud.setOpaque(true);
        shadowCloud.setBackground(new Color(0, 0, 0, 100)); // 반투명 검정색
        layeredPane.add(shadowCloud, JLayeredPane.DRAG_LAYER);

        // 카운트다운 라벨 추가
        JLabel countdownLabel = new JLabel("5", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        countdownLabel.setForeground(Color.RED);
        countdownLabel.setBounds(
                shadowCloud.getX() + shadowCloud.getWidth() / 2 - 15,
                shadowCloud.getY() - 30,
                30, 30
        );
        layeredPane.add(countdownLabel, JLayeredPane.POPUP_LAYER);

        Timer countdownTimer = new Timer(1000, new ActionListener() {
            private int countdown = 5;

            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--;
                countdownLabel.setText(String.valueOf(countdown));
                if (countdown == 0) {
                    layeredPane.remove(shadowCloud);
                    layeredPane.remove(countdownLabel);
                    layeredPane.repaint();
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        countdownTimer.start();
    }

    private void handlePlayerFaint(BearPlayer player, JLabel playerLabel, JLabel assistingPlayerLabel) {
        playerLabel.setEnabled(false);
        playerLabel.setFocusable(false);
        reviveController.handleFaint(player, bearLabel, tigerLabel, layeredPane, playerPanel, true);
    }

    private void handlePlayerFaint(TigerPlayer player, JLabel playerLabel, JLabel assistingPlayerLabel) {
        playerLabel.setEnabled(false);
        playerLabel.setFocusable(false);
        reviveController.handleFaint(player, tigerLabel, bearLabel, layeredPane, playerPanel, false);
    }

    private boolean checkCollision(JLabel attack, JLabel player) {
        return attack.getBounds().intersects(player.getBounds());
    }
}
