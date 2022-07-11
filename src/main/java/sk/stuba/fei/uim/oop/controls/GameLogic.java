package sk.stuba.fei.uim.oop.controls;

import lombok.Data;
import sk.stuba.fei.uim.oop.components.GameState;
import sk.stuba.fei.uim.oop.components.MyJPanel;
import sk.stuba.fei.uim.oop.components.StoneColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameLogic {

    private List<Position> stonesPositions;
    private List<Position> result;
    private MyJPanel panel;
    private Position bestTile;
    private int bestLength;

    public GameLogic(MyJPanel panel) {
        this.stonesPositions = new ArrayList<>();
        this.result = new ArrayList<>();
        this.panel = panel;
    }

    public List<Position> playAbleTiles(GameState gameState){
        this.stonesPositions.clear();
        this.result.clear();
        StoneColor currentColor;
        if(gameState.equals(GameState.BTurn)){
            currentColor = StoneColor.BLACK;
        } else {
            currentColor = StoneColor.WHITE;
        }

        for(int i = 0 ; i < this.panel.getGameSize() ; i++){
            for(int j = 0 ; j < this.panel.getGameSize() ; j++){
                if(this.panel.getGrid()[i][j].getStoneColor().equals(currentColor)){
                    this.stonesPositions.add(this.panel.getGrid()[i][j].getPosition());
                }

            }
        }

        for(Position stone : this.stonesPositions){
            this.searchAlgorithm(stone, currentColor, 0);
        }

        return this.result;
    }

    public void searchAlgorithm(Position position, StoneColor stoneColor, int purpose){
        int xCoord = position.getX();
        int yCoord = position.getY();
        StoneColor other;
        StoneColor lastTile = StoneColor.EMPTY;
        if(stoneColor.equals(StoneColor.BLACK)){
            other = StoneColor.WHITE;
        } else {
            other = StoneColor.BLACK;
        }

        if(purpose == 2){
            lastTile = stoneColor;
        }

        if(purpose == 1){
            this.setStones(this.panel.getGrid()[xCoord][yCoord],stoneColor,1);
        }

        int currentTilePoints = 0;
        for(Direction dir : Direction.values()){
            int length = 0;
            if(this.borderConditions(xCoord,yCoord,dir) && this.panel.getGrid()[xCoord+dir.getX()][yCoord+dir.getY()].getStoneColor().equals(other)){
                int movingX = xCoord;
                int movingY = yCoord;
                length++;
                while (this.borderConditions(movingX,movingY,dir) && this.panel.getGrid()[movingX+dir.getX()+dir.getX()][movingY+dir.getY()+dir.getY()].getStoneColor().equals(other)){
                    movingX = movingX + dir.getX();
                    movingY = movingY + dir.getY();
                    length++;
                }
                if(this.borderConditions(movingX,movingY,dir) && this.panel.getGrid()[movingX+dir.getX()+ dir.getX()][movingY+ dir.getY()+ dir.getY()].getStoneColor().equals(lastTile) && (purpose == 0 || purpose == 2)){
                    if(purpose == 0) {
                        this.result.add(new Position(movingX + dir.getX() + dir.getX(), movingY + dir.getY() + dir.getY()));
                    } else {
                        currentTilePoints = currentTilePoints + length;
                    }
                }
                if(this.borderConditions(movingX,movingY,dir) && this.panel.getGrid()[movingX+ dir.getX()+ dir.getX()][movingY+ dir.getY()+ dir.getY()].getStoneColor().equals(stoneColor) && purpose == 1){
                    movingX = movingX + dir.getX();
                    movingY = movingY + dir.getY();

                    while(this.panel.getGrid()[movingX][movingY].getStoneColor() != stoneColor){
                        this.setStones(this.panel.getGrid()[movingX][movingY],stoneColor,1);
                        this.setStones(this.panel.getGrid()[movingX][movingY],other,-1);
                        movingX = movingX - dir.getX();
                        movingY = movingY - dir.getY();
                    }
                }
            }
        }
        if(currentTilePoints > this.bestLength && purpose == 2){
            this.bestTile = position;
            System.out.println(currentTilePoints);
            this.bestLength = currentTilePoints;
        }

    }

    private boolean borderConditions(int movingX,int movingY, Direction direction){
        boolean condition = false;
        switch (direction){
            case NORTH:
                condition = movingX > 1;
                break;
            case NORTH_EAST:
                condition = movingX > 1 && movingY < this.panel.getGameSize()-2;
                break;
            case NORTH_WEST:
                condition = movingX > 1 && movingY > 1;
                break;
            case EAST:
                condition = movingY < this.panel.getGameSize()-2;
                break;
            case WEST:
                condition = movingY > 1;
                break;
            case SOUTH:
                condition = movingX < this.panel.getGameSize()-2;
                break;
            case SOUTH_EAST:
                condition = movingX < this.panel.getGameSize()-2 && movingY < this.panel.getGameSize()-2;
                break;
            case SOUTH_WEST:
                condition = movingX < this.panel.getGameSize()-2 && movingY > 1;
                break;
        }
        return condition;
    }

    private void setStones(MyJPanel sourcePanel,StoneColor stoneColor,int lvl){

        if(lvl == 1){
            sourcePanel.setStoneColor(stoneColor);
            sourcePanel.repaint();
        }
        if(stoneColor.equals(StoneColor.BLACK)){
            this.panel.getMyJMenuBar().setBCounter(this.panel.getMyJMenuBar().getBCounter()+lvl);
            this.panel.getMyJMenuBar().getScoreBoardB().setText("B: "+ (this.panel.getMyJMenuBar().getBCounter()));
        }
        if(stoneColor.equals(StoneColor.WHITE)){
            this.panel.getMyJMenuBar().setWCounter(this.panel.getMyJMenuBar().getWCounter()+lvl);
            this.panel.getMyJMenuBar().getScoreBoardW().setText("W: "+ (this.panel.getMyJMenuBar().getWCounter()));
        }
    }

    public void doTurn(MyJPanel sourcePanel, GameState gameState){
        if(gameState.equals(GameState.BTurn)){

            this.searchAlgorithm(sourcePanel.getPosition(),StoneColor.BLACK,1);
        } else {

            this.searchAlgorithm(sourcePanel.getPosition(),StoneColor.WHITE,1);
        }

        this.panel.revalidate();
        sourcePanel.setBorder(BorderFactory.createLineBorder(new Color(0,102,0),3));
        this.panel.repaint();

        if(gameState.equals(GameState.BTurn)){
            this.resetHighLight();
            this.panel.setGameState(GameState.WTurn);
        } else {
            this.resetHighLight();
            this.panel.setGameState(GameState.BTurn);
        }
    }

    private void resetHighLight(){
        for(int i = 0 ; i < this.panel.getGameSize() ; i++){
            for(int j = 0 ; j < this.panel.getGameSize() ; j++){
                MyJPanel tile = this.panel.getGrid()[i][j];
                if(tile.getStoneColor().equals(StoneColor.HIGHLIGHT)){
                    tile.setStoneColor(StoneColor.EMPTY);
                    tile.setBorder(BorderFactory.createLineBorder(new Color(0,102,0),3));
                }
            }
        }
    }

}