package actionlistener.stage2;

import model.characters.TigerPlayer;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Stage2TigerKeyListener implements KeyListener {
    private TigerPlayer tigerPlayer;
    private boolean isWPressed = false;
    private boolean isAPressed = false;
    private boolean isDPressed = false;
    private boolean hasJumped = false;
    private boolean isLadder;


    public Stage2TigerKeyListener(TigerPlayer tigerPlayer) {
        this.tigerPlayer = tigerPlayer;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 각 키 상태를 추적
        if (key == KeyEvent.VK_W) {
            isWPressed = true;
            if (isLadder && tigerPlayer.y > 800) {
                smoothMove(tigerPlayer, tigerPlayer.x, tigerPlayer.y - 200); // 부드럽게 위로 이동
                return;
            }
        }
        if (key == KeyEvent.VK_A) {
            isAPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            isDPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            if (isLadder && tigerPlayer.y < 800) {
                smoothMove(tigerPlayer, tigerPlayer.x, tigerPlayer.y + 200); // 부드럽게 아래로 이동
                return;
            }
        }

        // W와 A가 동시에 눌렸고 점프가 발생하지 않은 경우 왼쪽으로 점프
        if (isWPressed && isAPressed && !hasJumped) {
            tigerPlayer.jumpLeft(-130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
            startJumpCooldown();
        }
        // W와 D가 동시에 눌렸고 점프가 발생하지 않은 경우 오른쪽으로 점프
        else if (isWPressed && isDPressed && !hasJumped) {
            tigerPlayer.jumpRight(130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
            startJumpCooldown();
        }
        // 다른 키 입력 처리
        else {
            switch (key) {
                case KeyEvent.VK_A:
                    tigerPlayer.move(-10, 0); // 왼쪽으로 이동
                    break;
                case KeyEvent.VK_D:
                    tigerPlayer.move(10, 0);  // 오른쪽으로 이동
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // 키가 해제되면 상태를 업데이트
        if (key == KeyEvent.VK_W) {
            isWPressed = false;
        }
        if (key == KeyEvent.VK_A) {
            isAPressed = false;
        }
        if (key == KeyEvent.VK_D) {
            isDPressed = false;
        }
    }

    // 사다리와 호랑이의 겹침 여부 업데이트
    public void updateLadderState(boolean isOverlapping) {
        this.isLadder = isOverlapping;
    }

    public boolean getTigerPlayerJump(){
        return hasJumped;
    }

    //사다리 타고 올라가거나 내려갈때 부드럽게 이동
    private void smoothMove(TigerPlayer tigerPlayer, int targetX, int targetY) {
        new Thread(() -> {
            int currentX = tigerPlayer.x;
            int currentY = tigerPlayer.y;

            while (currentX != targetX || currentY != targetY) {
                // x와 y의 이동 방향 계산
                int stepX = (targetX > currentX) ? 2 : (targetX < currentX) ? -2 : 0;
                int stepY = (targetY > currentY) ? 2 : (targetY < currentY) ? -2 : 0;

                currentX += stepX;
                currentY += stepY;

                int finalX = currentX;
                int finalY = currentY;

                // GUI 업데이트는 스레드 안전하게 처리
                SwingUtilities.invokeLater(() -> tigerPlayer.setPosition(finalX, finalY));

                // 목표 위치에 도달한 경우 루프 종료
                if (currentX == targetX && currentY == targetY) break;

                try {
                    Thread.sleep(10); // 10ms 간격으로 실행
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
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

}
