package br.com.livroandroid.carros.domain.rx;

import java.util.List;

import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.Response;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface para o web service RESTFul
 */
public interface CarrosRetrofitRx {
    @GET("tipo/{tipo}")
    Observable<List<Carro>> getCarros(@Path("tipo") String tipo);

    @POST("./")
    Call<Response> save(@Body Carro carro);

    @DELETE("{id}")
    Observable<Response> delete(@Path("id") Long id);
}
