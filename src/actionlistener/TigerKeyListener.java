package actionlistener;

import model.characters.TigerPlayer;
import model.monsters.Boss;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TigerKeyListener implements KeyListener {
    private final TigerPlayer tigerPlayer;
    private final JLabel tigerLabel;
    private final JLabel bossLabel;
    private final Boss boss;

    public TigerKeyListener(TigerPlayer tigerPlayer, JLabel tigerLabel, JLabel bossLabel, Boss boss) {
        this.tigerPlayer = tigerPlayer;
        this.tigerLabel = tigerLabel;
        this.bossLabel = bossLabel;
        this.boss = boss;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 사용하지 않음
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                tigerPlayer.move(0, -10);
                tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y);
                break;
            case KeyEvent.VK_S:
                tigerPlayer.move(0, 10);
                tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y);
                break;
            case KeyEvent.VK_A:
                tigerPlayer.move(-10, 0);
                tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y);
                break;
            case KeyEvent.VK_D:
                tigerPlayer.move(10, 0);
                tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y);
                break;
            case KeyEvent.VK_Q: // 'Q' 키로 공격
                attackBoss();
                break;
        }
    }

    private void attackBoss() {
        if (tigerLabel.getBounds().intersects(bossLabel.getBounds())) {
            System.out.println("호랑이가 보스를 공격했습니다!");
            boss.decreaseHp(5); // 호랑이의 공격 데미지
        } else {
            System.out.println("보스와의 거리가 너무 멉니다.");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // 사용하지 않음
    }
}
