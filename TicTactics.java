import java.util.*;

/**
 * A back-bone class of doublely-nested tic-tac-toe(aka tic-tactics) game.
 * One can use this class instance with BoardPrinter instance and GamePlayer 
 * instances to run different types of games-e.g. Local two person game, 
 * over-the-network two-person game, single person game with computer AI etc.  
 *
 */

public class TicTactics{

    private final TicTacticsBoard board;
    private final BoardPrinter printer;
    private final GamePlayer player1, player2;
    private final Scanner sc;
    private int whosTurn;
    private int nextBigRow, nextBigCol;

    public TicTactics(TicTacticsBoard board, BoardPrinter printer,
                       GamePlayer player1, GamePlayer player2){
        this.board = board; 
        this.printer = printer;
        this.player1 = player1;
        this.player2 = player2;
        this.player1.addToGame(this);
        this.player2.addToGame(this);
        this.sc = new Scanner(System.in);
        this.whosTurn = 1; // X always goes first 
        this.nextBigRow = -1;
        this.nextBigCol = -1;
    }
    public TicTacticsBoard getBoard() {
        return this.board;
    } 
    public int getNextBigRow(){
        return this.nextBigRow;
    }
    public int getNextBigCol(){
        return this.nextBigCol;
    }
    public void setNextBigRow(int newRow){
        this.nextBigRow = newRow;
    }
    public void setNextBigCol(int newCol){
        this.nextBigCol = newCol;
    }
    public void printBoard(){
        this.printer.printBoard();
    }
    public void playNextTurn(){
        if ( this.whosTurn == 1 ) player1.play();
        else player2.play();
    }
    public String readLine(){
        return this.sc.nextLine();
    }
    public void close(){
        this.sc.close();
    }
    public void setNextTurn(){
        this.whosTurn *= -1;
    }

    public void run(){

        printBoard();
        System.out.println("X goes first.");
        while (true){
            try{
                this.player1.play();
                printBoard();
                break;
            }
            catch ( Exception e){
                System.out.println("invalid selection. Try again.");
            }
        }
        while (true){
            playNextTurn();
            printBoard();
            int result = board.evaluate();
            if ( result != 0 ){
                String whom = ( result == 1 ? "X" : "O");
                System.out.println(whom + " won the game!\n");
                close();
                System.exit(0);
            }
            if ( board.isDone() ){
                System.out.println("game over. tie game!");
                close();
                System.exit(0);
            }
        }
    }

}
