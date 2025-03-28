package ge.giorgi.springbootdemo.car.error;

public class InvalidPurchasingException extends RuntimeException{

    public InvalidPurchasingException(String message){
        super(message);
    }
}
