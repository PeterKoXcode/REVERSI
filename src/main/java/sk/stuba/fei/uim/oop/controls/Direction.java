package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;

public enum Direction {
    NORTH(new Position(-1,0)),
    NORTH_EAST(new Position(-1,1)),
    NORTH_WEST(new Position(-1,-1)),
    EAST(new Position(0,1)),
    WEST(new Position(0,-1)),
    SOUTH(new Position(1,0)),
    SOUTH_EAST(new Position(1,1)),
    SOUTH_WEST(new Position(1,-1));

    @Getter
    private int x;
    @Getter
    private int y;

    Direction(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }


}