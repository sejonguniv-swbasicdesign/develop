package actionlistener;

import model.Storage;
import model.characters.TigerPlayer;
import model.monsters.Boss;
import utils.constants.StageCoordination;
import view.container.frame.AnimationFrame;
import view.container.panel.third.FadePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TigerKeyListener implements KeyListener {
    private final TigerPlayer tigerPlayer;
    private final JLabel tigerLabel;
    private final JLabel bossLabel;
    private final Boss boss;
    private boolean isMoving = false;
    private JLayeredPane panel;


    public TigerKeyListener(TigerPlayer tigerPlayer, JLabel tigerLabel, JLabel bossLabel, Boss boss, JLayeredPane layeredPane) {
        this.tigerPlayer = tigerPlayer;
        this.tigerLabel = tigerLabel;
        this.bossLabel = bossLabel;
        this.boss = boss;
        this.panel = layeredPane;
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

            new Thread(() -> {
                if (isMoving) return; // 이미 이동 중이면 중복 실행 방지
                isMoving = true;
                boss.decreaseHp(5); // 호랑이의 공격 데미지
                checkBossDefeated(); // 보스 상태 확인
                new Thread(() -> {
                    try {
                        for (int i = 0; i < 2; i++) {
                            bossLabel.setVisible(false);
                            Thread.sleep(200);
                            bossLabel.setVisible(true);
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
                try {
                    int originalX = tigerLabel.getX();
                    int originalY = tigerLabel.getY();

                    if (tigerPlayer.isFacingRight) {
                        for (int i = 0; i < 10; i++) {
                            tigerPlayer.setPosition(originalX + i, originalY);
                            tigerLabel.setLocation(originalX + i, originalY);
                            Thread.sleep(20);
                        }

                        for (int i = 10; i > 0; i--) {
                            tigerPlayer.setPosition(originalX + i, originalY);
                            tigerLabel.setLocation(originalX + i, originalY);
                            Thread.sleep(20);
                        }
                    } else {
                        for (int i = 0; i < 10; i++) {
                            tigerPlayer.setPosition(originalX - i, originalY);
                            tigerLabel.setLocation(originalX - i, originalY);
                            Thread.sleep(20);
                        }

                        for (int i = 10; i > 0; i--) {
                            tigerPlayer.setPosition(originalX - i, originalY);
                            tigerLabel.setLocation(originalX - i, originalY);
                            Thread.sleep(20);
                        }
                    }


                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    isMoving = false; // 이동이 끝나면 플래그 해제
                }
            }).start();
        } else {
//            System.out.println("보스와의 거리가 너무 멉니다.");
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

    private void checkBossDefeated() {
        if (Storage.getInstance().getBoss().getHp() <= 0) {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(panel);
            if (currentFrame != null) {
                FadePanel fadePanel = new FadePanel();
                fadePanel.setBounds(0, 0, currentFrame.getWidth(), currentFrame.getHeight());
                currentFrame.getLayeredPane().add(fadePanel, JLayeredPane.DRAG_LAYER);

                Timer fadeTimer = new Timer(100, e -> {
                    fadePanel.increaseAlpha();
                    if (fadePanel.isFullyOpaque()) {
                        ((Timer) e.getSource()).stop();
                        currentFrame.dispose(); // 현재 프레임 닫기
                        AnimationFrame frame = new AnimationFrame();
                        frame.setStage(4);
                    }
                });

                fadeTimer.start();
            }
        }
    }

}