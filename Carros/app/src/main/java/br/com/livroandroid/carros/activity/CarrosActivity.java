package br.com.livroandroid.carros.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.fragments.CarrosFragment;

public class CarrosActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carros);
        // Configura a toolbar
        setUpToolbar();
        // Mostra o botão voltar "up navigation"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Mostra o tipo do carro no título
        String tipo = getString(getIntent().getIntExtra("tipo", 0));
        getSupportActionBar().setTitle(tipo);
        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            // Cria uma instância do fragment, e configura os argumentos.
            CarrosFragment frag = new CarrosFragment();
            // Dentre os argumentos que foram passados para a activity, está o tipo do carro.
            frag.setArguments(getIntent().getExtras());
            // Adiciona o fragment no layout de marcação
            getSupportFragmentManager().beginTransaction().add(R.id.container, frag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_editar) {
            Intent intent = new Intent(this, CarroFormActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.action_deletar) {
            Toast.makeText(this, "deletar o carro", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
