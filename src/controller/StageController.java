package controller;

import model.PlayerPosition;
import model.Storage;
import view.container.panel.third.TransparentPanel;

import javax.swing.*;
import java.awt.*;

public class StageController {
    private JFrame stageFrame;
    private JLayeredPane layeredPane;
    private JLabel[] hpLabels; // 하트를 표시할 라벨 배열
    private ImageIcon fullHeartIcon;
    private ImageIcon emptyHeartIcon;
    private JPanel overlayPanel; // 재시작 패널
    private JLabel countdownLabel;
    private Storage storage;
    private LightningAttackController lightningAttackController;

    public StageController(JFrame stageFrame, JLayeredPane layeredPane) {
        this.stageFrame = stageFrame;
        this.layeredPane = layeredPane;
        this.storage = Storage.getInstance();

        initializeHpDisplay();
        initializeCountdownLabel();
    }

    // HP 이미지를 초기화하고 화면에 표시
    private void initializeHpDisplay() {
        fullHeartIcon = new ImageIcon(new ImageIcon("src/assets/image/component/하트.png")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        emptyHeartIcon = new ImageIcon(new ImageIcon("src/assets/image/component/heart.jpg")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        JPanel hpPanel = new JPanel();
        hpPanel.setLayout(null);
        hpPanel.setBounds(10, 10, 150, 40); // 화면 상단 왼쪽
        hpPanel.setOpaque(false); // 배경 투명

        hpLabels = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            hpLabels[i] = new JLabel(fullHeartIcon);
            hpLabels[i].setBounds(i * 40, 0, 30, 30); // 하트 위치 설정
            hpPanel.add(hpLabels[i]);
        }

        layeredPane.add(hpPanel, JLayeredPane.POPUP_LAYER); // 더 높은 레이어에 추가
    }

    // HP가 감소할 때 하트 이미지를 업데이트
    public void updateHpDisplay() {
        int sharedHp = storage.getSharedHp(); // 공유 HP 가져오기
        for (int i = 0; i < 3; i++) {
            if (i < sharedHp) {
                hpLabels[i].setIcon(fullHeartIcon);
            } else {
                hpLabels[i].setIcon(emptyHeartIcon);
            }
        }

        if (sharedHp <= 0) {
            endStage(); // 공유 HP가 0이면 스테이지 종료
        }
    }

    // 스테이지 종료 처리
    public void endStage() {
        if (overlayPanel != null) return;

        // 번개 공격 중단
        if (lightningAttackController != null) {
            lightningAttackController.clearAllLightnings();
        }

        overlayPanel = new TransparentPanel(new Color(0, 0, 0));
        overlayPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        overlayPanel.setLayout(new GridBagLayout()); // 중앙 정렬

        JButton restartButton = new JButton("다시 시작하시겠습니까?");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setForeground(Color.BLACK);
        restartButton.setBackground(new Color(50, 50, 50));
        restartButton.setFocusPainted(false);

        restartButton.addActionListener(e -> restartStage());
        overlayPanel.add(restartButton);

        layeredPane.add(overlayPanel, JLayeredPane.DRAG_LAYER); // 최상위 레이어에 추가
        layeredPane.repaint();
    }

    // 스테이지를 재시작
    private void restartStage() {
        storage.resetSharedHp();

        // 플레이어 좌표 초기화
        storage.getBear().x = PlayerPosition.BEAR_START.getX();
        storage.getBear().y = PlayerPosition.BEAR_START.getY();
        storage.getTiger().x = PlayerPosition.TIGER_START.getX();
        storage.getTiger().y = PlayerPosition.TIGER_START.getY();

        // HP 이미지 초기화
        for (JLabel hpLabel : hpLabels) {
            hpLabel.setIcon(fullHeartIcon);
        }

        // 오버레이 제거
        layeredPane.remove(overlayPanel);
        overlayPanel = null;
        layeredPane.repaint();
    }

    // 번개 컨트롤러 설정
    public void setLightningAttackController(LightningAttackController controller) {
        this.lightningAttackController = controller;
    }

    private void initializeCountdownLabel() {
        countdownLabel = new JLabel();
        countdownLabel.setBounds(layeredPane.getWidth() - 400, 10, 160, 50); // 오른쪽 상단
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countdownLabel.setOpaque(true);
        countdownLabel.setBackground(Color.BLACK);
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        countdownLabel.setVisible(true); // 기본적으로 숨김

        layeredPane.add(countdownLabel, JLayeredPane.DRAG_LAYER);
    }

    public JLabel getCountdownLabel() {
        return countdownLabel;
    }
}
