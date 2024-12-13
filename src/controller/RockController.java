package controller;

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
    private final Rectangle cloudVisibleBounds;
    private final String cloudImagePath;

    public RockController(JLayeredPane panel, JLabel cloudLabel) {
        this.panel = panel;
        this.rocks = new ArrayList<>();
        startRockRespawnChecker();
        cloudImagePath = "src/assets/image/component/cloud_stage.png";
        this.cloudVisibleBounds = calculateCloudBounds(cloudLabel, cloudImagePath); // 구름의 실제 표시 범위 계산
    }

    public void generateRocks() {
        while (rocks.size() < 5) {
            JLabel rock = createRockWithinCloud();
            rocks.add(rock);
            panel.add(rock, JLayeredPane.DRAG_LAYER);
        }
        panel.repaint();
    }

    private JLabel createRockWithinCloud() {
        int rockWidth = 40;
        int rockHeight = 40;

        int x = (int) (cloudVisibleBounds.getX() + Math.random() * (cloudVisibleBounds.getWidth() - rockWidth));
        int y = (int) (cloudVisibleBounds.getY() + Math.random() * (cloudVisibleBounds.getHeight() - rockHeight));

        JLabel rock = new JLabel(new ImageIcon(
                new ImageIcon("src/assets/image/icon/돌.png")
                        .getImage()
                        .getScaledInstance(rockWidth, rockHeight, Image.SCALE_SMOOTH)
        ));
        rock.setBounds(x, y, rockWidth, rockHeight);
        return rock;
    }

    private Rectangle calculateCloudBounds(JLabel cloudLabel, String cloudImagePath) {
        try {
            BufferedImage cloudImage = ImageIO.read(new File(cloudImagePath));
            int minX = cloudImage.getWidth(), minY = cloudImage.getHeight();
            int maxX = 0, maxY = 0;

            for (int y = 0; y < cloudImage.getHeight(); y++) {
                for (int x = 0; x < cloudImage.getWidth(); x++) {
                    int alpha = (cloudImage.getRGB(x, y) >> 24) & 0xff; // 알파 값 추출
                    if (alpha > 0) { // 불투명한 픽셀만 고려
                        if (x < minX) minX = x;
                        if (y < minY) minY = y;
                        if (x > maxX) maxX = x;
                        if (y > maxY) maxY = y;
                    }
                }
            }

            // 구름 이미지의 표시 영역을 기반으로 JLabel의 상대 좌표 계산
            Rectangle labelBounds = cloudLabel.getBounds();
            return new Rectangle(
                    labelBounds.x + minX,
                    labelBounds.y + minY,
                    maxX - minX,
                    maxY - minY
            );
        } catch (IOException e) {
            e.printStackTrace();
            return cloudLabel.getBounds(); // 이미지 로드 실패 시 기본 범위 사용
        }
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
