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

    public Boss(int hp, int x, int y) throws IOException {
        super(hp, x, y);
        isAngryState = false;
        bossIcon = new ImageIcon(ImageIO.read(new File(originFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
    }

    public ImageIcon getBossIcon() {
        return bossIcon;
    }

    public void setFainted() {
        try {
            bossIcon = new ImageIcon(ImageIO.read(new File(faintedFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 10초 후 원래 상태로 복구
        new Timer(10000, e -> {
            try {
                bossIcon = new ImageIcon(ImageIO.read(new File(originFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).setRepeats(false); // Timer가 한 번만 실행되도록 설정
    }

    public void setHp(int newHp) {
        this.hp = newHp;

        // hp가 절반 이하로 내려가면 angry 상태로 변경
        if (hp <= 250) {
            try {
                bossIcon = new ImageIcon(ImageIO.read(new File(angryFilePath)).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
