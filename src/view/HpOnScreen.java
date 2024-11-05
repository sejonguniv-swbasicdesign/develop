package view;
import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import javax.swing.*;
import java.awt.*;

public class HpOnScreen extends JFrame{
	 private static Storage instance;
		private BearPlayer bearPlayer = instance.getBear();
		private TigerPlayer tigerPlayer = instance.getTiger(); // 곰 호랑이 플레이어 정보 받아오기
		
		
		int bearHeart=bearPlayer.hp;
		int tigerHeart=tigerPlayer.hp;
		
	public HpOnScreen() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // 좌측 상단 패널 그룹 생성
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1)); 
        add(leftPanel, BorderLayout.WEST);

        // 우측 상단 패널 그룹 생성
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 1)); 
        add(rightPanel, BorderLayout.EAST);

        // 좌측 패널 3개 생성 및 이미지 추가
        for (int i = 1; i <= bearHeart; i++) {
            JPanel panel = new JPanel();
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon("src/assets/image/heart.jpg")); // 이미지 경로 설정
            panel.add(label);
            leftPanel.add(panel);
        }

        // 우측 패널 3개 생성 및 이미지 추가
        for (int i = 1; i <= tigerHeart; i++) {
            JPanel panel = new JPanel();
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon("src/assets/image/heart.jpg")); // 이미지 경로 설정
            panel.add(label);
            rightPanel.add(panel);
        }

        setVisible(true);
    }



}
