package br.com.livroandroid.carros.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.FavoritosService;
import br.com.livroandroid.carros.domain.TipoCarro;

/**
 * Created by rlecheta on 11/03/18.
 */
public class FavoritosFragment extends CarrosFragment {
    private static final String TAG = "carros";

    public static FavoritosFragment newInstance() {
        Bundle args = new Bundle();
        args.putSerializable("tipo", TipoCarro.favoritos);
        FavoritosFragment f = new FavoritosFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    protected void taskCarros() {
        // Busca os carros do Favoritos
        new GetCarrosTask().execute();
    }
    // Task para buscar os carros
    private class GetCarrosTask extends AsyncTask<Void,Void,List<Carro>> {
        @Override
        protected List<Carro> doInBackground(Void... params) {
            // Busca os carros em background (Thread)
            return FavoritosService.getCarros();
        }
        // Atualiza a interface
        protected void onPostExecute(List<Carro> carros) {
            if(carros != null) {
                // A lista de carros precisa ser proteced em CarrosFragment
                FavoritosFragment.this.carros = carros;
                // O método onClickCarro() também precisa ser protected
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
            }
        }
    }
}
