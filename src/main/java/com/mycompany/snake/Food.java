/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake;

/**
 *
 * @author alber
 */
public class Food {
    private boolean special;
    private int posX;
    private int posY;
    public Food(boolean special, int posX, int posY) {
        this.special = special;
        this.posX = posX;
        this.posY = posY;
    }
    public boolean getState() {
        return special;
    }
    public void setSpecial(boolean state) {
        this.special = state;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    
    
}
