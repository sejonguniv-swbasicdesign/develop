package controller;

import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviveController {
    private JLabel faintedCircle;

    public void handleFaint(JLabel playerLabel, JLabel assistingPlayerLabel, JLayeredPane layeredPane, PlayerInitializerPanel panel) {
        // 기절 상태 설정
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
                    revivePlayer(playerLabel, faintedCircle, countdownLabel, layeredPane);
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        // 상호작용 설정
        setupInteraction(assistingPlayerLabel, faintedCircle, countdownLabel, assistTimer);
    }

    private void setupFaintState(JLabel playerLabel, JLayeredPane layeredPane) {
        System.out.println("setupFaintState called");

        playerLabel.setEnabled(false); // 플레이어 비활성화

        Point labelLocation = playerLabel.getLocation();
        System.out.println("Player Label Location: " + labelLocation);

        faintedCircle = new JLabel();
        faintedCircle.setOpaque(false);
        faintedCircle.setBounds(
                labelLocation.x - 20,
                labelLocation.y - 20,
                playerLabel.getWidth() + 40,
                playerLabel.getHeight() + 40
        );
        System.out.println("Circle Bounds (Before Adding): " + faintedCircle.getBounds());

        faintedCircle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // 검은 테두리
        layeredPane.add(faintedCircle, JLayeredPane.POPUP_LAYER);
        System.out.println("Circle Added to LayeredPane");
        layeredPane.repaint();
    }

    public JLabel createFaintCircle(JLabel playerLabel) {
        JLabel circle = new JLabel();
        circle.setOpaque(false); // 투명 배경
        circle.setBounds(
                playerLabel.getX() - 20,
                playerLabel.getY() - 20,
                playerLabel.getWidth() + 40,
                playerLabel.getHeight() + 40
        );
        circle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        return circle;
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

    public void revivePlayer(JLabel playerLabel, JLabel faintedCircle, JLabel countdownLabel, JLayeredPane layeredPane) {
        playerLabel.setEnabled(true); // 플레이어 활성화

        layeredPane.remove(faintedCircle);
        layeredPane.remove(countdownLabel);
        layeredPane.repaint();
    }

    private void setupInteraction(JLabel assistingPlayerLabel, JLabel faintedCircle, JLabel countdownLabel, Timer assistTimer) {
        Timer interactionCheck = new Timer(100, new ActionListener() {
            private boolean isInside = false; // 플레이어가 영역 안에 있는지 여부

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean intersects = assistingPlayerLabel.getBounds().intersects(faintedCircle.getBounds());
                if (intersects) {
                    if (!isInside) {
                        isInside = true;
                        assistTimer.start(); // 카운트다운 시작
                        countdownLabel.setForeground(Color.GREEN); // 도움받는 상태
                        System.out.println("Player assisting: Countdown started.");
                    }
                } else if (isInside) {
                    isInside = false;
                    assistTimer.stop(); // 카운트다운 중지
                    countdownLabel.setForeground(Color.RED); // 도움 중단
                    System.out.println("Player left the circle: Countdown stopped.");
                }
            }
        });

        interactionCheck.start();
    }
}
