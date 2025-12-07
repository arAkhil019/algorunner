package com.ar.AlgoRunner.Services;

import com.ar.AlgoRunner.Models.Maze;
import com.ar.AlgoRunner.Models.box;
import org.springframework.stereotype.Component;

@Component
public class MazeGen {

    private static final int MAZE_SIZE = 15;

    public box[][] generateMaze() {
        Maze maze = new Maze(MAZE_SIZE);
        maze.initialiseMaze();
        mazerDFS.dfs(maze);

        box[][] grid = maze.getMaze();
        for (int y = 0; y < MAZE_SIZE; y++) {
            for (int x = 0; x < MAZE_SIZE; x++) {
                grid[y][x].getWalls();
            }
        }

        return grid;
    }

    public static void main(String[] args) {
        box[][] maze = new MazeGen().generateMaze();
        System.out.println("Generated " + MAZE_SIZE + "x" + MAZE_SIZE + " maze");
    }
}