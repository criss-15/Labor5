package exceptions;

public class DataException extends Exception{
    public DataException(String Message){
        super(Message);
        System.out.println(Message);
    }
}
