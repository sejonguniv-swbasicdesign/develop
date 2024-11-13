package view.container.panel;

import actionlistener.BearKeyListener;
import actionlistener.TigerKeyListener;
import model.monsters.TigerMonster;
import controller.Stage2Controller;
import model.Storage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

//게임 패널 설정
public class Stage2Panel {

    private ImagePanel imagePanel;
    private Container container;
    private JPanel panel;
    private JLayeredPane layeredPane;

    private int panelWidth = 1500;
    private int panelHeight = 1000;
    private TigerMonster tigerMonster;
    private Stage2Controller stage2Controller;

    private JLabel bearPlayer;
    private JLabel tigerPlayer;
    private Storage storage;

    private boolean isRedButtonPressed = false;
    private boolean isBlueButtonPressed = false;
    private boolean isYellowButtonPressed = false;
    private boolean isLeverPressed = false;
    private boolean isFalled = false;

    private JLabel[] roads;
    private JLabel step1,step2;
    private JLabel floorButton1,floorButton2,floorButton3;
    private JLabel lever,ladder;
    private JLabel rock1,rock2,bigRock;
    private JLabel exit, monsterEntrance, monsterExit;
    private JLabel wall1, wall2, wall3,wall4;
    private JLabel portal1,portal2;

    public Stage2Panel() {

        stage2Controller = new Stage2Controller();
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

        container.addKeyListener(new BearKeyListener(storage.getBear()));
        container.addKeyListener(new TigerKeyListener(storage.getTiger()));
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
        //redButton 발판 설정
        if (!isRedButtonPressed && (isLabelOverlapping(floorButton1, tigerPlayer) || isLabelOverlapping(floorButton1, bearPlayer)|| isLabelOverlapping(floorButton1, rock1) || isLabelOverlapping(floorButton1, rock2))) {
            isRedButtonPressed = true; // 위치 변경 후 상태 유지
            if(isLabelOverlapping(rock1,step2)){
                stage2Controller.animateStep(rock1, rock1.getY() + 200);
            }
            stage2Controller.animateStep(step2, step2.getY() + 200);
        }
        else if (isRedButtonPressed && !isLabelOverlapping(floorButton1, tigerPlayer) && !isLabelOverlapping(floorButton1, bearPlayer) && !isLabelOverlapping(floorButton1, rock1)&& !isLabelOverlapping(floorButton1, rock2)) {
            isRedButtonPressed = false; // 상태 초기화
            stage2Controller.animateStep(step2, step2.getY() -200);
        }

        //blueButton 벽 움직임 설정
        if (!isBlueButtonPressed && (isLabelOverlapping(floorButton2, tigerPlayer) || isLabelOverlapping(floorButton2, bearPlayer))) {
            isBlueButtonPressed = true; // 위치 변경 후 상태 유지
            stage2Controller.animateStep(wall4, wall4.getY() - 120);

        }
        else if (isBlueButtonPressed && !isLabelOverlapping(floorButton2, tigerPlayer) && !isLabelOverlapping(floorButton2, bearPlayer)) {
            isBlueButtonPressed = false; // 상태 초기화
            stage2Controller.animateStep(wall4, wall4.getY() + 120);
        }

        //yellowButton 포탈 활성화 설정
        if (!isYellowButtonPressed && (isLabelOverlapping(floorButton3, tigerPlayer) || isLabelOverlapping(floorButton3, bearPlayer))) {
            isYellowButtonPressed = true;
            ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/active_portal.png");
            portal1.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
            portal2.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
        }
        else if (isYellowButtonPressed && !isLabelOverlapping(floorButton3, tigerPlayer) && !isLabelOverlapping(floorButton3, bearPlayer)) {
            isYellowButtonPressed = false;
            ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/nonactive_portal.png");
            portal1.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
            portal2.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(120,120, Image.SCALE_SMOOTH)));
        }

        //레버로 발판 움직임 설정
        if(!isLeverPressed && (isLabelOverlapping(lever, tigerPlayer) || isLabelOverlapping(lever, bearPlayer))){
            isLeverPressed = true;
            ImageIcon originalIcon = new ImageIcon("src/assets/image/stage2/lever_down.png");
            lever.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH)));
            stage2Controller.animateStep(step1, step1.getY() - 200);
        }

        //돌 떨어짐 구현
        if(!isLabelOverlapping(rock2,roads[8]) && !isFalled){
            isFalled = true;
            stage2Controller.animateStep(rock2, rock2.getY() + 200);
        }

        //돌, 곰 플레이어 상호작용 설정
        if (isLabelOverlapping(rock1, bearPlayer)) {
            if (storage.getBear().isFacingRight) {
                // 곰이 오른쪽을 바라보면 상자도 오른쪽으로 밀기
                rock1.setLocation(rock1.getX() + 10, rock1.getY());  // 10만큼 오른쪽으로 이동
            } else {
                // 곰이 왼쪽을 바라보면 상자도 왼쪽으로 밀기
                rock1.setLocation(rock1.getX() - 10, rock1.getY());  // 10만큼 왼쪽으로 이동
            }
        }
        if (isLabelOverlapping(rock2, bearPlayer)) {
            if (storage.getBear().isFacingRight) {
                // 곰이 오른쪽을 바라보면 상자도 오른쪽으로 밀기
                rock2.setLocation(rock2.getX() + 10, rock2.getY());  // 10만큼 오른쪽으로 이동
            } else {
                // 곰이 왼쪽을 바라보면 상자도 왼쪽으로 밀기
                rock2.setLocation(rock2.getX() - 10, rock2.getY());  // 10만큼 왼쪽으로 이동
            }
        }
    }

    //몬스터 설정
    private void setMonster(){
        tigerMonster = new TigerMonster(50,75);
        tigerMonster.setMonster(panelWidth, panelHeight, panel);
    }

    private void setElements(){
        wall1 = createScaledLabel("src/assets/image/stage2/wall.png",50,120,1230,530);
        wall2 = createScaledLabel("src/assets/image/stage2/wall.png",50,120,1000,330);
        wall3 = createScaledLabel("src/assets/image/stage2/wall.png",50,120,595,130);
        wall4 = createScaledLabel("src/assets/image/stage2/wall.png",50,120,800,0);

        floorButton1 = createScaledLabel("src/assets/image/stage2/red_floor_button.png", 30, 30, 80, 710);
        floorButton2 = createScaledLabel("src/assets/image/stage2/blue_floor_button.png", 30, 30, 800, 310);
        floorButton3 = createScaledLabel("src/assets/image/stage2/yellow_floor_button.png", 30, 30, 1150, 310);

        step1 = createScaledLabel("src/assets/image/stage2/step.png", 110, 40, 340, 530);
        step2 = createScaledLabel("src/assets/image/stage2/step.png", 110, 40, 440, 730);

        portal1 = createScaledLabel("src/assets/image/stage2/nonactive_portal.png", 120, 120, 500, 420);
        portal2 = createScaledLabel("src/assets/image/stage2/nonactive_portal.png", 120, 120, 950, 620);

        rock1 = createScaledLabel("src/assets/image/stage2/rock.png",60,60,650,680);
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
