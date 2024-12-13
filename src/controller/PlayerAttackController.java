package controller;

import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.monsters.Boss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class PlayerAttackController {
    private BearPlayer bear;
    private TigerPlayer tiger;
    private Boss boss;
    private JLayeredPane layeredPane;
    private boolean bearHoldingStone = false;

    public PlayerAttackController(BearPlayer bear, TigerPlayer tiger, Boss boss, JLayeredPane layeredPane) {
        this.bear = bear;
        this.tiger = tiger;
        this.boss = boss;
        this.layeredPane = layeredPane;
        setupStoneSpawn();
    }

    private void setupStoneSpawn() {
        Timer stoneSpawnTimer = new Timer(5000, e -> {
            Random random = new Random();
            int x = random.nextInt(800) + 100;
            int y = random.nextInt(600) + 100;

            JLabel stone = new JLabel(new ImageIcon("src/assets/image/attacks/stone.png"));
            stone.setBounds(x, y, 50, 50);
            layeredPane.add(stone, JLayeredPane.PALETTE_LAYER);

            stone.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!bearHoldingStone) {
                        bearHoldingStone = true;
                        layeredPane.remove(stone);
                        layeredPane.repaint();
                    }
                }
            });
        });
        stoneSpawnTimer.start();
    }

    public void bearThrowStone() {
        if (!bearHoldingStone) return;

        JLabel stone = new JLabel(new ImageIcon("src/assets/image/attacks/stone.png"));
        stone.setBounds(bear.getBounds().x, bear.getBounds().y, 50, 50);
        layeredPane.add(stone, JLayeredPane.PALETTE_LAYER);

        Timer throwTimer = new Timer(50, e -> {
            stone.setLocation(stone.getX(), stone.getY() - 10);
            if (checkCollision(stone, boss)) {
                boss.setHp(boss.getHp() - 50);
                layeredPane.remove(stone);
                bearHoldingStone = false;
                ((Timer) e.getSource()).stop();
            }
            if (stone.getY() < 0) {
                layeredPane.remove(stone);
                bearHoldingStone = false;
                ((Timer) e.getSource()).stop();
            }
            layeredPane.repaint();
        });
        throwTimer.start();
    }

    public void tigerAttack() {
        if (Math.abs(tiger.getBounds().x - boss.getBounds().x) < 100) { // 호랑이가 보스 근처에 있는지 확인
            boss.setHp(boss.getHp() - 30); // 보스 HP 감소
            tiger.setImage("attack"); // 공격 이미지로 변경
            layeredPane.repaint(); // 화면 갱신

            // 500ms 후 원래 이미지로 복구
            Timer resetImageTimer = new Timer(500, e -> {
                tiger.setImage("normal"); // 기본 이미지로 복구
                layeredPane.repaint(); // 화면 갱신
            });
            resetImageTimer.setRepeats(false);
            resetImageTimer.start();
        }
    }


    private boolean checkCollision(JLabel object, Boss boss) {
        return object.getBounds().intersects(boss.getBounds());
    }
}
