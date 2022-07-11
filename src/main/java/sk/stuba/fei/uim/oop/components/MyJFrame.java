package sk.stuba.fei.uim.oop.components;

import lombok.Data;
import sk.stuba.fei.uim.oop.listeners.MyKeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Data
public class MyJFrame extends JFrame {


    private MyJPanel panel;
    private MyJMenuBar myJMenuBar;
    private int gameSize;

    public MyJFrame(int size) throws HeadlessException {
        super("REVERSI");
        this.gameSize = size;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        BufferedImage image = null;
        try {
            image = ImageIO.read(MyJPanel.class.getResourceAsStream("/reversi.png/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setIconImage(image);


        this.myJMenuBar = new MyJMenuBar(this);
        this.panel = new MyJPanel(this.gameSize,this.myJMenuBar);

        this.setJMenuBar(this.myJMenuBar);

        this.add(panel);
        this.addKeyListener(new MyKeyListener(this.panel));
        this.pack();
        this.setVisible(true);

    }
}
