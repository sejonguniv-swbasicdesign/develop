package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

	private int x, y;
	private int hp;
	private int name;
	private int checkDirection;
	private boolean movingUp, movingDown, movingLeft, movingRight;
	private String bearRightimagePath = "./src/assets/image/곰_완쪽.png";
	private String bearLeftimagePath = "./src/assets/image/곰.png";
	private String tigerRightimagePath = "./src/assets/image/호랑이.png";
	private String tigerLeftimagePath = "./src/assets/image/호랑이_오른쪽.png";
	private Image RightBearImage;
	private Image LeftBearImage;
	private Image RightTigerImage;
	private Image LeftTigerImage;

	public Player(int x, int y, int name) {
		this.x = x;
		this.y = y;
		hp=3;
		this.name = name;
		if(name==0) {
		checkDirection = 1;
		}
		else {
			checkDirection=0;
		}
		try {
			RightBearImage = ImageIO.read(new File(bearRightimagePath));
			RightBearImage = RightBearImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			LeftBearImage = ImageIO.read(new File(bearLeftimagePath));
			LeftBearImage = LeftBearImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

			RightTigerImage = ImageIO.read(new File(tigerRightimagePath));
			RightTigerImage = RightTigerImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			LeftTigerImage = ImageIO.read(new File(tigerLeftimagePath));
			LeftTigerImage = LeftTigerImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());

		}
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
		checkDirection = 0;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
		checkDirection = 1;
	}

	public double getDirectionX() {
		return (movingRight ? 1 : 0) - (movingLeft ? 1 : 0);
	}

	public double getDirectionY() {
		return (movingDown ? 1 : 0) - (movingUp ? 1 : 0);
	}

	public void move() {
		double dirX = getDirectionX();
		double dirY = getDirectionY();
		if (dirX != 0 || dirY != 0) {
			double length = Math.sqrt(dirX * dirX + dirY * dirY);
			dirX /= length;
			dirY /= length;
		}
		if (name == 0) {
			x += dirX * 2;
			y += dirY * 2;
		} else {
			x += dirX * 4;
			y += dirY * 4;
		}
		x = Math.max(0, Math.min(x, 800));
		y = Math.max(0, Math.min(y, 600));
	}

	public void draw(Graphics g) {

		if (name == 0) {
			if (checkDirection == 0) {
				// 이미지 중심을 기준으로 그리기
				g.drawImage(RightBearImage, x - 20, y - 20, null);
			} else {
				g.drawImage(LeftBearImage, x - 20, y - 20, null);
			}

		} else {
			if (checkDirection == 0) {
				// 이미지 중심을 기준으로 그리기
				g.drawImage(RightTigerImage, x - 20, y - 20, null);
			} else {
				g.drawImage(LeftTigerImage, x - 20, y - 20, null);
			}

		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	public void setHp(int x) {
		hp+=x;
		
	}
	public int getHp() {
		return hp;
		
	}
}
