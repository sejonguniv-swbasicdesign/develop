package view.component.third;

import model.monsters.Boss;

import javax.swing.*;
import java.io.IOException;

public class BossInitializerPanel extends JPanel {

    private JLabel bossLabel;
    private Boss bossMonster;

    public BossInitializerPanel() {
        setLayout(null);
        setOpaque(false); // 배경 투명

        try {
            // 보스 몬스터 초기화
            bossMonster = new Boss(500, 550, -10);
            initializeBossLabel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeBossLabel() {
        bossLabel = new JLabel();
        bossLabel.setIcon(bossMonster.getBossIcon());

        // 보스 몬스터의 위치 및 크기 설정
        bossLabel.setBounds(bossMonster.x, bossMonster.y, 200, 200);
        add(bossLabel);
    }
}
