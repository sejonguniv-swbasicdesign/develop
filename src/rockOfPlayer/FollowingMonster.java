
package rockOfPlayer;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FollowingMonster {//--------------------바위 몬스터
	private int x;
	private int y;
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
		this.speed =1;
		checkDirection=0;
		this.detectionRange = 70;
		try {
			Image RightImage = ImageIO.read(new File(RightimagePath));
			monsterImageRight = RightImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
			Image LeftImage = ImageIO.read(new File(LeftimagePath));
			monsterImageLeft = LeftImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());

		}
	}

	private double calculateDistance(int playerX, int playerY) {
		return Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));
	}

	public void followPlayer(int playerX, int playerY) {
		double distance = calculateDistance(playerX, playerY);

		if (distance <= detectionRange) {

			int dx = playerX - x;
			int dy = playerY - y;


			if (dx != 0)
				x += speed * dx / Math.abs(dx);

			if (dy != 0)
				y += speed * dy / Math.abs(dy);

			if(dx>0)checkDirection=1;
			else checkDirection=0;
		}
	}
	public boolean collidesWith(Player player) {
		int playerX = player.getX();
		int playerY = player.getY();
		return Math.abs(x - playerX) < 43&& Math.abs(y - playerY) < 43;
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x=x;
	}
	public void setY(int y) {
		this.y=y;
	}
	public void draw(Graphics g) {
		if (checkDirection==1) {
			// 이미지 중심을 기준으로 그리기
			g.drawImage(monsterImageRight, x - 30, y - 30, null);
		} else {
			g.drawImage(monsterImageLeft, x - 30, y - 30, null);
		}
	}

}
