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

    private boolean checkCollision(JLabel object, Boss boss) {
        return object.getBounds().intersects(boss.getBounds());
    }
}