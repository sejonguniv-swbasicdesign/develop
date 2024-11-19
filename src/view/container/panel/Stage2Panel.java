package view.container.panel;

import actionlistener.stage2.Stage2BearKeyListener;
import actionlistener.stage2.Stage2TigerKeyListener;
import model.dto.stage2.BlueButtonDto;
import model.dto.stage2.RedButtonDto;
import model.dto.stage2.YellowButtonDto;
import model.monsters.TigerMonster;
import controller.Stage2Controller;
import model.Storage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

//게임 패널 설정
public class Stage2Panel {

    private Timer timer;
    private ImagePanel imagePanel;
    private Container container;
    private JPanel panel;
    private JLayeredPane layeredPane;

    private int panelWidth = 1500;
    private int panelHeight = 1000;
    private TigerMonster tigerMonster;
    private Stage2Controller stage2Controller;
    private Stage2BearKeyListener stage2BearKeyListener;
    private Stage2TigerKeyListener stage2TigerKeyListener;

    private JLabel bearPlayer;
    private JLabel tigerPlayer;
    private Storage storage;

    private boolean isRedButtonPressed = false;
    private boolean isBlueButtonPressed = false;
    private boolean isYellowButtonPressed = false;
    private boolean isLeverPressed = false;
    private boolean isRockFalled = false;
    private boolean isFalling = false;
    private boolean isTigerPlayer=false;
    private boolean isBearPlayerFalling = false;
    private boolean isTigerPlayerFalling = false;

    private JLabel[] roads;
    private JLabel step1,step2;
    private JLabel floorButton1,floorButton2,floorButton3;
    private JLabel lever,ladder;
    private JLabel rock1,rock2,bigRock;
    private JLabel exit, monsterEntrance, monsterExit;
    private JLabel wall1, wall2, wall3,wall4;
    private JLabel portal1,portal2;

    public Stage2Panel() {

        stage2Controller = new Stage2Controller(this);
    }

    public void setStage2Panel(Container container){
        this.container = container;

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(container.getSize());

        setBackgroundPanel();
        setRoad();
        setElements();
        setMonster();
        setPlayer();

        container.add(layeredPane);
    }

    private void setBackgroundPanel() {
        imagePanel = new ImagePanel();
        imagePanel.setImage("src/assets/image/background/game_background.png");
        imagePanel.setSize(new Dimension(panelWidth, panelHeight));
        imagePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        imagePanel.setMinimumSize(new Dimension(panelWidth, panelHeight));
        layeredPane.add(imagePanel, JLayeredPane.DEFAULT_LAYER);

    }

    private void setRoad(){
        panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(new Dimension(panelWidth, panelHeight));
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        panel.setMinimumSize(new Dimension(panelWidth, panelHeight));
        panel.setOpaque(false);

        // 도로 이미지 위치 좌표 배열
        int[][] roadPositions = {{1050,930}, {950, 930}, {400, 930}, {0, 930}, {0, 730}, {550,730}, {900,730}, {1050,730}, {150,530}, {250,530}, {800,530}, {-100,330}, {450,330}, {1000,330},{1350,330}, {0,130}, {200,130}};

        int t=0;
        roads = new JLabel[roadPositions.length];
        // 각 위치에 대해 도로 이미지를 생성하고 추가
        for (int[] pos : roadPositions) {
            JLabel road = new JLabel(new ImageIcon("src/assets/image/background/road.png"));
            road.setSize(road.getPreferredSize());
            road.setBounds(pos[0], pos[1], road.getPreferredSize().width, road.getPreferredSize().height);
            roads[t++]=road;
            panel.add(road);
        }
        layeredPane.add(panel, JLayeredPane.PALETTE_LAYER);
    }

