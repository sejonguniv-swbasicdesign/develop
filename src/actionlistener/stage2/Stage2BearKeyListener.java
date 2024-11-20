package actionlistener.stage2;

import model.characters.BearPlayer;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Stage2BearKeyListener implements KeyListener {
    private BearPlayer bearPlayer;
    private boolean isUpPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean hasJumped = false;
    private boolean isPortal1;
    private boolean isPortal2;
    private boolean isBigRock=false;
    private JLabel bigRock;

    public Stage2BearKeyListener(BearPlayer bearPlayer) {
        this.bearPlayer = bearPlayer;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 각 키 상태를 추적
        if (key == KeyEvent.VK_UP) {
            isUpPressed = true;
            if(isPortal1){
                bearPlayer.setPosition(950,680);
            }
            if(isPortal2){
                bearPlayer.setPosition(500,480);
            }
            if(isBigRock){
                throwRock();
                isBigRock = false;
            }
        }
        if (key == KeyEvent.VK_LEFT) {
            isLeftPressed = true;
            if(isBigRock){
                moveRock(-10);
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            isRightPressed = true;
            if(isBigRock){
                moveRock(10);
            }
        }

        // 방향키 Up과 Left가 동시에 눌렸고 점프가 발생하지 않은 경우 왼쪽으로 점프
        if (isUpPressed && isLeftPressed && !hasJumped) {
            bearPlayer.jumpLeft(-130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
            startJumpCooldown();
        }
        // 방향키 Up과 Right가 동시에 눌렸고 점프가 발생하지 않은 경우 오른쪽으로 점프
        else if (isUpPressed && isRightPressed && !hasJumped) {
            bearPlayer.jumpRight(130, 60);
            hasJumped = true; // 점프가 발생했음을 표시
            startJumpCooldown();
        }
        // 다른 키 입력 처리
        else {
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bearPlayer.move(-10, 0); // 왼쪽으로 이동
                    break;
                case KeyEvent.VK_RIGHT:
                    bearPlayer.move(10, 0);  // 오른쪽으로 이동
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // 키가 해제되면 상태를 업데이트
        if (key == KeyEvent.VK_UP) {
            isUpPressed = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            isLeftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            isRightPressed = false;
        }
    }

    public void updatePortal1State(boolean isOverlapping){
        this.isPortal1 = isOverlapping;
    }
    public void updatePortal2State(boolean isOverlapping){
        this.isPortal2 = isOverlapping;
    }
    public void updateBigRockState(boolean isOverlapping,JLabel bigRock){
        this.isBigRock = isOverlapping;
        this.bigRock = bigRock;
    }

    public boolean getBearPlayerJump(){
        return hasJumped;
    }

    private void startJumpCooldown() {
        Timer jumpCooldownTimer = new Timer(830, e -> hasJumped = false); // 500ms 후에 hasJumped를 false로 설정
        jumpCooldownTimer.setRepeats(false); // 한 번만 실행되도록 설정
        jumpCooldownTimer.start();
    }

    private void moveRock(int dx) {

        // 돌 위치 이동
        int currentRockX = bigRock.getX();
        int currentRockY = bigRock.getY();
        bigRock.setLocation(currentRockX + dx, currentRockY);
    }

    private void throwRock() {
        // 돌의 현재 위치를 저장할 배열
        double[] position = {bigRock.getX(), bigRock.getY()};

        // 목표 위치
        double targetX = 500;
        double targetY = 40;

        // 이동 단계 설정
        int steps = 50;  // 이동 단계 수
        int delay = 10;  // 단계 간 지연 시간 (밀리초)

        // 단계별 이동 거리 계산
        double dx = (targetX - position[0]) / steps;
        double dy = (targetY - position[1]) / steps;

        // 타이머를 사용한 애니메이션
        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            // 현재 위치 업데이트
            position[0] += dx;
            position[1] += dy;

            // 목표 위치에 도달하면 타이머 중지
            if (Math.abs(position[0] - targetX) < Math.abs(dx) && Math.abs(position[1] - targetY) < Math.abs(dy)) {
                bigRock.setLocation((int) targetX, (int) targetY);
                timer.stop();


            } else {
                // 한 단계씩 이동
                bigRock.setLocation((int) position[0], (int) position[1]);
            }
        });

        // 타이머 시작
        timer.start();
    }
}
