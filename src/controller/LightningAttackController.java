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
    private StageController stageController; // StageController 참조
    private int attackPhase = 0; // 공격 단계 (0 또는 1)

    public LightningAttackController(int bossX, int bossY, BearPlayer bearPlayer, TigerPlayer tigerPlayer, JLayeredPane panel, StageController stageController) {
        this.panel = panel;
        this.stageController = stageController; // StageController 의존성 주입
        lightnings = new ArrayList<>();

        // 번개 공격 실행
        executeLightningAttack(bossX, bossY);

        // 번개 이동 및 충돌 처리
        timer = new Timer(30, e -> {
            for (Lightning lightning : new ArrayList<>(lightnings)) {
                lightning.move();

                // BearPlayer와 충돌 감지
                if (bearPlayer.getBounds().intersects(lightning.getBounds())) {
                    bearPlayer.decreaseHp(1);
                    stageController.updateHpDisplay(); // 하트 업데이트
                    removeLightning(lightning);
                }

                // TigerPlayer와 충돌 감지
                if (tigerPlayer.getBounds().intersects(lightning.getBounds())) {
                    tigerPlayer.decreaseHp(1);
                    stageController.updateHpDisplay(); // 하트 업데이트
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

    private void executeLightningAttack(int bossX, int bossY) {
        int radius = 150; // 반원의 반지름
        int numOfLightnings = 5; // 번개의 개수
        double angleStep = Math.PI / (numOfLightnings - 1);

        double startAngle = attackPhase == 0 ? 0 : angleStep / 2;

        for (int i = 0; i < numOfLightnings; i++) {
            double angle = startAngle + angleStep * i;
            int lightningX = bossX + (int) (Math.cos(angle) * radius);
            int lightningY = bossY + (int) (Math.sin(angle) * radius);
            Lightning lightning = new Lightning(lightningX, lightningY, 0, 10, 40, 40);
            lightnings.add(lightning);

            panel.add(lightning, JLayeredPane.MODAL_LAYER);
        }

        attackPhase = (attackPhase + 1) % 2;
    }

    private void removeLightning(Lightning lightning) {
        lightnings.remove(lightning);
        panel.remove(lightning);
        panel.repaint();
    }

    private boolean isOutOfBounds(Lightning lightning) {
        return lightning.getX() < 0 || lightning.getY() < 0 || lightning.getX() > panel.getWidth()
                || lightning.getY() > panel.getHeight();
    }

    public void stop() {
        timer.stop();
    }
}
