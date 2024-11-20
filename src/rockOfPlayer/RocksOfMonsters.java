package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RocksOfMonsters {
	private int x, y;
	private double angle;
	private boolean destroyed = false;
	private int distanceTraveled = 0;
	private static final int MAX_DISTANCE = 500;
	 private Image rockImage;

	public RocksOfMonsters(int x, int y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = Math.toRadians(angle);
        try {
            rockImage = ImageIO.read(new File("./src/assets/image/돌_산신용.png"));
            rockImage = rockImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH); 

        } catch (IOException e) {
            System.err.println("이미지 로드 실패: " + e.getMessage());
   
        }
	}

	public void move() {
		if (distanceTraveled < MAX_DISTANCE) {
			x += Math.cos(angle) * 5;
			y += Math.sin(angle) * 5;
			distanceTraveled += 5;
		} else {
			destroyed = true;
		}
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public boolean collidesWith(Player player) {
		int playerX = player.getX();
		int playerY = player.getY();
		return Math.hypot(x - playerX, y - playerY) < 20;
	}

	public void draw(Graphics g) {
		 g.drawImage(rockImage, (int)x - 10,(int) y - 10, null); 
		
	}
}
