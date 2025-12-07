package com.ar.AlgoRunner;

import org.springframework.stereotype.Component;

@Component
public class MazeGen {

    public box[][] generateMaze() {
        Maze maze = new Maze(5);
        maze.initialiseMaze();
        mazerBFS.bfs(maze);
//        System.out.println("Maze:");
        box[][] op = maze.getOutMaze(maze);
//        System.out.println(op[0][0].length);
        return op;
    }
    public static void main(String[] args) {
        new MazeGen().generateMaze();
    }
}