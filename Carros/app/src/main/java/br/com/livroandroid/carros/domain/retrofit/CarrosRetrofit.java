package br.com.livroandroid.carros.domain.retrofit;

import java.util.List;

import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.Response;
import retrofit2.Call;
import retrofit2.http.*;

public interface CarrosRetrofit {
    @GET("tipo/{tipo}")
    Call<List<Carro>> getCarros(@Path("tipo") String tipo);

    @POST("./")
    Call<Response> save(@Body Carro carro);

    @DELETE("{id}")
    Call<Response> delete(@Path("id") Long id);
}
