
package rockOfPlayer;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FollowingMonster {// --------------------바위 몬스터
	private double x;
	private double y;
	private double speed;
	private int detectionRange;
	private int checkDirection;
	private Image monsterImageRight;
	private Image monsterImageLeft;
	private String RightimagePath = "./src/assets/image/stage1/바위몬스터_오른쪽.png";
	private String LeftimagePath = "./src/assets/image/stage1/바위몬스터.png";

	public FollowingMonster(int x, int y) {
		this.x = x;
		this.y = y;
		this.speed = 0.3;
		checkDirection = 0;
		this.detectionRange = 1500;
		try {
			Image RightImage = ImageIO.read(new File(RightimagePath));
			monsterImageRight = RightImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
			Image LeftImage = ImageIO.read(new File(LeftimagePath));
			monsterImageLeft = LeftImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());
		}
	}

	public double calculateDistance(int playerX, int playerY) {
		return Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));
	}

	public void followPlayer(int playerX, int playerY,int i) {
		
		double distance = calculateDistance(playerX, playerY);
		if (playerY > 435||i==1) {
			if (distance <= detectionRange) {
				double dx = playerX - x;
				double dy = playerY - y;

				if (dx != 0)
					x += speed * dx / Math.abs(dx);

				if (dy != 0)
					y += speed * dy / Math.abs(dy);

				if ((int) dx > 0)
					checkDirection = 1;
				else
					checkDirection = 0;
			}
		}
	}

	public boolean collidesWith(Player player) {
		int playerX = player.getX();
		int playerY = player.getY();
		return Math.abs(x - playerX) < 40 && Math.abs(y - playerY) < 40;
	}

	public int getX() {
		return (int) Math.round(x); // 화면 표시용으로 정수 변환
	}

	public int getY() {
		return (int) Math.round(y); // 화면 표시용으로 정수 변환
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void draw(Graphics g) {
		if (checkDirection == 1) {
			g.drawImage(monsterImageRight, getX() - 30, getY() - 30, null);
		} else {
			g.drawImage(monsterImageLeft, getX() - 30, getY() - 30, null);
		}
	}

	public int getDetectionRange() {
		return detectionRange;
	}
}
