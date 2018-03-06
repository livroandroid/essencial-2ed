package br.com.livroandroid.carros.domain;

/**
 * Created by rlecheta on 05/03/18.
 */

class Response {
    public Long id;
    public String status;
    public String msg;
    public String url;

    public boolean isOk() {
        return "OK".equals(status);
    }
}
