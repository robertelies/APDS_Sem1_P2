import edu.salleurl.arcade.labyrinth.controller.LabyrinthRenderer;
import edu.salleurl.arcade.labyrinth.model.LabyrinthSolver;
import edu.salleurl.arcade.labyrinth.model.enums.Cell;
import edu.salleurl.arcade.labyrinth.model.enums.Direction;

import javax.swing.*;
import java.util.*;

public class DemoLabyrinthSolver implements LabyrinthSolver{

    //the actual labyrinth with WALL, EMPTY...
    Cell[][] maze;

    //matrix that indicates if the position has been visited
    Visited visited = new Visited();

    //list of directions to follow
    List<Direction> directions = new ArrayList<>();

    //Backtracking or brute force or branch and bound
    int mode;

    /*
    //COUNTERS
    int branch_counter;
    int back_counter;
    int brute_counter;

    //more counters
    int recalls;
    int deadEnd;
    int alive;
    int reversed;
    long start;
    long end;
    long total;
    int hola;
     */

    public DemoLabyrinthSolver(int i) {
        this.mode = i;
        /*
        this.back_counter = 0;
        this.branch_counter = 0;
        this.brute_counter = 0;

        this.recalls = 0;
        this.deadEnd = 0;
        this.reversed = 0;
        this.alive = 0;

        this.start = 0;
        this.end = 0;
        this.total = 0;
        this.hola = 0;
         */
    }

    //-----------------------------------------------------------------------------------------------------------BACKTRACKING

    //NOTES: the preferences directions are: DOWN -> RIGHT -> LEFT -> UP
    public boolean backtracking(int x, int y, Direction dir){

        // we check if we finished the maze
        if (maze[x][y] == Cell.EXIT){
            directions.add(dir);
            return true;
        }

        // we check if we reached a WALL or a visited point
        if (maze[x][y] == Cell.WALL || visited.getStatus(x,y)) {
            return false;
        } else{
            directions.add(dir);
        }
        //if we reached this point it means that we are in a valid cell

        visited.setVisited(x,y); // we set the position as visited
        if (backtracking(x, y + 1, Direction.RIGHT)) return true;

        if (backtracking(x + 1, y, Direction.DOWN)) return true;

        if (backtracking(x, y - 1, Direction.LEFT)) return true;

        if (backtracking(x - 1, y, Direction.UP)) return true;

        //if we reach this point it means that we cannot move,
        // so we just erase the last movement and return false
        visited.setUnvisited(x,y);
        directions.remove(directions.size() - 1);
        return false;
    }

    //-----------------------------------------------------------------------------------------------------------BRUTE FORCE

    Direction[] fourDirections = { Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP};
    int final_K = 2147483647;

    public void bruteforce( int x, int y, int [] config, int k) {

        // initLevel
        config[k] = 0;
        visited.setVisited(x,y);

        // while hasSuccessors
        while(config[k] < 4) {

            if (maze[x][y] == Cell.EXIT) {
                // Solution case
                // this means that it has done fewer movements to arrive to the end than the previous solutions
                if(k < final_K){

                    directions = getDirections(config, k);
                    final_K = k;
                }

            } else {
                switch (config[k]){
                    case 0: if(visitable(x + 1, y)){ bruteforce(x + 1, y, config, k + 1); } break;
                    case 1: if(visitable(x, y + 1)){ bruteforce(x, y + 1, config, k + 1); } break;
                    case 2: if(visitable(x, y - 1)){ bruteforce(x, y - 1, config, k + 1); } break;
                    case 3: if(visitable(x - 1, y)){ bruteforce(x - 1, y, config, k + 1); } break;
                }
            }
            // nextSuccessor
            config[k]++;
        }
    }

    private List<Direction> getDirections(int[] config, int k) {
        List<Direction> direc = new ArrayList<>();
        for(int i = 0; i < k; i++){
            direc.add(fourDirections[config[i]]);
        }
        return direc;
    }


    private boolean visitable(int x, int y) {
        return ((maze[x][y] == Cell.EMPTY || maze[x][y] == Cell.EXIT) && !visited.getStatus(x,y));
    }

    //----------------------------------------------------------------------------------------------------------- Branch and Bound

    public void branchAndBound(){

        int best = Integer.MAX_VALUE;
        PriorityQueue<TSPConfig> queue = new PriorityQueue<>();

        // We generate the first configuration and queue it
        TSPConfig initial = new TSPConfig(this.maze);
        queue.offer(initial);

        while (!queue.isEmpty()) {
            //As it is a priority Queue, the first configuration will be the most efficient
            TSPConfig first = queue.poll();

            // We expand
            Iterable<TSPConfig> successors = first.expand();

            for (TSPConfig successor : successors) {

                if (successor.isSolution()) {
                    // Optimization Process
                    if (successor.getCost() < best) {
                        best = successor.getCost();
                        this.directions = successor.config;
                    }

                } else {
                    // If it's not a solution -> PBCBS
                    if (successor.getCost() < best) {
                        queue.offer(successor);
                    }
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------SOLVE
    @Override
    public List<Direction> solve(Cell[][] cells, LabyrinthRenderer labyrinthRenderer) {

        //set up the maze
        this.maze = cells;

        if(mode == 1) {
            JOptionPane.showMessageDialog(null, "BACKTRACKING");
            //call the backtracking
            backtracking(Constants.MAZE_X_START, Constants.MAZE_Y_START, Direction.LEFT);

            //erase the first position of the directions list
            directions.remove(0);

            labyrinthRenderer.render(cells, directions);
            this.visited.reset();
        }

        if(mode == 2) {
            JOptionPane.showMessageDialog(null, "BRUTE FORCE");
            int[] config = new int[1000];

            bruteforce(Constants.MAZE_X_START, Constants.MAZE_Y_START, config, 0);
            labyrinthRenderer.render(cells, directions);
            this.visited.reset();
        }
        if(mode == 3){
            JOptionPane.showMessageDialog(null, "BRANCH & BOUND");

            branchAndBound();
            labyrinthRenderer.render(cells, directions);
            this.visited.reset();
        }

        return directions;
    }

/*
    //------------------------
    end = System.nanoTime();

                if(hola == 0){
        hola = 1;
    }else{
        System.out.println("Time: " + (end - start));
        total = total + (end - start);
    }
    start = System.nanoTime();
    recalls++;//marking
    //------------------------

 */

}
