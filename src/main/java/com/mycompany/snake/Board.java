/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;

/**
 *
 * @author alber
 */
public class Board extends javax.swing.JPanel {
    private Snake snake;
    private int[][] matrix;
    private Direction directionSnake;
    private Direction lastDirection;
    int numCols; 
    int numRows;
    private Timer timer;
    private Food fruit;
    private int pendentGrowth;

    
    public Board() {
        initComponents();
        addKeyListener(new MyKeyAdapter());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        initBoard();
    }

    class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    canMove(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    canMove(Direction.RIGHT);
                    break;
                case KeyEvent.VK_UP:
                    canMove(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    canMove(Direction.DOWN);
                    break;
                default:
                    break;
            }
            repaint();
        }
    }

    public void initMatrix(int numRows, int numCols) {
        matrix = new int[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                matrix[row][col] = 0;
            }
        }
    }

    public void initBoard() {
        numRows = ConfigData.getInstance().getNumRows();
        numCols = ConfigData.getInstance().getNumCols();
        initMatrix(numRows, numCols);
        snake = new Snake();
        newFruit();
        updateMatrix();
        directionSnake = Direction.RIGHT;
        lastDirection = Direction.RIGHT;
        int deltaTime = ConfigData.getInstance().getDeltaTime();
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(deltaTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                tick();
            }
        });
        timer.start();
    }

    private void tick() {
        if (shapeHitsMatrix()) {
            //GameOver();
            return;
        } else {
            moveSnake();
            updateMatrix();
            
        }
        repaint();
    }
    
    

    public void updateMatrix() {
        for (int i = 0; i < snake.getSize(); i++) {
            int row = snake.getElementNodeRow(i);
            int col = snake.getElementNodeCol(i);
            if (row >= 0) {
                matrix[col][row] = 1;
                
            }
            matrix[snake.getFirstNodeCol()][snake.getFirstNodeRow()] = 2;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintMatrix(g);
        if (snake != null) {
        }
    }

    private void canMove(Direction direction) {
        if (direction.ordinal()%2 != lastDirection.ordinal()%2) {
            directionSnake = direction;
        }
    }

    private void paintMatrix(Graphics g) {
        for (int row = 0; row < ConfigData.getInstance().getNumRows(); row++) {
            for (int col = 0; col < ConfigData.getInstance().getNumCols(); col++) {
                drawSquare(g, row, col, matrix[row][col]);
            }
        }
    }

    private void drawSquare(Graphics g, int row, int col, int colorIndex) {
        Color[] colors = {Color.white, Color.green, Color.CYAN, Color.BLUE, Color.YELLOW, Color.MAGENTA, new Color(218, 170, 0)};
        Color color = colors[colorIndex];
        if (colorIndex == 0 && row%2 == 0 && col%2 == 0) {
            color = Color.lightGray;
        } else if (colorIndex == 0 && row%2 == 1 && col%2 == 1) {
            color = Color.lightGray;
        }
        int x = col * getSquareWidth();
        int y = row * getSquareHeight();
        g.setColor(color);
        g.fillRect(x + 1, y + 1, getSquareWidth() - 2, getSquareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + getSquareHeight() - 1, x, y);
        g.drawLine(x, y, x + getSquareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + getSquareHeight() - 1);
        g.drawLine(x + getSquareWidth() - 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + 1);
    }

    private int getSquareWidth() {
        return getWidth() / numCols;
    }

    private int getSquareHeight() {
        return getHeight() / numRows;
    }

    private boolean shapeHitsMatrix() {
        int headRow = snake.getFirstNodeRow();
        int headCol = snake.getFirstNodeCol();
        return headCol < 0 || headCol >= ConfigData.getInstance().getNumCols() || headRow >= ConfigData.getInstance().getNumRows();
    }

    private void moveSnake() {
        lastDirection = directionSnake;
        int row = snake.getFirstNodeRow();
        int col = snake.getFirstNodeCol();
        int lastCol = snake.getLastNodeCol();
        int lastRow = snake.getLastNodeRow();
        matrix[lastCol][lastRow] = 0;
        matrix[col][row] = 1;
        if (directionSnake == Direction.UP) {
            col -= 1;
        } else if (directionSnake == Direction.DOWN) {
            col +=1;
        } else if (directionSnake == Direction.RIGHT) {
            row +=1;
        } else {
            row -=1;
        }
        if (isGameOver(col, row)) {
            gameOver();
        }
        else if (checkFruit(row,col)) {
            pendentGrowth += 1;
            snake.deleteNodes();
            snake.addNodes(row,col);
        } else {
            if (pendentGrowth > 0) {
                snake.addNodes(row,col);
                pendentGrowth--;
            } else {
                snake.deleteNodes();
                snake.addNodes(row,col);
            }
            
        }
        
    }
    private boolean isGameOver(int col, int row) {
        if (col == numCols || row == numRows || col == -1 || row == -1) {
            return true;
        }
  
        for (int i = 0; i < snake.getSize();i++) {
            if (snake.getElementNodeCol(i) == col & snake.getElementNodeRow(i) == row) {
                return true;
            }
        }
        return false;
    }
    private void gameOver() {
        timer.stop();
        initBoard();
    }
    private boolean checkFruit(int row, int col) {
        if (row == fruit.getPosX() && col == fruit.getPosY()) {
            matrix[fruit.getPosY()][fruit.getPosX()] = 0;
            newFruit();
            return true;
        }
        return false;
    }
    private void newFruit() {
        boolean isSnakeThere = true;
        int row = 0;
        int col = 0;
        while (isSnakeThere) {
            isSnakeThere = false;
            Random random = new Random();
            row = random.nextInt(numRows);
            col = random.nextInt(numCols);
            for (int i = 0; i < snake.getSize();i++) {
                if (snake.getElementNodeCol(i) == col & snake.getElementNodeRow(i) == row) {
                    isSnakeThere = true;
                }
            }
        }
        matrix[col][row] = 3;
        fruit = new Food(false,row,col);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
