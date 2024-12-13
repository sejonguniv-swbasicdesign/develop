package controller;

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
    private boolean isAttackInProgress;
    private PlayerInitializerPanel playerPanel;

    public BossAttackController(Boss boss, JLayeredPane layeredPane, JLabel bearLabel, JLabel tigerLabel, PlayerInitializerPanel playerPanel) {
        this.boss = boss;
        this.layeredPane = layeredPane;
        this.storage = Storage.getInstance();
        this.bearLabel = bearLabel;
        this.tigerLabel = tigerLabel;
        this.random = new Random();
        this.reviveController = new ReviveController();
        this.isAttackInProgress = false;
        this.playerPanel = playerPanel; // PlayerInitializerPanel 저장
    }

    public void startAttacks() {
        nextAttack(); // 첫 공격 시작
    }

    private void nextAttack() {
        if (isAttackInProgress) return;

        isAttackInProgress = true;

        int attackType = random.nextInt(3); // 0: 번개, 1: 토네이도, 2: 먹구름
        switch (attackType) {
            case 0 -> lightningAttack(() -> {
                isAttackInProgress = false;
                nextAttack(); // 다음 공격 실행
            });
            case 1 -> tornadoAttack(() -> {
                isAttackInProgress = false;
                nextAttack(); // 다음 공격 실행
            });
            case 2 -> {
                if (boss.isRageMode()) {
                    shadowCloudAttack(() -> {
                        isAttackInProgress = false;
                        nextAttack(); // 다음 공격 실행
                    });
                } else {
                    isAttackInProgress = false;
                    nextAttack(); // 분노 모드가 아닐 경우, 다음 공격 바로 실행
                }
            }
        }
    }

    private void tornadoAttack(Runnable onComplete) {
        // 토네이도 시작 위치 (스테이지 좌측 상단에서 시작)
        int startX = 50; // 스테이지 좌측 상단 X 좌표
        int startY = 100; // 스테이지 좌측 상단 Y 좌표

        // 토네이도 생성 및 초기화
        new TornadoAttackController(
                startX,
                startY,
                bearLabel,
                tigerLabel,
                layeredPane,
                100, // 토네이도 속도 감소를 위해 딜레이 증가
                onComplete, // 토네이도 종료 후 다음 공격 실행
                reviveController,
                playerPanel
        );
    }

    private void lightningAttack(Runnable onComplete) {
        int lightningCount = 5; // 번개의 개수
        int radius = 100;
        int centerX = boss.getBounds().x + boss.getBounds().width / 2;
        int centerY = boss.getBounds().y + boss.getBounds().height;

        JLabel[] lightningLabels = new JLabel[lightningCount];
        Timer lightningTimer = new Timer(50, null);

        for (int i = 0; i < lightningCount; i++) {
            double angle = Math.PI * i / (lightningCount - 1);
            int lightningX = (int) (centerX + radius * Math.cos(angle));
            int lightningY = centerY;

            JLabel lightning = new JLabel(new ImageIcon(
                    new ImageIcon("src/assets/image/icon/lightning.png")
                            .getImage()
                            .getScaledInstance(30, 100, Image.SCALE_SMOOTH)
            ));
            lightning.setBounds(lightningX - 15, lightningY, 30, 100);
            layeredPane.add(lightning, JLayeredPane.MODAL_LAYER);
            lightningLabels[i] = lightning;
        }

        lightningTimer.addActionListener(e -> {
            boolean allRemoved = true;
            for (int i = 0; i < lightningLabels.length; i++) {
                JLabel lightning = lightningLabels[i];
                if (lightning != null) {
                    lightning.setLocation(lightning.getX(), lightning.getY() + 10);

                    if (checkCollision(lightning, bearLabel)) {
                        handlePlayerFaint(storage.getBear(), bearLabel, tigerLabel);
                        layeredPane.remove(lightning);
                        lightningLabels[i] = null;
                    } else if (checkCollision(lightning, tigerLabel)) {
                        handlePlayerFaint(storage.getTiger(), tigerLabel, bearLabel);
                        layeredPane.remove(lightning);
                        lightningLabels[i] = null;
                    }

                    if (lightning.getY() > layeredPane.getHeight()) {
                        layeredPane.remove(lightning);
                        lightningLabels[i] = null;
                    }

                    if (lightningLabels[i] != null) {
                        allRemoved = false;
                    }
                }
            }

            if (allRemoved) {
                lightningTimer.stop();
                onComplete.run(); // 다음 공격 실행
            }
            layeredPane.repaint();
        });
        lightningTimer.start();
    }

    private void shadowCloudAttack(Runnable onComplete) {
        if (!boss.isRageMode()) {
            onComplete.run(); // 분노 모드가 아니면 다음 공격 실행
            return;
        }

        JLabel shadowCloud = new JLabel();
        shadowCloud.setBounds(boss.getBounds().x - 50, boss.getBounds().y, 200, 200);
        shadowCloud.setOpaque(true);
        shadowCloud.setBackground(new Color(0, 0, 0, 100)); // 반투명 검정색 배경
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
                    onComplete.run(); // 다음 공격 실행
                }
            }
        });

        Timer attackTimer = new Timer(5000, e -> {
            if (checkCollision(shadowCloud, bearLabel)) {
                handlePlayerFaint(storage.getBear(), bearLabel, tigerLabel);
            }
            if (checkCollision(shadowCloud, tigerLabel)) {
                handlePlayerFaint(storage.getTiger(), tigerLabel, bearLabel);
            }
        });

        attackTimer.setInitialDelay(0);
        attackTimer.setRepeats(false);
        attackTimer.start();
        countdownTimer.start();
    }



    private void handlePlayerFaint(BearPlayer player, JLabel playerLabel, JLabel assistingPlayerLabel) {
        playerLabel.setEnabled(false);
        playerLabel.setFocusable(false); // 포커스 제거
        reviveController.handleFaint(bearLabel, tigerLabel, layeredPane, playerPanel);
    }

    private void handlePlayerFaint(TigerPlayer player, JLabel playerLabel, JLabel assistingPlayerLabel) {
        playerLabel.setEnabled(false);
        playerLabel.setFocusable(false); // 포커스 제거
        reviveController.handleFaint(tigerLabel, bearLabel, layeredPane, playerPanel);
    }

    private boolean checkCollision(JLabel attack, JLabel player) {
        return attack.getBounds().intersects(player.getBounds());
    }

}
