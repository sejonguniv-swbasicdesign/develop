package controller;

import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.dto.stage2.BlueButtonDto;
import model.dto.stage2.RedButtonDto;
import model.dto.stage2.YellowButtonDto;
import model.monsters.TigerMonster;
import view.container.panel.Stage2Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Stage2Controller {

    private Stage2Panel stage2Panel;
    private boolean isFalling;
    private Timer timer;

    public Stage2Controller(Stage2Panel stage2Panel){
        this.stage2Panel = stage2Panel;
    }

    public boolean seRedButtonInteraction(RedButtonDto redButtonDto){

        boolean isButtonPressed = redButtonDto.isButtonPressed();
        JLabel floorButton = redButtonDto.getFloorButton();
        JLabel tigerPlayer = redButtonDto.getTigerPlayer();
        JLabel bearPlayer = redButtonDto.getBearPlayer();
        JLabel rock1 = redButtonDto.getRock1();
        JLabel rock2 = redButtonDto.getRock2();
        JLabel step = redButtonDto.getStep();
        Storage storage = redButtonDto.getStorage();


        if (!isButtonPressed && (isLabelOverlapping(floorButton, tigerPlayer) || isLabelOverlapping(floorButton, bearPlayer)|| isLabelOverlapping(floorButton, rock1) || isLabelOverlapping(floorButton, rock2))) {
            isButtonPressed = true; // 위치 변경 후 상태 유지
            if(isLabelOverlapping(rock1,step)){
                animateElement(rock1, rock1.getY() + 200);
            }
            if(isLabelOverlapping(bearPlayer,step)){
                animateBearPlayer(storage.getBear(), storage.getBear().y + 200);
            }
            animateElement(step, step.getY() + 200);
        }
        else if (isButtonPressed && !isLabelOverlapping(floorButton, tigerPlayer) && !isLabelOverlapping(floorButton, bearPlayer) && !isLabelOverlapping(floorButton, rock1)&& !isLabelOverlapping(floorButton, rock2)) {

            if(isLabelOverlapping(bearPlayer,step)){
                animateBearPlayer(storage.getBear(), storage.getBear().y - 200);
            }
            if(isLabelOverlapping(rock1,step)){
                animateElement(rock1, rock1.getY() - 200);
            }

            animateElement(step, step.getY() -200);
            isButtonPressed = false; // 상태 초기화
        }

        return isButtonPressed;
    }

    public boolean setBlueButtonInteraction(BlueButtonDto blueButtonDto){
        boolean isBlueButtonPressed = blueButtonDto.isBlueButtonPressed();
        JLabel floorButton = blueButtonDto.getFloorButton();
        JLabel tigerPlayer = blueButtonDto.getTigerPlayer();
        JLabel bearPlayer = blueButtonDto.getBearPlayer();
        JLabel wall = blueButtonDto.getWall();


        if (!isBlueButtonPressed && (isLabelOverlapping(floorButton, tigerPlayer) || isLabelOverlapping(floorButton, bearPlayer))) {
            isBlueButtonPressed = true; // 위치 변경 후 상태 유지
            animateElement(wall, wall.getY() - 120);

        }
        else if (isBlueButtonPressed && !isLabelOverlapping(floorButton, tigerPlayer) && !isLabelOverlapping(floorButton, bearPlayer)) {
            isBlueButtonPressed = false; // 상태 초기화
            animateElement(wall, wall.getY() + 120);
        }
        return isBlueButtonPressed;
    }

    public boolean setYellowButtonInteraction(YellowButtonDto yellowButtonDto){
        boolean isYellowButtonPressed = yellowButtonDto.isButtonPressed();
        JLabel floorButton = yellowButtonDto.getFloorButton();
        JLabel tigerPlayer = yellowButtonDto.getTigerPlayer();
        JLabel bearPlayer = yellowButtonDto.getBearPlayer();
        JLabel portal1 = yellowButtonDto.getPortal1();
        JLabel portal2 = yellowButtonDto.getPortal2();

        if (!isYellowButtonPressed && (isLabelOverlapping(floorButton, tigerPlayer) || isLabelOverlapping(floorButton, bearPlayer))) {
            isYellowButtonPressed = true;
            ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/active_portal.png");
            portal1.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
            portal2.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
        }
        else if (isYellowButtonPressed && !isLabelOverlapping(floorButton, tigerPlayer) && !isLabelOverlapping(floorButton, bearPlayer)) {
            isYellowButtonPressed = false;
            ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/nonactive_portal.png");
            portal1.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
            portal2.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
        }
        return isYellowButtonPressed;
    }

    public boolean setLeverInteraction(boolean isLeverPressed,JLabel lever,JLabel tigerPlayer,JLabel bearPlayer,JLabel step1){
        if(!isLeverPressed && (isLabelOverlapping(lever, tigerPlayer) || isLabelOverlapping(lever, bearPlayer))){
            isLeverPressed = true;
            ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/lever_down.png");
            lever.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH)));
            animateElement(step1, step1.getY() - 200);
        }
        return isLeverPressed;
    }



    public boolean setRockFallingInteraction(JLabel rock2, JLabel[] roads, boolean isRockFalled){
        if(!isLabelOverlapping(rock2,roads[8]) && !isRockFalled){
            isRockFalled = true;
            animateElement(rock2, rock2.getY() + 200);
        }
        return isRockFalled;
    }

    public void setRockInteraction(Storage storage, JLabel rock,JLabel bearPlayer){
        if (isLabelOverlapping(rock, bearPlayer)) {
            if (storage.getBear().isFacingRight) {
                // 곰이 오른쪽을 바라보면 상자도 오른쪽으로 밀기
                rock.setLocation(rock.getX() + 10, rock.getY());  // 10만큼 오른쪽으로 이동
            } else {
                // 곰이 왼쪽을 바라보면 상자도 왼쪽으로 밀기
                rock.setLocation(rock.getX() - 10, rock.getY());  // 10만큼 왼쪽으로 이동
            }
        }
    }

    public void removeTigerMonster(TigerMonster tigerMonster,JLabel monsterExit, JPanel panel){
        if(isLabelOverlapping(tigerMonster.getLabel(),monsterExit)){
            panel.remove(tigerMonster.getLabel());
        }
    }

    public void setTigerMonsterMovement(TigerMonster tigerMonster, JLabel rock1, JLabel rock2){
        if(isLabelOverlapping(rock1,tigerMonster.getLabel()) || isLabelOverlapping(rock2,tigerMonster.getLabel())){
            tigerMonster.changeDirection(tigerMonster.getDirection()*-1);
        }
    }

    public boolean setTigerPlayerWallInteraction(JLabel wall1,JLabel wall2,JLabel wall3, JLabel tigerPlayer, Storage storage, boolean isTigerPlayer){
        if(!isTigerPlayer && isLabelOverlapping(wall1,tigerPlayer)){
            isTigerPlayer = true;
            storage.getTiger().stopCurrentAction();
            animateTigerPlayer(storage.getTiger(), 480);
        }
        else if(isTigerPlayer && !isLabelOverlapping(wall1,tigerPlayer)){
            isTigerPlayer = false;
        }

        if(!isTigerPlayer && isLabelOverlapping(wall2,tigerPlayer)){
            isTigerPlayer = true;
            storage.getTiger().stopCurrentAction();
            animateTigerPlayer(storage.getTiger(), 280);
        }
        else if(isTigerPlayer && !isLabelOverlapping(wall2,tigerPlayer)){
            isTigerPlayer = false;
        }

        if(!isTigerPlayer && isLabelOverlapping(wall3,tigerPlayer)){
            isTigerPlayer = true;
            storage.getTiger().stopCurrentAction();
            animateTigerPlayer(storage.getTiger(), 80);
        }
        else if(isTigerPlayer && !isLabelOverlapping(wall3,tigerPlayer)){
            isTigerPlayer = false;
        }

        return isTigerPlayer;
    }


    private boolean isLabelOverlapping(JLabel label1, JLabel label2) {
        Rectangle bounds1 = label1.getBounds();
        Rectangle bounds2 = label2.getBounds();
        return bounds1.intersects(bounds2);
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

    public void animateBearPlayer(BearPlayer player, int targetY){
        Timer timer = new Timer(10, new ActionListener() {
            int currentY = player.y;
            int direction = targetY > currentY ? 1 : -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if ((direction == 1 && currentY < targetY) || (direction == -1 && currentY > targetY)) {
                    currentY += direction * 5; // 속도 조절을 위해 한번에 이동하는 거리 설정
                    player.setPosition(player.x, currentY);
                } else {
                    ((Timer)e.getSource()).stop(); // 목표 위치에 도달하면 타이머 중지
                }
            }
        });
        timer.start();
    }

    public void fallTigerPlayer(TigerPlayer player, int targetY){
        Timer timer = new Timer(10, new ActionListener() {
            int currentY = player.y;
            int direction = targetY > currentY ? 1 : -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if ((direction == 1 && currentY < targetY) || (direction == -1 && currentY > targetY)) {
                    currentY += direction * 5; // 속도 조절을 위해 한번에 이동하는 거리 설정
                    player.setPosition(player.x, currentY);
                } else {
                    ((Timer)e.getSource()).stop(); // 목표 위치에 도달하면 타이머 중지
                }
            }
        });
        timer.start();
    }

    public void animateTigerPlayer(TigerPlayer player, int targetY){
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentY = player.y; // 현재 y 좌표 가져오기
                int step = 5; // 이동 속도 (한번에 이동할 거리)

                if (currentY > targetY) {
                    // 목표보다 현재 위치가 아래에 있으면 위로 이동
                    player.setPosition(player.x, Math.max(currentY - step, targetY));
                } else {
                    // 목표 위치에 도달하면 타이머 중지
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }
}
