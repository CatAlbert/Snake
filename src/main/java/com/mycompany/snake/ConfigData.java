/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author alber
 */
public class ConfigData {
    public int numRows = 15;
    public int numCols = 15;
    private int deltaTime;
    private int score;
    private int stateLoser;
    private static ConfigData instance;
    
    private ConfigData() {
        deltaTime = 500;
        score = 0;
        stateLoser = 0;
    }
    
    public static ConfigData getInstance() {
        if (instance == null) {
            instance = new ConfigData();
        }
        return instance;
    }
    
    public int getNumRows() {
        return numRows;
    }
    public int getNumCols() {
        return numCols;
    }
    public void setNumRows(int newNum) {
        numRows = newNum;
    }
    public void setNumCols(int newNum) {
        numCols = newNum;
    }
    public int getDeltaTime() {
        return deltaTime;
    }
    public void setDeltaTime(int deltaTime) {
        this.deltaTime = deltaTime;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void setStateLoser(int state) {
        this.stateLoser = state;
    }
    
}
