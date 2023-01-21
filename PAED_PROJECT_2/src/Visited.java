public class Visited{
    boolean[][] matrix;

    public Visited() {
        this.matrix = new boolean[Constants.MAZE_DIMENSION][Constants.MAZE_DIMENSION];
        for(boolean visi[] : matrix){
            for(boolean visi_2 : visi){
                visi_2 = false;
            }
        }
    }
    public Visited(Visited that){
        this.matrix = new boolean[Constants.MAZE_DIMENSION][Constants.MAZE_DIMENSION];
        for(int i =0; i < Constants.MAZE_DIMENSION; i++){
            for(int e =0; e < Constants.MAZE_DIMENSION; e++){
                this.matrix[i][e] = that.getStatus(i,e);
            }
        }
    }

    public void reset(){
        for(boolean visi[] : matrix){
            for(boolean visi_2 : visi){
                visi_2 = false;
            }
        }
    }
    //setter
    public void setVisited(int x, int y){
        this.matrix[x][y] = true;
    }

    public void setUnvisited(int x, int y){
        this.matrix[x][y] = false;
    }


    //getter
    public boolean getStatus(int x, int y){
        return matrix[x][y];
    }

}
