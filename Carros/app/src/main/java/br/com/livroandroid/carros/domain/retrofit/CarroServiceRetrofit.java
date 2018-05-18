package br.com.livroandroid.carros.domain.retrofit;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.Response;
import br.com.livroandroid.carros.domain.TipoCarro;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Implementação da classe com Retrofit
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
    public static List<Carro> getCarros(Context context, TipoCarro tipo) throws IOException {
        Call<List<Carro>> call = getRetrofit().getCarros(tipo.name());
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
}
