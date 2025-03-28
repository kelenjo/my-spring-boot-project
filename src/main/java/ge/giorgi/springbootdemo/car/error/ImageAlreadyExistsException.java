package ge.giorgi.springbootdemo.car.error;

public class ImageAlreadyExistsException extends RuntimeException {
    public ImageAlreadyExistsException(String message) {
        super(message);
    }
}

