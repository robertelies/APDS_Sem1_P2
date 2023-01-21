import edu.salleurl.arcade.ArcadeBuilder;
import edu.salleurl.arcade.Arcade;

/**************************************************
 CREATOR: MARCEL FARRES & ROBERT ELIES
 DATE OF CREATION: 22/2/2021
 LAST CHANGE MADE ON: 29/06/2022
 LANGUAGE: JAVA
 **************************************************/


public class Main {

    public static void main(String[] args) {

        ArcadeBuilder aB = new ArcadeBuilder();
        aB.setLabyrinthColumns(Constants.MAZE_DIMENSION);
        aB.setLabyrinthRows(Constants.MAZE_DIMENSION);
        aB.setLabyrinthSolver(new DemoLabyrinthSolver(Constants.OPTION_MAZE));

        aB.setWordsColumns(Constants.WORD_DIMENSION);
        aB.setWordsRows(Constants.WORD_DIMENSION);
        aB.setWordsSolver(new DemoWordsSolver());

        aB.setSeed(24);
        Arcade arcade = aB.build();
        arcade.run();

    }
}
