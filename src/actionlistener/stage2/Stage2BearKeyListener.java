package actionlistener.stage2;

import model.characters.BearPlayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Stage2BearKeyListener implements KeyListener {
    private BearPlayer bearPlayer;
    private boolean isUpPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean hasJumped = false;

    public Stage2BearKeyListener(BearPlayer bearPlayer) {
        this.bearPlayer = bearPlayer;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 각 키 상태를 추적
        if (key == KeyEvent.VK_UP) {
            isUpPressed = true;
        }
        if (key == KeyEvent.VK_LEFT) {
            isLeftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            isRightPressed = true;
        }

        // 방향키 Up과 Left가 동시에 눌렸고 점프가 발생하지 않은 경우 왼쪽으로 점프
        if (isUpPressed && isLeftPressed && !hasJumped) {
            bearPlayer.jumpLeft(-130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
        }
        // 방향키 Up과 Right가 동시에 눌렸고 점프가 발생하지 않은 경우 오른쪽으로 점프
        else if (isUpPressed && isRightPressed && !hasJumped) {
            bearPlayer.jumpRight(130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
        }
        // 다른 키 입력 처리
        else {
            switch (key) {
                case KeyEvent.VK_UP:
                    bearPlayer.move(0, -10); // 위로 이동
                    break;
                case KeyEvent.VK_DOWN:
                    bearPlayer.move(0, 10);  // 아래로 이동
                    break;
                case KeyEvent.VK_LEFT:
                    bearPlayer.move(-10, 0); // 왼쪽으로 이동
                    break;
                case KeyEvent.VK_RIGHT:
                    bearPlayer.move(10, 0);  // 오른쪽으로 이동
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // 키가 해제되면 상태를 업데이트
        if (key == KeyEvent.VK_UP) {
            isUpPressed = false;
            hasJumped = false; // Up 키가 해제되면 점프 가능하도록 리셋
        }
        if (key == KeyEvent.VK_LEFT) {
            isLeftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            isRightPressed = false;
        }
    }

    public boolean getBearPlayerJump(){
        return hasJumped;
    }
}
