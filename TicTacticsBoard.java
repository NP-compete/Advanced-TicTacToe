/**
 * Class representing a board of Tic-Tactics(or double-nested tic-tac-toe) game.
 * This class object contains internal SubBoard double-array, and methods to
 * evaluate winnings.
 */

public class TicTacticsBoard{

    private SubBoard[][] board;

    public TicTacticsBoard(){
        this.board = new SubBoard[3][3];
        for ( int row = 0; row < 3; row++){
            for ( int col = 0; col < 3; col++){
                this.board[row][col] = new SubBoard();
            }
        }
    }

    public SubBoard getSubBoard(int bigRow, int bigCol){
        return this.board[bigRow][bigCol];
    }

    public void putX(int bigRow, int bigCol, int subRow, int subCol){
        this.board[bigRow][bigCol].putX(subRow, subCol);
    }    

    public void putO(int bigRow, int bigCol, int subRow, int subCol){
        this.board[bigRow][bigCol].putO(subRow, subCol);
    }    

    /***************************************************************
     *                      Evaluation methods                     *
     ***************************************************************/
    public int evaluate(){
        // check diagonals
        int diagResult = checkDiags();
        if ( diagResult != 0){
            return diagResult;
        }
        int rowResult = 0;
        for ( int col = 0; col < 3; col++){
            rowResult = checkRow(col);
            if ( rowResult != 0 ) return rowResult;
        }
        int colResult = 0;
        for ( int row = 0; row < 3; row++){
            colResult = checkCol(row);
            if ( colResult != 0 ) return colResult;
        }
        return 0; // draw or undetermined        
    }
    private int checkDiags(){
        if ((( this.board[0][0].evaluate() == this.board[1][1].evaluate() )&&
             (this.board[0][0].evaluate() == this.board[2][2].evaluate()) ) ||
           (( this.board[0][2].evaluate() == this.board[1][1].evaluate() )&&
             (this.board[0][2].evaluate() == this.board[2][0].evaluate() ))){
            return this.board[1][1].evaluate();
        }
        return 0;
    }
    private int checkRow(int col){
        if ( this.board[0][col].evaluate() == this.board[1][col].evaluate() &&
             this.board[0][col].evaluate() == this.board[2][col].evaluate() ){
            return this.board[0][col].evaluate();
        }
        return 0;
    }
    private int checkCol(int row){
        if ( this.board[row][0].evaluate() == this.board[row][1].evaluate() &&
             this.board[row][0].evaluate() == this.board[row][2].evaluate() ){
            return this.board[row][0].evaluate();
        }
        return 0;
    }
    public int evaluateSubBoard(int row, int col){
        return this.board[row][col].evaluate();
    }


    // check if still playable subboard is left
    public boolean isDone(){
        for ( int row = 0; row < 3; row++){
            for ( int col = 0; col < 3; col++){
                if (this.board[row][col].evaluate() == 0){
                    return false;
                }
            }
        }
        return true; 
    }
    /***************************************************************/



}
