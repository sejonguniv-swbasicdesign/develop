package view.component.third;

import javax.swing.*;
import controller.movement.MoveInteraction;
import controller.RockController;
import utils.constants.PlayerPosition;
import model.Storage;
import actionlistener.BearKeyListener;
import actionlistener.TigerKeyListener;
import model.monsters.Boss;

import java.awt.*;

public class PlayerInitializerPanel extends JPanel {

    private JLabel bearLabel;
    private JLabel tigerLabel;
    private Storage storage;
    private MoveInteraction moveInteraction;
    private BearKeyListener bearKeyListener;
    private TigerKeyListener tigerKeyListener;
    private JLayeredPane layeredPane;
    private JPanel tigerPanel;
    private JPanel bearPanel;

    private RockController rockController;
    private JLabel bossLabel;
    private Boss boss;

    private boolean isBearFainted = false;  // 곰이 기절했는지 여부
    private boolean isTigerFainted = false; // 호랑이가 기절했는지 여부

    public JLabel getTigerLabel() {
        return tigerLabel;
    }

    public JLabel getBearLabel() {
        return bearLabel;
    }

    public PlayerInitializerPanel(JLayeredPane panel, JLabel bossLabel) {
        storage = Storage.getInstance();
        tigerPanel = storage.getTigerPanel();
        bearPanel = storage.getBearPanel();

        this.bossLabel = bossLabel;
        this.layeredPane = panel;
        this.boss = storage.getBoss();

        this.moveInteraction = new MoveInteraction();
        this.rockController = new RockController(panel);
        rockController.generateRocks(); // 초기 돌 생성

        setLayout(null);
        setOpaque(false);

        setBounds(0, 0, 1300, 800); // 또는 부모 패널의 크기와 동일하게 설정
        setPreferredSize(new Dimension(1300, 800));

        setLabel();

        // 주기적으로 캐릭터 위치 업데이트
        Timer timer = new Timer(16, e -> {
            moveInteraction.updateCharacterPositions(bearLabel, tigerLabel);
            repaint();
        });

        timer.start();

        setFocusable(true);
        requestFocusInWindow();
    }

    private void setLabel() {
        bearLabel = new JLabel();
        tigerLabel = new JLabel();

        bearLabel.setIcon(new ImageIcon(storage.getBear().getCurrentIcon().getImage()));
        tigerLabel.setIcon(new ImageIcon(storage.getTiger().getCurrentIcon().getImage()));

        storage.getBear().x = PlayerPosition.BEAR_START.getX();
        storage.getBear().y = PlayerPosition.BEAR_START.getY();
        storage.getTiger().x = PlayerPosition.TIGER_START.getX();
        storage.getTiger().y = PlayerPosition.TIGER_START.getY();

        bearPanel.setLayout(null);
        bearPanel.setBounds(storage.getBear().x, storage.getBear().y, storage.getBear().getWidth(), storage.getBear().getHeight());
        bearPanel.setOpaque(false);
        bearLabel.setBounds(storage.getBear().x, storage.getBear().y, storage.getBear().getWidth(), storage.getBear().getHeight());
        bearPanel.add(bearLabel);

        System.out.println("Bear Panel Bounds: " + bearPanel.getBounds());
        System.out.println("Bear Label Bounds: " + bearLabel.getBounds());
        System.out.println("Bear Icon: " + bearLabel.getIcon());

        System.out.println("PlayerInitializerPanel Bounds: " + this.getBounds());
        System.out.println("LayeredPane Bounds: " + layeredPane.getBounds());

        System.out.println("Bear Icon Size: " + storage.getBear().getCurrentIcon().getIconWidth() + "x" + storage.getBear().getCurrentIcon().getIconHeight());


        tigerPanel.setLayout(null);
        tigerPanel.setBounds(storage.getTiger().x, storage.getTiger().y, storage.getTiger().getWidth(), storage.getTiger().getHeight());
        tigerPanel.setOpaque(false);
        tigerLabel.setBounds(storage.getTiger().x, storage.getTiger().y, storage.getTiger().getWidth(), storage.getTiger().getHeight());
        tigerPanel.add(tigerLabel);

        add(bearPanel);
        add(tigerPanel);
        revalidate();
        repaint();
    }


    public void disableBearKeyListener() {
        removeKeyListener(bearKeyListener); // 곰의 키 리스너 제거
        System.out.println("곰의 키 리스너가 비활성화되었습니다.");
    }

    public void enableBearKeyListener() {
        addKeyListener(bearKeyListener); // 곰의 키 리스너 복원
        requestFocusInWindow();
        System.out.println("곰의 키 리스너가 활성화되었습니다.");
    }

    public void disableTigerKeyListener() {
        removeKeyListener(tigerKeyListener); // 호랑이의 키 리스너 제거
        System.out.println("호랑이의 키 리스너가 비활성화되었습니다.");
    }

    public void enableTigerKeyListener() {
        addKeyListener(tigerKeyListener); // 호랑이의 키 리스너 복원
        requestFocusInWindow();
        System.out.println("호랑이의 키 리스너가 활성화되었습니다.");
    }

    public void setBearFainted(boolean fainted) {
        this.isBearFainted = fainted;
        checkFocusState();
    }

    public void setTigerFainted(boolean fainted) {
        this.isTigerFainted = fainted;
        checkFocusState();
    }

    private void checkFocusState() {
        // 두 캐릭터 중 하나라도 기절했으면 포커스를 다른 패널로 이동
        if (isBearFainted || isTigerFainted) {
            JPanel dummyPanel = new JPanel(); // 더미 패널
            dummyPanel.setFocusable(true);   // 포커스 가능 설정
            this.add(dummyPanel);
            dummyPanel.requestFocusInWindow(); // 포커스 전환
            System.out.println("포커스가 캐릭터 패널에서 제거되었습니다.");
        } else {
            // 둘 다 기절 상태가 아니면 다시 포커스 복원
            this.requestFocusInWindow();
            System.out.println("포커스가 캐릭터 패널로 복원되었습니다.");
        }
    }

}
