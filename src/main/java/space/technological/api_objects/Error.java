package space.technological.api_objects;

public class Error<T> extends APIObject {
    public T error;
    public String message;

    public Error(T error) {
        this.error = error;
    }

    public Error(T error, String message) {
        this.error = error;
        this.message = message;
    }
}
