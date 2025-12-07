package com.ar.AlgoRunner.Models;

import java.util.HashMap;
import java.util.Map;

public class box {
    private int state;
    private boolean left;
    private boolean right;
    private boolean top;
    private boolean bottom;
    private Map<String,Boolean> walls;

    public box(int state){
        this.state = state;
        this.left = true;
        this.right = true;
        this.top = true;
        this.bottom = true;
        this.walls = new HashMap<>();
    }

    public int getState(){
        return this.state;
    }

    public void setState(int state){
        this.state = state;
    }

    public Map<String,Boolean> getWalls(){
        this.walls.put("left",this.left);
        this.walls.put("right",this.right);
        this.walls.put("top",this.top);
        this.walls.put("bottom",this.bottom);
        return this.walls;
    }

    public void openWall(int[] move){
        if (move[0] == 0 && move[1] == -1 ){
            this.top = false;
        }
        else if (move[0] == 0 && move[1] == 1 ){
            this.bottom = false;
        }
        else if (move[1] == 0 && move[0] == -1 ){
            this.left = false;
        }
        else if (move[1] == 0 && move[0] == 1 ){
            this.right = false;
        }
    }
}