    private void setPlayer(){
        bearPlayer = new JLabel();
        tigerPlayer = new JLabel();

        try {
            storage = Storage.getInstance(1050,880);
            bearPlayer.setIcon(new ImageIcon(storage.getBear().getCurrentIcon().getImage()));
            tigerPlayer.setIcon(new ImageIcon(storage.getTiger().getCurrentIcon().getImage()));
        } catch (IOException e){
            System.out.println("파일을 읽어들이는 데에 실패했습니다. 프로그램을 다시 실행해 주세요.");
        }

        bearPlayer.setBounds(1050,880, 64, 64);
        tigerPlayer.setBounds(1150,880, 64, 64);
        panel.add(bearPlayer);
        panel.add(tigerPlayer);
        panel.setComponentZOrder(bearPlayer, 0);
        panel.setComponentZOrder(tigerPlayer, 0);

        stage2BearKeyListener = new Stage2BearKeyListener(storage.getBear());
        stage2TigerKeyListener = new Stage2TigerKeyListener(storage.getTiger());

        container.addKeyListener(stage2BearKeyListener);
        container.addKeyListener(stage2TigerKeyListener);
        container.setFocusable(true);
        container.requestFocusInWindow();

        Timer timer = new Timer(16, e -> updateCharacterPositions());
        timer.start();
    }

    public void updateCharacterPositions() {
        // 현재 방향에 맞는 아이콘을 가져와서 설정
        bearPlayer.setIcon(storage.getBear().getCurrentIcon());
        tigerPlayer.setIcon(storage.getTiger().getCurrentIcon());

        // 캐릭터 위치 업데이트
        bearPlayer.setLocation(storage.getBear().x, storage.getBear().y);
        tigerPlayer.setLocation(storage.getTiger().x, storage.getTiger().y);
        setElementsInteraction();

        container.repaint();
    }

    private void setElementsInteraction(){

        //redButton 발판 움직임 설정
        isRedButtonPressed = stage2Controller.seRedButtonInteraction(new RedButtonDto(storage,floorButton1,tigerPlayer,bearPlayer,rock1,rock2,step2,isRedButtonPressed));

        //blueButton 벽 움직임 설정
        isBlueButtonPressed = stage2Controller.setBlueButtonInteraction(new BlueButtonDto(isBlueButtonPressed,floorButton2,wall4,tigerPlayer,bearPlayer));

        //yellowButton 포탈 활성화 설정
        isYellowButtonPressed = stage2Controller.setYellowButtonInteraction(new YellowButtonDto(isYellowButtonPressed, floorButton3,tigerPlayer,bearPlayer,portal1,portal2));

        //레버로 발판 움직임 설정
        isLeverPressed = stage2Controller.setLeverInteraction(isLeverPressed, lever,tigerPlayer,bearPlayer,step1);

        //돌 떨어짐 구현
        isRockFalled = stage2Controller.setRockFallingInteraction(rock2, roads,isRockFalled);

        //돌, 곰 플레이어 상호작용 설정
        stage2Controller.setRockInteraction(storage,rock1,bearPlayer);
        stage2Controller.setRockInteraction(storage,rock2,bearPlayer);

        if (isLabelOverlapping(ladder, tigerPlayer)) {
            stage2TigerKeyListener.updateLadderState(true);
        } else {
            stage2TigerKeyListener.updateLadderState(false);
        }

        isTigerPlayer = stage2Controller.setTigerPlayerWallInteraction(wall1,wall2,wall3, tigerPlayer,storage,isTigerPlayer);

        if (!isFalling &&!isLabelOverlappingRoads(tigerMonster.getLabel(), roads) && !isLabelOverlapping(tigerMonster.getLabel(), step2)) {
            isFalling = true;
            stage2Controller.animateElement(tigerMonster.getLabel(), tigerMonster.getLabel().getY() + 200);

            timer = new Timer(1000, e -> checkIfLanded());
            timer.setRepeats(false);
            timer.start();

        }

        if(!stage2BearKeyListener.getBearPlayerJump() && !isBearPlayerFalling &&!isLabelOverlappingRoads(bearPlayer, roads) && !isLabelOverlapping(bearPlayer, step2)){
            isBearPlayerFalling = true;
            stage2Controller.animateBearPlayer(storage.getBear(), storage.getBear().y + 200);

            timer = new Timer(1000, e -> checkIfBearPlayerLanded());
            timer.setRepeats(false);
            timer.start();
        }

        if(!stage2TigerKeyListener.getTigerPlayerJump()  &&!isTigerPlayerFalling &&!isLabelOverlappingRoads(tigerPlayer, roads) && !isLabelOverlapping(tigerPlayer, step2) && !isLabelOverlapping(tigerPlayer,ladder) && !isLabelOverlapping(tigerPlayer,wall1) && !isLabelOverlapping(tigerPlayer,wall2)&& !isLabelOverlapping(tigerPlayer,wall3)){
            isTigerPlayerFalling = true;
            stage2Controller.fallTigerPlayer(storage.getTiger(), storage.getTiger().y + 200);

            timer = new Timer(1000, e -> checkIfTigerPlayerLanded());
            timer.setRepeats(false);
            timer.start();
        }

        //호랑이몬스터와 돌 부딪혔을때 호랑이 몬스터 방향전환
        stage2Controller.setTigerMonsterMovement(tigerMonster,rock1, rock2);
        //호랑이 몬스터가 출구에 도착했을때 삭제
        stage2Controller.removeTigerMonster(tigerMonster,monsterExit,panel);
    }

