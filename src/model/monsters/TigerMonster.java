package model.monsters;

import model.Monster;

import javax.swing.*;
import java.awt.Image;
import java.util.Random;


public class TigerMonster extends Monster {

    private JLabel monsterLabel;
    private Timer movementTimer;
    private Timer directionTimer;

    private int direction = 1;
    private JPanel panel;
    private int panelWidth;
    private int panelHeight;
    private ImageIcon leftIcon;
    private ImageIcon rightIcon;

    public TigerMonster(int x, int y) {
        super(3,x,y);

    }


    //몬스터 이미지 설정
    public void setMonster(int panelWidth, int panelHeight, JPanel panel) {
        //왼쪽 방향
        ImageIcon originalLeftIcon = new ImageIcon("src/assets/image/stage2/tiger_monster_left.png");
        Image leftImage = originalLeftIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        leftIcon = new ImageIcon(leftImage);

        //오른쪽 방향
        ImageIcon originalRightIcon = new ImageIcon("src/assets/image/stage2/tiger_monster_right.png");
        Image rightImage = originalRightIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        rightIcon = new ImageIcon(rightImage);

        monsterLabel = new JLabel(rightIcon);
        this.panel = panel;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        addMonsterToCenter();
        move();
    }


    private void addMonsterToCenter() {
        int monsterWidth = monsterLabel.getIcon().getIconWidth();
        int monsterHeight = monsterLabel.getIcon().getIconHeight();

        monsterLabel.setBounds(super.x, super.y, monsterWidth, monsterHeight);
        panel.add(monsterLabel);
        panel.setComponentZOrder(monsterLabel, 0);
    }

    //몬스터 움직임
    @Override
    public void move() {

        //3초마다 랜덤으로 좌우로 방향 변화
        Random random = new Random();

        directionTimer = new Timer(3000, e -> {
            direction = random.nextInt(2) == 0 ? -1 : 1;
            updateMonsterIcon();
        });
        directionTimer.start();

        //자동으로 움직임
        movementTimer = new Timer(30, e -> {
            super.x += direction * 5;
            if (super.x < 0 || super.x > panelWidth - monsterLabel.getWidth()) {
                direction *= -1;
                updateMonsterIcon();
            }
            monsterLabel.setLocation(super.x, monsterLabel.getY());
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

    public JLabel getLabel() {
        return monsterLabel;
    }
}
