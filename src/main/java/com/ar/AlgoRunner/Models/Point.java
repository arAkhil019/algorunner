package com.ar.AlgoRunner.Models;
import java.util.Objects;

public record Point(int x, int y) {

    // Overrides required by record for standard methods (equals, hashCode, toString) are generated automatically.
    // We only need a custom toString for use in HashSets (A* closedSet keys).

    @Override
    public String toString() {
        return x + "," + y;
    }

    /**
     * Checks if two points are adjacent (used for movement validation).
     * @param other The point to compare against.
     * @return true if the distance is exactly 1 (horizontally or vertically).
     */
    public boolean isAdjacent(Point other) {
        if (other == null) return false;
        int dx = Math.abs(this.x - other.x);
        int dy = Math.abs(this.y - other.y);
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }
}