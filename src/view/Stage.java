package view;

import javax.imageio.ImageIO;
import javax.swing.*;
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

    public Stage() {
        setTitle("Temporary Stage");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        bearLabel = new JLabel();
        tigerLabel = new JLabel();

        try {
            storage = Storage.getInstance();
            bearLabel.setIcon(new ImageIcon(storage.getBear().getCurrentIcon().getImage()));
            tigerLabel.setIcon(new ImageIcon(storage.getTiger().getCurrentIcon().getImage()));
        } catch (IOException e){
            System.out.println("파일을 읽어들이는 데에 실패했습니다. 프로그램을 다시 실행해 주세요.");
        }

        // 초기 위치 설정
        bearLabel.setBounds(storage.getBear().x, storage.getBear().y, 64, 64);
        tigerLabel.setBounds(storage.getTiger().x, storage.getTiger().y, 64, 64);
        add(bearLabel);
        add(tigerLabel);

        // 키 리스너 추가 및 포커스 설정
        addKeyListener(new BearKeyListener(storage.getBear()));
        addKeyListener(new TigerKeyListener(storage.getTiger()));
        setFocusable(true);
        requestFocusInWindow();  // 창이 포커스를 받을 수 있도록 설정

        // 윈도우 창 보이기
        setVisible(true);

        // 주기적으로 캐릭터 위치 업데이트
        Timer timer = new Timer(16, e -> updateCharacterPositions());
        timer.start();
    }

    // 캐릭터 위치 업데이트 메서드
    public void updateCharacterPositions() {
        // 현재 방향에 맞는 아이콘을 가져와서 설정
        bearLabel.setIcon(storage.getBear().getCurrentIcon());
        tigerLabel.setIcon(storage.getTiger().getCurrentIcon());

        // 캐릭터 위치 업데이트
        bearLabel.setLocation(storage.getBear().x, storage.getBear().y);
        tigerLabel.setLocation(storage.getTiger().x, storage.getTiger().y);

        repaint();
    }
}
