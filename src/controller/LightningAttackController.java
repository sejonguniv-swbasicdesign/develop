package controller;

import model.attacks.Lightning;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class LightningAttackController {
    private List<Lightning> lightnings; // 번개 리스트
    private Timer timer;
    private JLayeredPane panel;

    public LightningAttackController(int bossX, int bossY, BearPlayer bearPlayer, TigerPlayer tigerPlayer, JLayeredPane panel) {
        this.panel = panel;
        lightnings = new ArrayList<>();

        // 번개의 초기 위치를 반원 형태로 설정
        int radius = 100; // 반원의 반지름
        int numOfLightnings = 5; // 번개의 개수
        double angleStep = Math.PI / (numOfLightnings - 1); // 각 번개 간의 각도 차이

        for (int i = 0; i < numOfLightnings; i++) {
            double angle = angleStep * i; // 현재 번개의 각도
            int lightningX = bossX + (int) (Math.cos(angle) * radius); // 반원의 x 좌표
            int lightningY = bossY + (int) (Math.sin(angle) * radius); // 반원의 y 좌표
            Lightning lightning = new Lightning(lightningX, lightningY, 0, 5);
            lightnings.add(lightning);

            // 번개를 MODAL_LAYER에 추가
            panel.add(lightning, JLayeredPane.MODAL_LAYER);
        }

        // 번개 이동 및 충돌 처리
        timer = new Timer(50, e -> {
            for (Lightning lightning : new ArrayList<>(lightnings)) { // ConcurrentModification 방지
                lightning.move();

                // BearPlayer와 충돌 감지
                if (bearPlayer.getBounds().intersects(lightning.getBounds())) {
                    bearPlayer.decreaseHp(1);
                    removeLightning(lightning);
                }

                // TigerPlayer와 충돌 감지
                if (tigerPlayer.getBounds().intersects(lightning.getBounds())) {
                    tigerPlayer.decreaseHp(1);
                    removeLightning(lightning);
                }

                // 번개가 화면 밖으로 나가면 제거
                if (isOutOfBounds(lightning)) {
                    removeLightning(lightning);
                }
            }
        });

        timer.start();
    }

    private void removeLightning(Lightning lightning) {
        lightnings.remove(lightning);
        panel.remove(lightning);
        panel.repaint();
    }

    private boolean isOutOfBounds(Lightning lightning) {
        return lightning.getX() < 0 || lightning.getY() < 0 || lightning.getX() > panel.getWidth() || lightning.getY() > panel.getHeight();
    }

    public void stop() {
        timer.stop();
    }
}
