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
    private Timer throwRockTimer; // 던지기 애니메이션 타이머

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
                throwRock();
                isBigRock = false;
            }
        }
        if (key == KeyEvent.VK_LEFT) {
            isLeftPressed = true;
            if (isBigRock) {
                moveRock(-10);
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            isRightPressed = true;
            if (isBigRock) {
                moveRock(10);
            }
        }

        if (isUpPressed && isLeftPressed && !hasJumped && !isBigRock) {
            bearPlayer.jumpLeft(-130, 60);
            hasJumped = true;
            startJumpCooldown();
        } else if (isUpPressed && isRightPressed && !hasJumped && !isBigRock) {
            bearPlayer.jumpRight(130, 60);
            hasJumped = true;
            startJumpCooldown();
        } else {
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bearPlayer.move(-10, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    bearPlayer.move(10, 0);
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

    private void startJumpCooldown() {
        Timer jumpCooldownTimer = new Timer(900, e -> hasJumped = false);
        jumpCooldownTimer.setRepeats(false);
        jumpCooldownTimer.start();
    }

    private void moveRock(int dx) {
        int currentRockX = bigRock.getX();
        int currentRockY = bigRock.getY();
        bigRock.setLocation(currentRockX + dx, currentRockY);
    }

    public void throwRock() {
        if (throwRockTimer != null && throwRockTimer.isRunning()) {
            throwRockTimer.stop(); // 이미 실행 중인 타이머를 멈춤
        }

        double[] position = {bigRock.getX(), bigRock.getY()};
        double targetX = 500;
        double targetY = 40;

        int steps = 50;
        int delay = 10;

        double dx = (targetX - position[0]) / steps;
        double dy = (targetY - position[1]) / steps;

        throwRockTimer = new Timer(delay, null);
        throwRockTimer.addActionListener(e -> {
            position[0] += dx;
            position[1] += dy;

            if (Math.abs(position[0] - targetX) < Math.abs(dx) && Math.abs(position[1] - targetY) < Math.abs(dy)) {
                bigRock.setLocation((int) targetX, (int) targetY);
                throwRockTimer.stop();
            } else {
                bigRock.setLocation((int) position[0], (int) position[1]);
            }
        });

        throwRockTimer.start();
    }

    public void stopRockMovement() {
        if (throwRockTimer != null && throwRockTimer.isRunning()) {
            throwRockTimer.stop(); // 돌의 이동을 멈춤
        }
    }
}
