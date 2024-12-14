package controller;

import model.characters.Characters;
import utils.constants.PlayerState;
import view.component.third.PlayerInitializerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviveController {

    public void handleFaint(Characters characters, JLabel playerLabel, JLabel assistingPlayerLabel, JLayeredPane layeredPane, PlayerInitializerPanel panel, boolean isBear) {
        // 이미 기절 상태면
        if (characters.isFainted()) {
            return;
        }

        characters.setState(PlayerState.FAINTED);

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

        // 상호작용 설정
        setupInteraction(assistingPlayerLabel, playerLabel, countdownLabel, assistTimer);
    }

    private void setupFaintState(JLabel playerLabel, JLayeredPane layeredPane) {
        playerLabel.setEnabled(false); // 플레이어 비활성화
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
