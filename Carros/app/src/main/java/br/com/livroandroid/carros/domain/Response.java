package br.com.livroandroid.carros.domain;

public class Response {
    public Long id;
    public String status;
    public String msg;
    public String url;

    public boolean isOk() {
        return "OK".equals(status);
    }
}
