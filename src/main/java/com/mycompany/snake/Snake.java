/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author alber
 */
public class Snake {
    private Queue<Node> snake;
    public Snake() {
        snake = new LinkedList<Node>();
        snake.add(new Node(ConfigData.getInstance().getNumCols()/3 - 2, ConfigData.getInstance().getNumCols()/2 - 1));
        snake.add(new Node(ConfigData.getInstance().getNumCols()/3 - 1, ConfigData.getInstance().getNumCols()/2 - 1));
        snake.add(new Node(ConfigData.getInstance().getNumCols()/3, ConfigData.getInstance().getNumCols()/2 - 1));
        
    }
    
public void addNodes(int col, int row) {
      snake.add(new Node(col, row));
}
public int getSize() {
    return snake.size();
}
public void deleteNodes() {
    snake.poll();
}
public int getFirstNodeCol() {
   Queue<Node> snakeRemove = new LinkedList<>(snake);
    for (int num = 0; num < (getSize() - 1);num++ ) {
        snakeRemove.poll();
    }
    return snakeRemove.peek().getPosY();
}
public int getFirstNodeRow() {
    Queue<Node> snakeRemove = new LinkedList<>(snake);
    for (int num = 0; num < (getSize() - 1);num++ ) {
        snakeRemove.poll();
    }
    return snakeRemove.peek().getPosX();
}

public int getLastNodeRow() {
    return snake.peek().getPosX();
}
public int getLastNodeCol() {
    return snake.peek().getPosY();
}



public int getElementNodeRow(int i) {
    Queue<Node> snakeRemove = new LinkedList<>(snake);
    for (int num = 0; num < i;num++ ) {
        snakeRemove.poll();
    }
    return snakeRemove.peek().getPosX();
}
public int getElementNodeCol(int i) {
    Queue<Node> snakeRemove = new LinkedList<>(snake);
    for (int num = 0; num < i;num++ ) {
        snakeRemove.poll();
    }
    return snakeRemove.peek().getPosY();
}


    
    
}
