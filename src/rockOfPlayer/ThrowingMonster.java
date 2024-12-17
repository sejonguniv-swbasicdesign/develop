package rockOfPlayer;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
class ThrowingMonster {
	private int x, y;
	private static final int SPEED = 4;
	private Image image;

	public ThrowingMonster(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void move() {
		x -= SPEED;
	}

	public boolean isOffScreen() {
		return x < -40;
	}

	public boolean collidesWith(Player player) {
		int playerX = player.getX();
		int playerY = player.getY();
		return Math.abs(x - playerX) < 30 && Math.abs(y - playerY) < 30;
	}

	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x - 20, y - 20, 40, 40);
	}
}