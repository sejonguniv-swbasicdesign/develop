package controller;

import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class TornadoAttackController {
    private Timer moveTimer;
    private JLabel tornado;
    private JLayeredPane panel;
    private boolean isTornadoFinished; // 토네이도 종료 상태 플래그
    private StageController stageController;

    // 충돌 상태를 추적하기 위한 Set
    private Set<BearPlayer> bearCollisionSet = new HashSet<>();
    private Set<TigerPlayer> tigerCollisionSet = new HashSet<>();

    public TornadoAttackController(int x, int y, BearPlayer bearPlayer, TigerPlayer tigerPlayer, JLayeredPane panel, StageController stageController, Runnable onComplete) {
        this.panel = panel;
        this.stageController = stageController;
        this.isTornadoFinished = false; // 초기화

        // 토네이도 생성
        tornado = createTornado(x, y);
        panel.add(tornado, JLayeredPane.MODAL_LAYER);

        // 토네이도 이동 로직
        moveTimer = new Timer(30, e -> {
            moveTornado(bearPlayer, tigerPlayer, onComplete);
        });

        moveTimer.start(); // 토네이도 이동 시작
    }

    private JLabel createTornado(int x, int y) {
        JLabel tornado = new JLabel();
        tornado.setOpaque(true);
        tornado.setBackground(Color.GRAY); // 토네이도 색상
        tornado.setBounds(x, y, 100, 100); // 초기 위치 및 크기
        return tornado;
    }

    private void moveTornado(BearPlayer bearPlayer, TigerPlayer tigerPlayer, Runnable onComplete) {
        Point location = tornado.getLocation();

        // U자 경로에 따라 이동
        int newX = location.x + 5; // 오른쪽으로 이동
        int newY = calculateUPath(location.x); // U자형 경로 계산

        tornado.setLocation(newX, newY);

        // BearPlayer와 충돌 처리
        if (checkCollision(tornado, bearPlayer)) {
            if (!bearCollisionSet.contains(bearPlayer)) {
                handleCollision(bearPlayer); // 처음 충돌 시 처리
                bearCollisionSet.add(bearPlayer); // 충돌 상태 추가
            }
        } else {
            bearCollisionSet.remove(bearPlayer); // 충돌 해제
        }

        // TigerPlayer와 충돌 처리
        if (checkCollision(tornado, tigerPlayer)) {
            if (!tigerCollisionSet.contains(tigerPlayer)) {
                handleCollision(tigerPlayer); // 처음 충돌 시 처리
                tigerCollisionSet.add(tigerPlayer); // 충돌 상태 추가
            }
        } else {
            tigerCollisionSet.remove(tigerPlayer); // 충돌 해제
        }

        // 화면 밖으로 나가면 제거
        if (newX > panel.getWidth() || newY < 0) {
            removeTornado();
            onComplete.run(); // 토네이도 종료 콜백 실행
        }

        panel.repaint();
    }

    private int calculateUPath(int x) {
        // U자형 경로 계산
        int amplitude = 150; // 진폭
        int baseY = 300; // 중심 Y 좌표
        return baseY + (int) (amplitude * Math.sin(x * 0.02)); // 경로 계산 (부드러운 곡선)
    }

    private boolean checkCollision(JLabel tornado, BearPlayer player) {
        return player.getBounds().intersects(tornado.getBounds());
    }

    private boolean checkCollision(JLabel tornado, TigerPlayer player) {
        return player.getBounds().intersects(tornado.getBounds());
    }

    private void handleCollision(BearPlayer player) {
        player.decreaseHp(1); // HP 감소
        stageController.updateHpDisplay(); // HP UI 업데이트
        panel.repaint();
    }

    private void handleCollision(TigerPlayer player) {
        player.decreaseHp(1); // HP 감소
        stageController.updateHpDisplay(); // HP UI 업데이트
        panel.repaint();
    }

    private void removeTornado() {
        panel.remove(tornado);
        isTornadoFinished = true;
        panel.repaint();
        if (moveTimer != null) {
            moveTimer.stop();
        }
    }

    public boolean isFinished() {
        return isTornadoFinished;
    }

    public void stop() {
    }
}
