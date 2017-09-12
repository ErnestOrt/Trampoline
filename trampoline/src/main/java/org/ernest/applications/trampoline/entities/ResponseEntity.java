package org.ernest.applications.trampoline.entities;

public class ResponseEntity<T> {
    private final String status;
    private final T body;

    private ResponseEntity(String status, T body) {
        this.status = status;
        this.body = body;
    }

    public static <T> ResponseEntity success(T body) {
        return new ResponseEntity<>("success", body);
    }

    public static ResponseEntity emptySuccess() {
        return new ResponseEntity<>("success", null);
    }

    public static ResponseEntity failed() {
        return new ResponseEntity<>("failed", null);
    }

    public String getStatus() {
        return status;
    }

    public T getBody() {
        return body;
    }
}
