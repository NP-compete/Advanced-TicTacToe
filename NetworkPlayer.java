import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class NetworkPlayer implements GamePlayer{

    private TicTactics game;
    private Socket opponent;
    private final int side; // 1 if X, -1 if O
    private final String symbol;   
    private final boolean isLocal;
    private InputStream sockInput;
    private OutputStream sockOutput;
    /**
     * public constructor 
     * 
     * @param side integer indicating which side the player plays for 
     *        "X" (side=1) or for "O" (side=-1)
     * @param isLocal true if this instance represents local player 
     *        and false if it represents a player over the network 
     * @param opponent Socket object connected to the opponent(client or server)
     */
    public NetworkPlayer(int side, boolean isLocal, Socket opponent){
        assert ( side == 1 || side == -1 );
        this.side = side;
        this.symbol = ( side == 1? "X" : "O");
        this.isLocal = isLocal; 
        this.opponent = opponent;
    }

    /**
     * a public method making a channel from this instance to a game instance.
     * this method must be invoked before invoking play() method.
     *
     * @param game an instance object of TicTactics interface where this instance
     *             will be played
     */
    public void addToGame(TicTactics game){
        this.game = game;
    }

    public void play(int bigRow, int bigCol){
        this.game.setNextBigRow(bigRow);
        this.game.setNextBigCol(bigCol);
        play();
    }

    public void play(){
        chooseSubBoard();
        if ( this.isLocal ) playLocal();
        else playRemote();
    }

    private void playLocal(){

        int bigRow = this.game.getNextBigRow();
        int bigCol = this.game.getNextBigCol();
        String whom = this.symbol;
        System.out.println(String.format(
                           "%s Playing cell %1d-%1d", whom, bigRow, bigCol));
        while (true){
            try{    
                //chooseSubBoard();
                System.out.println(String.format(
                                   "Enter row, col to place %s in the sub-board", whom));

                String[] tokens = this.game.readLine().trim().split(",");
                int subRow =  Integer.parseInt(tokens[0].trim());
                int subCol =  Integer.parseInt(tokens[1].trim());
                if ( this.side == 1 ) {
                    this.game.getBoard().putX(bigRow, bigCol, subRow, subCol);
                }
                else {
                    this.game.getBoard().putO(bigRow, bigCol, subRow, subCol);
                }
                this.game.setNextBigRow(subRow);
                this.game.setNextBigCol(subCol);
                /**
                 * send new info to the opponent
                 */
                PrintWriter out = new PrintWriter(this.opponent.getOutputStream(), true);
                String newInfo = String.valueOf(subRow) + ", " + String.valueOf(subCol);
                out.println( newInfo ); 
                this.game.setNextTurn();
                break;
            }
            catch ( Exception e){
                System.out.println("invalid selection. Try again.");
            }
        }
    }


    public void playRemote(){

        int bigRow = this.game.getNextBigRow();
        int bigCol = this.game.getNextBigCol();
        String whom = this.symbol;
        System.out.println(String.format(
                           "%s Playing cell %1d-%1d", whom, bigRow, bigCol));
        while (true){
            try{    
                int subRow = -1;
                int subCol = -1;
                BufferedReader input = 
                    new BufferedReader(new InputStreamReader(opponent.getInputStream()));
                String msg = input.readLine();
                String[] tokens = msg.trim().split(",");
                subRow = Integer.parseInt(tokens[0].trim());
                subCol = Integer.parseInt(tokens[1].trim());

                if ( this.side == 1 ) {
                    this.game.getBoard().putX(bigRow, bigCol, subRow, subCol);
                }
                else {
                    this.game.getBoard().putO(bigRow, bigCol, subRow, subCol);
                }
                this.game.setNextBigRow(subRow);
                this.game.setNextBigCol(subCol);
                this.game.setNextTurn();
                break;
            }
            catch ( Exception e){
                System.out.println("invalid selection. Try again.");
            }
        }
    }


    private void chooseSubBoard(){
        if ( this.isLocal ) chooseSubBoardLocal();
        else chooseSubBoardRemote();
    }

    private void chooseSubBoardLocal(){

        String whom = this.symbol; 
        TicTacticsBoard board = this.game.getBoard();
        int nextBigRow = this.game.getNextBigRow();
        int nextBigCol = this.game.getNextBigCol();
        while ( !isValid(nextBigRow, nextBigCol) || 
                board.evaluateSubBoard(nextBigRow, nextBigCol) != 0 ){
            if ( !isValid(nextBigRow, nextBigCol) ) {
                System.out.println(String.format(
                        "choose two integer numbers between 0 and 2 separated by comma."));
            }
            else {
                System.out.println(String.format("sub-board %d-%d is already closed.",
                                                 nextBigRow, nextBigCol));
            }

            System.out.println("Choose new sub-board(row, col) to play : ");
            String[] tokens = this.game.readLine().trim().split(",");
            nextBigRow = Integer.parseInt(tokens[0].trim());
            nextBigCol = Integer.parseInt(tokens[1].trim());
        } 
        // if nextBigRow or nextBigCol has been updated, send new ones to the opponent
        if ( nextBigRow != this.game.getNextBigRow() || 
             nextBigCol != this.game.getNextBigCol()) {

            /**
             * send new bigRow/bigCol to the opponent.
             * This is a blocking method. Blocked until the opponent receive the message.
             */
            try{
                PrintWriter out = new PrintWriter(this.opponent.getOutputStream(), true);
                String newInfo = String.valueOf(nextBigRow) + ", " + String.valueOf(nextBigCol);
                out.println( newInfo ); 
            }
            catch (IOException e){
                System.out.println(
                    "unexpected exception caught in sending message to the opponent.");
                System.exit(0);
            }
            this.game.setNextBigRow(nextBigRow);
            this.game.setNextBigCol(nextBigCol);
        }
    }

    private void chooseSubBoardRemote(){
        String whom = this.symbol; 
        TicTacticsBoard board = this.game.getBoard();
        int nextBigRow = this.game.getNextBigRow();
        int nextBigCol = this.game.getNextBigCol();
        if ( !isValid(nextBigRow, nextBigCol) || 
                  board.evaluateSubBoard(nextBigRow, nextBigCol) != 0 ){
            try{
                BufferedReader input = 
                    new BufferedReader(new InputStreamReader(opponent.getInputStream()));
                String msg = input.readLine();
                String[] tokens = msg.trim().split(",");
                nextBigRow = Integer.parseInt(tokens[0].trim());
                nextBigCol = Integer.parseInt(tokens[1].trim());
                this.game.setNextBigRow(nextBigRow);
                this.game.setNextBigCol(nextBigCol);
            }
            catch (IOException e){
                System.out.println(
                    "unexpected exception caught in receiving message from the opponent.");
                System.exit(0);
            }
        }
    }

    private static boolean isValid(int row, int col){
        return (row >= 0 && row < 3 && col >= 0 && col < 3);
    }



    

}


