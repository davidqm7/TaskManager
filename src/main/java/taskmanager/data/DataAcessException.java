package taskmanager.data;

public class DataAcessException extends Exception{
    public DataAcessException(String message)
    {
        super(message); //inheriting message from exception
    }
    public DataAcessException(String message, Throwable cause)
    {
        super(message,cause);
    }
}
