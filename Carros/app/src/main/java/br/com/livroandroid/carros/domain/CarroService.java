package br.com.livroandroid.carros.domain;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.utils.HttpHelper;

public class CarroService {
    private static final String URL = " http://livrowebservices.com.br/rest/carros/";
    // Busca a lista de carros pelo tipo
    public static List<Carro> getCarros(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String url = URL.replace("{tipo}", tipoString);
        // Faz a requisição HTTP no servidor e retorna a string com o JSON
        String json = HttpHelper.get(url + "tipo/" + tipoString);
        List<Carro> carros = parserJSON(context, json);
        return carros;
    }
    // Converte a constante para string, para criar a URL do web service
    private static String getTipo(int tipo) {
        if (tipo == R.string.classicos) {
            return "classicos";
        } else if (tipo == R.string.esportivos) {
            return "esportivos";
        }
        return "luxo";
    }


    private static List<Carro> parserJSON(Context context, String json) throws IOException {
        // Informa ao GSON que vamos converter uma lista de Carros
        Type listType = new TypeToken<ArrayList<Carro>>() {}.getType();
        // Faz o parser em apenas uma linha e cria a lista
        List<Carro> carros = new Gson().fromJson(json, listType);
        return carros;
    }
}
