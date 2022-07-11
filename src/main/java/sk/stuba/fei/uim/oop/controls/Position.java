package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;


public class Position {
    @Getter
    private int x;
    @Getter
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}