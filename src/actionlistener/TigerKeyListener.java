package actionlistener;

import model.characters.TigerPlayer;
import model.monsters.Boss;
import utils.constants.StageCoordination;

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
        if (tigerPlayer.isFainted()) {
            return;
        }

        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                moveWithBoundaryCheck(0, -10);
                tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y);
                break;
            case KeyEvent.VK_S:
                moveWithBoundaryCheck(0, 10);
                tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y);
                break;
            case KeyEvent.VK_A:
                moveWithBoundaryCheck(-10, 0);
                tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y);
                break;
            case KeyEvent.VK_D:
                moveWithBoundaryCheck(10, 0);
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

    private void moveWithBoundaryCheck(int deltaX, int deltaY) {
        // TigerPlayer의 현재 위치와 이동 후 위치를 계산
        int newX = tigerPlayer.x + deltaX;
        int newY = tigerPlayer.y + deltaY;

        // StageCoordination의 경계 내에 있는지 확인
        if (isWithinBoundary(newX, newY)) {
            tigerPlayer.move(deltaX, deltaY); // 이동
            tigerLabel.setLocation(tigerPlayer.x, tigerPlayer.y); // 라벨 위치 업데이트
        } else {
            System.out.println("호랑이가 이동할 수 없습니다. 경계를 벗어남!");
        }
    }

    private boolean isWithinBoundary(int x, int y) {
        // StageCoordination 좌표를 기반으로 경계 확인
        int minX = StageCoordination.CLOUD_FIRST_VERTEX.getX();
        int maxX = StageCoordination.CLOUD_SECOND_VERTEX.getX();
        int minY = StageCoordination.CLOUD_FIRST_VERTEX.getY();
        int maxY = StageCoordination.CLOUD_THIRD_VERTEX.getY();

        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

}
