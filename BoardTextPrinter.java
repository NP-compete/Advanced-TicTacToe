public class BoardTextPrinter implements BoardPrinter{

    private TicTacticsBoard board;
    private static final String horizontalLine = getHorizontalLine(45);
    private static final String horizontalSubLine = getHorizontalSubLine(11, 5);

    public BoardTextPrinter(TicTacticsBoard board){
        this.board = board;
    }

    public void printBoard(){
        System.out.println(horizontalLine);            
        for ( int bigRow = 0; bigRow < 3; bigRow++){
            for ( int subRow = 0; subRow < 3; subRow++){
                System.out.println(getRowString(bigRow, subRow));
                if ( subRow != 2 ) System.out.println(horizontalSubLine);
            }
            System.out.println(horizontalLine);
        }
    }
    private String getRowString(int bigRow, int subRow){
        String retval = "";
        for ( int bigCol = 0; bigCol < 3; bigCol++){
            SubBoard subBoard = this.board.getSubBoard(bigRow, bigCol);
            retval += subBoard.getRowString(subRow);
            if ( bigCol != 2 ) retval += " | ";
        }
        retval = retval.replace("X", "\033[31mX\033[0m");
        retval = retval.replace("O", "\033[32mO\033[0m");
        return retval;
    }
    private static String getHorizontalLine(int size){
        return new String(new char[size]).replace("\0", "-");
    }
    private static String getHorizontalSubLine(int subsize, int spacingsize){
        String spacing = new String(new char[spacingsize]).replace("\0", " ");
        String retval = " ";
        for ( int col = 0; col < 3; col++){
            retval += getHorizontalLine(subsize);
            if ( col != 2 ) retval += spacing;
        }
        return retval;
    }



}
