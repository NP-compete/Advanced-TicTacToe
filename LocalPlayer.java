/**
 * This class implements local-player version of GamePlayer interface.
 * It ensures a local player to paly within the proper game rule.
 * Since there can be no player without an actual game, an instance of 
 * this class works only with a TicTactics instance, which needs to
 * be added to the instance object by addToGame() method.
 *
 */

public class LocalPlayer implements GamePlayer{
   
    private TicTactics game;
    private final int side; // 1 if X, -1 if O
    private final String symbol;   

    /**
     * public constructor 
     * 
     * @param side integer indicating which side the player plays for 
     *        "X" (side=1) or for "O" (side=-1)
     */
    public LocalPlayer(int side){
        assert ( side == 1 || side == -1 );
        this.side = side;
        this.symbol = ( side == 1? "X" : "O"); 
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

    public void play(){
        chooseSubBoard();
        int bigRow = this.game.getNextBigRow();
        int bigCol = this.game.getNextBigCol();
        play(bigRow, bigCol);
    }

    public void play(int bigRow, int bigCol){
        while (true){
            try{    
                chooseSubBoard();
                String whom = this.symbol;
                System.out.println(String.format(
                                   "%s Playing cell %1d-%1d", whom, bigRow, bigCol));
                System.out.println(String.format(
                                   "Enter row, col to place %s in the sub-board", whom));
                String[] tokens = this.game.readLine().trim().split(",");
                int subRow =  Integer.parseInt(tokens[0].trim());
                int subCol =  Integer.parseInt(tokens[1].trim());
                this.game.setNextBigRow(subRow);
                this.game.setNextBigCol(subCol);
                if ( this.side == 1 ) {
                    this.game.getBoard().putX(bigRow, bigCol, subRow, subCol);
                }
                else {
                    this.game.getBoard().putO(bigRow, bigCol, subRow, subCol);
                }
                this.game.setNextTurn();
                break;
            }
            catch ( Exception e){
                System.out.println("invalid selection. Try again.");
            }
        }
    }

    private void chooseSubBoard(){
        String whom = this.symbol; 
        TicTacticsBoard board = this.game.getBoard();
        int nextBigRow = this.game.getNextBigRow();
        int nextBigCol = this.game.getNextBigCol();
        while ( !isValid(nextBigRow, nextBigCol) ){
            System.out.println("Choose sub-board(row, col) to play : ");
            String[] tokens = this.game.readLine().trim().split(",");
            nextBigRow = Integer.parseInt(tokens[0].trim());
            nextBigCol = Integer.parseInt(tokens[1].trim());
        } 
        while ( board.evaluateSubBoard(nextBigRow, nextBigCol) != 0 ){
            System.out.println(String.format("sub-board %d-%d is already closed.",
                                             nextBigRow, nextBigCol));
            System.out.println(whom + " choose new sub-board(row, col) to play : ");
            String[] tokens = this.game.readLine().trim().split(",");
            nextBigRow = Integer.parseInt(tokens[0].trim());
            nextBigCol = Integer.parseInt(tokens[1].trim());
        }
        this.game.setNextBigRow(nextBigRow);
        this.game.setNextBigCol(nextBigCol);
    }

    private static boolean isValid(int row, int col){
        return (row >= 0 && row < 3 && col >= 0 && col < 3);
    }

}
