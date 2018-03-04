package br.com.livroandroid.carros.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;

public class CarroActivity extends BaseActivity {

    private Carro carro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        // Configura a Toolbar
        setUpToolbar();

        // Liga o up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Lê o objeto carro enviado por parâmetro
        this.carro = getIntent().getParcelableExtra("carro");

        // Atualiza o título da tela com o nome do carro
        getSupportActionBar().setTitle(carro.nome);

        // Atualiza a descrição do carro
        TextView tDesc = findViewById(R.id.tDesc);
        tDesc.setText(carro.desc);

        // Mostra a foto do carro no ImageView
        // A lib Picasso está dando uma força aqui
        final ImageView imgView = findViewById(R.id.img);
        Picasso.with(getContext()).load(carro.urlFoto).fit().into(imgView);
    }
}
