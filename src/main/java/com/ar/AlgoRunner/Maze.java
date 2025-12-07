package com.ar.AlgoRunner;

import java.util.HashMap;

public class Maze {
    private box[][] maze;
    private int size;
    private int startX;
    private int startY;

    public Maze(int size) {
        this.size = size;
        this.maze = new box[size][size];
        // optional: set default start/end positions
        this.startX = 0;
        this.startY = 0;
    }

    public box[][] getMaze() {
        return maze;
    }

    public void initialiseMaze(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                maze[i][j] = new box(0);
            }
        }
    }
    public int[] getStart(){
        return new int[]{startX,startY};
    }
    public int getSize(){
        return size;
    }
    public box[][] getOutMaze(Maze mazeObj){
        int size = mazeObj.getSize();
        box[][] outMaze = new box[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                outMaze[i][j] = mazeObj.getMaze()[i][j];
//                System.out.print(mazeObj.getMaze()[i][j].getWalls().values());
            }
//            System.out.println();
        }
        return outMaze;
    }
}
