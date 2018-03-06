package br.com.livroandroid.carros.domain.retrofit;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
Implementação da classe com Retrofit
 */
public class CarroServiceRetrofit {
    private static final String BASE_URL = "http://livrowebservices.com.br/rest/carros/";

    // Cria a interface do Retrofit
    private static CarrosRetrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CarrosRetrofit cr = retrofit.create(CarrosRetrofit.class);
        return cr;
    }

    // Busca a lista de carros pelo tipo
    public static List<Carro> getCarros(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        Call<List<Carro>> call = getRetrofit().getCarros(tipoString);
        List<Carro> carros = call.execute().body();
        return carros;
    }

    // Salva um carro
    public static Response save(Carro carro) throws IOException {
        Call<Response> call = getRetrofit().save(carro);
        Response response = call.execute().body();
        return response;
    }

    // Deleta um carro
    public static Response delete(Carro carro) throws IOException {
        Call<Response> call = getRetrofit().delete(carro.id);
        Response response = call.execute().body();
        return response;
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
}
