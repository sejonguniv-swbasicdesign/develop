package view.component.third;

import javax.swing.*;
import controller.MoveInteraction;
import controller.RockController;
import model.PlayerPosition;
import model.Storage;
import actionlistener.BearKeyListener;
import actionlistener.TigerKeyListener;
import model.monsters.Boss;

public class PlayerInitializerPanel extends JPanel {

    private JLabel bearLabel;
    private JLabel tigerLabel;
    private Storage storage;
    private MoveInteraction moveInteraction;
    private BearKeyListener bearKeyListener;
    private TigerKeyListener tigerKeyListener;
    private JLayeredPane layeredPane;

    private RockController rockController;
    private JLabel bossLabel;
    private Boss boss;

    public JLabel getTigerLabel() {
        return tigerLabel;
    }

    public JLabel getBearLabel() {
        return bearLabel;
    }

    public PlayerInitializerPanel(JLayeredPane panel, JLabel bossLabel, JLabel cloudLabel) {
        moveInteraction = new MoveInteraction();
        this.bossLabel = bossLabel;
        this.layeredPane = panel;
        this.storage = Storage.getInstance();
        this.boss = storage.getBoss();
        this.rockController = new RockController(panel, cloudLabel);
        storage = Storage.getInstance();
        rockController.generateRocks(); // 초기 돌 생성

        setLayout(null);
        setOpaque(false);

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

        bearLabel.setBounds(storage.getBear().x, storage.getBear().y, 100, 100);
        tigerLabel.setBounds(storage.getTiger().x, storage.getTiger().y, 100, 100);
        add(bearLabel);
        add(tigerLabel);

        // 키 리스너 추가
        bearKeyListener = new BearKeyListener(storage.getBear(), bearLabel, rockController, bossLabel, layeredPane);
        tigerKeyListener = new TigerKeyListener(storage.getTiger(), tigerLabel, bossLabel, boss);
        addKeyListener(bearKeyListener);
        addKeyListener(tigerKeyListener);
    }

    public void disableBearKeyListener() {
        removeKeyListener(bearKeyListener);
    }

    public void enableBearKeyListener() {
        addKeyListener(bearKeyListener);
        requestFocusInWindow();
    }

    public void disableTigerKeyListener() {
        removeKeyListener(tigerKeyListener);
    }

    public void enableTigerKeyListener() {
        addKeyListener(tigerKeyListener);
        requestFocusInWindow();
    }

    public void setFocusToPanel() {
        setFocusable(true);
        requestFocusInWindow();
    }

}
