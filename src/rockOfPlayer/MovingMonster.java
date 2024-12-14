package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Graphics;
/*
public class MovingMonster {
	private int x, y;
	private static final int SPEED = 4;

	public MovingMonster(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveLeft() {
		x -= SPEED;
	}

	public void moveRight() {
		x += SPEED;
	}

	public boolean isOffScreen() {
		return (x < -40 || x > 840);
	}

	public boolean collidesWith(Player player) {
		int playerX = player.getX();
		int playerY = player.getY();
		return Math.abs(x - playerX) < 30 && Math.abs(y - playerY) < 30;
	}

	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x - 20, y - 20, 40, 40);
		g.setColor(Color.green);
		g.drawString("곰부족", x - 19, y);
	}
}
*/

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class MovingMonster {// ---------------------곰부족
	private int x, y;
	private static final int SPEED = 2;
	private Image RightmonsterImage;
	private Image LeftmonsterImage;
	private int checkDirection;
	// 이미지 파일 경로
	private String RightimagePath = "./src/assets/image/stage1/곰부족_왼쪽.png";
	private String LeftimagePath = "./src/assets/image/stage1/곰부족.png";

	public MovingMonster(int x, int y) {
		this.x = x;
		this.y = y;
		checkDirection = 2;
		// 이미지 로드 및 스케일 조정
		try {
			Image RightImage = ImageIO.read(new File(RightimagePath));
			RightmonsterImage = RightImage.getScaledInstance(54, 50, Image.SCALE_SMOOTH);
			Image LeftImage = ImageIO.read(new File(LeftimagePath));
			LeftmonsterImage = LeftImage.getScaledInstance(54, 50, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());

		}
	}

	public void moveLeft() {
		x -= SPEED;
		checkDirection = 1;
	}

	public void moveRight() {
		x += SPEED;
		checkDirection = 0;
	}

	public boolean isOffScreen() {
		return (x < -40 || x > 840);
	}

	public boolean collidesWith(Player player) {
		int playerX = player.getX();
		int playerY = player.getY();
		return Math.abs(x - playerX) < 30 && Math.abs(y - playerY) < 30;
	}

	public void draw(Graphics g) {
		if (checkDirection == 1) {
			// 이미지 중심을 기준으로 그리기
			g.drawImage(RightmonsterImage, x - 27, y - 25, null);
		} else {
			g.drawImage(LeftmonsterImage, x - 27, y - 25, null);
		}
	}
}