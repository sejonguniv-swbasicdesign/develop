package controller;

import utils.constants.StageCoordination;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RockController {
    private final JLayeredPane panel;
    private final List<JLabel> rocks;

    public RockController(JLayeredPane panel) {
        this.panel = panel;
        this.rocks = new ArrayList<>();
        startRockRespawnChecker();
    }

    public void generateRocks() {
        while (rocks.size() < 2) {
            JLabel rock = createRockWithPolygon();
            if (rock != null) {
                rocks.add(rock);
                panel.add(rock, JLayeredPane.DRAG_LAYER);
            }
        }
        panel.repaint();
    }

    private JLabel createRockWithPolygon() {
        int rockWidth = 40;
        int rockHeight = 40;

        Polygon stagePolygon = createStagePolygon();
        Rectangle bounds = stagePolygon.getBounds();

        for (int attempt = 0; attempt < 100; attempt++) {
            int x = bounds.x + (int) (Math.random() * bounds.width);
            int y = bounds.y + (int) (Math.random() * bounds.height);

            if (stagePolygon.contains(x, y)) {
                JLabel rock = new JLabel(new ImageIcon(
                        new ImageIcon("src/assets/image/icon/돌.png")
                                .getImage()
                                .getScaledInstance(rockWidth, rockHeight, Image.SCALE_SMOOTH)
                ));
                rock.setBounds(x, y, rockWidth, rockHeight);
                return rock;
            }
        }

        return null;
    }

    private Polygon createStagePolygon() {
        Polygon polygon = new Polygon();
        for (StageCoordination coord : StageCoordination.values()) {
            polygon.addPoint(coord.getX(), coord.getY());
        }
        return polygon;
    }

    public JLabel findClosestRock(JLabel bearLabel) {
        JLabel closestRock = null;
        double minDistance = Double.MAX_VALUE;

        for (JLabel rock : rocks) {
            double distance = bearLabel.getLocation().distance(rock.getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                closestRock = rock;
            }
        }
        return closestRock;
    }

    public void hideRock(JLabel rock) {
        rock.setVisible(false); // 돌을 숨김
        rocks.remove(rock); // 리스트에서 제거
    }

    public void removeRock(JLabel rock) {
        panel.remove(rock); // 돌을 패널에서 제거
        rocks.remove(rock); // 리스트에서 제거
        panel.repaint();
    }

    public void startRockRespawnChecker() {
        Timer respawnChecker = new Timer(1000, e -> {
            if (rocks.size() < 5) {
                generateRocks(); // 돌 자동 생성
            }
        });
        respawnChecker.start();
    }

}
