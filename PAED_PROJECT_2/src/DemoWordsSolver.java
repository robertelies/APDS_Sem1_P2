import edu.salleurl.arcade.words.controller.WordsRenderer;
import edu.salleurl.arcade.words.model.WordsSolver;

import javax.swing.*;
import java.util.ArrayList;

public class DemoWordsSolver implements WordsSolver{

    //solution declaration variable
    private int[] solution = new int[4];

    //board full of letters
    char[][] board;

    //word to find
    char[] word;

    //max characters of the word
    int maxchars;

    public DemoWordsSolver() {
    }

    //-----------------------------------------------------------------------------------------------------------GREEDY

    public void greedy(int x, int y, int index, ArrayList<Integer> solutionV2, int direction){
        ArrayList<Integer> solution = new ArrayList<>(solutionV2);

       switch (index){
           case 0:
               for(int x_board = 0; x_board < Constants.WORD_DIMENSION; x_board++){
                   for(int y_board = 0; y_board < Constants.WORD_DIMENSION; y_board++){
                       if(board[x_board][y_board] == this.word[0]){
                           solution.add(x_board);
                           solution.add(y_board);

                           greedy(x_board, y_board, 1, solution, 0);
                           solution.clear();
                       }
                   }
               }
               break;

           case 1:
               if(valid(x,y,2)) {
                   if (board[x + 1][y + 1] == this.word[1]) {

                       solution.add(x + 1);
                       solution.add(y + 1);
                       greedy(x + 1, y + 1, 2, solution, 2);//diagonal = 2
                   }
               }
               if(valid(x,y,1)) {
                   if(board[x + 1][y] == this.word[1] && valid(x,y,2)) {
                       solution.add(x + 1);
                       solution.add(y);
                       greedy(x + 1, y, 2, solution, 1);//down = 1
                   }
               }
               if(valid(x,y,3)) {
                   if (board[x][y + 1] == this.word[1] && valid(x, y, 2)) {
                       solution.add(x);
                       solution.add(y + 1);
                       greedy(x, y + 1, 2, solution, 3);//RIGHT = 3
                   }
               }

               break;

           default:
               if(this.maxchars <= solution.size()/2 ){
                   //solution
                   this.solution = convertToSolution(solution);
               }else{
                   switch (direction){
                       case 2:
                           if(x + 1 < Constants.WORD_DIMENSION && y + 1 < Constants.WORD_DIMENSION) {
                               if (board[x + 1][y + 1] == this.word[index]) {
                                   solution.add(x + 1);
                                   solution.add(y + 1);
                                   greedy(x + 1, y + 1, index + 1, solution, 2);
                               }
                           }
                           break;
                       case 1:
                           if(x + 1 < Constants.WORD_DIMENSION && y < Constants.WORD_DIMENSION) {
                               if (board[x + 1][y] == this.word[index]) {
                                   solution.add(x + 1);
                                   solution.add(y);
                                   greedy(x + 1, y, index + 1, solution, 1);//down = 1
                               }
                           }
                           break;
                       case 3:
                           if(x < Constants.WORD_DIMENSION && y + 1 < Constants.WORD_DIMENSION) {
                               if (board[x][y + 1] == this.word[index]) {
                                   solution.add(x);
                                   solution.add(y + 1);
                                   greedy(x, y + 1, index + 1, solution, 3);//RIGHT = 3
                               }
                           }
                            break;
                       default:
                           System.out.println("problems");
                           break;
                   }
               }
        }
    }

    private boolean valid(int x, int y, int direction) {
        if(direction == 1){
            return this.word.length > 2 && (x + 1) < Constants.WORD_DIMENSION;
        }else if(direction == 2){
            return this.word.length > 2 && (x + 1) < Constants.WORD_DIMENSION && (y + 1) < Constants.WORD_DIMENSION;
        }else if(direction == 3){
            return this.word.length > 2 && (y + 1) < Constants.WORD_DIMENSION;
        }
        return false;
    }

    private int[] convertToSolution(ArrayList<Integer> solution) {
        int [] ret = new int[4];

        ret[0] = solution.get(0);
        ret[1] = solution.get(1);

        ret[2] = solution.get(solution.size() - 2);
        ret[3] = solution.get(solution.size() - 1);


        return ret;
    }

    public int[] solve(char[][] board, String word, WordsRenderer wordsRenderer){

        this.board = board;
        this.word = word.toCharArray();
        this.maxchars = word.length();
        ArrayList<Integer> solu = new ArrayList<>();

        JOptionPane.showMessageDialog(null, "GREEDY");
        greedy(Constants.WORD_X_START, Constants.WORD_Y_START, 0, solu, 0);

        wordsRenderer.render(board, word, this.solution);

        return this.solution;
    }
}