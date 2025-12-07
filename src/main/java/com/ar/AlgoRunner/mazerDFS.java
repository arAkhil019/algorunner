package com.ar.AlgoRunner;
import java.util.*;

public class mazerDFS {

    //to check boundaries
    public static boolean inLimit(int x, int y, int cols, int rows){
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    public static void dfs(Maze mazeObj) {
        int size = mazeObj.getSize();
        int rows = size;
        int cols = size;

        int[] start = mazeObj.getStart();
        box[][] grid = mazeObj.getMaze();

        // (x, y) = (col, row)
        int startX = start[0];  // column
        int startY = start[1];  // row

        Stack<int[]> stack = new Stack<>();

        grid[startY][startX].setState(1);  // Marked as visited
        stack.push(new int[]{startX, startY});

        // (0, -1)=Up, (0, 1)=Down, (-1, 0)=Left, (1, 0)=Right
        List<int[]> directions = new ArrayList<>(Arrays.asList(
                new int[]{0, -1},
                new int[]{0, 1},
                new int[]{-1, 0},
                new int[]{1, 0}
        ));

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int currentX = current[0];  // column
            int currentY = current[1];  // row
            box currentBox = grid[currentY][currentX];

            // Adding valid unvisited neighbors
            List<int[]> neighbors = new ArrayList<>();

            // Randomizing directions
            Collections.shuffle(directions);

             for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];

                int nx = currentX + dx;  // next column
                int ny = currentY + dy;  // next row

                if (inLimit(nx, ny, cols, rows) && (grid[ny][nx].getState() == 0 || Math.random() < 0.05)) {
                    neighbors.add(new int[]{nx, ny, dx, dy});
                }
            }

            if (neighbors.isEmpty()) {
                stack.pop();
            } else {
                int[] chosen = neighbors.get((int)(Math.random() * neighbors.size()));
                int nextX = chosen[0];
                int nextY = chosen[1];
                int dx = chosen[2];
                int dy = chosen[3];

                box nextBox = grid[nextY][nextX];

                currentBox.openWall(new int[]{dx, dy});
                nextBox.openWall(new int[]{-dx, -dy});

                nextBox.setState(1);
                stack.push(new int[]{nextX, nextY});
            }
        }
    }

    public static void main(String[] args) {
        Maze mazeObj = new Maze(15);
        mazeObj.initialiseMaze();
        dfs(mazeObj);

        System.out.println("Generated " + mazeObj.getSize() + "x" + mazeObj.getSize() + " maze:");
        for(int y = 0; y < mazeObj.getSize(); y++){
            for(int x = 0; x < mazeObj.getSize(); x++){
                System.out.print(mazeObj.getMaze()[y][x].getWalls().values() + " ");
            }
            System.out.println();
        }
    }
}