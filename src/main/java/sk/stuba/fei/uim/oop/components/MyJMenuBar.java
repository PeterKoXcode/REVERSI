package sk.stuba.fei.uim.oop.components;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class MyJMenuBar extends JMenuBar {

    private int gameSize;
    @Getter
    private JLabel scoreBoardB;
    @Getter
    private JLabel scoreBoardW;
    @Getter
    private JLabel currentTurn;
    @Getter @Setter
    private int bCounter;
    @Getter @Setter
    private int wCounter;
    private String currentTurnString;
    private JLabel currentGameSize;
    @Getter
    private MyJFrame frame;

    public MyJMenuBar(MyJFrame frame) {
        super();
        this.frame = frame;
        this.gameSize = this.frame.getGameSize();
        this.bCounter = 2;
        this.wCounter = 2;
        this.currentTurnString = "Start";

        JMenu gameSize = new JMenu("GameSize");
        JMenuItem newGame = new JMenuItem("GameReset");
        this.currentGameSize = new JLabel(this.gameSize+"x"+ this.gameSize);
        this.currentTurn = new JLabel(this.currentTurnString);
        this.scoreBoardB = new JLabel("B: "+ this.bCounter);
        this.scoreBoardW = new JLabel("W: "+ this.wCounter);


        this.add(newGame);

        gameSize.setPreferredSize(new Dimension(70,23));
        newGame.setMaximumSize(new Dimension(80,23));
        this.currentGameSize.setMaximumSize(new Dimension(50,23));
        this.currentTurn.setMaximumSize(new Dimension(100,23));
        this.scoreBoardB.setMaximumSize(new Dimension(50,23));
        this.scoreBoardW.setMaximumSize(new Dimension(50,23));

        JMenuItem x6 = new JMenuItem("6X6");
        JMenuItem x8 = new JMenuItem("8X8");
        JMenuItem x10 = new JMenuItem("10X10");
        JMenuItem x12 = new JMenuItem("12X12");
        this.add(gameSize);
        gameSize.add(x6);
        gameSize.add(x8);
        gameSize.add(x10);
        gameSize.add(x12);

        this.add(this.currentGameSize);
        this.add(this.currentTurn);
        this.add(this.scoreBoardB);
        this.add(this.scoreBoardW);

        newGame.addActionListener(actionEvent -> this.restart());

        x6.addActionListener(actionEvent -> this.setAction(6));
        x8.addActionListener(actionEvent -> this.setAction(8));
        x10.addActionListener(actionEvent -> this.setAction(10));
        x12.addActionListener(actionEvent -> this.setAction(12));
    }

    private void setAction(int size){
        this.gameSize = size;
        this.restart();
        this.frame.pack();
    }

    private void setDefaultVal(){
        this.bCounter = 2;
        this.wCounter = 2;
        this.scoreBoardB.setText("B: "+ this.bCounter);
        this.scoreBoardW.setText("W: "+ this.wCounter);
        this.currentTurnString = "Start";
        this.currentTurn.setText(this.currentTurnString);
        this.currentGameSize.setText(this.gameSize+"x"+this.gameSize);
    }

    public void restart(){
        this.frame.remove(this.frame.getPanel());

        this.setDefaultVal();

        this.repaint();
        this.frame.setPanel(new MyJPanel(this.gameSize,this));
        this.frame.add(this.frame.getPanel());
        this.setBCounter(2);
        this.frame.revalidate();
        this.frame.repaint();
    }


}