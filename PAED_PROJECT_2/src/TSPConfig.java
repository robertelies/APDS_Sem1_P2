import edu.salleurl.arcade.labyrinth.model.enums.Cell;
import edu.salleurl.arcade.labyrinth.model.enums.Direction;

import java.util.ArrayList;
import java.util.Collection;

// Helper class that will represent a configuration and make it easier for us to implement the BnB algorithm
public class TSPConfig implements Comparable<TSPConfig>{

    // The actual configuration (represents the order in which we visit the cities)
    public ArrayList<Direction> config;
    // The marking array for visited cities
    public Visited visited;
    //the maze
    public Cell[][] maze;
    //the actual position
    int x,y;
    //the EXIT cell position
    int end_position_x, end_position_y;

    //marking
    //public static int deadEnd;

    // Default constructor, for the first configuration
    public TSPConfig(Cell[][] board) {
        //include everything you need from the outside,
        this.config = new ArrayList<>();

        this.maze = board;

        this.x = Constants.MAZE_X_START;
        this.y = Constants.MAZE_Y_START;

        this.visited = new Visited();
        this.visited.setVisited(Constants.MAZE_X_START, Constants.MAZE_Y_START);

        this.end_position_x = Constants.MAZE_DIMENSION - 2;
        this.end_position_y = Constants.MAZE_DIMENSION - 2;
    }

    // Private constructor to "clone" a configuration, used when expanding it
    private TSPConfig(TSPConfig that, int x, int y) {
        this.config = new ArrayList<>();
        this.config.addAll(that.config);
        this.maze = that.maze;
        this.x = x;
        this.y = y;
        this.end_position_x = that.end_position_x;
        this.end_position_y = that.end_position_y;
        this.visited = new Visited(that.visited);
        this.visited.setVisited(x , y);

    }

    // Function that expands the current configuration, returning its successors as an arraylist
    public Iterable<TSPConfig> expand() {
        Collection<TSPConfig> successors = new ArrayList<>();

        // For each possible cell we can visit next
        if(visitable(x + 1, y)){ //GO DOWN
            TSPConfig successor = new TSPConfig(this, x + 1, y);
            successor.config.add(Direction.DOWN);

            successors.add(successor);
        }
        if(visitable(x, y + 1)){ //GO RIGHT
            TSPConfig successor1 = new TSPConfig(this, x , y + 1);
            successor1.config.add(Direction.RIGHT);

            successors.add(successor1);
        }
        if(visitable(x, y - 1)){ //GO LEFT
            TSPConfig successor2 = new TSPConfig(this, x, y - 1);
            successor2.config.add(Direction.LEFT);

            successors.add(successor2);
        }
        if(visitable(x - 1, y)){ //GO UP
            TSPConfig successor3 = new TSPConfig(this, x - 1, y);
            successor3.config.add(Direction.UP);

            successors.add(successor3);
        }
        /*
        //marking
        if(visitable(x + 1, y) || visitable(x, y + 1) || visitable(x, y - 1) || visitable(x - 1, y)){
            System.out.println("1  ");
            deadEnd++;
            System.out.println(deadEnd);
        }
         */

        return successors;
    }

    private boolean visitable(int x, int y) {
        return ((maze[x][y] == Cell.EMPTY || maze[x][y] == Cell.EXIT) && !visited.getStatus(x,y));
    }


    public boolean isSolution() {
        return maze[x][y] == Cell.EXIT;
    }

    public void show() {
        System.out.println("We found on the (" + x + ", " + y + ") a solution ");
        for(Direction dir : this.config){
            System.out.print(dir + " ");
        }
        System.out.println("\n\n");

    }

    public int getCost() {
        return config.size();
    }

    public int estimate() {
        return (getCost() + Math.abs(this.end_position_x - x) + Math.abs(this.end_position_y - y));
    }

    @Override
    public int compareTo(TSPConfig that) {
        // We compare estimated costs
        return (this.estimate() - that.estimate());
    }

}
