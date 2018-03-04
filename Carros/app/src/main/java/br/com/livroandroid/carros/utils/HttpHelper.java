package br.com.livroandroid.carros.utils;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Classe utilitária para fazer requisições HTTP com a lib OKHttp
 */
public class HttpHelper {

    private static final String TAG = "http";
    private static final boolean LOG_ON = true;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // GET
    public static final String get( String url) throws IOException {
        log("HttpHelper.get: " + url);
        Request request = new Request.Builder().url(url).get().build();
        return getJson(request);
    }

    // POST com JSON
    public static final String post( String url, String json) throws IOException {
        log("HttpHelper.post: " + url + " > " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return getJson(request);
    }

    // POST com parâmetros (form-urlencoded)
    public static final String postForm(String url, Map params) throws IOException {
        log("HttpHelper.postForm: " + url + " > " + params);

        FormBody.Builder builder = new FormBody.Builder();
        for (Object o : params.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            builder.add(key, value);
        }
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        return getJson(request);
    }

    // DELETE
    public static final String delete(String url) throws IOException {
        log("HttpHelper.delete: " + url);
        Request request = new Request.Builder().url(url).delete().build();
        return getJson(request);
    }

    // Lê a resposta do servidor no formato JSON
    private static String getJson(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if(responseBody != null) {
            String json = responseBody.string();
            log("   << : " + json);
            return json;
        } else {
            throw new IOException("Erro ao fazer a requisição");
        }
    }

    private static void log(String s) {
        if(LOG_ON) {
            Log.d(TAG, s);
        }
    }
}
