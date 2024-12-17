package model.monsters;

import model.monsters.Monster;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TigerMonster extends Monster {

    private JLabel monsterLabel;
    private ScheduledExecutorService movementExecutor;
    private ScheduledExecutorService directionExecutor;
    private int direction = 1;
    private JPanel panel;
    private int panelWidth;
    private int panelHeight;
    private ImageIcon leftIcon;
    private ImageIcon rightIcon;
    private boolean isMoving = true;
    private long lastDirectionChangeTime = 0; // 방향 변경 시간을 추적

    public TigerMonster(int x, int y) {
        super(3, x, y);
    }

    // 몬스터 이미지 설정
    public void setMonster(int panelWidth, int panelHeight, JPanel panel) {
        // 왼쪽 방향
        ImageIcon originalLeftIcon = new ImageIcon("src/assets/image/stage2/tiger_monster_left.png");
        Image leftImage = originalLeftIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        leftIcon = new ImageIcon(leftImage);

        // 오른쪽 방향
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

    public void stopMoving() {
        this.isMoving = false;
        shutdownExecutors();
    }

    private void shutdownExecutors() {
        if (movementExecutor != null && !movementExecutor.isShutdown()) {
            movementExecutor.shutdownNow();
        }
        if (directionExecutor != null && !directionExecutor.isShutdown()) {
            directionExecutor.shutdownNow();
        }
    }

    @Override
    public void move() {
        if (isMoving) {
            Random random = new Random();

            // 방향 전환 스케줄러 (3초마다 랜덤 방향 전환)
            directionExecutor = Executors.newSingleThreadScheduledExecutor();
            directionExecutor.scheduleAtFixedRate(() -> {
                if (canChangeDirection()) {
                    direction = random.nextInt(2) == 0 ? -1 : 1;
                    lastDirectionChangeTime = System.currentTimeMillis(); // 방향 변경 시간 기록
                    SwingUtilities.invokeLater(this::updateMonsterIcon);
                }
            }, 0, 3, TimeUnit.SECONDS);

            // 움직임 스케줄러 (30ms마다 위치 업데이트)
            movementExecutor = Executors.newSingleThreadScheduledExecutor();
            movementExecutor.scheduleAtFixedRate(() -> {
                super.x += direction * 5;
                if (super.x < 0 || super.x > panelWidth - monsterLabel.getWidth()) {
                    direction *= -1;
                    SwingUtilities.invokeLater(this::updateMonsterIcon);
                }
                SwingUtilities.invokeLater(() -> {
                    monsterLabel.setLocation(super.x, monsterLabel.getY());
                    panel.repaint();
                });
            }, 0, 30, TimeUnit.MILLISECONDS);
        }
    }

    private void updateMonsterIcon() {
        if (direction == 1) {
            monsterLabel.setIcon(rightIcon);
        } else {
            monsterLabel.setIcon(leftIcon);
        }
    }

    private boolean canChangeDirection() {
        // 마지막 방향 변경 시간으로부터 0.5초가 지났을 경우에만 방향 변경 가능
        return System.currentTimeMillis() - lastDirectionChangeTime >= 500;
    }

    public void changeDirection(int newDirection) {
        if (newDirection == 1 || newDirection == -1) {
            if (canChangeDirection()) {
                direction = newDirection;
                lastDirectionChangeTime = System.currentTimeMillis(); // 방향 변경 시간 기록
                updateMonsterIcon();
            }
        } else {
            throw new IllegalArgumentException("Direction must be 1 (right) or -1 (left).");
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction){
        changeDirection(direction);
    }

    public JLabel getLabel() {
        return monsterLabel;
    }
}
