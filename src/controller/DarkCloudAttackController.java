package controller;

import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class DarkCloudAttackController {
    private JLayeredPane panel;
    private JLabel darkCloud;
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private StageController stageController;
    private Storage storage;

    public DarkCloudAttackController(int x, int y, BearPlayer bearPlayer, TigerPlayer tigerPlayer, JLayeredPane panel, StageController stageController) {
        this.panel = panel;
        this.bearPlayer = bearPlayer;
        this.tigerPlayer = tigerPlayer;
        this.stageController = stageController;
        this.countdownLabel = stageController.getCountdownLabel();
        storage = Storage.getInstance();

        // 먹구름 생성
        initializeDarkCloud(x, y);

        // 카운트다운 시작
        startCountdown(() -> {
            if (isInsideDarkCloud(bearPlayer) || isInsideDarkCloud(tigerPlayer)) {
                handlePlayerCollision();
            }
            removeDarkCloud();
        });
    }

    // 먹구름 초기화
    private void initializeDarkCloud(int x, int y) {
        darkCloud = createDarkCloud(x, y);
        panel.add(darkCloud, JLayeredPane.MODAL_LAYER);

        // 카운트다운 라벨 설정
        countdownLabel.setText("5"); // 초기값
        countdownLabel.setBounds(panel.getWidth() - 150, 20, 100, 50); // 오른쪽 상단
        countdownLabel.setForeground(Color.RED);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        countdownLabel.setVisible(true);

        if (!panel.isAncestorOf(countdownLabel)) {
            panel.add(countdownLabel, JLayeredPane.POPUP_LAYER); // 라벨 추가
        }

        panel.revalidate();
        panel.repaint();
    }

    private JLabel createDarkCloud(int x, int y) {
        JLabel cloud = new JLabel();
        cloud.setOpaque(false);
        cloud.setBounds(x - 250, y - 250, 700, 500); // 가로로 긴 동그라미
        cloud.setIcon(createOvalIcon(700, 500, new Color(50, 50, 50, 150))); // 먹구름 이미지
        return cloud;
    }

    private Icon createOvalIcon(int width, int height, Color color) {
        Image image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillOval(0, 0, width, height); // 동그라미 그리기
        return new ImageIcon(image);
    }

    private void startCountdown(Runnable onComplete) {
        countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {
            int secondsRemaining = 5;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    countdownLabel.setText("Time: " + secondsRemaining + "s");
                    panel.revalidate();
                    panel.repaint();
                });

                if (secondsRemaining <= 0) {
                    countdownTimer.cancel();
                    SwingUtilities.invokeLater(() -> {
                        if (isInsideDarkCloud(bearPlayer) || isInsideDarkCloud(tigerPlayer)) {
                            handlePlayerCollision(); // 카운트다운 종료 후 HP 감소 확인
                        }
                        onComplete.run();
                    });
                }

                secondsRemaining--;
            }
        }, 0, 1000);
    }


    private void handlePlayerCollision() {
        boolean isHpReduced = false;

        if (isInsideDarkCloud(bearPlayer)) {
            if (bearPlayer.getHp() > 0) { // HP가 0보다 클 때만 감소
                bearPlayer.decreaseHp(3); // Bear 즉사 처리
                isHpReduced = true;
            }
        }

        if (isInsideDarkCloud(tigerPlayer)) {
            if (tigerPlayer.getHp() > 0) { // HP가 0보다 클 때만 감소
                tigerPlayer.decreaseHp(3); // Tiger 즉사 처리
                isHpReduced = true;
            }
        }

        if (isHpReduced) {
            stageController.updateHpDisplay(); // HP 이미지 갱신

            // 스테이지 종료 조건 확인
            if (storage.getSharedHp() <= 0) {
                stageController.endStage(); // 스테이지 종료 처리
            }
        }
    }


    private boolean isInsideDarkCloud(BearPlayer player) {
        Rectangle cloudBounds = darkCloud.getBounds();
        Rectangle playerBounds = player.getBounds();

        // 플레이어 중심 좌표가 먹구름 내부에 있는지 확인
        int playerCenterX = playerBounds.x + playerBounds.width / 2;
        int playerCenterY = playerBounds.y + playerBounds.height / 2;

        return cloudBounds.contains(playerCenterX, playerCenterY);
    }

    private boolean isInsideDarkCloud(TigerPlayer player) {
        Rectangle cloudBounds = darkCloud.getBounds();
        Rectangle playerBounds = player.getBounds();

        // 플레이어 중심 좌표가 먹구름 내부에 있는지 확인
        int playerCenterX = playerBounds.x + playerBounds.width / 2;
        int playerCenterY = playerBounds.y + playerBounds.height / 2;

        return cloudBounds.contains(playerCenterX, playerCenterY);
    }

    private void removeDarkCloud() {
        panel.remove(darkCloud);
        darkCloud = null;

        countdownLabel.setVisible(false);
        panel.revalidate();
        panel.repaint();
    }
}
