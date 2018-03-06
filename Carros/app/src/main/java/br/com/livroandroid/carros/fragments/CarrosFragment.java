package br.com.livroandroid.carros.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.activity.CarroActivity;
import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;
import br.com.livroandroid.carros.domain.event.RefreshListEvent;

public class CarrosFragment extends BaseFragment {
    private static final String TAG = "caros";
    // Tipo enviado pelos parâmetros
    private int tipo;
    // Lista de carros
    protected RecyclerView recyclerView;
    private List<Carro> carros;

    private ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshListEvent event) {
        taskCarros();
    }

    // Método para instanciar esse fragment pelo tipo
    public static CarrosFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        CarrosFragment f = new CarrosFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carros, container,false);

        // Cria a lista de carros
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        // Faz a leitura dos parâmetros
        this.tipo = getArguments().getInt("tipo");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // O método onResume() é chamado sempre que entrar na tela.
        taskCarros();
    }

    private void taskCarros() {
//        boolean internetOk = AndroidUtils.isNetworkAvailable(getContext());
//        Toast.makeText(getContext(), "Internet OK: " + internetOk, Toast.LENGTH_SHORT).show();

        // Busca os carros: Dispara a Task
        new GetCarrosTask().execute();
    }
    
    // Task para buscar os carros
    private class GetCarrosTask extends AsyncTask<Void,Void,List<Carro>> {
        @Override
        protected List<Carro> doInBackground(Void... params) {
            try {
                // Busca os carros em background (Thread)
                return CarroService.getCarros(getContext(), tipo);
            } catch (IOException e) {
                Log.e(TAG,"Erro: " + e.getMessage());
                return null;
            }
        }
        // Atualiza a interface
        protected void onPostExecute(List<Carro> carros) {
            if(carros != null) {
                CarrosFragment.this.carros = carros;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
            }
        }
    }

    // Da mesma forma que tratamos o evento de clique em um botão (OnClickListener)
    // Vamos tratar o evento de clique na lista
    // A diferença é que a interface CarroAdapter.CarroOnClickListener nós mesmo criamos
    private CarroAdapter.CarroOnClickListener onClickCarro() {
        return new CarroAdapter.CarroOnClickListener() {
            @Override
            public void onClickCarro(View view, int idx) {
                // Carro selecionado
                Carro c = carros.get(idx);

                // Navega para a tela CarroActivity
                Intent intent = new Intent(getContext(), CarroActivity.class);
                intent.putExtra("carro", c);
                startActivity(intent);
            }
        };
    }

}
