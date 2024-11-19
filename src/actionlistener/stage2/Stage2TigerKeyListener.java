package actionlistener.stage2;

import model.characters.TigerPlayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Stage2TigerKeyListener implements KeyListener {
    private TigerPlayer tigerPlayer;
    private boolean isWPressed = false;
    private boolean isAPressed = false;
    private boolean isDPressed = false;
    private boolean hasJumped = false;



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
        }
        if (key == KeyEvent.VK_A) {
            isAPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            isDPressed = true;
        }

        // W와 A가 동시에 눌렸고 점프가 발생하지 않은 경우 왼쪽으로 점프
        if (isWPressed && isAPressed && !hasJumped) {
            tigerPlayer.jumpLeft(-130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
        }
        // W와 D가 동시에 눌렸고 점프가 발생하지 않은 경우 오른쪽으로 점프
        else if (isWPressed && isDPressed && !hasJumped) {
            tigerPlayer.jumpRight(130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
        }
        // 다른 키 입력 처리
        else {
            switch (key) {
                case KeyEvent.VK_W:
                    tigerPlayer.move(0, -10); // 위로 이동
                    break;
                case KeyEvent.VK_S:
                    tigerPlayer.move(0, 10);  // 아래로 이동
                    break;
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
            hasJumped = false; // W 키가 해제되면 점프 가능하도록 리셋
        }
        if (key == KeyEvent.VK_A) {
            isAPressed = false;
        }
        if (key == KeyEvent.VK_D) {
            isDPressed = false;
        }
    }
}
