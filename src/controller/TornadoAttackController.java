package controller;

import model.Storage;
import model.characters.Characters;
import utils.constants.PlayerState;
import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;

public class TornadoAttackController {
    private Timer moveTimer;
    private JLabel tornado;
    private JLayeredPane panel;
    private JLabel bearLabel;
    private JLabel tigerLabel;
    private ReviveController reviveController;
    private PlayerInitializerPanel playerInitializerPanel;

    public TornadoAttackController(int x, int y, JLabel bearLabel, JLabel tigerLabel,
                                   JLayeredPane panel, int speedDelay,
                                   ReviveController reviveController, PlayerInitializerPanel playerInitializerPanel) {
        this.bearLabel = bearLabel;
        this.tigerLabel = tigerLabel;
        this.panel = panel;
        this.reviveController = reviveController;
        this.playerInitializerPanel = playerInitializerPanel;

        // 토네이도 생성
        tornado = createTornado(x, y);
        panel.add(tornado, JLayeredPane.MODAL_LAYER);

        // 토네이도 이동 로직
        moveTimer = new Timer(speedDelay, e -> moveTornado());
        moveTimer.start(); // 토네이도 이동 시작
    }

    private JLabel createTornado(int x, int y) {
        JLabel tornado = new JLabel(new ImageIcon(
                new ImageIcon("src/assets/image/icon/토네이도.png")
                        .getImage()
                        .getScaledInstance(150, 150, Image.SCALE_SMOOTH) // 크기 조정
        ));
        tornado.setBounds(x, y, 150, 150); // 초기 위치 및 크기
        return tornado;
    }

    private void moveTornado() {
        Point location = tornado.getLocation();

        // 토네이도 이동 경로 (지그재그로 하단 이동)
        int newX = location.x + 5; // 오른쪽으로 이동
        int newY = location.y + 3; // 하단으로 천천히 이동

        tornado.setLocation(newX, newY);

        // BearPlayer와 충돌 처리
        if (!Storage.getInstance().getBear().isFainted() && checkCollision(tornado, bearLabel)) {
            handleCollision(bearLabel, Storage.getInstance().getBear(), true);
        }

        // TigerPlayer와 충돌 처리
        if (!Storage.getInstance().getTiger().isFainted() && checkCollision(tornado, tigerLabel)) {
            handleCollision(tigerLabel, Storage.getInstance().getTiger(), false);
        }

        // 화면 밖으로 나가면 제거
        if (newX > panel.getWidth() || newY > panel.getHeight()) {
            removeTornado();
        }

        panel.repaint();
    }

    private boolean checkCollision(JLabel tornado, JLabel playerLabel) {
        return tornado.getBounds().intersects(playerLabel.getBounds());
    }

    private void handleCollision(JLabel playerLabel, Characters playerObject, boolean isBear) {
        reviveController.handleFaint(playerObject, playerLabel, getOppositePlayerLabel(playerLabel),
                panel, playerInitializerPanel, isBear); // 기절 처리
    }

    private JLabel getOppositePlayerLabel(JLabel playerLabel) {
        // 플레이어 라벨에 따라 반대 플레이어 라벨 반환
        return playerLabel == bearLabel ? tigerLabel : bearLabel;
    }

    private void removeTornado() {
        panel.remove(tornado);
        panel.repaint();
        if (moveTimer != null) {
            moveTimer.stop();
        }
    }
}
