package model.characters;

import javax.swing.*;

public abstract class Character {

    public int hp;
    public int x, y;
    public boolean isFacingRight;

    public Character() {
        hp = 3;
        x = 0;
        y = 0;
        isFacingRight = true;
    }

    public Character(int x, int y) {
        this.hp = 3;
        this.x = x;
        this.y = y;
        this.isFacingRight = true;
    }

    protected void resetHp() {
        hp = 3;
    }

    public abstract void move(int x, int y);

    public int getX(){
        return x;
    }
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getY(){
        return y;
    }

    public void jumpLeft(int deltaX, int deltaY) {
        int targetX = this.x + deltaX;
        int originalY = this.y;
        int peakY = this.y - deltaY; // 점프하는 동안 상승할 y의 최고 위치

        Timer timer = new Timer(10, null); // 10밀리초 간격으로 움직임
        timer.addActionListener(e -> {
            if (this.y > peakY && this.x > targetX) {
                move(-3, -3); // 왼쪽 위로 조금씩 이동하여 점프
            } else {
                ((Timer) e.getSource()).stop(); // 상승 끝나면 타이머 정지
                fallLeft(targetX, originalY); // 원래 y위치로 돌아가기
            }
        });
        timer.start();
    }

    public void jumpRight(int deltaX, int deltaY) {
        int targetX = this.x + deltaX;
        int originalY = this.y;
        int peakY = this.y - deltaY; // 점프하는 동안 상승할 y의 최고 위치

        Timer timer = new Timer(10, null); // 10밀리초 간격으로 움직임
        timer.addActionListener(e -> {
            if (this.y > peakY && this.x < targetX) {
                move(3, -3); // 오른쪽 위로 조금씩 이동하여 점프
            } else {
                ((Timer) e.getSource()).stop(); // 상승 끝나면 타이머 정지
                fallRight(targetX, originalY); // 원래 y위치로 돌아가기
            }
        });
        timer.start();
    }

    private void fallLeft(int targetX, int originalY) {
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            if (this.y < originalY) {
                move(-3, 3); // 아래로 조금씩 이동하여 원래 y위치로 돌아가기
            } else {
                this.y = originalY; // 정확한 y 위치로 고정
                this.x = targetX; // x 위치를 목표 지점으로 설정
                ((Timer) e.getSource()).stop(); // 하강 완료 후 타이머 정지
            }
        });
        timer.start();
    }

    private void fallRight(int targetX, int originalY){
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            if (this.y < originalY) {
                move(3, 3); // 아래로 조금씩 이동하여 원래 y위치로 돌아가기
            } else {
                this.y = originalY; // 정확한 y 위치로 고정
                this.x = targetX; // x 위치를 목표 지점으로 설정
                ((Timer) e.getSource()).stop(); // 하강 완료 후 타이머 정지
            }
        });
        timer.start();
    }

}
