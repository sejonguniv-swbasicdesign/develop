package model.characters;

import javax.swing.*;

public abstract class Character {

    public int hp;
    public int x, y;
    public boolean isFacingRight;
    private Timer currentTimer; // 현재 실행 중인 Timer 저장
    private boolean isMoving; // 현재 움직임 여부 저장

    public Character() {
        hp = 3;
        x = 0;
        y = 0;
        isFacingRight = true;
        isMoving = false;
    }

    public Character(int x, int y) {
        this.hp = 3;
        this.x = x;
        this.y = y;
        this.isFacingRight = true;
        this.isMoving = false;
    }

    protected void resetHp() {
        hp = 3;
    }

    public abstract void move(int x, int y);

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 외부에서 움직임 중단 요청
    public void stopCurrentAction() {
        if (currentTimer != null && currentTimer.isRunning()) {
            currentTimer.stop();
        }
        isMoving = false;
    }

    public void jumpLeft(int deltaX, int deltaY) {
        if (isMoving) return; // 이미 움직이는 중이면 새로운 점프 무시
        isMoving = true;

        int targetX = this.x + deltaX;
        int originalY = this.y;
        int peakY = this.y - deltaY; // 점프하는 동안 상승할 y의 최고 위치

        currentTimer = new Timer(10, null); // 10밀리초 간격으로 움직임
        currentTimer.addActionListener(e -> {
            if (this.y > peakY && this.x > targetX) {
                move(-3, -3); // 왼쪽 위로 조금씩 이동하여 점프
            } else {
                currentTimer.stop(); // 상승 끝나면 타이머 정지
                fallLeft(targetX, originalY); // 원래 y위치로 돌아가기
            }
        });
        currentTimer.start();
    }

    public void jumpRight(int deltaX, int deltaY) {
        if (isMoving) return; // 이미 움직이는 중이면 새로운 점프 무시
        isMoving = true;

        int targetX = this.x + deltaX;
        int originalY = this.y;
        int peakY = this.y - deltaY; // 점프하는 동안 상승할 y의 최고 위치

        currentTimer = new Timer(10, null); // 10밀리초 간격으로 움직임
        currentTimer.addActionListener(e -> {
            if (this.y > peakY && this.x < targetX) {
                move(3, -3); // 오른쪽 위로 조금씩 이동하여 점프
            } else {
                currentTimer.stop(); // 상승 끝나면 타이머 정지
                fallRight(targetX, originalY); // 원래 y위치로 돌아가기
            }
        });
        currentTimer.start();
    }

    private void fallLeft(int targetX, int originalY) {
        currentTimer = new Timer(10, null);
        currentTimer.addActionListener(e -> {
            if (this.y < originalY) {
                move(-3, 3); // 아래로 조금씩 이동하여 원래 y위치로 돌아가기
            } else {
                this.y = originalY; // 정확한 y 위치로 고정
                this.x = targetX; // x 위치를 목표 지점으로 설정
                currentTimer.stop(); // 하강 완료 후 타이머 정지
                isMoving = false; // 움직임 상태 초기화
            }
        });
        currentTimer.start();
    }

    private void fallRight(int targetX, int originalY) {
        currentTimer = new Timer(10, null);
        currentTimer.addActionListener(e -> {
            if (this.y < originalY) {
                move(3, 3); // 아래로 조금씩 이동하여 원래 y위치로 돌아가기
            } else {
                this.y = originalY; // 정확한 y 위치로 고정
                this.x = targetX; // x 위치를 목표 지점으로 설정
                currentTimer.stop(); // 하강 완료 후 타이머 정지
                isMoving = false; // 움직임 상태 초기화
            }
        });
        currentTimer.start();
    }
}
