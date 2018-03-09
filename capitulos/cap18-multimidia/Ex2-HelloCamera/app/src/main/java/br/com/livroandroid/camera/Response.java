package br.com.livroandroid.camera;

/**
 * Classe que trata a resposta do ws.
 * É a mesma usada no app dos carros;
 */
public class Response {
    public Long id;
    public String status;
    public String msg;
    public String url;
    public boolean isOk() {
        return "OK".equals(status);
    }
}
