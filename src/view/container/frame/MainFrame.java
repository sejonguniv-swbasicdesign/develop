package view.container.frame;

import javax.swing.*;
import java.awt.*;
import view.container.panel.MainPanel;
import view.container.panel.SettingPanel;

//메인화면
public class MainFrame extends JFrame{

    private MainPanel mainPanel;
    private SettingPanel settingPanel;

    public MainFrame(){
        this.settingPanel = new SettingPanel();
        this.mainPanel = new MainPanel(settingPanel);
        setFrame();
    }

    private void setFrame(){
        setTitle("Main");
        setSize(800, 630);
        setPreferredSize(new Dimension(800, 630));
        setMinimumSize(new Dimension(800, 630));
        setLocationRelativeTo(null);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        mainPanel.setContainer(this.getContentPane());
        settingPanel.setContainer(this.getContentPane());
        mainPanel.setMainPanel();
    }
}
