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
import br.com.livroandroid.carros.domain.TipoCarro;
import br.com.livroandroid.carros.domain.event.RefreshListEvent;

public class CarrosFragment extends BaseFragment {
    private static final String TAG = "carros";
    // Tipo enviado pelos parâmetros
    private TipoCarro tipo;
    // Lista de carros
    protected  RecyclerView recyclerView;
    protected  List<Carro> carros;
    private ProgressDialog dialog;

    // Método para instanciar esse fragment pelo tipo
    public static CarrosFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        CarrosFragment f = new CarrosFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Registra no bus de eventos
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancela o registro no bus de eventos
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshListEvent event) {
        // Recebeu o evento.
        taskCarros();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cria a view utilizada para o fragment
        View view = inflater.inflate(R.layout.fragment_carros, container, false);
        // Cria a lista de carros
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        // Faz a leitura dos parâmetros
        if(getArguments() != null) {
            this.tipo = (TipoCarro) getArguments().getSerializable("tipo");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskCarros();
    }

    protected  void taskCarros() {
        // Busca os carros: Dispara a Task
        new GetCarrosTask().execute();
    }

    // Task para buscar os carros
    private class GetCarrosTask extends AsyncTask<Void, Void, List<Carro>> {
        @Override
        protected List<Carro> doInBackground(Void... params) {
            try {
                // Busca os carros em background (Thread)
                return CarroService.getCarros(getContext(), tipo);
            } catch (IOException e) {
                Log.e(TAG, "Erro: " + e.getMessage());
                return null;
            }
        }

        // Atualiza a interface
        protected void onPostExecute(List<Carro> carros) {
            if (carros != null) {
                CarrosFragment.this.carros = carros;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
            }
        }
    }


    // Da mesma forma que tratamos o evento de clique em um botão (OnClickListener)
    // Vamos tratar o evento de clique na lista
    // A diferença é que a interface CarroAdapter.CarroOnClickListener nós mesmo criamos
    protected  CarroAdapter.CarroOnClickListener onClickCarro() {
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
