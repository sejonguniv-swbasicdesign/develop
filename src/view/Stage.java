package view;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.MoveInteraction;
import model.Storage;
import actionlistener.BearKeyListener;
import actionlistener.TigerKeyListener;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class Stage extends JFrame {

    private JLabel bearLabel;
    private JLabel tigerLabel;
    private Storage storage;
    private MoveInteraction moveInteraction;

    public Stage() {
        moveInteraction = new MoveInteraction();
        storage = Storage.getInstance();

        setTitle("Temporary Stage");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);

        setLabel();

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

        bearLabel.setIcon(new ImageIcon(storage.getBear().getCurrentIcon().getImage()));
        tigerLabel.setIcon(new ImageIcon(storage.getTiger().getCurrentIcon().getImage()));

        // 초기 위치 설정
        bearLabel.setBounds(storage.getBear().x, storage.getBear().y, 64, 64);
        tigerLabel.setBounds(storage.getTiger().x, storage.getTiger().y, 64, 64);
        add(bearLabel);
        add(tigerLabel);

        // 키 리스너 추가 및 포커스 설정
        addKeyListener(new BearKeyListener(storage.getBear()));
        addKeyListener(new TigerKeyListener(storage.getTiger()));
    }
}