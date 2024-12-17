package actionlistener;

import model.Storage;
import model.characters.BearPlayer;
import controller.RockController;
import utils.constants.StageCoordination;
import view.container.frame.AnimationFrame;
import view.container.panel.third.FadePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BearKeyListener implements KeyListener {
    private final BearPlayer bearPlayer;
    private final JLabel bearLabel;
    private final RockController rockController;
    private JLabel heldRockLabel; // 곰이 들고 있는 돌 라벨
    private final JLabel bossLabel;
    private final JLayeredPane panel;

    public BearKeyListener(BearPlayer bearPlayer, JLabel bearLabel, RockController rockController, JLabel bossLabel, JLayeredPane panel) {
        this.bearPlayer = bearPlayer;
        this.bearLabel = bearLabel;
        this.rockController = rockController;
        this.bossLabel = bossLabel;
        this.panel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (bearPlayer.isFainted()) {
            return;
        }

        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                moveWithBoundaryCheck(0, -10);
                updateRockPosition(); // 돌 위치 업데이트
                break;
            case KeyEvent.VK_DOWN:
                moveWithBoundaryCheck(0, 10);
                updateRockPosition(); // 돌 위치 업데이트
                break;
            case KeyEvent.VK_LEFT:
                moveWithBoundaryCheck(-10, 0);
                updateRockPosition(); // 돌 위치 업데이트
                break;
            case KeyEvent.VK_RIGHT:
                moveWithBoundaryCheck(10, 0);
                updateRockPosition(); // 돌 위치 업데이트
                break;
            case KeyEvent.VK_SLASH: // '/' 키
                if (!bearPlayer.isHoldingRock()) {
                    pickUpRock(); // 돌 주우기
                } else {
                    throwRock(); // 돌 던지기
                }
                break;
        }
    }

    private void updateRockPosition() {
        // 돌을 곰과 함께 움직이도록 위치를 업데이트
        if (bearPlayer.isHoldingRock() && heldRockLabel != null) {
            heldRockLabel.setBounds(
                    bearLabel.getX() + bearLabel.getWidth() / 2 - 25, // 곰 중심에 돌 배치
                    bearLabel.getY(), // 곰의 위쪽에 배치
                    50, 50 // 돌 크기 유지
            );
        }
    }

    private void throwRock() {
        if (heldRockLabel == null) {
//            System.out.println("던질 돌이 없습니다!");
            return;
        }

//        System.out.println("곰이 돌을 던졌습니다!");

        Timer throwTimer = new Timer(30, new ActionListener() {
            private final int targetX = bossLabel.getX() + bossLabel.getWidth() / 2;
            private final int targetY = bossLabel.getY() + bossLabel.getHeight() / 2;

            @Override
            public void actionPerformed(ActionEvent e) {
                int currentX = heldRockLabel.getX();
                int currentY = heldRockLabel.getY();

                // 보스를 향해 이동 (간단한 선형 이동)
                int stepX = (targetX - currentX) / 10;
                int stepY = (targetY - currentY) / 10;

                heldRockLabel.setLocation(currentX + stepX, currentY + stepY);

                // 보스와 충돌 감지
                if (heldRockLabel.getBounds().intersects(bossLabel.getBounds())) {
//                    System.out.println("돌이 보스에게 명중했습니다!");
                    Storage.getInstance().getBoss().decreaseHp(10); // 보스 HP 감소
                    checkBossDefeated();
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
                    rockController.hideRock(heldRockLabel); // 스테이지에서 돌 숨기기
                    panel.remove(heldRockLabel);
                    panel.repaint();
                    heldRockLabel = null; // 돌 제거
                    bearPlayer.throwRock(); // 상태 초기화
                    ((Timer) e.getSource()).stop();
                }

                // 화면을 벗어나면 돌 제거
                if (currentX > panel.getWidth() || currentY > panel.getHeight()) {
                    System.out.println("돌이 화면을 벗어났습니다!");
                    panel.remove(heldRockLabel);
                    panel.repaint();
                    heldRockLabel = null; // 돌 제거
                    bearPlayer.throwRock(); // 상태 초기화
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        throwTimer.start();
    }

    private void pickUpRock() {
//        System.out.println("곰이 돌을 주우려 합니다...");
        JLabel closestRock = rockController.findClosestRock(bearLabel);

        if (closestRock != null && bearLabel.getBounds().intersects(closestRock.getBounds())) {
//            System.out.println("곰이 돌을 주웠습니다!");
            bearPlayer.pickUpRock();
            heldRockLabel = closestRock; // 돌 라벨 설정
//            rockController.hideRock(closestRock); // 스테이지에서 돌 숨기기
            updateRockPosition(); // 초기 위치 업데이트
            panel.add(heldRockLabel, JLayeredPane.DRAG_LAYER); // 돌을 곰과 함께 이동
        } else {
//            System.out.println("주울 돌이 없습니다!");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void moveWithBoundaryCheck(int deltaX, int deltaY) {
        // BearPlayer의 현재 위치와 이동 후 위치를 계산
        int newX = bearPlayer.x + deltaX;
        int newY = bearPlayer.y + deltaY;

        // StageCoordination의 경계 내에 있는지 확인
        boolean withinBoundary = isWithinBoundary(newX, newY);

        if (withinBoundary) {
            bearPlayer.move(deltaX, deltaY); // 이동
            updateRockPosition(); // 돌 위치 업데이트
        } else {
            System.out.println("곰이 이동할 수 없습니다. 경계를 벗어남!");
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
