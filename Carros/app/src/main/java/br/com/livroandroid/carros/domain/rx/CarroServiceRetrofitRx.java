package br.com.livroandroid.carros.domain.rx;

import java.io.IOException;
import java.util.List;

import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.Response;
import br.com.livroandroid.carros.domain.TipoCarro;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// Implementação da classe com Retrofit
public class CarroServiceRetrofitRx {
    private static final String BASE_URL = "http://livrowebservices.com.br/rest/carros/";

    // Cria a interface do Retrofit
    private static CarrosRetrofitRx getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        CarrosRetrofitRx cr = retrofit.create(CarrosRetrofitRx.class);
        return cr;
    }

    // Busca a lista de carros pelo tipo
    public static Observable<List<Carro>> getCarros(TipoCarro tipo) {
        return getRetrofit().getCarros(tipo.name());
    }

    // Salva um carro
    public static Response save(Carro carro) throws IOException {
        Call<Response> call = getRetrofit().save(carro);
        Response response = call.execute().body();
        return response;
    }

    // Deleta um carro
    public static Observable<Response> delete(Carro carro) {
        return getRetrofit().delete(carro.id);
    }
}
