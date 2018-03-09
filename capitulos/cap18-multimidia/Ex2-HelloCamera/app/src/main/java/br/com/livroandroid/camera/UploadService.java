package br.com.livroandroid.camera;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.livroandroid.camera.lib.HttpHelper;
import br.com.livroandroid.camera.lib.IOUtils;


/**
 * Created by rlecheta on 09/03/18.
 */

public class UploadService {
    private static final String URL_BASE = "http://livrowebservices.com.br/rest/carros";
    private static final String TAG = "carros";

    public static Response upload(File file) throws IOException {
        String url = URL_BASE + "/postFotoBase64";

        // Converte para Base64
        byte[] bytes = IOUtils.toBytes(new FileInputStream(file));
        String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);

        Log.d(TAG, "base64: " + base64);

        // Faz POST
        Map<String, String> params = new HashMap<String, String>();
        params.put("fileName", file.getName());
        params.put("base64", base64);

        String json = HttpHelper.postForm(url, params);
        Log.d(TAG, "response: " + json);

        // Converte a resposta para objeto
        Response response = new Gson().fromJson(json, Response.class);

        return response;
    }
}
