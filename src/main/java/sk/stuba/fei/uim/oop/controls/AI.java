package sk.stuba.fei.uim.oop.controls;

import sk.stuba.fei.uim.oop.components.MyJPanel;
import sk.stuba.fei.uim.oop.components.StoneColor;

import java.util.List;

public class AI {

    private MyJPanel panel;

    public AI(MyJPanel panel) {
        this.panel = panel;
    }

    public MyJPanel chooseBestMove(List<Position> playAbleTiles){

        this.panel.getLogic().setBestLength(0);
        this.panel.getLogic().setBestTile(null);

        for(Position tile : playAbleTiles){

            this.panel.getLogic().searchAlgorithm(tile, StoneColor.WHITE, 2);

        }

        Position bestTile = this.panel.getLogic().getBestTile();
        return this.panel.getGrid()[bestTile.getX()][bestTile.getY()];

    }
}