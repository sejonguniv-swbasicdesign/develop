package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;

public class RocksOfMonsters {
	private int x, y;
	private double angle;
	private boolean destroyed = false;
	private int distanceTraveled = 0;
	private static final int MAX_DISTANCE = 200;

	public RocksOfMonsters(int x, int y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = Math.toRadians(angle);
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
		g.setColor(Color.GRAY);
		g.fillOval(x - 5, y - 5, 10, 10);
	}
}
