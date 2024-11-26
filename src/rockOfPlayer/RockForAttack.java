package rockOfPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RockForAttack {//-----------------------곰플레이어가 던지는 돌
	private double x, y;
	private Image rockImage;
	private boolean destroyed = false;
	public RockForAttack(double x, double y) {
		this.x = x;
		this.y = y;
        try {
            rockImage = ImageIO.read(new File("./src/assets/image/돌_공격용.png"));
            rockImage = rockImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH); 

        } catch (IOException e) {
            System.err.println("이미지 로드 실패: " + e.getMessage());
   
        }
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

	public boolean isDestroyed() {
		return destroyed;
	}

	public boolean collidesWithMonster(RockMonster rockMonster) {
		int monsterX = rockMonster.getX();
		int monsterY = rockMonster.getY();
		
		return Math.hypot(x - monsterX, y - monsterY) < 30;
	}
	public void draw(Graphics g) {
		 g.drawImage(rockImage, (int)(x - 12.5),(int)( y - 12.5), null); 
	}
}
