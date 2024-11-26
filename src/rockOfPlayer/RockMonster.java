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
	private Random random = new Random();
    private Image monsterImage;
    private String monsterimagePath = "./src/assets/image/산신.png"; 
	public RockMonster(int x, int y) {
		this.x = x;
		this.y = y;
		hp=2;
        try {
            monsterImage = ImageIO.read(new File(monsterimagePath));
            monsterImage = monsterImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH); 

        } catch (IOException e) {
            System.err.println("이미지 로드 실패: " + e.getMessage());
   
        }
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
		if (random.nextInt(50) == 0) { 
			rocks.add(new RocksOfMonsters(x, y, random.nextInt(360)));
		}
		
	}

	public void draw(Graphics g) {

            g.drawImage(monsterImage, x - 20, y - 20, null); 

	}
}
