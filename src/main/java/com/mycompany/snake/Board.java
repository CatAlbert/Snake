/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.snake;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import static java.lang.Math.random;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
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
    private ScoreInterface score;
    private Image[] images;
    private Food specialFood;
    
    public Board() {
        initComponents();
        loadImages();
        addKeyListener(new MyKeyAdapter());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        initBoard();
    }

    class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    canMove(Direction.LEFT);
                    break;
                case KeyEvent.VK_D:    
                case KeyEvent.VK_RIGHT:
                    canMove(Direction.RIGHT);
                    break;
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    canMove(Direction.UP);
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    canMove(Direction.DOWN);
                    break;
                case KeyEvent.VK_R:
                    //reinicio el board
                    score.reset();
                    initBoard();
                    break;
                default:
                    break;
            }
            repaint();
        }
    }
    public void setScoreInterface(ScoreInterface scoreInterface) {
        this.score = scoreInterface;
    }

    public void initMatrix(int numRows, int numCols) {
        //inicializo la matriz toda a 1
        matrix = new int[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                matrix[row][col] = 0;
            }
        }
    }
    
    public void loadImages() {
        images = new Image[17];
        try {
            images[0] = ImageIO.read(getClass().getResource("/images/suelo.png"));
            images[1] = ImageIO.read(getClass().getResource("/images/cuerpo.png"));
            images[2] = ImageIO.read(getClass().getResource("/images/cuerpolado.png"));
            images[3] = ImageIO.read(getClass().getResource("/images/cabezaAbajo.png"));
            images[4] = ImageIO.read(getClass().getResource("/images/colaAbajo.png"));
            images[5] = ImageIO.read(getClass().getResource("/images/arribaderecha.png"));
            images[6] = ImageIO.read(getClass().getResource("/images/arribaizquierda.png"));
            images[7] = ImageIO.read(getClass().getResource("/images/abajoderecha.png"));
            images[8] = ImageIO.read(getClass().getResource("/images/abajoizquierda.png"));
            images[9] = ImageIO.read(getClass().getResource("/images/cabezaizquierda.png"));
            images[10] = ImageIO.read(getClass().getResource("/images/cabezaDerecha.png"));
            images[11] = ImageIO.read(getClass().getResource("/images/cabezaArriba.png"));
            images[12] = ImageIO.read(getClass().getResource("/images/colaizquierda.png"));
            images[13] = ImageIO.read(getClass().getResource("/images/colaDerecha.png"));
            images[14] = ImageIO.read(getClass().getResource("/images/colaArriba.png"));
            images[15] = ImageIO.read(getClass().getResource("/images/fruta.png"));
            images[16] = ImageIO.read(getClass().getResource("/images/manzana.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initBoard() {
        pendentGrowth = 0;
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
        moveSnake();
        //Se comprueba si se tiene que borrar o crear una fruta especial (saldra en uno de cada 30 ticks)
        Random random = new Random();
        int numeroAleatorio = random.nextInt(30) + 1;
        if (numeroAleatorio == 5) {
            if (specialFood == null) {
                newSpecialFruit();
            } else {
                matrix[specialFood.getPosY()][specialFood.getPosX()] = 0;
                specialFood = null;
            }
        }
        updateMatrix();
        repaint();
    }
    
    private void newSpecialFruit() {
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
        matrix[col][row] = 16;
        specialFood = new Food(true,row,col);
    }
    
    

    public void updateMatrix() {
        for (int i = 0; i < snake.getSize(); i++) {
            int row = snake.getElementNodeRow(i);
            int col = snake.getElementNodeCol(i);
            int nextRow = -1;
            int nextCol = -1;
            int lastRow = -1;
            int lastCol = -1;

            if (i != 0) {
                lastRow = snake.getElementNodeRow(i - 1);
                lastCol = snake.getElementNodeCol(i - 1);
            }

            if (i < snake.getSize() - 1) {
                nextRow = snake.getElementNodeRow(i + 1);
                nextCol = snake.getElementNodeCol(i + 1);
            }

            matrix[col][row] = 1;

            if (i != 0 && i < snake.getSize() - 1) {
                // si se esta moviendo horizontalmente
                if (row == lastRow && row == nextRow) {
                    matrix[col][row] = 1;
                }
                // si se esta moviendo verticalmente
                else if (col == lastCol && col == nextCol) {
                    matrix[col][row] = 2;
                }
                // Comprobar si el nodo está girando (cambio de dirección)
                else {
                    if ((row != lastRow || col != lastCol) && (row != nextRow || col != nextCol)) {
                        if (lastRow == row && nextCol == col) {
                            if (nextRow < row) {
                                    if (lastCol > col) {
                                        //gira arriba izquierda
                                        matrix[col][row] = 6;
                                    } else {
                                        //gira abajo izquierda
                                        matrix[col][row] = 8;
                                    }
                            } else {
                                    if (lastCol > col) {
                                        //gira arriba derecha
                                        matrix[col][row] = 5;
                                    } else {
                                        //gira abajo derecha
                                        matrix[col][row] = 7;
                                    }

                            }
                        } else if (lastCol == col && nextRow == row) {
                            if (nextCol < col) {
                                if (row > lastRow) {
                                    //gira abajo izquierda
                                    matrix[col][row] = 8;
                                } else {
                                    //gira abajo derecha
                                    matrix[col][row] = 7;
                                }
                            } else {
                                if (row > lastRow) {
                                    //gira arriba izquierda
                                    matrix[col][row] = 6;
                                } else {
                                    //gira arriba derecha
                                    matrix[col][row] = 5;
                                }
                            }
                        } 
                    }
                }
            } else if (i == 0) {
                if (nextRow != -1 && nextCol != -1) {
                    if (row == nextRow) {
                        if (col < nextCol) {
                            //Cola mira arriba
                            matrix[col][row] = 14;
                        } else {
                            //Cola mira abajo
                            matrix[col][row] = 4;
                        }
                    } else if (col == nextCol) {
                        if (row < nextRow) {
                            //Cola mira izquierda
                            matrix[col][row] = 12;
                        } else {
                            //Cola mira derecha
                            matrix[col][row] = 13;
                        }
                    }
                }
            } else if (i == snake.getSize() - 1) {
                if (lastRow != -1 && lastCol != -1) {
                    if (row == lastRow) {
                        if (col < lastCol) {
                            //cabeza mira arriba
                            matrix[col][row] = 11;
                        } else {
                            //Cabeza mira abajo
                            matrix[col][row] = 3;
                        }
                    } else if (col == lastCol) {
                        if (row < lastRow) {
                            //Cabeza mira izquierda
                            matrix[col][row] = 9;
                        } else {
                            //Cabeza mira derecha
                            matrix[col][row] = 10;
                        }
                    }
                }
            }

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
    //Dibuajamos cada cuadrado del tablero
    private void drawSquare(Graphics g, int row, int col, int imageIndex) {
    Image backgroundImage = images[0]; 
    int x = col * getSquareWidth();
    int y = row * getSquareHeight();
    int backgroundMargin = 1;  
    int imageMargin = -1; 
    // Dibujar los cuadrados del fondo
    if (backgroundImage != null) {
        g.drawImage(backgroundImage, x, y, getSquareWidth(), getSquareHeight(), this);
    }
    
    // Si el colorIndex no es 0, dibujar la imagen correspondiente
    if (imageIndex != 0) {
        Image image = images[imageIndex];
        if (image != null) {
            g.drawImage(image, x + imageMargin, y + imageMargin, getSquareWidth() - 2 * imageMargin, getSquareHeight() - 2 * imageMargin, this);
        }
    }
}




    private int getSquareWidth() {
        return getWidth() / numCols;
    }

    private int getSquareHeight() {
        return getHeight() / numRows;
    }
    //comprobar que no se choque con 
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
        //Comprobamos hacia donde se quiere mover
        if (directionSnake == Direction.UP) {
            col -= 1;
        } else if (directionSnake == Direction.DOWN) {
            col +=1;
        } else if (directionSnake == Direction.RIGHT) {
            row +=1;
        } else {
            row -=1;
        }
        //Comprobamos si el juego ha acabado
        if (isGameOver(col, row)) {
            gameOver();
        }
        //comprobamos si se ha cmido una fruta
        else if (checkFruit(row,col)) {
            snake.deleteNodes();
            snake.addNodes(row,col);
        } else {
            //Se comprueba si se tiene que crecer en este tick
            if (pendentGrowth > 0) {
                snake.addNodes(row,col);
                pendentGrowth--;
                //si no se mueve toda la snake
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
            if (snake.getElementNodeCol(i) == col && snake.getElementNodeRow(i) == row ) {
                if (snake.getLastNodeCol() == col && snake.getLastNodeRow() == row) {
                    if (pendentGrowth != 0) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    private void gameOver() {
        timer.stop();
        //Se inicializa un nuevo JFrame diciendo que hemos muerto
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        currentFrame.dispose();
        LoserFrame loserFrame = new LoserFrame();
        loserFrame.setVisible(true); 
    }
    //Se comprueba la posicion de la fruta y si se ha comido
    private boolean checkFruit(int row, int col) {
        if (row == fruit.getPosX() && col == fruit.getPosY()) {
            matrix[fruit.getPosY()][fruit.getPosX()] = 0;
            score.incrementScore();
            pendentGrowth += 1;
            newFruit();
            return true;
        } else if (specialFood != null && row == specialFood.getPosX() && col == specialFood.getPosY()) {
            matrix[specialFood.getPosY()][specialFood.getPosX()] = 0;
            score.incrementScore();
            score.incrementScore();
            score.incrementScore();
            pendentGrowth += 3;
            return true;
        }
        return false;
    }
    //Se crea una nueva fruta
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
        matrix[col][row] = 15;
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
