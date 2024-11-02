package view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DesignedButton extends JButton {

    private Color textColor;
    private Color hoverTextColor;

    public DesignedButton() {
        super();
        decorate();
    }

    public void decorate() {
        setBorderPainted(false);
        setOpaque(false);
        textColor = Color.black;
        hoverTextColor = Color.blue;

        setFont(new Font("Arial", Font.BOLD, 30));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(hoverTextColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(textColor);
            }
        });
    }

    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D graphics = (Graphics2D) g;

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();


        int textX = (width - stringBounds.width) / 2;
        int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();

        graphics.setColor(getForeground());
        graphics.drawString(getText(), textX, textY);
        graphics.dispose();

    }
}
