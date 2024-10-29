package controller;

import javax.swing.*;
import java.awt.Image;
import java.util.Random;

//몬스터 자동으로 움직이게 하는 컨트롤러
public class MonsterMovementController {

    private JLabel monsterLabel;
    private Timer movementTimer;
    private Timer directionTimer;
    private int monsterX;
    private int direction = 1;
    private JPanel panel;
    private int panelWidth;
    private int panelHeight;
    private ImageIcon leftIcon;
    private ImageIcon rightIcon;

    public MonsterMovementController() {

    }

    //몬스터 이미지 설정 ( 몬스터 이미지 아직 없어서 일단 호랑이 이미지 사용함 )
    public void setMonster(int panelWidth, int panelHeight, JPanel panel) {
        //왼쪽 방향
        ImageIcon originalLeftIcon = new ImageIcon("src/assets/image/icon/tiger_left.png");
        Image leftImage = originalLeftIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        leftIcon = new ImageIcon(leftImage);

        //오른쪽 방향
        ImageIcon originalRightIcon = new ImageIcon("src/assets/image/icon/tiger_right.png");
        Image rightImage = originalRightIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        rightIcon = new ImageIcon(rightImage);

        monsterLabel = new JLabel(rightIcon);
        this.panel = panel;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        addMonsterToCenter();
        startMonsterMovement();
    }

    //몬스터 위치 임시로 가운데로
    private void addMonsterToCenter() {
        int monsterWidth = monsterLabel.getIcon().getIconWidth();
        int monsterHeight = monsterLabel.getIcon().getIconHeight();

        monsterX = (panelWidth - monsterWidth) / 2;
        int monsterY = (panelHeight - monsterHeight) / 2;

        monsterLabel.setBounds(monsterX, monsterY, monsterWidth, monsterHeight);
        panel.setLayout(null);
        panel.add(monsterLabel);
    }

    //몬스터 움직임
    private void startMonsterMovement() {
        //2초마다 랜덤으로 좌우로 방향 변화
        Random random = new Random();

        directionTimer = new Timer(2000, e -> {
            direction = random.nextInt(2) == 0 ? -1 : 1;
            updateMonsterIcon();
        });
        directionTimer.start();

        //자동으로 움직임
        movementTimer = new Timer(30, e -> {
            monsterX += direction * 5;
            if (monsterX < 0 || monsterX > panelWidth - monsterLabel.getWidth()) {
                direction *= -1;
                updateMonsterIcon();
            }
            monsterLabel.setLocation(monsterX, monsterLabel.getY());
            panel.repaint();
        });
        movementTimer.start();
    }

    //방향에 따라 이미지 변경
    private void updateMonsterIcon() {
        if (direction == 1) {
            monsterLabel.setIcon(rightIcon);
        } else {
            monsterLabel.setIcon(leftIcon);
        }
    }
}
