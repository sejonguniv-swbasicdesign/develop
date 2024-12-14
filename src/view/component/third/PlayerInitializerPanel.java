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

        bearLabel.setBounds(storage.getBear().x, storage.getBear().y, storage.getBear().getWidth(), storage.getBear().getHeight());
        tigerLabel.setBounds(storage.getTiger().x, storage.getTiger().y, storage.getTiger().getWidth(), storage.getTiger().getHeight());

        add(bearLabel);
        add(tigerLabel);

        bearKeyListener = new BearKeyListener(storage.getBear(), bearLabel, rockController, bossLabel, layeredPane);
        tigerKeyListener = new TigerKeyListener(storage.getTiger(), tigerLabel, bossLabel, boss);
        addKeyListener(bearKeyListener);
        addKeyListener(tigerKeyListener);
    }

}
