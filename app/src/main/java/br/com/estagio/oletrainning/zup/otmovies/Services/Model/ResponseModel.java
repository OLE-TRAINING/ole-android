package br.com.estagio.oletrainning.zup.otmovies.Services.Model;

public class ResponseModel <T> {

    private T response;
    private ErrorMessage errorMessage;

    private int code;

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}