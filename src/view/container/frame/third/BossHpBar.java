package view.container.frame.third;

import model.monsters.Boss;

import javax.swing.*;
import java.awt.*;

public class BossHpBar extends JProgressBar {
    private Boss boss;

    public BossHpBar(Boss boss) {
        super(0, boss.getHp());
        this.boss = boss;
        setValue(boss.getHp());
        setStringPainted(true);
        setForeground(Color.RED);
    }

    public void updateHp() {
        setValue(boss.getHp());
    }
}
