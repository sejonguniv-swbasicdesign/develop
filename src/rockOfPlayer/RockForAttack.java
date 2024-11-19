package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;

public class RockForAttack {
	private double x, y;

	public RockForAttack(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void move(double dx, double dy) {
		x += dx;
		y += dy;
	}

	public boolean isOffScreen() {
		return x < 0 || x > 800 || y < 0 || y > 600;
	}

	public boolean collidesWith(Player player) {
		int playerX = player.getX();
		int playerY = player.getY();
		return Math.abs(x - playerX) < 20 && Math.abs(y - playerY) < 20;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillOval((int) x - 10, (int) y - 10, 20, 20);
	}
}
