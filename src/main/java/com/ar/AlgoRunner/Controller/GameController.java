package com.ar.AlgoRunner.Controller;
import com.ar.AlgoRunner.DTO.GameState;
import com.ar.AlgoRunner.DTO.MoveRequest;
import com.ar.AlgoRunner.Models.Point;
import com.ar.AlgoRunner.Models.box;
import com.ar.AlgoRunner.Services.MazeGen;
import com.ar.AlgoRunner.Services.MazeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class GameController {
    private final MazeService maze;
    private final MazeGen mazeGen;

    @Autowired
    public GameController(MazeService mazeService, MazeGen mazeGenerationService) {
        this.maze = mazeService;
        this.mazeGen = mazeGenerationService;
    }

    private box[][] currentMaze; // The maze grid
    private Point botPos;        // Current Bot Location
    private Point playerPos;     // Current Player Location
    private boolean isGameStarted = false;
    private int GRID_SIZE;

    // Start via WebSocket or HTTP depending on client. For WS, use @MessageMapping("/start").
    @MessageMapping("/start")
    @SendTo("/topic/game-state")
    public GameState startGame() {
        // 1. Generate Maze (DFS)
        this.currentMaze = mazeGen.generateMaze();
        this.GRID_SIZE = this.currentMaze.length;
        // 2. Set Spawn Points
        this.playerPos = new Point(1, 1);
        this.botPos = new Point(GRID_SIZE - 2, GRID_SIZE - 2); // Opposite corner inside bounds

        this.isGameStarted = true;
        return new GameState(playerPos, botPos, false);
    }

    @MessageMapping("/move")
    @SendTo("/topic/game-state")
    public GameState processMove(MoveRequest move) {
        if (!isGameStarted) return new GameState(new Point(1,1), new Point(1,1), false);

        // 1. Validate Player Move using walls and adjacency
        int targetX = move.x(); // adapt to your DTO accessor
        int targetY = move.y();
        if (isValidMove(playerPos, targetX, targetY)) {
            this.playerPos = new Point(targetX, targetY);
        }

        // Convert box[][] to simple PATH/WALL grid if service expects int[][]
        int[][] grid01 = toBinaryGrid(currentMaze);
        Point nextBotPos = maze.getNextBotMove(grid01, botPos, playerPos);
        this.botPos = nextBotPos;

        // 3. Check Collision
        boolean gameOver = playerPos.equals(botPos);
        return new GameState(playerPos, botPos, gameOver);
    }

    // Validate move: within bounds, adjacent by 1 step, and no wall blocking between cells
    private boolean isValidMove(Point from, int toX, int toY) {
        if (toX < 0 || toX >= GRID_SIZE || toY < 0 || toY >= GRID_SIZE) return false;
        int dx = toX - from.x();
        int dy = toY - from.y();
        if (Math.abs(dx) + Math.abs(dy) != 1) return false; // only one-step moves

        Map<String, Boolean> walls = currentMaze[from.y()][from.x()].getWalls();
        if (dx == 1 && walls.get("right")) return false;
        if (dx == -1 && walls.get("left")) return false;
        if (dy == 1 && walls.get("bottom")) return false;
        if (dy == -1 && walls.get("top")) return false;
        return true;
    }

    // Minimal converter: treat any cell with an open wall as PATH(0), otherwise WALL(1)
    private int[][] toBinaryGrid(box[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] out = new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Map<String, Boolean> w = grid[y][x].getWalls();
                boolean allClosed = w.get("top") && w.get("bottom") && w.get("left") && w.get("right");
                out[y][x] = allClosed ? 1 : 0;
            }
        }
        return out;
    }
}
