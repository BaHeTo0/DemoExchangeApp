package me.BaHeTo0.demoExchange.models.responses;

public class Response {

    private boolean success;
    private ErrorInfo error;

    public Response() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }


}
