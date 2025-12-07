package com.ar.AlgoRunner;
import java.util.*;

public class mazerBFS {

    // Helper to check boundaries
    public static boolean inLimit(int x, int y, int size){
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public static void bfs(Maze mazeObj) {
        int size = mazeObj.getSize();
        int[] start = mazeObj.getStart();
        box[][] grid = mazeObj.getMaze();

        // Stack for DFS (Backtracking)
        Stack<int[]> stack = new Stack<>();

        // 1. Initialize Start
        // We assume '0' or '1' is visited, '-1' is unvisited
        grid[start[0]][start[1]].setState(1);
        stack.push(new int[]{start[0], start[1]});

        List<int[]> moves = new ArrayList<>(Arrays.asList(
                new int[]{0, 1},  // Down
                new int[]{0, -1}, // Up
                new int[]{1, 0},  // Right
                new int[]{-1, 0}  // Left
        ));

        // 2. Main Loop
        while (!stack.isEmpty()) {
            // ERROR FIX: Use PEEK, not POP. We need to stay on this cell until it has no neighbors.
            int[] currentXY = stack.peek();
            int r = currentXY[0];
            int c = currentXY[1];
            box currentObj = grid[r][c];
            List<int[]> neighbours = new ArrayList<>();

            boolean foundNeighbor = false;

            // Randomize directions to create maze structure
            Collections.shuffle(moves);

            // 3. Check Neighbors
            for (int[] move : moves) {
                int dx = move[0];
                int dy = move[1];

                int nr = r + dx;
                int nc = c + dy;

                // ERROR FIX: Added check for 'neighbor.state == -1' (Unvisited)
                // ERROR FIX: Removed dangerous Math.random logic
                if (inLimit(nr, nc, size) && grid[nr][nc].getState() == 0) {
                    neighbours.add(new int[]{nr, nc, dx, dy});
                }
            }
            if (neighbours.isEmpty()) {
                stack.pop();
            }
            else{
                int rand = (int) (Math.random() * neighbours.size());
                int[] move = neighbours.get(rand);
                int nr = move[0];
                int nc = move[1];
                int dx = move[2];
                int dy = move[3];

                box next_neighbour = grid[nr][nc];
                    // Open Walls
                    // Note: Ensure your openWall method handles the logic correctly
                    // Opening wall on current relative to movement
                currentObj.openWall(new int[]{dx, dy});
                    // Opening wall on neighbor relative to where we came from
                next_neighbour.openWall(new int[]{-dx, -dy});

                    // Mark as visited
                next_neighbour.setState(1);

                    // Push new cell to stack
                stack.push(new int[]{nr, nc});
            }
        }
    }

    public static void main(String[] args) {
        // Assuming Maze class constructor handles initialization correctly
        Maze mazeObj = new Maze(5);
        mazeObj.initialiseMaze();
        bfs(mazeObj);
        for(int i = 0; i < mazeObj.getSize(); i++){
            for(int j = 0; j < mazeObj.getSize(); j++){
//                System.out.print(mazeObj.getMaze()[i][j].getWalls().keySet());
                System.out.print(mazeObj.getMaze()[i][j].getWalls().values());
            }

            System.out.println();
        }
    }
}