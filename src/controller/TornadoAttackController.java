package controller;

import model.Storage;
import model.characters.Characters;
import model.monsters.Boss;
import utils.constants.PlayerState;
import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TornadoAttackController {
    private Timer moveTimer;
    private JLabel tornado;
    private JLayeredPane panel;
    private boolean isTornadoFinished; // 토네이도 종료 상태 플래그
    private JLabel bearLabel;
    private JLabel tigerLabel;
    private ReviveController reviveController;
    private PlayerInitializerPanel playerInitializerPanel;

    public TornadoAttackController(int x, int y, JLabel bearLabel, JLabel tigerLabel,
                                   JLayeredPane panel, int speedDelay, Runnable onComplete,
                                   ReviveController reviveController, PlayerInitializerPanel playerInitializerPanel) {
        this.bearLabel = bearLabel;
        this.tigerLabel = tigerLabel;
        this.panel = panel;
        this.isTornadoFinished = false;
        this.reviveController = reviveController;
        this.playerInitializerPanel = playerInitializerPanel;

        // 토네이도 생성
        tornado = createTornado(x, y);
        panel.add(tornado, JLayeredPane.MODAL_LAYER);

        // 토네이도 이동 로직
        moveTimer = new Timer(speedDelay, e -> {
            moveTornado(onComplete);
        });

        moveTimer.start(); // 토네이도 이동 시작
    }

    private JLabel createTornado(int x, int y) {
        JLabel tornado = new JLabel(new ImageIcon(
                new ImageIcon("src/assets/image/icon/토네이도.png")
                        .getImage()
                        .getScaledInstance(120, 120, Image.SCALE_SMOOTH) // 크기 조정
        ));
        tornado.setBounds(x, y, 120, 120); // 초기 위치 및 크기
        return tornado;
    }

    private void moveTornado(Runnable onComplete) {
        Point location = tornado.getLocation();

        // 토네이도 이동 경로 (지그재그로 하단 이동)
        int newX = location.x + 5; // 오른쪽으로 이동
        int newY = location.y + 3; // 하단으로 천천히 이동

        tornado.setLocation(newX, newY);

        // BearPlayer와 충돌 처리
        if (!Storage.getInstance().getBear().isFainted() && checkCollision(tornado, bearLabel)) {
            handleCollision(bearLabel, Storage.getInstance().getBear());
        }

        // TigerPlayer와 충돌 처리
        if (!Storage.getInstance().getTiger().isFainted() && checkCollision(tornado, tigerLabel)) {
            handleCollision(tigerLabel, Storage.getInstance().getTiger());
        }

        // 화면 밖으로 나가면 제거
        if (newX > panel.getWidth() || newY > panel.getHeight()) {
            removeTornado();
            onComplete.run(); // 다음 공격 실행
        }

        panel.repaint();
    }

    private boolean checkCollision(JLabel tornado, JLabel playerLabel) {
        return tornado.getBounds().intersects(playerLabel.getBounds());
    }

    private void handleCollision(JLabel playerLabel, Characters playerObject) {
        System.out.println(playerLabel.getName() + "와 토네이도 충돌");

        // 이미 기절한 상태인지 확인
        if (playerObject.isFainted()) {
            System.out.println(playerLabel.getName() + "는 이미 기절 상태입니다.");
            return; // 이미 기절한 상태라면 처리 중단
        }

        playerObject.setState(PlayerState.FAINTED); // 기절 상태로 설정
        reviveController.handleFaint(playerLabel, getOppositePlayerLabel(playerLabel),
                panel, playerInitializerPanel); // 기절 처리
    }

    private JLabel getOppositePlayerLabel(JLabel playerLabel) {
        // 플레이어 라벨에 따라 반대 플레이어 라벨 반환
        return playerLabel == bearLabel ? tigerLabel : bearLabel;
    }

    private void handleCollision(JLabel playerLabel) {
        System.out.println(playerLabel.getName() + "와 토네이도 충돌");

        // 플레이어 라벨 비활성화 및 포커스 제거
        playerLabel.setEnabled(false);
        playerLabel.setFocusable(false);

        // 기절 상태 설정
        JLabel faintedCircle = reviveController.createFaintCircle(playerLabel);
        panel.add(faintedCircle, JLayeredPane.MODAL_LAYER);

        // 카운트다운 라벨 추가
        JLabel countdownLabel = reviveController.createCountdownLabel(playerLabel, panel);
        panel.add(countdownLabel, JLayeredPane.POPUP_LAYER);

        Timer countdownTimer = new Timer(1000, new ActionListener() {
            private int countdown = 3;

            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--;
                countdownLabel.setText(String.valueOf(countdown));

                if (countdown == 0) {
                    reviveController.revivePlayer(playerLabel, faintedCircle, countdownLabel, panel);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        countdownTimer.start();
    }


    private void removeTornado() {
        panel.remove(tornado);
        isTornadoFinished = true;
        panel.repaint();
        if (moveTimer != null) {
            moveTimer.stop();
        }
    }
}
