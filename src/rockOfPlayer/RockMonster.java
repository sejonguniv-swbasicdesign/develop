package rockOfPlayer;


import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class RockMonster {// -------------------바위산신
	private int x, y;
	private int hp;
	private int SPEED;
	private int checkDirection;
	private boolean isVisible;
	private Random random = new Random();
	private Image monsterImage;
	private Image monsterImage2;
	private String monsterimagePath = "./src/assets/image/stage1/산신2.png";
	private String monsterimagePath2 = "./src/assets/image/stage1/산신2_반대.png";

	public RockMonster(int x, int y) {
		this.x = x;
		this.y = y;
		hp = 2;

		try {
			monsterImage = ImageIO.read(new File(monsterimagePath));
			monsterImage = monsterImage.getScaledInstance(100, 90, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());

		}

		try {
			monsterImage2 = ImageIO.read(new File(monsterimagePath2));
			monsterImage2 = monsterImage2.getScaledInstance(100, 90, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());

		}
		isVisible = true;
		SPEED = 2;

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setHp(int x) {
		hp += x;

	}

	public int getHp() {
		return hp;

	}

	public void throwRocks(ArrayList<RocksOfMonsters> rocks) {
		if (random.nextInt(40) == 0) {
			if (SPEED < 0)
				rocks.add(new RocksOfMonsters(x - 34, y - 28, 70 + random.nextInt(40)));
			else
				rocks.add(new RocksOfMonsters(x + 34, y - 28, 70 + random.nextInt(40)));
		}

	}

	public void moveLeft() {

		x += SPEED;
		checkDirection = 0;
	}

	public void moveRight() {

		x += SPEED;
		checkDirection = 1;
	}

	public int getSPEED() {
		return SPEED;
	}

	public void setSPEED(int SPEED) {
		this.SPEED = SPEED;
	}

	public void draw(Graphics g) {
		if (checkDirection == 0) {
			g.drawImage(monsterImage, x - 45, y - 40, null);
		} else {
			g.drawImage(monsterImage2, x - 45, y - 40, null);
		}

	}

	public boolean getIsVisible() {
		return isVisible;

	}

	public void setIsVisible(boolean i) {
		isVisible = i;

	}
}