package com.ar.AlgoRunner.Services;

import com.ar.AlgoRunner.Models.AStarNode;
import com.ar.AlgoRunner.Models.Point;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service responsible for Bot Intelligence and Pathfinding (A*).
 * Logic for Maze Generation has been moved to MazeGenerationService.
 */
@Service
public class MazeService {

    private static final int WALL = 1;
    // Direction vectors: Up, Down, Left, Right
    private static final int[][] DIRECTIONS = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

    /**
     * Calculates next Bot move using A* Pathfinding.
     * Finds the shortest path and returns the first step on that path.
     */
    public Point getNextBotMove(int[][] maze, Point botStart, Point playerTarget) {
        // A* Algorithm uses F cost (G + H) to prioritize exploration
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(Comparator.comparingInt(AStarNode::getF));
        Set<String> closedSet = new HashSet<>();

        AStarNode startNode = new AStarNode(botStart, null, 0, manhattan(botStart, playerTarget));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();

            // If we found the target (or are adjacent to it), reconstruct path
            if (current.getPoint().equals(playerTarget)) {
                return getFirstStep(current);
            }

            closedSet.add(current.getPoint().toString());

            for (int[] dir : DIRECTIONS) {
                int nx = current.getPoint().x() + dir[0];
                int ny = current.getPoint().y() + dir[1];
                Point neighborPoint = new Point(nx, ny);

                // Validation: Bounds check and Wall check
                if (nx < 0 || ny < 0 || nx >= maze[0].length || ny >= maze.length
                        || maze[ny][nx] == WALL || closedSet.contains(neighborPoint.toString())) {
                    continue;
                }

                // Calculate costs
                int newG = current.getG() + 1;
                int newH = manhattan(neighborPoint, playerTarget);

                // Simplified A* implementation: always add to openSet if not in closedSet
                AStarNode neighborNode = new AStarNode(neighborPoint, current, newG, newH);
                openSet.add(neighborNode);
            }
        }

        // Fallback: stay put if no path found
        return botStart;
    }

    // --- Helpers ---

    private int manhattan(Point a, Point b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    /**
     * Backtracks the A* path to find the second node, which is the bot's next move.
     */
    private Point getFirstStep(AStarNode targetNode) {
        AStarNode curr = targetNode;
        // The first node on the path is the child of the starting node (whose parent is null).
        // We stop when curr.parent's parent is null (meaning curr.parent is the start node).
        while (curr.getParent() != null && curr.getParent().getParent() != null) {
            curr = curr.getParent();
        }
        return curr.getPoint();
    }
}