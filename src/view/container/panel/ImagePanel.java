package view.container.panel;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private ImageIcon image;
    public ImagePanel() {
        super();
        decorate();
    }

    public void decorate(){
        setOpaque(false);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getImage(),0,0,getWidth(),getHeight(),this);
    }

    public void setImage(String path){
        this.image = new ImageIcon(path);
    }
}
