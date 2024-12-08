package controller;

import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.monsters.Boss;

import javax.swing.*;

// BossAttackController 클래스
public class BossAttackController {
    private Timer timer;
    private JLayeredPane panel;
    private JLabel bossLabel;
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;
    private Boss boss;
    private int attackState; // 현재 공격 상태
    private int lightningCount; // 번개 공격 횟수
    private boolean isRageMode; // 분노 모드 여부
    private boolean isTornadoActive; // 토네이도 활성화 여부
    private StageController stageController;

    public BossAttackController(Boss boss, JLabel bossLabel, BearPlayer bearPlayer, TigerPlayer tigerPlayer, JLayeredPane panel, StageController stageController) {
        this.boss = boss;
        this.panel = panel;
        this.stageController = stageController;
        this.bossLabel = bossLabel; // 보스 JLabel 추가

        Storage storage = Storage.getInstance();
        this.bearPlayer = storage.getBear();
        this.tigerPlayer = storage.getTiger();

        this.attackState = 0;
        this.lightningCount = 0;
        this.isRageMode = false;
        this.isTornadoActive = false;

        startAttackCycle();
    }

    private void startAttackCycle() {
        timer = new Timer(3000, e -> {
            if (isTornadoActive) return; // 토네이도가 활성화 중이라면 대기

            if (!boss.isRageMode()) {
                // 일반 모드: 번개 3회 → 토네이도 1회
                if (lightningCount < 3) {
                    performLightningAttack();
                    lightningCount++;
                } else {
                    performTornadoAttack();
                    lightningCount = 0; // 번개 공격 횟수 초기화
                }
            } else {
                // 분노 모드: 번개 3회 → 토네이도 1회 → 먹구름(번개 3회 포함)
                switch (attackState) {
                    case 0: // 번개 공격 3회
                        if (lightningCount < 3) {
                            performLightningAttack();
                            lightningCount++;
                        } else {
                            attackState = 1; // 다음 공격으로 전환
                            lightningCount = 0;
                        }
                        break;
                    case 1: // 토네이도 공격
                        performTornadoAttack();
                        break;
                    case 2: // 먹구름 + 번개 공격
                        performDarkCloudAttack();
                        attackState = 0; // 공격 순환
                        break;
                }
            }
        });

        timer.start();
    }

    private void performLightningAttack() {
        int bossX = boss.x + 75;
        int bossY = boss.y + 75;
        LightningAttackController lightningController = new LightningAttackController(
                bossX,
                bossY,
                bearPlayer,
                tigerPlayer,
                panel,
                stageController
        );

        // StageController에 LightningAttackController 등록
        stageController.setLightningAttackController(lightningController);
    }

    private void performTornadoAttack() {
        int bossX = boss.x - 150; // 보스 왼쪽
        int bossY = boss.y; // 보스 Y 좌표
        TornadoAttackController tornadoController = new TornadoAttackController(
                bossX,
                bossY,
                bearPlayer,
                tigerPlayer,
                panel,
                stageController,
                () -> {
                    isTornadoActive = false; // 종료 시 플래그 해제
                    attackState = 2; // 먹구름 공격으로 전환
                }
        );
        isTornadoActive = true; // 토네이도 활성화 플래그 설정
    }

    private void performDarkCloudAttack() {
        int bossX = boss.x;
        int bossY = boss.y;

        DarkCloudAttackController darkCloudController = new DarkCloudAttackController(
                bossX,
                bossY,
                bearPlayer,
                tigerPlayer,
                panel,
                stageController
        );

        // 번개 3회 동시에 추가
        for (int i = 0; i < 3; i++) {
            performLightningAttack();
        }
    }

    public void checkBossRageMode() {
        if (boss.getHp() <= boss.getMaxHp() / 2) {
            bossLabel.setIcon(boss.getBossIcon());

            // UI 갱신
            SwingUtilities.invokeLater(() -> {
                panel.repaint();
                panel.revalidate();
            });

            if (!boss.isRageMode()) {
                boss.updateStateBasedOnHp(); // 보스 상태 갱신
            }
        }
    }

    public void resetAttackCycle() {
        if (timer != null) {
            timer.stop();
        }
        attackState = 0; // 공격 상태 초기화
        lightningCount = 0; // 번개 공격 횟수 초기화
        isTornadoActive = false; // 토네이도 비활성화
        startAttackCycle(); // 루틴 재시작
    }


    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }
}