package com.ar.AlgoRunner;

import com.ar.AlgoRunner.Maze;
import javax.swing.*;
import java.awt.*;

// =========================================
// 1. DATA PARSING
// =========================================
class GridBoxInfo {
    public boolean top, bottom, left, right;

    // Constructor that takes the boolean array [Top, Bottom, Left, Right]
    public GridBoxInfo(boolean[] flags) {
        this.top    = flags[0];
        this.bottom = flags[1];
        this.left   = flags[2];
        this.right  = flags[3];
    }
}

// =========================================
// 2. DRAWING PANEL
// =========================================
class MazePanel extends JPanel {
    private final GridBoxInfo[][] gridData;
    private final int rows;
    private final int cols;
    private final int cellSize = 60; // Made cells bigger (60px) since it's a small 5x5 grid
    private final int padding = 40;

    public MazePanel(boolean[][][] rawData) {
        this.rows = rawData.length;
        this.cols = rawData[0].length;
        this.gridData = new GridBoxInfo[rows][cols];

        // Convert raw boolean arrays to Objects
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                gridData[r][c] = new GridBoxInfo(rawData[r][c]);
            }
        }

        // Set window size
        setPreferredSize(new Dimension(cols * cellSize + padding * 2, rows * cellSize + padding * 2));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Make lines look smooth and thick
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(4)); // Thicker walls
        g2d.setColor(Color.decode("#2c3e50")); // Dark Blue-Grey color

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GridBoxInfo cell = gridData[r][c];

                int x1 = c * cellSize + padding;
                int y1 = r * cellSize + padding;
                int x2 = (c + 1) * cellSize + padding;
                int y2 = (r + 1) * cellSize + padding;

                // Draw Walls based on the [T, B, L, R] mapping
                if (cell.top)    g2d.drawLine(x1, y1, x2, y1); // Top
                if (cell.bottom) g2d.drawLine(x1, y2, x2, y2); // Bottom
                if (cell.left)   g2d.drawLine(x1, y1, x1, y2); // Left
                if (cell.right)  g2d.drawLine(x2, y1, x2, y2); // Right
            }
        }
    }
}

// =========================================
// 3. MAIN EXECUTION
// =========================================
public class MazeVisualizer {

    public static void main(String[] args) {
        // Your specific data converted to Java Syntax 
        // Mapping assumption: {TOP, BOTTOM, LEFT, RIGHT}
        boolean[][][] rawData = {
                // Row 0
                {
                        {true, true, true, false}, {true, true, false, false}, {false, true, true, false}, {true, true, false, true}, {false, true, true, false}
                },
                // Row 1
                {
                        {true, false, true, false}, {true, false, true, false}, {true, false, true, false}, {true, true, false, false}, {false, false, true, true}
                },
                // Row 2
                {
                        {true, false, false, true}, {false, false, true, true}, {true, false, true, false}, {true, false, false, true}, {false, true, true, false}
                },
                // Row 3
                {
                        {true, true, false, false}, {false, true, true, true}, {true, false, true, false}, {true, true, false, false}, {false, false, true, false}
                },
                // Row 4
                {
                        {true, false, false, true}, {false, true, false, true}, {false, false, false, true}, {false, false, true, true}, {true, false, true, true}
                }
        };

        // Create Window
        JFrame frame = new JFrame("5x5 Maze Data Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MazePanel panel = new MazePanel(rawData);
        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}