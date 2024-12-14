package controller;

import model.characters.Characters;
import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviveController {

    public void handleFaint(Characters characters, JLabel playerLabel, JLabel assistingPlayerLabel, JLayeredPane layeredPane, PlayerInitializerPanel panel, boolean isBear) {
        // 기절 상태 설정
        if (characters.isFainted()) {
            return;
        }

        if (isBear) {
            panel.setBearFainted(true); // 곰 기절 처리
            panel.disableBearKeyListener();
        } else {
            panel.setTigerFainted(true); // 호랑이 기절 처리
            panel.disableTigerKeyListener();
        }

        setupFaintState(playerLabel, layeredPane);

        // 카운트다운 라벨 생성
        JLabel countdownLabel = createCountdownLabel(playerLabel, layeredPane);

        Timer assistTimer = new Timer(1000, new ActionListener() {
            private int countdown = 3;

            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--;
                countdownLabel.setText(String.valueOf(countdown));
                if (countdown == 0) {
                    revivePlayer(playerLabel, countdownLabel, layeredPane);
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        Timer reviveTimer = new Timer(3000, e -> {
            if (isBear) {
                panel.setBearFainted(false); // 곰 복구 처리
                panel.enableBearKeyListener();
            } else {
                panel.setTigerFainted(false); // 호랑이 복구 처리
                panel.enableTigerKeyListener();
            }
            ((Timer) e.getSource()).stop();
        });

        reviveTimer.start();

        // 상호작용 설정
        setupInteraction(assistingPlayerLabel, playerLabel, countdownLabel, assistTimer);
    }

    private void setupFaintState(JLabel playerLabel, JLayeredPane layeredPane) {
        playerLabel.setEnabled(false); // 플레이어 비활성화

        Point labelLocation = playerLabel.getLocation();
/*
        faintedCircle = new JLabel();
        faintedCircle.setOpaque(false);
        faintedCircle.setBounds(
                labelLocation.x - 20,
                labelLocation.y - 20,
                playerLabel.getWidth() + 40,
                playerLabel.getHeight() + 40
        );

        faintedCircle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // 검은 테두리
        layeredPane.add(faintedCircle, JLayeredPane.POPUP_LAYER);
        System.out.println("Circle Added to LayeredPane");
 */
        layeredPane.repaint();
    }

    public JLabel createCountdownLabel(JLabel playerLabel, JLayeredPane layeredPane) {
        JLabel countdownLabel = new JLabel("3", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 16));
        countdownLabel.setForeground(Color.RED); // 기본 색상: 빨강
        countdownLabel.setBounds(
                playerLabel.getX() + playerLabel.getWidth() / 2 - 10,
                playerLabel.getY() - 30,
                30,
                20
        );
        layeredPane.add(countdownLabel, JLayeredPane.DRAG_LAYER);
        return countdownLabel;
    }

    public void revivePlayer(JLabel playerLabel, JLabel countdownLabel, JLayeredPane layeredPane) {
        playerLabel.setEnabled(true); // 플레이어 활성화

        // 카운트다운 라벨 제거
        if (countdownLabel != null) {
            layeredPane.remove(countdownLabel);
        }

        layeredPane.repaint(); // 화면 갱신
    }


    private void setupInteraction(JLabel assistingPlayerLabel, JLabel playerLabel, JLabel countdownLabel, Timer assistTimer) {
        Timer interactionCheck = new Timer(100, new ActionListener() {
            private boolean isInside = false; // 플레이어가 영역 안에 있는지 여부

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean intersects = assistingPlayerLabel.getBounds().intersects(playerLabel.getBounds());
                if (intersects) {
                    if (!isInside) {
                        isInside = true;
                        assistTimer.start(); // 카운트다운 시작
                        countdownLabel.setForeground(Color.GREEN); // 도움받는 상태
                    }
                } else if (isInside) {
                    isInside = false;
                    assistTimer.stop(); // 카운트다운 중지
                    countdownLabel.setForeground(Color.RED); // 도움 중단
                }
            }
        });

        interactionCheck.start();
    }
}
