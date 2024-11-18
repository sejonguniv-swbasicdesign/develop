package view.component.third;

import javax.swing.*;
import controller.MoveInteraction;
import model.Storage;
import actionlistener.BearKeyListener;
import actionlistener.TigerKeyListener;

public class PlayerInitializerPanel extends JPanel {

    private JLabel bearLabel;
    private JLabel tigerLabel;
    private Storage storage;
    private MoveInteraction moveInteraction;

    public PlayerInitializerPanel() {
        moveInteraction = new MoveInteraction();
        storage = Storage.getInstance();
        setLayout(null);
        setOpaque(false);

        setLabel();

        setFocusable(true);
        requestFocusInWindow();

        // 주기적으로 캐릭터 위치 업데이트
        Timer timer = new Timer(16, e -> {
            moveInteraction.updateCharacterPositions(bearLabel, tigerLabel);
            repaint();
        });

        timer.start();
    }

    private void setLabel() {
        bearLabel = new JLabel();
        tigerLabel = new JLabel();

        storage.getTiger().setOppositeDirection();

        bearLabel.setIcon(new ImageIcon(storage.getBear().getCurrentIcon().getImage()));
        tigerLabel.setIcon(new ImageIcon(storage.getTiger().getCurrentIcon().getImage()));

        // 초기 위치 설정
        storage.getBear().x = 300;
        storage.getBear().y = 400;
        storage.getTiger().x = 800;
        storage.getTiger().y = 400;
        bearLabel.setBounds(storage.getBear().x, storage.getBear().y, 100, 100);
        tigerLabel.setBounds(storage.getTiger().x, storage.getTiger().y, 100, 100);
        add(bearLabel);
        add(tigerLabel);

        // 키 리스너 추가
        addKeyListener(new BearKeyListener(storage.getBear()));
        addKeyListener(new TigerKeyListener(storage.getTiger()));
    }
}
