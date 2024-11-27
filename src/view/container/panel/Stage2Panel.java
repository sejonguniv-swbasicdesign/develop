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
import java.util.ArrayList;
import java.util.List;

//게임 패널 설정
public class Stage2Panel {

    private StageClearPanel stageClearPanel;
    private ImagePanel imagePanel;
    private Container container;
    private JPanel panel;
    private JLayeredPane layeredPane;
    private JPanel hpPanel;

    private final int panelWidth = 1500;
    private final int panelHeight = 1000;
    private final Stage2Controller stage2Controller;
    private Stage2BearKeyListener stage2BearKeyListener;
    private Stage2TigerKeyListener stage2TigerKeyListener;
    private Timer monsterSpawnTimer;

    private JLabel bearPlayer;
    private JLabel tigerPlayer;
    private Storage storage;

    private boolean isRedButtonPressed = false;
    private boolean isBlueButtonPressed = false;
    private boolean isYellowButtonPressed = false;
    private boolean isLeverPressed = false;
    private boolean isRockFalled = false;
    private ArrayList<Boolean> isTigerFalling;
    private boolean isTigerPlayer=false;
    private boolean isBearPlayerFalling = false;
    private boolean isTigerPlayerFalling = false;
    private boolean isCooldown = false;
    private boolean isLeverCooldown = false;
    private int itemCount = 0;
    private int hpCount = 0;

    private List<JLabel> hps;
    private JLabel[] roads;
    private ArrayList<TigerMonster> tigers;
    private JLabel step1,step2;
    private JLabel floorButton1,floorButton2,floorButton3;
    private JLabel lever,ladder;
    private JLabel rock1,rock2,bigRock;
    private JLabel exit, monsterEntrance, monsterExit;
    private JLabel wall1, wall2, wall3,wall4;
    private JLabel portal1,portal2;
    private JLabel item1, item2 ,item3, item4;


    public Stage2Panel() {
        stage2Controller = new Stage2Controller();
    }

    public void setStage2Panel(Container container){
        this.container = container;
        stageClearPanel = new StageClearPanel();
        stageClearPanel.setPanel();
        stageClearPanel.setBounds(750,500,500,400);
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(container.getSize());

        setBackgroundPanel();
        setRoad();
        setElements();
        setMonster();
        setPlayer();
        setHP();

        container.add(layeredPane);
//        layeredPane.add(stageClearPanel);
//        panel.setComponentZOrder(stageClearPanel, 0);
    }

