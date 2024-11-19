package controller;

import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import view.container.panel.third.TransparentPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StageController {
    private JFrame stageFrame;
    private JLayeredPane layeredPane;
    private JLabel[] hpLabels; // 하트를 표시할 라벨 배열
    private ImageIcon fullHeartIcon;
    private ImageIcon emptyHeartIcon;
    private JPanel overlayPanel; // 재시작 패널
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;
    private Storage storage;
    private TransparentPanel transparentPanel;

    public StageController(JFrame stageFrame, JLayeredPane layeredPane) {
        storage = Storage.getInstance();
        this.stageFrame = stageFrame;
        this.layeredPane = layeredPane;
        Storage storage = Storage.getInstance();
        this.bearPlayer = storage.getBear();
        this.tigerPlayer = storage.getTiger();

        initializeHpDisplay();
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
    private void endStage() {
        if (overlayPanel != null) return; // 이미 종료 상태라면 중복 처리 방지

        overlayPanel = new TransparentPanel(new Color(0, 0, 0)); // 반투명 검정 배경
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
        layeredPane.repaint();
    }

    // 스테이지를 재시작
    private void restartStage() {
        storage.resetSharedHp(); // 공유 HP 초기화
        layeredPane.remove(overlayPanel); // 오버레이 제거
        overlayPanel = null;
        initializeHpDisplay();
        layeredPane.repaint();
    }
}
