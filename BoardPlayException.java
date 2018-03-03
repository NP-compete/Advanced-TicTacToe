import java.lang.RuntimeException;

public class BoardPlayException extends RuntimeException{
    public BoardPlayException(String msg){
        super(msg);
    }
}
