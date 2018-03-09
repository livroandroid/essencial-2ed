package br.com.livroandroid.carros.domain;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.carros.CarrosApplication;
import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.dao.CarroDAO;
import br.com.livroandroid.carros.utils.HttpHelper;

public class CarroService {
    private static final boolean LOG_ON = false;
    private static final String TAG = "CarroService";
    private static final String BASE_URL = " http://livrowebservices.com.br/rest/carros";
    // Busca a lista de carros pelo tipo
    public static List<Carro> getCarros(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String url = BASE_URL + "/tipo/" + tipoString;
        // Faz a requisição HTTP no servidor e retorna a string com o JSON
        String json = HttpHelper.get(url);
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

    // Deleta um carro
    public static Response delete(Carro carro) throws IOException {
        String url = BASE_URL + "/" + carro.id;
        String json = HttpHelper.delete(url);
        // Lê a resposta
        Response response = new Gson().fromJson(json, Response.class);
        if(response.isOk()) {
            // Se removeu do servidor, remove dos favoritos
            CarroDAO dao = CarrosApplication.getInstance().getCarroDAO();
            dao.delete(carro);
        }
        return response;
    }

    // Salva um carro
    public static Response save(Carro carro) throws IOException {
        // Converte o carro para JSON
        String carroJson = new Gson().toJson(carro);
        // Faz POST do JSON carro
        String json = HttpHelper.post(BASE_URL, carroJson);
        // Lê a resposta
        Response response = new Gson().fromJson(json, Response.class);
        return response;
    }

}
