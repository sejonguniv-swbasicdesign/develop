package model.monsters;

import model.Storage;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Boss {
    private int hp;
    private int x, y;
    private boolean isRageMode;
    private ImageIcon normalIcon, rageIcon;
    private PropertyChangeSupport support; // 상태 변경 알림 지원

    public Boss(int x, int y) {
        this.hp = 500;
        this.x = x;
        this.y = y;
        this.isRageMode = false;
        this.support = new PropertyChangeSupport(this);

        normalIcon = new ImageIcon("src/assets/image/characters/천신_기본.png");
        rageIcon = new ImageIcon("src/assets/image/characters/2페이즈_천신.png");
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (hp <= 250 && !isRageMode) {
            isRageMode = true;
        }
    }

    public boolean isRageMode() {
        return isRageMode;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 150, 150);
    }

    public ImageIcon getCurrentIcon() {
        return isRageMode ? rageIcon : normalIcon;
    }

    // 보스 위치 설정 메서드 추가
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void decreaseHp(int damage) {
        int oldHp = this.hp;
        this.hp -= damage;

        support.firePropertyChange("hp", oldHp, this.hp);

        if (hp <= 250 && !isRageMode) {
            activateRageMode();
        }
    }

    private void activateRageMode() {
        boolean oldRageMode = this.isRageMode;
        this.isRageMode = true;

        // 분노 모드 변경 알림
        support.firePropertyChange("rageMode", oldRageMode, this.isRageMode);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
