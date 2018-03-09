package br.com.livroandroid.carros.fragments;

import android.os.AsyncTask;

import java.util.List;

import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.FavoritosService;

/**
 * Created by rlecheta on 09/03/18.
 */

public class FavoritosFragment extends CarrosFragment {

    private static final String TAG = "carros";

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
                FavoritosFragment.this.carros = carros;

                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
            }
        }
    }
}
