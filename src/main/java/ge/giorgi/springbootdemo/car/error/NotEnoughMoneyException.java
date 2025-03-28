package ge.giorgi.springbootdemo.car.error;

public class NotEnoughMoneyException extends RuntimeException{

    public NotEnoughMoneyException(String message){
        super(message);
    }
}
