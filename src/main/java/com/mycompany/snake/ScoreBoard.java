/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.snake;

/**
 *
 * @author alber
 */
public class ScoreBoard extends javax.swing.JPanel implements ScoreInterface {

    /**
     * Creates new form ScoreBoard
     */
    public ScoreBoard() {
        initComponents();
    }
    
    public void incrementScore() {
        int currentScore = ConfigData.getInstance().getScore() + 1;
        ConfigData.getInstance().setScore(currentScore);
        updateScoreLabel();
    }
    
    public void reset() {
        ConfigData.getInstance().setScore(0);
        updateScoreLabel();
    }
    
    private void updateScoreLabel() {
        jLabelScore.setText("" + ConfigData.getInstance().getScore());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelScore = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jLabelScore.setText("0");
        add(jLabelScore, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelScore;
    // End of variables declaration//GEN-END:variables
}
