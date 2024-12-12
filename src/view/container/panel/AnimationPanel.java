package view.container.panel;

import view.container.frame.GameFrame;
import view.container.frame.MainFrame;
import view.container.frame.Stage1Frame;
import view.container.frame.third.InitBossStage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnimationPanel extends JPanel {

    private Container container;
    private int stage = 0;
    private int currentImageIndex = 0; // start 배열의 현재 이미지 인덱스
    private Timer timer; // 5초마다 이미지를 변경할 타이머

    private ArrayList<Image> start = new ArrayList<>();
    private ArrayList<Image> beforeStage1 = new ArrayList<>();
    private ArrayList<Image> beforeStage2 = new ArrayList<>();
    private ArrayList<Image> beforeStage3 = new ArrayList<>();
    private ArrayList<Image> last = new ArrayList<>();

    public AnimationPanel(Container container) {
        this.container = container;
        setSize(800, 630);
        setPreferredSize(new Dimension(800, 630));
        initImages();
        setVisible(true);

        this.container.revalidate();
        this.container.repaint();

        startAutoImageSwitching(); // 자동 이미지 전환 시작
    }

    public void setStage(int stage) {
        this.stage = stage;
        repaint();
    }

    private void initImages() {
        setImage("src/assets/image/animation/1-a.png", start);
        setImage("src/assets/image/animation/1-b.png", start);
        setImage("src/assets/image/animation/1-c.png", start);
        setImage("src/assets/image/animation/1-c-2.png", start);
        setImage("src/assets/image/animation/1-d.jpg", start);

        setImage("src/assets/image/animation/2-a.png", beforeStage1);
        setImage("src/assets/image/animation/2-b.png", beforeStage1);

        setImage("src/assets/image/animation/3-a.png", beforeStage2);
        setImage("src/assets/image/animation/3-b.png", beforeStage2);

        setImage("src/assets/image/animation/4-a.png", beforeStage3);

        setImage("src/assets/image/animation/4-b-1.png", last);
        setImage("src/assets/image/animation/4-b-2.png", last);
        setImage("src/assets/image/animation/4-b-3.png", last);
        setImage("src/assets/image/animation/4-b-4.png", last);

    }

    private void setImage(String string, ArrayList<Image> images) {
        ImageIcon imageIcon = new ImageIcon(string);
        Image image = imageIcon.getImage();
        images.add(image);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stage == 0) {
            Image image = start.get(currentImageIndex);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        else if(stage==1){
            Image image = beforeStage1.get(currentImageIndex);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        else if(stage==2){
            Image image = beforeStage2.get(currentImageIndex);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        else if(stage==3){
            Image image = beforeStage3.get(currentImageIndex);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        else if(stage==4){
            Image image = last.get(currentImageIndex);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // 5초마다 start 배열의 이미지를 순차적으로 전환하는 메서드
    private void startAutoImageSwitching() {
        timer = new Timer(5000, e -> {
            if (stage == 0) {
                currentImageIndex++;
                if (currentImageIndex >= start.size()) {
                    currentImageIndex=0;
                    JFrame currentFrame = (JFrame) SwingUtilities.getRoot(this);
                    currentFrame.dispose();
                    new MainFrame();
                }
                repaint(); // 화면 다시 그리기
            }
            else if(stage==1){
                currentImageIndex++;
                if (currentImageIndex >= beforeStage1.size()) {
                    currentImageIndex=0;
                    JFrame currentFrame = (JFrame) SwingUtilities.getRoot(this);
                    currentFrame.dispose();
                    Stage1Frame stage1 = new Stage1Frame();
                    stage1.setStage1Frame();
                }
                repaint(); // 화면 다시 그리기
            }
            else if(stage==2){
                currentImageIndex++;
                if (currentImageIndex >= beforeStage2.size()) {
                    currentImageIndex=0;
                    JFrame currentFrame = (JFrame) SwingUtilities.getRoot(this);
                    currentFrame.dispose();
                    GameFrame gameFrame = new GameFrame();
                    gameFrame.setVisible(true);
                }
                repaint(); // 화면 다시 그리기
            }
            else if(stage==3){
                currentImageIndex++;
                if (currentImageIndex >= beforeStage3.size()) {
                    currentImageIndex=0;
                    JFrame currentFrame = (JFrame) SwingUtilities.getRoot(this);
                    currentFrame.dispose();
                    new InitBossStage();
                }
                repaint(); // 화면 다시 그리기
            }
            else if(stage==4){
                currentImageIndex++;
                if (currentImageIndex >= last.size()) {
                    currentImageIndex=0;
                }
                repaint(); // 화면 다시 그리기
            }
        });
        timer.start();
    }

    // 패널이 종료될 때 타이머도 중지
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (timer != null) {
            timer.stop();
        }
    }
}
