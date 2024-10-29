package view;
import controller.HpConrtol;
import javax.swing.*;
import java.awt.*;

public class HpOnScreen extends JFrame{
	public HpOnScreen() {
        setTitle("Image Panel Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // 좌측 상단 패널 그룹 생성
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1)); // 3개의 세로 레이아웃
        add(leftPanel, BorderLayout.WEST);

        // 우측 상단 패널 그룹 생성
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 1)); // 3개의 세로 레이아웃
        add(rightPanel, BorderLayout.EAST);

        // 좌측 패널 3개 생성 및 이미지 추가
        for (int i = 1; i <= 3; i++) {
            JPanel panel = new JPanel();
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon("./image/heart" + i + ".jpg")); // 이미지 경로 설정
            panel.add(label);
            leftPanel.add(panel);
        }

        // 우측 패널 3개 생성 및 이미지 추가
        for (int i = 1; i <= 3; i++) {
            JPanel panel = new JPanel();
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon("./image/heart" + (i + 3) + ".jpg")); // 이미지 경로 설정
            panel.add(label);
            rightPanel.add(panel);
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new HpOnScreen();
    }

}
