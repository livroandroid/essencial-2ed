package br.com.livroandroid.carros.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;

public class CarrosActivity extends BaseActivity {
    // Tipo enviado pelos parâmetros
    private int tipo;
    // Lista de carros
    protected RecyclerView recyclerView;
    private List<Carro> carros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carros);

        // Configura a Toolbar
        setUpToolbar();

        // Liga o up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Cria a lista de carros
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        // Faz a leitura dos parâmetros
        this.tipo = getIntent().getExtras().getInt("tipo");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // O método onResume() é chamado sempre que entrar na tela.
        taskCarros();
    }

    // Método que encapsula a busca dos carros
    private void taskCarros() {
        // Busca os carros pelo tipo
        this.carros = CarroService.getCarros(getContext(), tipo);
        // É aqui que utiliza o adapter. O adapter fornece o conteúdo para a lista
        recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
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
