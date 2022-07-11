package sk.stuba.fei.uim.oop.components;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.controls.AI;
import sk.stuba.fei.uim.oop.controls.GameLogic;
import sk.stuba.fei.uim.oop.controls.Position;
import sk.stuba.fei.uim.oop.listeners.MyMouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MyJPanel extends JPanel {
    @Setter
    private MyJPanel[][] grid;
    private int gameSize;
    @Setter
    private StoneColor stoneColor;
    private MyJMenuBar myJMenuBar;
    private GameLogic logic;
    private Position position;
    private List<Position> playAbleTiles;
    private AI ai;

    public MyJPanel(Position position,MyJPanel panel) {
        super();
        this.gameSize = panel.getGameSize();
        this.grid = panel.getGrid();
        this.myJMenuBar = panel.getMyJMenuBar();
        this.logic = panel.getLogic();
        this.playAbleTiles = panel.getPlayAbleTiles();
        this.position = position;

        this.setUpMidStones(this.position.getX(),this.position.getY());
    }

    public MyJPanel(int gameSize,MyJMenuBar myJMenuBar) {
        super();
        this.gameSize = gameSize;
        this.myJMenuBar = myJMenuBar;
        this.logic = new GameLogic(this);
        this.playAbleTiles = new ArrayList<>();
        this.ai = new AI(this);

        this.setBackground(new Color(0, 204, 0));
        this.setLayout(new GridLayout(this.gameSize,this.gameSize));
        this.setBorder(BorderFactory.createLineBorder(new Color(0,102,0),3));

        this.grid = new MyJPanel[this.gameSize][this.gameSize];

        this.fillUpGrid();

        this.setGameState(GameState.BTurn);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(this.stoneColor == StoneColor.BLUE){
            g.setColor(Color.BLUE);
            g.fillOval(20,20,30,30);
        }
        if(this.stoneColor == StoneColor.BLACK){
            g.setColor(Color.BLACK);
            g.fillArc(5, 5, 60, 60, 0, 360);
        }
        if(this.stoneColor == StoneColor.WHITE){
            g.setColor(Color.WHITE);
            g.fillArc(5, 5, 60, 60, 0, 360);
        }
    }

    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if(keyCode == KeyEvent.VK_R) {
            this.getMyJMenuBar().restart();
        }
    }

    private void fillUpGrid(){
        for(int row = 0 ; row < this.gameSize ; row++){
            for(int col = 0 ; col < this.gameSize ; col++){
                MyJPanel tile = new MyJPanel(new Position(row,col),this);
                tile.setPreferredSize(new Dimension(70,70));
                tile.setBorder(BorderFactory.createLineBorder(new Color(0,102,0),3));
                tile.setBackground(new Color(0, 204, 0));
                this.grid[row][col] = tile;

                MyMouseListener tileListener = new MyMouseListener(tile);
                tile.addMouseListener(tileListener);

                this.add(tile);
            }
        }
    }

    private void setUpMidStones(int xCoord,int yCoord){
        if(this.midCoords(xCoord,1) && this.midCoords(yCoord,1)){
            this.stoneColor = StoneColor.BLACK;
        } else if(this.midCoords(xCoord,0) && this.midCoords(yCoord,0)){
            this.stoneColor = StoneColor.BLACK;
        } else if(this.midCoords(xCoord,0) && this.midCoords(yCoord,1)){
            this.stoneColor = StoneColor.WHITE;
        } else if(this.midCoords(xCoord,1) && this.midCoords(yCoord,0)){
            this.stoneColor = StoneColor.WHITE;
        } else {
            this.stoneColor = StoneColor.EMPTY;
        }
    }

    private boolean midCoords(int coord, int mid){
        return coord == (this.gameSize/2)-mid;
    }

    public void setGameState(GameState gameState){
        this.playAbleTiles.clear();
        switch (gameState){
            case BTurn:

                this.playAbleTiles = this.logic.playAbleTiles(gameState);

                if(this.playAbleTiles.size() < 1){
                    this.validTurnCheck();
                } else {

                    this.myJMenuBar.getCurrentTurn().setText("It's Black turn.");

                    this.setHighlightedBorder();
                }

                break;
            case WTurn:

                this.playAbleTiles = this.logic.playAbleTiles(gameState);

                if(this.playAbleTiles.size() < 1){
                    this.setGameState(GameState.BTurn);
                } else {

                    this.myJMenuBar.getCurrentTurn().setText("It's White turn.");

                    this.setHighlightedBorder();

                    this.getLogic().doTurn(this.getAi().chooseBestMove(this.playAbleTiles),gameState);
                }

                break;
            case BWins:
                this.myJMenuBar.getCurrentTurn().setText("Black is WINNER");
                JOptionPane.showMessageDialog(this.getMyJMenuBar().getFrame(),"BLACK IS WINNER\nR - restart the game\nEsc - close the game");
                break;
            case WWins:
                this.myJMenuBar.getCurrentTurn().setText("White is WINNER");
                JOptionPane.showMessageDialog(this.getMyJMenuBar().getFrame(),"WHITE IS WINNER\nR - restart the game\nEsc - close the game");
                break;
            case Draw:
                this.myJMenuBar.getCurrentTurn().setText("Draw !");
                JOptionPane.showMessageDialog(this.getMyJMenuBar().getFrame(),"DRAW !\nR - restart the game\nEsc - close the game");
                break;
        }

    }

    private void validTurnCheck(){
        if(this.logic.playAbleTiles(GameState.WTurn).size() < 1){
            if(this.getMyJMenuBar().getBCounter() > this.getMyJMenuBar().getWCounter()){
                this.setGameState(GameState.BWins);
            } else if(this.getMyJMenuBar().getWCounter() > this.getMyJMenuBar().getBCounter()){
                this.setGameState(GameState.WWins);
            } else {
                this.setGameState(GameState.Draw);
            }
        } else {
            this.setGameState(GameState.WTurn);
        }
    }

    private void setHighlightedBorder(){
        for(Position playAbleTile : this.playAbleTiles) {
            this.getGrid()[playAbleTile.getX()][playAbleTile.getY()].setStoneColor(StoneColor.HIGHLIGHT);
        }
        for(int i = 0 ; i < this.gameSize ; i++){
            for(int j = 0 ; j <this.gameSize ; j++){
                if(this.getGrid()[i][j].getStoneColor().equals(StoneColor.HIGHLIGHT)){
                    this.getGrid()[i][j].setBorder(BorderFactory.createLineBorder(new Color(255,204,0),3));
                }
            }
        }
    }

}