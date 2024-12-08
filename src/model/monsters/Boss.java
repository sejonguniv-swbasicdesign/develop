package model.monsters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Boss extends Monster {
    private ImageIcon bossIcon;

    private String originFilePath = "src/assets/image/characters/천신_기본.png";
    private String faintedFilePath = "src/assets/image/characters/천신_기절.png";
    private String angryFilePath = "src/assets/image/characters/2페이즈_천신.png";

    private Boolean isAngryState;
    private Boolean isFaintedState;
    private int maxHp;

    public Boss(int hp, int x, int y) throws IOException {
        super(hp, x, y);
        maxHp = hp;
        isAngryState = false;
        isFaintedState = false;
        bossIcon = new ImageIcon(ImageIO.read(new File(originFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
    }

    public ImageIcon getBossIcon() {
        return bossIcon;
    }

    public void setFainted() {
        if (isFaintedState) {
            return;
        }

        isFaintedState = true;
        isAngryState = false;

        try {
            bossIcon = new ImageIcon(ImageIO.read(new File(faintedFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timer timer = new Timer(10000, e -> {
            isFaintedState = false;
            updateStateBasedOnHp();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void setHp(int newHp) {
        super.hp = newHp;

        if (!isFaintedState) {
            updateStateBasedOnHp();
        }
    }

    public int getHp() {
        return super.hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isRageMode() {
        return isAngryState;
    }

    public void updateStateBasedOnHp() {
        if (hp <= maxHp / 2) {
            if (!isAngryState) {
                try {
                    bossIcon = new ImageIcon(ImageIO.read(new File(angryFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    isAngryState = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                bossIcon = new ImageIcon(ImageIO.read(new File(originFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                isAngryState = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        hp = maxHp;
        isFaintedState = false;
        isAngryState = false;
        updateStateBasedOnHp();
    }

}
