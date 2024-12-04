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
	private String bearRightimagePath = "./src/assets/image/곰_완쪽.png";
	private String bearLeftimagePath = "./src/assets/image/곰.png";
	private String tigerRightimagePath = "./src/assets/image/호랑이.png";
	private String tigerLeftimagePath = "./src/assets/image/호랑이_오른쪽.png";
	private Image RightBearImage;
	private Image LeftBearImage;
	private Image RightTigerImage;
	private Image LeftTigerImage;
	private boolean ifOnBridge;
	private boolean ifOnUpperSide;
	private int minX, maxX;
	private int minY, maxY;
	private boolean isVisible; 
	private boolean restart;
	  private Timer blinkTimer;
	  private boolean doesRestart = false;
	public Player(int x, int y, int name) {
		this.x = x;
		this.y = y;
		hp = 3;
		this.name = name;
		ifOnBridge = false;
		ifOnUpperSide = false;
		minX = 10;
		maxX = 575;
		minY = 455;
		maxY = 753;
		isVisible=true;
		restart=false;
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
		if (name == 1) {
			System.out.println(x);
			System.out.println(y);
		}

		if (name == 1) {
			if(ifOnBridge==true) {
				if(((340<=x&&x<=346)&&(454<=y&&y<=459))||((340<=x&&x<=346)&&(360<=y&&y<=363))) {
					minX = 340;
					maxX = 346;
					minY = 359;
					maxY = 458;
				}
				else if((10<x&&x<340)&&(455<y)||(340<=x&&x<=346)&&(458<y)||(346<x&&x<570)&&(455<y)) {
					minX = 10;
					maxX = 575;
					minY = 455;
					maxY = 753;
				}
				else if((10<x&&x<340)&&(y<344)||(340<=x&&x<=346)&&(y<360)||(346<x&&x<570)&&(y<344)) {
					minX = 10;
					maxX = 575;
					minY = 10;
					maxY = 360;
				}
			}
			else {

				if((10<x&&x<340)&&(455<y)||(340<=x&&x<=346)&&(458<y)||(346<x&&x<570)&&(455<y)) {
					minX = 10;
					maxX = 575;
					minY = 455;
					maxY = 753;
				}
				else if((10<x&&x<340)&&(y<344)||(340<=x&&x<=346)&&(y<360)||(346<x&&x<570)&&(y<344)) {
					minX = 10;
					maxX = 575;
					minY = 10;
					maxY = 360;
				}
			}


		}

		x = Math.max(minX, Math.min(x, maxX));
		y = (Math.max(minY, Math.min(y, maxY)));

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
    public void blinkImage() {
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop(); // 이전 타이머 중지
        }

        int duration = 3000; // 3초 동안 깜빡임
        int interval = 200; // 200ms 간격으로 토글

        Timer timer = new Timer(interval, e -> {
            isVisible = !isVisible; // 보이는 상태를 토글
            if (doesRestart) {
                doesRestart = false;
                ((Timer) e.getSource()).stop(); // 타이머 종료
                isVisible = true; // 종료 후 보이는 상태로 복원
            }
        });
        timer.setRepeats(true);
        timer.start();

        // 일정 시간 후 타이머 종료
        new Timer(duration, e -> {
            timer.stop();
            isVisible = true; // 항상 보이도록 복원
        }).start();

        this.blinkTimer = timer;
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
		this.restart=i;
		
	}
	public int getCheckDirection() {
		return checkDirection;
	}

	public void setIfOnBridge(boolean i) {
		ifOnBridge = i;
	}

}
