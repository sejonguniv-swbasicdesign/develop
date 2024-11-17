package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Stage2Controller {

    public Stage2Controller(){

    }

    public void animateElement(JLabel element, int targetY){
        Timer animationTimer = new Timer(10, new ActionListener() {
            int currentY = element.getY();
            int direction = targetY > currentY ? 1 : -1; // 위로 올라가거나 아래로 내려가도록 방향 설정

            @Override
            public void actionPerformed(ActionEvent e) {
                if ((direction == 1 && currentY < targetY) || (direction == -1 && currentY > targetY)) {
                    currentY += direction * 5; // 속도 조절을 위해 한번에 이동하는 거리 설정
                    element.setLocation(element.getX(), currentY);
                } else {
                    ((Timer)e.getSource()).stop(); // 목표 위치에 도달하면 타이머 중지
                }
            }
        });
        animationTimer.start();
    }
}
