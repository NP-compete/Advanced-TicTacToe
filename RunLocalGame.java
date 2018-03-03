/**
 * Basic local two-person play version of doubly nested tic-tac-toe
 * (a.k.a tic-tactics) game. Game board is displayed using BoardTextPrinter
 * which supports colored texts when run in bash enviornment. 
 *
 * @author Eunsong Choi
 * @version 1.0
 */

public class RunLocalGame{
    public static void main(String[] args){

        TicTacticsBoard board = new TicTacticsBoard();
        BoardPrinter printer = new BoardTextPrinter(board);
        GamePlayer player1 = new LocalPlayer(1);
        GamePlayer player2 = new LocalPlayer(-1);
        TicTactics game = new TicTactics(board, printer, player1, player2); 
        game.run();
    }
}
