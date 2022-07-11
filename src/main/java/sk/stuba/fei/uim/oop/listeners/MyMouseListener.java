package sk.stuba.fei.uim.oop.listeners;

import sk.stuba.fei.uim.oop.components.GameState;
import sk.stuba.fei.uim.oop.components.MyJPanel;
import sk.stuba.fei.uim.oop.components.StoneColor;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MyMouseListener extends UniversalAdapter{

    private MyJPanel panel;

    public MyMouseListener(MyJPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        super.mouseClicked(mouseEvent);

        MyJPanel sourcePanel = ((MyJPanel)mouseEvent.getSource());

        if(sourcePanel.getStoneColor().equals(StoneColor.BLUE)){
            this.panel.getLogic().doTurn(sourcePanel,GameState.BTurn);

        }

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        var point = this.panel.getComponentAt(mouseEvent.getPoint());
        int xCoord = (point.getX()-3)/70;
        int yCoord = (point.getY()-3)/70;
        System.out.println("{"+ yCoord +","+ xCoord +"}");

        MyJPanel sourcePanel = ((MyJPanel)mouseEvent.getSource());

        if(sourcePanel.getStoneColor().equals(StoneColor.HIGHLIGHT)){
            sourcePanel.setStoneColor(StoneColor.BLUE);
            this.panel.repaint();
        }

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        MyJPanel sourcePanel = ((MyJPanel)mouseEvent.getSource());
        if(sourcePanel.getStoneColor().equals(StoneColor.BLUE)){
            sourcePanel.setStoneColor(StoneColor.HIGHLIGHT);
        }
        this.panel.repaint();
        this.panel.setBackground(new Color(0, 204, 0));
    }

}