public class AiPlayer implements GamePlayer{

    private TicTactics game;
    private final int side; // 1 if X, -1 if O
    private final String symbol;

    /**
     * public constructor
     *
     * @param side integer indicating which side the player plays for
     *        "X" (side=1) or for "O" (side=-1)
     */
    public AiPlayer(int side){
        assert ( side == 1 || side == -1 );
        this.side = side;
        this.symbol = ( side == 1? "X" : "O");
    }


    /**
     * returns the value of the given game using brute-force Min-Max theorem
     */
    private int getValueMinMax(int turn){
        return 0;
    }

    public void play(){
		;

    }

    public void play(int bigRow, int bigCol){
		;
    }

    public void addToGame(TicTactics game){
		;
    }



}
