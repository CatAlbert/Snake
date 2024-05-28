/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake;

/**
 *
 * @author alber
 */
public class ConfigData {
    public static final int NUM_ROWS = 20;
    public static final int NUM_COLS = 20;
    private int deltaTime;
    private static ConfigData instance;
    
    private ConfigData() {
        deltaTime = 500;
    }
    
    public static ConfigData getInstance() {
        if (instance == null) {
            instance = new ConfigData();
        }
        return instance;
    }
    
    public int getNumRows() {
        return NUM_ROWS;
    }
    public int getNumCols() {
        return NUM_COLS;
    }
    public int getDeltaTime() {
        return deltaTime;
    }
    public void setDeltaTime(int deltaTime) {
        this.deltaTime = deltaTime;
    }
    
}
