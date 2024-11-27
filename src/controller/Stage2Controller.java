package controller;

import actionlistener.stage2.Stage2BearKeyListener;
import actionlistener.stage2.Stage2TigerKeyListener;
import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.dto.stage2.BlueButtonDto;
import model.dto.stage2.RedButtonDto;
import model.dto.stage2.YellowButtonDto;
import model.monsters.TigerMonster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Stage2Controller {
    private boolean isRockFalling = false;

    public Stage2Controller(){

    }

    //red button 눌렀을때 발판 상호작용
    public boolean seRedButtonInteraction(RedButtonDto redButtonDto){

        boolean isButtonPressed = redButtonDto.isButtonPressed();
        JLabel floorButton = redButtonDto.getFloorButton();
        JLabel tigerPlayer = redButtonDto.getTigerPlayer();
        JLabel bearPlayer = redButtonDto.getBearPlayer();
        JLabel rock1 = redButtonDto.getRock1();
        JLabel rock2 = redButtonDto.getRock2();
        JLabel step = redButtonDto.getStep();
        Storage storage = redButtonDto.getStorage();

        //버튼 눌렀을때 발판 아래로 내려가게(돌과 플레이어 위치)
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

    //blue button과 돌벽 상호작용
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

    //yellow button과 포탈 상호작용
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

    //돌이 허공에 떠있을때 떨어짐
    public boolean setRockFallingInteraction(JLabel rock2, JLabel[] roads, boolean isRockFalled){
        if(!isLabelOverlapping(rock2,roads[8]) && !isRockFalled){
            isRockFalled = true;
            animateElement(rock2, rock2.getY() + 200);
        }
        return isRockFalled;
    }

    //곰이 돌 미는 동작 구현
    public void setRockInteraction(Storage storage, JLabel rock,JLabel bearPlayer, Stage2BearKeyListener stage2BearKeyListener){
        if (isLabelOverlapping(rock, bearPlayer) && !stage2BearKeyListener.getBearPlayerJump()) {
            if (storage.getBear().isFacingRight) {
                // 곰이 오른쪽을 바라보면 돌도 오른쪽으로 밀기
                rock.setLocation(rock.getX() + 10, rock.getY());  // 10만큼 오른쪽으로 이동
            } else {
                // 곰이 왼쪽을 바라보면 돌도 왼쪽으로 밀기
                rock.setLocation(rock.getX() - 10, rock.getY());  // 10만큼 왼쪽으로 이동
            }
        }
    }

    //호랑이 몬스터가 출구에 닿을때 삭제
    public void removeTigerMonster(ArrayList<TigerMonster> tigerMonster,JLabel monsterExit, JPanel panel,ArrayList<Boolean> isTigerFalling){
        int index = isTigerMonsterOverlapping(monsterExit,tigerMonster);
        if(index!=-1){
            tigerMonster.get(index).getLabel().setVisible(false);
            tigerMonster.get(index).stopMoving();
        }
    }

    //호랑이 몬스터가 돌과 만났을때 방향 전환
    public void setTigerMonsterMovement(ArrayList<TigerMonster> tigerMonster, JLabel rock1, JLabel rock2){
        int index =isTigerMonsterOverlapping(rock1,tigerMonster);
        int index2 = isTigerMonsterOverlapping(rock2,tigerMonster);

        if(index!=-1){
            tigerMonster.get(index).changeDirection(tigerMonster.get(index).getDirection()*-1);
        }
//        else if(index2!=-1){
//            tigerMonster.get(index2).changeDirection(tigerMonster.get(index2).getDirection()*-1);
//        }
    }

    //호랑이 플레이어가 벽 타고 올라가도록 구현
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

    //포탈이 작동할때 곰이 위쪽 화살표 눌렀을때 텔레포트
    public void setPortalInteraction(boolean isYellowButtonPressed, JLabel portal1, JLabel portal2, JLabel bearPlayer, Stage2BearKeyListener stage2BearKeyListener){
        stage2BearKeyListener.updatePortal1State(isYellowButtonPressed && isLabelOverlapping(portal1, bearPlayer));

        stage2BearKeyListener.updatePortal2State(isYellowButtonPressed && isLabelOverlapping(portal2, bearPlayer));
    }

    //호랑이 플레이어가 사다리 타고 올라갈 수 있도록
    public void setLadderInteraction(JLabel ladder, JLabel tigerPlayer, Stage2TigerKeyListener stage2TigerKeyListener){
        stage2TigerKeyListener.updateLadderState(isLabelOverlapping(ladder, tigerPlayer));
    }

    //곰 플레이어가 큰 바위 들었을때 상호작용 구현
    public void setBigRockInteraction(JLabel bigRock, JLabel bearPlayer, Stage2BearKeyListener stage2BearKeyListener,JLabel wall4){

        if(isLabelOverlapping(bearPlayer,bigRock)){
            bigRock.setLocation(bearPlayer.getX(), bigRock.getY()-20);
            stage2BearKeyListener.updateBigRockState(true,bigRock);
        }

        if(!isRockFalling && isLabelOverlapping(bigRock,wall4)){
            stage2BearKeyListener.stopRockMovement();
            animateElement(bigRock,240);
            isRockFalling = true;
        }
    }

    public int setItemInteraction(JLabel tigerPlayer, JLabel bearPlayer, JLabel item1, JLabel item2, JLabel item3, JLabel item4, int itemCount, JPanel panel){
        if(isLabelOverlapping(tigerPlayer,item1)){
            panel.remove(item1);
            item1.setBounds(-100, -100, 0, 0);
            itemCount+=1;
        }
        if(isLabelOverlapping(tigerPlayer,item2)){
            panel.remove(item2);
            item2.setBounds(-100, -100, 0, 0);
            itemCount+=1;
        }
        if(isLabelOverlapping(bearPlayer,item3)){
            panel.remove(item3);
            item3.setBounds(-100, -100, 0, 0);
            itemCount+=1;
        }
        if(isLabelOverlapping(bearPlayer,item4)){
            panel.remove(item4);
            item4.setBounds(-100, -100, 0, 0);
            itemCount+=1;
        }
        panel.revalidate(); // 레이아웃 재계산
        panel.repaint();
        return itemCount;
    }

    //게임 클리어 조건 확인
    public void checkStageFinish(int itemCount, JLabel bigRock, JLabel tigerPlayer, JLabel bearPlayer,JLabel exit,JPanel panel, ArrayList<TigerMonster> tigers){

        if(itemCount == 4 && bigRock.getY()<100 && allTigersInvisible(tigers)){
            if(isLabelOverlapping(tigerPlayer,exit)){
                panel.remove(tigerPlayer);
            }
            if(isLabelOverlapping(bearPlayer,exit)){
                panel.remove(bearPlayer);
            }
        }
    }
    private boolean allTigersInvisible(ArrayList<TigerMonster> tigers) {
        // tigers 리스트의 모든 요소가 visible이 false인지 확인
        for (TigerMonster tiger : tigers) {
            if (tiger.getLabel().isVisible()) {
                return false;
            }
        }
        return true; // 모든 요소가 invisible일 경우 true 반환
    }
    //게임 오버 된지 확인(일단 게임 오버 된지 로그로 찍기만 가능)
    public void checkGameOver(JLabel bearPlayer, JLabel tigerPlayer, ArrayList<TigerMonster> tigerMonster, int hpCount){
        if(bearPlayer.getY()>1000 || tigerPlayer.getY()>1000 || isCheckTigerMonsterFall(tigerMonster) || hpCount==3){
            System.out.println("Game Over");
        }
    }

    private boolean isCheckTigerMonsterFall(ArrayList<TigerMonster> tigerMonsters){
        for(TigerMonster tigerMonster : tigerMonsters){
            if(tigerMonster.getLabel().getY() > 1000 && tigerMonster.getLabel().isVisible()){
                return true;
            }
        }
        return false;
    }

    //라벨들이 겹치는지 확인
    public boolean isLabelOverlapping(JLabel label1, JLabel label2) {
        Rectangle bounds1 = label1.getBounds();
        Rectangle bounds2 = label2.getBounds();
        return bounds1.intersects(bounds2);
    }

    public boolean isLabelOverlappingRoads(JLabel label, JLabel[] roads){
        boolean isOverlapped = false;
        for (JLabel road : roads) {
            if (isLabelOverlapping(road, label)) {
                isOverlapped = true;
                break;
            }
        }
        return isOverlapped;

    }

    public int isTigerMonsterOverlapping(JLabel label, ArrayList<TigerMonster> tigerMonsters){
        for (int i = 0 ;i<tigerMonsters.size();i++) {
            if (isLabelOverlapping(tigerMonsters.get(i).getLabel(), label) && tigerMonsters.get(i).getLabel().isVisible()) {
                return i;
            }
        }
        return -1;
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
