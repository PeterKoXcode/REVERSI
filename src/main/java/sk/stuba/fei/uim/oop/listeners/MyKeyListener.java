package sk.stuba.fei.uim.oop.listeners;

import sk.stuba.fei.uim.oop.components.MyJPanel;

import java.awt.event.KeyEvent;

public class MyKeyListener extends UniversalAdapter {

    MyJPanel panel;

    public MyKeyListener(MyJPanel panel) {
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        this.panel.handleInput(keyEvent.getKeyCode());
    }
}
