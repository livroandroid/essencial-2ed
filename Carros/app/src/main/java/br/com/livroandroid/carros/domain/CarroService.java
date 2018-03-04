package br.com.livroandroid.carros.domain;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.utils.FileUtils;

public class CarroService {
    private static final boolean LOG_ON = false;
    private static final String TAG = "CarroService";
    public static List<Carro> getCarros(Context context, int tipo) throws IOException {
        String json = readFile(context, tipo);
        List<Carro> carros = parserJSON(context, json);
        return carros;
    }
    private static List<Carro> parserJSON(Context context, String json) throws IOException {
        // Informa ao GSON que vamos converter uma lista de Carros
        Type listType = new TypeToken<ArrayList<Carro>>() {}.getType();
        // Faz o parser em apenas uma linha e cria a lista
        List<Carro> carros = new Gson().fromJson(json, listType);
        return carros;
    }

    // Faz a leitura do arquivo que está na pasta /res/raw
    private static String readFile(Context context, int tipo) throws IOException {
        if (tipo == R.string.classicos) {
            return FileUtils.readRawFileString(context, R.raw.carros_classicos, "UTF-8");
        } else if (tipo == R.string.esportivos) {
            return FileUtils.readRawFileString(context, R.raw.carros_esportivos, "UTF-8");
        }
        return FileUtils.readRawFileString(context, R.raw.carros_luxo, "UTF-8");
    }
}
