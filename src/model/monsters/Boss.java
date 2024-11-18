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

    public Boss(int hp, int x, int y) throws IOException {
        super(hp, x, y);
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
        this.hp = newHp;

        // 기절 상태에서는 이미지 변경 로직을 실행하지 않음
        if (!isFaintedState) {
            updateStateBasedOnHp();
        }
    }

    private void updateStateBasedOnHp() {
        if (hp <= 250) {
            // 분노 상태로 전환
            if (!isAngryState) {
                try {
                    bossIcon = new ImageIcon(ImageIO.read(new File(angryFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    isAngryState = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 원래 상태로 복구
            try {
                bossIcon = new ImageIcon(ImageIO.read(new File(originFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                isAngryState = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
