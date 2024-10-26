package actionlistener;

import model.characters.BearPlayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BearKeyListener implements KeyListener {

    private BearPlayer bearPlayer;

    public BearKeyListener(BearPlayer bearPlayer) {
        this.bearPlayer = bearPlayer;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            // 방향키로 곰 움직임 처리
            case KeyEvent.VK_UP:
                bearPlayer.move(0, -1);  // 위로 이동
                break;
            case KeyEvent.VK_DOWN:
                bearPlayer.move(0, 1);   // 아래로 이동
                break;
            case KeyEvent.VK_LEFT:
                bearPlayer.move(-1, 0);  // 왼쪽으로 이동
                break;
            case KeyEvent.VK_RIGHT:
                bearPlayer.move(1, 0);   // 오른쪽으로 이동
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
