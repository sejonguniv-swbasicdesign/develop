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

    // 방향 수동 변경 메서드
    public void changeDirection(int newDirection) {
        if (newDirection == 1 || newDirection == -1) {
            direction = newDirection;
            updateMonsterIcon();
            Timer smoothMoveTimer = new Timer(30, null); // 30ms 간격으로 실행
            int targetDistance = 20 * direction; // 이동할 목표 거리
            int steps = 10; // 이동 단계를 설정
            int distancePerStep = targetDistance / steps; // 단계별 이동 거리
            int[] currentStep = {0}; // 현재 단계

            smoothMoveTimer.addActionListener(e -> {
                // 단계별로 이동
                if (currentStep[0] < steps) {
                    super.x += distancePerStep; // x 좌표를 단계별로 증가/감소
                    monsterLabel.setLocation(super.x, monsterLabel.getY());
                    panel.repaint();
                    currentStep[0]++;
                } else {
                    // 모든 단계가 완료되면 타이머 중지
                    ((Timer) e.getSource()).stop();
                }
            });

            smoothMoveTimer.start();
        } else {
            throw new IllegalArgumentException("Direction must be 1 (right) or -1 (left).");
        }
    }

    public int getDirection(){
        return direction;
    }
    public JLabel getLabel() {
        return monsterLabel;
    }
}
