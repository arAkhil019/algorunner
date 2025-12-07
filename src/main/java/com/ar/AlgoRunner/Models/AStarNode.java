package com.ar.AlgoRunner.Models;

/**
 * Node structure specifically used by the A* pathfinding algorithm.
 * Contains path cost information (g, h) and a reference to the previous node (parent).
 */
public class AStarNode {
    private final Point point;
    private AStarNode parent;
    private int g; // Cost from start
    private final int h; // Heuristic (estimated cost to end)

    public AStarNode(Point point, AStarNode parent, int g, int h) {
        this.point = point;
        this.parent = parent;
        this.g = g;
        this.h = h;
    }

    // Getters and Setters
    public Point getPoint() { return point; }
    public AStarNode getParent() { return parent; }
    public int getG() { return g; }
    public int getH() { return h; }

    public int getF() {
        return g + h;
    }

    // Since 'g' and 'parent' might be updated if a shorter path is found.
    public void setG(int g) {
        this.g = g;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }
}