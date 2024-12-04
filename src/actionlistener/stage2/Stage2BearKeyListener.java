package actionlistener.stage2;

import model.characters.BearPlayer;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Stage2BearKeyListener implements KeyListener {
    private BearPlayer bearPlayer;
    private boolean isUpPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean hasJumped = false;
    private boolean isPortal1;
    private boolean isPortal2;
    private boolean isBigRock = false;
    private JLabel bigRock;
    private Thread rockThrowThread;


    public Stage2BearKeyListener(BearPlayer bearPlayer) {
        this.bearPlayer = bearPlayer;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            isUpPressed = true;
            if (isPortal1) {
                bearPlayer.setPosition(950, 680);
            }
            if (isPortal2) {
                bearPlayer.setPosition(500, 480);
            }
            if (isBigRock) {
                throwRockCooldown();
                throwRock();
            }
        }
        if (key == KeyEvent.VK_LEFT) {
            isLeftPressed = true;

        }
        if (key == KeyEvent.VK_RIGHT) {
            isRightPressed = true;
        }

        if (isUpPressed && isLeftPressed && !hasJumped && !isBigRock) {
            bearPlayer.jumpLeft(-130, 60);
            hasJumped = true;
            startJumpCooldown();
        } else if (isUpPressed && isRightPressed && !hasJumped && !isBigRock) {
            bearPlayer.jumpRight(130, 60);
            hasJumped = true;
            startJumpCooldown();
        }

        else {
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bearPlayer.move(-10, 0);
                    if (isBigRock) {
                        moveRock(-10);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    bearPlayer.move(10, 0);
                    if (isBigRock) {
                        moveRock(10);
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            isUpPressed = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            isLeftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            isRightPressed = false;
        }
    }

    public void updatePortal1State(boolean isOverlapping) {
        this.isPortal1 = isOverlapping;
    }

    public void updatePortal2State(boolean isOverlapping) {
        this.isPortal2 = isOverlapping;
    }

    public void updateBigRockState(boolean isOverlapping, JLabel bigRock) {
        this.isBigRock = isOverlapping;
        this.bigRock = bigRock;
    }

    public boolean getBearPlayerJump() {
        return hasJumped;
    }

    private void throwRockCooldown() {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            isBigRock = false;
        }).start();
    }

    private void startJumpCooldown() {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1.5초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            hasJumped = false;
        }).start();
    }

    private void moveRock(int dx) {
        SwingUtilities.invokeLater(() -> {
            int currentRockX = bigRock.getX();
            int currentRockY = bigRock.getY();
            bigRock.setLocation(currentRockX + dx, currentRockY);
        });
    }

    public void throwRock() {

        // 기존 스레드가 실행 중인 경우 멈추고 새로운 스레드를 시작
        if (rockThrowThread != null && rockThrowThread.isAlive()) {
            rockThrowThread.interrupt(); // 이전 스레드를 중단
        }

        rockThrowThread = new Thread(() -> {
            try {
                double[] position = {bigRock.getX(), bigRock.getY()};
                double targetX = 500;
                double targetY = 40;

                int steps = 50;
                int delay = 10;

                double dx = (targetX - position[0]) / steps;
                double dy = (targetY - position[1]) / steps;

                for (int i = 0; i < steps; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        return; // 스레드가 중단되었으면 실행을 멈춤
                    }

                    position[0] += dx;
                    position[1] += dy;

                    int finalX = (int) position[0];
                    int finalY = (int) position[1];

                    SwingUtilities.invokeLater(() -> bigRock.setLocation(finalX, finalY));

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // 스레드가 인터럽트되었으면 종료
                        return;
                    }
                }

                SwingUtilities.invokeLater(() -> bigRock.setLocation((int) targetX, (int) targetY));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        rockThrowThread.start(); // 던지기 스레드 시작
    }

    public void stopRockMovement() {
        // 던지기 스레드를 중단
        if (rockThrowThread != null && rockThrowThread.isAlive()) {
            rockThrowThread.interrupt(); // 스레드 중단
        }
    }


}
