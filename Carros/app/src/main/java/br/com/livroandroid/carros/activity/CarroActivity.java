package br.com.livroandroid.carros.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        final ImageView imgView = findViewById(R.id.appBarImg);
        Picasso.with(getContext()).load(carro.urlFoto).fit().into(imgView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_editar) {
            // Abre a tela de cadastro (editar o carro)
            Intent intent = new Intent(this, CarroFormActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            finish();
            return true;
        } else if(item.getItemId() == R.id.action_deletar) {
            Toast.makeText(this, "deletar o carro", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
