package controller;

import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.monsters.Boss;

import javax.swing.*;

public class BossAttackController {
    private Timer timer;
    private JLayeredPane panel;
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;
    private Boss boss;
    private int state; // 0: 번개 공격, 1: 다른 공격, 2: 기절

    public BossAttackController(Boss boss, BearPlayer bearPlayer, TigerPlayer tigerPlayer, JLayeredPane panel) {
        this.boss = boss;
        this.panel = panel;

        // Storage에서 플레이어 객체 가져오기
        Storage storage = Storage.getInstance();
        this.bearPlayer = storage.getBear();
        this.tigerPlayer = storage.getTiger();

        this.state = 0;

        startAttackCycle();
    }

    private void startAttackCycle() {
        timer = new Timer(3000, e -> { // 공격 주기를 3초로 단축
            switch (state) {
                case 0:
                    performLightningAttack();
                    break;
                case 1:
                    performFaintedAttack();
                    break;
                case 2:
                    performFaint();
                    break;
            }

            state = (state + 1) % 3; // 상태 순환
        });

        timer.start();
    }

    private void performLightningAttack() {
        int bossX = boss.x + 75;
        int bossY = boss.y + 75;
        new LightningAttackController(bossX, bossY, bearPlayer, tigerPlayer, panel);
    }

    private void performFaintedAttack() {
    }

    private void performFaint() {
        boss.setFainted();
    }

    public void stop() {
        timer.stop();
    }
}