    private void setHP(){
        hps = new ArrayList<>();
        hpPanel = new JPanel();
        hpPanel.setLayout(new GridLayout(1, 3)); // 3개의 세로 레이아웃
        hpPanel.setBounds(1300,0,200,50);
        hpPanel.setOpaque(false);
        panel.add(hpPanel);

        for (int i = 1; i <= 3; i++) {
            ImageIcon originalIcon = new ImageIcon("src/assets/image/component/하트.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);
            JLabel label = new JLabel(resizedIcon);
            label.setSize(resizedIcon.getIconWidth(), resizedIcon.getIconHeight());
            hps.add(label);
            hpPanel.add(label);
        }
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

    private void updateCharacterPositions() {
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
        setLeverInteraction();

        //돌 떨어짐 구현
        isRockFalled = stage2Controller.setRockFallingInteraction(rock2, roads,isRockFalled);

        //돌, 곰 플레이어 상호작용 설정
        stage2Controller.setRockInteraction(storage,rock1,bearPlayer,stage2BearKeyListener);
        stage2Controller.setRockInteraction(storage,rock2,bearPlayer,stage2BearKeyListener);

        //호랑이 플레이어와 사다리 상호작용 설정
        stage2Controller.setLadderInteraction(ladder,tigerPlayer,stage2TigerKeyListener);

        //포탈 상호작용 설정
        stage2Controller.setPortalInteraction(isYellowButtonPressed,portal1,portal2,bearPlayer,stage2BearKeyListener);

        //호랑이 플레이어가 벽타고 올라가는 로직 설정
        isTigerPlayer = stage2Controller.setTigerPlayerWallInteraction(wall1,wall2,wall3, tigerPlayer,storage,isTigerPlayer);

        //호랑이몬스터, 플레이어들 떨어짐 설정
        setFalling();

        //호랑이몬스터와 돌 부딪혔을때 호랑이 몬스터 방향전환
        stage2Controller.setTigerMonsterMovement(tigers,rock1, rock2);
        //호랑이 몬스터가 출구에 도착했을때 삭제
        stage2Controller.removeTigerMonster(tigers,monsterExit,panel,isTigerFalling);
        //곰이 큰 바위를 들었을때 상호작용
        stage2Controller.setBigRockInteraction(bigRock,bearPlayer,stage2BearKeyListener,wall4);
        //곰과 몬스터가 부딪혔을때 hp감소시키는 로직
        setMonsterBearInteraction();

        if(bigRock.getX() == 500 && bigRock.getY()==40){
            stopMonsterSpawn();
        }
        //아이템 먹은 개수 확인
        itemCount = stage2Controller.setItemInteraction(tigerPlayer,bearPlayer,item1,item2,item3,item4,itemCount,panel);

        //게임 클리어 조건 설정
        stage2Controller.checkStageFinish(itemCount,bigRock,tigerPlayer,bearPlayer,exit,panel,tigers);

        //게임 오버 조건 설정
        stage2Controller.checkGameOver(bearPlayer,tigerPlayer,tigers,hpCount);
    }
    
    //곰과 호랑이 몬스터가 부딪혔을때 하트 하나 삭제
    private void setMonsterBearInteraction(){
        if(stage2Controller.isTigerMonsterOverlapping(bearPlayer,tigers)!=-1 && !stage2BearKeyListener.getBearPlayerJump()){
            if (isCooldown) {
                return; // 유예 중일 때는 아무 작업도 하지 않음
            }

            if (stage2Controller.isTigerMonsterOverlapping(bearPlayer,tigers)!=-1 && !stage2BearKeyListener.getBearPlayerJump()) {
                for (JLabel hp : hps) {
                    if (hp.isVisible()) {
                        hp.setVisible(false); // 해당 라벨을 invisible 처리
                        startCooldown(); // 3초 유예 시작
                        break; // 첫 번째 visible 라벨만 처리하고 종료
                    }
                }
                hpCount+=1;
            }
        }
    }

    private void startCooldown() {
        isCooldown = true; // 유예 시작
        Timer timer = new Timer(3000, e -> isCooldown = false); // 3초 후 유예 해제
        timer.setRepeats(false); // 한 번만 실행되도록 설정
        timer.start();
    }

    //레버 상호작용 설정
    private void setLeverInteraction() {
        // 레버가 눌리지 않은 상태에서 플레이어가 레버와 겹치면
        if (!isLeverPressed && !isLeverCooldown &&
                (stage2Controller.isLabelOverlapping(lever, tigerPlayer) || stage2Controller.isLabelOverlapping(lever, bearPlayer))) {
            isLeverPressed = true;
            isLeverCooldown = true; // 유예 상태로 설정
            // 레버 이미지 변경
            ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/lever_down.png");
            lever.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

            // 발판과 곰의 상태 변경
            if (stage2Controller.isLabelOverlapping(bearPlayer, step1)) {
                stage2Controller.animateBearPlayer(storage.getBear(), storage.getBear().y - 200); // 곰 위치 아래로 이동
            }
            stage2Controller.animateElement(step1, step1.getY() - 200); // 발판 위치 아래로 이동

            // 레버 쿨다운 타이머 시작
            startLeverCooldown();
        }
        // 레버가 이미 눌린 상태에서 중복 호출 방지
        else if (isLeverPressed && !isLeverCooldown &&
                (stage2Controller.isLabelOverlapping(lever, tigerPlayer) || stage2Controller.isLabelOverlapping(lever, bearPlayer))) {
            initLever();
            startLeverCooldown();
        }
    }

    // 3초 뒤 레버 상태 초기화
    private void startLeverCooldown() {
        Timer timer = new Timer(3000, e -> {
            isLeverCooldown = false; // 중복 호출 가능하도록 설정
        });
        timer.setRepeats(false); // 한 번만 실행
        timer.start();
    }

    // 레버와 발판 초기화
    private void initLever() {
        isLeverPressed = false;
        isLeverCooldown = true;
        // 레버 이미지를 원래 상태로 변경
        ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/lever_up.png");
        lever.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

        // 발판과 곰의 상태를 원래 위치로 복원
        if (stage2Controller.isLabelOverlapping(bearPlayer, step1)) {
            stage2Controller.animateBearPlayer(storage.getBear(), storage.getBear().y + 200); // 곰 위치 원래대로 복원
        }
        stage2Controller.animateElement(step1, step1.getY() + 200); // 발판 위치 원래대로 복원
    }

    //호랑이몬스터, 플레이어들 떨어짐 설정(Controller로 분리 실패)
    private void setFalling(){
        //호랑이 몬스터
        for (int i = 0 ;i<tigers.size();i++) {
            if (!isTigerFalling.get(i) && !stage2Controller.isLabelOverlappingRoads(tigers.get(i).getLabel(), roads) && !stage2Controller.isLabelOverlapping(tigers.get(i).getLabel(), step2) &&!stage2Controller.isLabelOverlapping(tigers.get(i).getLabel(), step1)) {
                isTigerFalling.set(i,true);
                stage2Controller.animateElement(tigers.get(i).getLabel(), tigers.get(i).getLabel().getY() + 200);

                int finalI = i;
                Timer timer = new Timer(1300, e -> checkIfLanded(tigers.get(finalI).getLabel(),finalI));
                timer.setRepeats(false);
                timer.start();

            }
        }

        //곰 플레이어
        if(!stage2BearKeyListener.getBearPlayerJump() && !isBearPlayerFalling &&!stage2Controller.isLabelOverlappingRoads(bearPlayer, roads) && !stage2Controller.isLabelOverlapping(bearPlayer, step2)&& !stage2Controller.isLabelOverlapping(bearPlayer, step1)){
            isBearPlayerFalling = true;
            stage2Controller.animateBearPlayer(storage.getBear(), storage.getBear().y + 200);

            Timer timer = new Timer(1500, e -> checkIfBearPlayerLanded());
            timer.setRepeats(false);
            timer.start();
        }
        //호랑이 플레이어
        if(!stage2TigerKeyListener.getTigerPlayerJump()  &&!isTigerPlayerFalling &&!stage2Controller.isLabelOverlappingRoads(tigerPlayer, roads) && !stage2Controller.isLabelOverlapping(tigerPlayer, step2)&& !stage2Controller.isLabelOverlapping(tigerPlayer, step1) && !stage2Controller.isLabelOverlapping(tigerPlayer,ladder) && !stage2Controller.isLabelOverlapping(tigerPlayer,wall1) && !stage2Controller.isLabelOverlapping(tigerPlayer,wall2)&& !stage2Controller.isLabelOverlapping(tigerPlayer,wall3)){
            isTigerPlayerFalling = true;
            stage2Controller.fallTigerPlayer(storage.getTiger(), storage.getTiger().y + 200);

            Timer timer = new Timer(1500, e -> checkIfTigerPlayerLanded());
            timer.setRepeats(false);
            timer.start();
        }
    }

    //호랑이 몬스터가 도로 위에 착지 했는지 확인
    private void checkIfLanded(JLabel label,int i) {
        if (stage2Controller.isLabelOverlappingRoads(label, roads)) {
            isTigerFalling.set(i,false);
        }
    }

    //호랑이 플레이어가 도로 위에 착지 했는지 확인
    private void checkIfTigerPlayerLanded(){
        if (stage2Controller.isLabelOverlappingRoads(tigerPlayer, roads)) {
            isTigerPlayerFalling = false;
        }
    }

    //곰 플레이어가 도로 위에 착지 했는지 확인
    private void checkIfBearPlayerLanded(){
        if (stage2Controller.isLabelOverlappingRoads(bearPlayer, roads)) {
            isBearPlayerFalling = false;
        }
    }
    //몬스터 설정
    private void setMonster(){
        tigers= new ArrayList<>();
        isTigerFalling = new ArrayList<>();
        TigerMonster tigerMonster = new TigerMonster(50, 75);
        tigerMonster.setMonster(panelWidth, panelHeight, panel);
        tigers.add(tigerMonster);
        isTigerFalling.add(false);
        container.repaint();

         monsterSpawnTimer = new Timer(20000, e ->SwingUtilities.invokeLater(()-> {
            TigerMonster tigerMonster2 = new TigerMonster(50, 75);
            tigerMonster2.setMonster(panelWidth, panelHeight, panel);
            tigers.add(tigerMonster2);
            isTigerFalling.add(false);
            container.repaint();
        }));
        monsterSpawnTimer.start();
    }

    private void stopMonsterSpawn() {
        if (monsterSpawnTimer != null && monsterSpawnTimer.isRunning()) {
            monsterSpawnTimer.stop(); // 타이머 중단
        }
    }

    //스테이지 구성 요소들 라벨 생성하여 각 위치에 배치
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

        item1 = createScaledLabel("src/assets/image/item/garlic.png",40,40,370,460);
        item2 = createScaledLabel("src/assets/image/item/garlic.png",40,40,300,60);
        item3 = createScaledLabel("src/assets/image/item/mugwort.png",40,40,60,260);
        item4 = createScaledLabel("src/assets/image/item/mugwort.png",40,40,1430,260);

        exit = createScaledLabel("src/assets/image/exit.png",150,150,1300,802);

        monsterEntrance = createScaledLabel("src/assets/image/stage2/monster_exit.png",120,120,50,12);
        monsterExit = createScaledLabel("src/assets/image/stage2/monster_exit.png",120,120,50,812);

    }

    //요소마다 라벨 생성
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