    private void checkIfLanded() {
        if (isLabelOverlappingRoads(tigerMonster.getLabel(), roads)) {
            isFalling = false;
        }
    }

    private void checkIfTigerPlayerLanded(){
        if (isLabelOverlappingRoads(tigerPlayer, roads)) {
            isTigerPlayerFalling = false;
        }
    }
    private void checkIfBearPlayerLanded(){
        if (isLabelOverlappingRoads(bearPlayer, roads)) {
            isBearPlayerFalling = false;
        }
    }
    //몬스터 설정
    private void setMonster(){
        tigerMonster = new TigerMonster(50,75);
        tigerMonster.setMonster(panelWidth, panelHeight, panel);
    }

    private void setElements(){
        wall1 = createScaledLabel("src/assets/image/stage2/wall.png",50,100,1190,550);
        wall2 = createScaledLabel("src/assets/image/stage2/wall.png",50,100,1000,350);
        wall3 = createScaledLabel("src/assets/image/stage2/wall.png",50,100,595,150);
        wall4 = createScaledLabel("src/assets/image/stage2/wall.png",50,120,800,0);

        floorButton1 = createScaledLabel("src/assets/image/stage2/red_floor_button.png", 30, 30, 80, 710);
        floorButton2 = createScaledLabel("src/assets/image/stage2/blue_floor_button.png", 30, 30, 800, 310);
        floorButton3 = createScaledLabel("src/assets/image/stage2/yellow_floor_button.png", 30, 30, 1150, 310);

        step1 = createScaledLabel("src/assets/image/stage2/step.png", 110, 40, 340, 530);
        step2 = createScaledLabel("src/assets/image/stage2/step.png", 110, 40, 440, 730);

        portal1 = createScaledLabel("src/assets/image/stage2/nonactive_portal.png", 120, 120, 500, 420);
        portal2 = createScaledLabel("src/assets/image/stage2/nonactive_portal.png", 120, 120, 950, 620);

        rock1 = createScaledLabel("src/assets/image/stage2/rock.png",60,60,580,680);
        rock2 = createScaledLabel("src/assets/image/stage2/rock.png",60,60,200,480);
        bigRock = createScaledLabel("src/assets/image/stage2/gray_stone.png",100,100,1300,240);

        ladder = createScaledLabel("src/assets/image/stage2/ladder.png",150,250,1050,700);

        lever = createScaledLabel("src/assets/image/stage2/lever_up.png", 40, 40, 0, 265);

        exit = createScaledLabel("src/assets/image/exit.png",150,150,1300,802);

        monsterEntrance = createScaledLabel("src/assets/image/stage2/monster_exit.png",120,120,50,12);
        monsterExit = createScaledLabel("src/assets/image/stage2/monster_exit.png",120,120,50,812);

    }


    private boolean isLabelOverlapping(JLabel label1, JLabel label2) {
        Rectangle bounds1 = label1.getBounds();
        Rectangle bounds2 = label2.getBounds();
        return bounds1.intersects(bounds2);
    }

    private boolean isLabelOverlappingRoads(JLabel label, JLabel[] roads){
        boolean isOverlapped = false;
        for (JLabel road : roads) {
            if (isLabelOverlapping(road, label)) {
                isOverlapped = true;
                break;
            }
        }
        return isOverlapped;

    }

    private JLabel createScaledLabel(String imagePath, int width, int height, int x, int y) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(resizedIcon);
        label.setSize(resizedIcon.getIconWidth(), resizedIcon.getIconHeight());
        label.setBounds(x, y, resizedIcon.getIconWidth(), resizedIcon.getIconHeight());
        panel.add(label);
        panel.setComponentZOrder(label, 0);
        return label;
    }


}
