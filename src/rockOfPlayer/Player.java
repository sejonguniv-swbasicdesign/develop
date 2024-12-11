
package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Player {// ------------------------- name=0->곰 플레이어, name=1->호랑이 플레이어

	private int x, y;
	private int hp;
	private int name;
	private int checkDirection;
	private boolean movingUp, movingDown, movingLeft, movingRight;
	private String bearRightimagePath = "./src/assets/image/characters/곰_완쪽.png";
	private String bearLeftimagePath = "./src/assets/image/characters/곰.png";
	private String tigerRightimagePath = "./src/assets/image/characters/호랑이.png";
	private String tigerLeftimagePath = "./src/assets/image/characters/호랑이_오른쪽.png";
	private String tigerWithRockLeftimagePath = "./src/assets/image/stage1/돌든호랑이.png";
	private String tigerWithRockRightimagePath = "./src/assets/image/stage1/돌든호랑이_오른쪽.png";
	private String bearWithRockLeftimagePath = "./src/assets/image/stage1/돌든곰_왼쪽.png";
	private String bearWithRockRightimagePath = "./src/assets/image/stage1/돌든곰.png";
	private Image RightBearImage;
	private Image LeftBearImage;
	private Image RightTigerImage;
	private Image LeftTigerImage;
	private Image LeftTigerWithRockImage;
	private Image RightTigerWithRockImage;
	private Image LeftBearWithRockImage;
	private Image RightBearWithRockImage;
	private boolean ifOnBridge;

	private int minX, maxX;
	private int minY, maxY;
	private boolean isVisible;
	private boolean restart;
	private boolean showImage;

	private boolean hasRock;

	public Player(int x, int y, int name) {
		this.x = x;
		this.y = y;
		showImage = true;
		hp = 3;
		this.name = name;
		ifOnBridge = false;

		minX = 10;
		maxX = 575;
		minY = 455;
		maxY = 753;
		isVisible = true;
		hasRock = false;

		restart = false;
		if (name == 0) {
			checkDirection = 1;
		} else {
			checkDirection = 0;
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
		try {
			LeftTigerWithRockImage = ImageIO.read(new File(tigerWithRockLeftimagePath));
			LeftTigerWithRockImage = LeftTigerWithRockImage.getScaledInstance(46, 68, Image.SCALE_SMOOTH);
			RightTigerWithRockImage = ImageIO.read(new File(tigerWithRockRightimagePath));
			RightTigerWithRockImage = RightTigerWithRockImage.getScaledInstance(46, 68, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());

		}
		try {
			LeftBearWithRockImage = ImageIO.read(new File(bearWithRockLeftimagePath));
			LeftBearWithRockImage = LeftBearWithRockImage.getScaledInstance(74, 50, Image.SCALE_SMOOTH);
			RightBearWithRockImage = ImageIO.read(new File(bearWithRockRightimagePath));
			RightBearWithRockImage = RightBearWithRockImage.getScaledInstance(74, 50, Image.SCALE_SMOOTH);
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

	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;

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
			if (hasRock == true) {
				x += dirX * 2;
				y += dirY * 2;
			} else {

				x += dirX * 4;
				y += dirY * 4;
			}
		}
		/*
		 * if (name == 1) { System.out.println(x); System.out.println(y); }
		 */
		int i=20;
		if (name == 1) {
			if (ifOnBridge == true) {
				if (((340 <= x && x <= 346) && (454-i <= y && y <= 459-i))
						|| ((340 <= x && x <= 346) && (360-i <= y && y <= 363-i))) {//다리 바운더리
					minX = 340;
					maxX = 346;
					minY = 359-i;
					maxY = 458-i;
				} else if ((10 < x && x < 340) && (455-i < y) || (340 <= x && x <= 346) && (458-i < y)
						|| (346 < x && x < 570) && (455-i < y)) {//아래 부분 바운더리
					minX = 10;
					maxX = 575;
					minY = 455-i;
					maxY = 753;
				} else if ((10 < x && x < 340) && (y < 344-i) || (340 <= x && x <= 346) && (y < 360-i)
						|| (346 < x && x < 570) && (y < 344-i)) {//위 부분 바운더리
					minX = 10;
					maxX = 575;
					minY = 10;
					maxY = 360-i;
				}
			} else {

				if ((10 < x && x < 340) && (455-i < y) || (340 <= x && x <= 346) && (458-i < y)
						|| (346 < x && x < 570) && (455-i < y)) {//아래 부분 바운더리
					minX = 10;
					maxX = 575;
					minY = 455-i;
					maxY = 753;
				} else if ((10 < x && x < 340) && (y < 344-i) || (340 <= x && x <= 346) && (y < 360-i)
						|| (346 < x && x < 570) && (y < 344-i)) {//위 부분 
					minX = 10;
					maxX = 575;
					minY = 10;
					maxY = 360-i;
				}
			}

		}

		x = Math.max(minX, Math.min(x, maxX));
		y = (Math.max(minY, Math.min(y, maxY)));

	}

	public void draw(Graphics g) {

		if (showImage) {
			if (name == 0) {
				if (checkDirection == 0) {
					// 이미지 중심을 기준으로 그리기
					if (hasRock == true) {
						g.drawImage(LeftBearWithRockImage, x - 37, y - 25, null);
					} else
						g.drawImage(RightBearImage, x - 20, y - 20, null);
				} else {
					if (hasRock == true) {
						g.drawImage(RightBearWithRockImage, x - 37, y - 25, null);
					} else
						g.drawImage(LeftBearImage, x - 20, y - 20, null);
				}

			} else {
				if (checkDirection == 0) {
					if (hasRock == true) {
						g.drawImage(LeftTigerWithRockImage, x - 26, y - 34, null);
					} else
						g.drawImage(RightTigerImage, x - 20, y - 20, null);
				} else {
					if (hasRock == true) {
						g.drawImage(RightTigerWithRockImage, x - 26, y - 34, null);
					} else
						g.drawImage(LeftTigerImage, x - 20, y - 20, null);
				}

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
		hp += x;

	}

	public int getHp() {
		return hp;

	}

	public boolean getDoesRestart() {
		return restart;

	}

	public void setDoesRestart(boolean i) {
		this.restart = i;

	}

	public int getCheckDirection() {
		return checkDirection;
	}

	public void setCheckDirection(int checkDirection) {
		this.checkDirection = checkDirection;
	}

	public void setIfOnBridge(boolean i) {
		ifOnBridge = i;
	}

	public boolean getHasRock() {
		return hasRock;
	}

	public void setHasRock(boolean hasRock) {
		this.hasRock = hasRock;
	}

	public boolean deliverRock(int x, int y) {
		
		//Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2)) < 14
		if (Math.abs(this.x - x) < 15 && Math.abs(this.y - y) < 15)
			return true;
		else
			return false;

	}

	public boolean getIsVisible() {
		return isVisible;

	}

	public void setIsVisible(boolean i) {
		isVisible = i;

	}
	public void setShowImage(boolean showImage) {
		this.showImage=showImage;
	}
}
