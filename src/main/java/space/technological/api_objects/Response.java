package space.technological.api_objects;

public class Response<T> extends APIObject {
    public T response;
    public String description;

    public Response(T response) {
        this.response = response;
    }

    public Response(T response, String description) {
        this.response = response;
        this.description = description;
    }
}
