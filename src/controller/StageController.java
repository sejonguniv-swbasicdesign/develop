package controller;

import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import javax.swing.*;
import java.awt.*;

public class StageController {
    private JFrame stageFrame;
    private JPanel overlayPanel;
    private JLabel[] hpLabels;
    private ImageIcon fullHeartIcon;
    private ImageIcon emptyHeartIcon;
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;

    public StageController(JFrame stageFrame) {
        this.stageFrame = stageFrame;
        Storage storage = Storage.getInstance();
        this.bearPlayer = storage.getBear();
        this.tigerPlayer = storage.getTiger();

        initializeHpDisplay();
    }

    // 하트 이미지를 초기화
    private void initializeHpDisplay() {
        fullHeartIcon = new ImageIcon(new ImageIcon("src/assets/image/component/하트.png")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        emptyHeartIcon = new ImageIcon(new ImageIcon("src/assets/image/component/heart.jpg")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        JPanel hpPanel = new JPanel();
        hpPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        hpPanel.setBounds(10, 10, 150, 40); // 화면 상단 왼쪽
        hpPanel.setOpaque(false);

        hpLabels = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            hpLabels[i] = new JLabel(fullHeartIcon);
            hpPanel.add(hpLabels[i]);
        }

        stageFrame.add(hpPanel, JLayeredPane.PALETTE_LAYER);
    }

    public void updateHpDisplay() {
        int remainingHp = Math.min(3, Math.max(0, bearPlayer.hp + tigerPlayer.hp));
        for (int i = 0; i < 3; i++) {
            if (i < remainingHp) {
                hpLabels[i].setIcon(fullHeartIcon);
            } else {
                hpLabels[i].setIcon(emptyHeartIcon);
            }
        }

        if (remainingHp <= 0) {
            endStage(); // HP가 0이면 스테이지 종료
        }
    }

    private void endStage() {
        overlayPanel = new JPanel();
        overlayPanel.setBounds(0, 0, stageFrame.getWidth(), stageFrame.getHeight());
        overlayPanel.setBackground(new Color(0, 0, 0, 150)); // 어두운 투명 배경
        overlayPanel.setLayout(new GridBagLayout());

        JButton restartButton = new JButton("다시 시작하시겠습니까?");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.addActionListener(e -> restartStage());
        overlayPanel.add(restartButton);

        stageFrame.add(overlayPanel, JLayeredPane.MODAL_LAYER);
        stageFrame.repaint();
    }

    private void restartStage() {
        Storage storage = Storage.getInstance();
        bearPlayer.hp = 3;
        tigerPlayer.hp = 3;

        stageFrame.remove(overlayPanel);
        stageFrame.repaint();

        initializeHpDisplay();
    }
}
