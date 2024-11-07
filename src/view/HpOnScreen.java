package view;

import controller.HpControl;
import javax.swing.*;
import java.awt.*;

public class HpOnScreen extends JFrame {
	private int bearHp;
	private int tigerHp;

	// -----------------------------------------------------
	public HpOnScreen(int bearHp, int tigerHp) {
		this.bearHp = bearHp;
		this.tigerHp = tigerHp;
	}

	public void updateHpStatus(int bearHp, int tigerHp) {
		this.bearHp = bearHp;
		this.tigerHp = tigerHp;

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

		// 좌측 패널 bearHp개수 만큼 생성 및 이미지 추가
		for (int i = 1; i <= bearHp; i++) {
			JPanel panel = new JPanel();
			JLabel label = new JLabel();
			label.setIcon(new ImageIcon("src/assets/image/heart.jpg")); // 이미지 경로 설정
			panel.add(label);
			leftPanel.add(panel);
		}

		// 우측 패널 tigerHp개수 만큼 생성 및 이미지 추가
		for (int i = 1; i <= tigerHp; i++) {
			JPanel panel = new JPanel();
			JLabel label = new JLabel();
			label.setIcon(new ImageIcon("src/assets/image/heart.jpg")); // 이미지 경로 설정
			panel.add(label);
			rightPanel.add(panel);
		}

		setVisible(true);
	}

}
