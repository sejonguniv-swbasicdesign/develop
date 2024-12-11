package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class RockMonster {//-------------------바위산신
	private int x, y;
	private int hp;
	private boolean isVisible;
	private Random random = new Random();
	private Image monsterImage;
	private String monsterimagePath = "./src/assets/image/stage1/산신3.png";
	public RockMonster(int x, int y) {
		this.x = x;
		this.y = y;
		hp=3;

		try {
			monsterImage = ImageIO.read(new File(monsterimagePath));
			monsterImage = monsterImage.getScaledInstance(90, 80, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			System.err.println("이미지 로드 실패: " + e.getMessage());

		}
		isVisible = true;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setHp(int x) {
		hp+=x;

	}
	public int getHp() {
		return hp;

	}

	public void throwRocks(ArrayList<RocksOfMonsters> rocks) {
		if (random.nextInt(40)== 0) {
			rocks.add(new RocksOfMonsters(x-12, y-10, 60+random.nextInt(40)));
		}

	}

	public void draw(Graphics g) {

		g.drawImage(monsterImage, x - 45, y - 40, null);


	}
	public boolean getIsVisible() {
		return isVisible;

	}
	public void setIsVisible(boolean i) {
		isVisible=i;

	}
}