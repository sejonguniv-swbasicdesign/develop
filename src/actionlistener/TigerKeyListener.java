package actionlistener;

import model.characters.TigerPlayer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TigerKeyListener implements KeyListener {
    private TigerPlayer tigerPlayer;

    public TigerKeyListener(TigerPlayer tigerPlayer) {
        this.tigerPlayer = tigerPlayer;
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

    @Override
    public void keyReleased(KeyEvent e) {
        // 사용하지 않음
    }
}
