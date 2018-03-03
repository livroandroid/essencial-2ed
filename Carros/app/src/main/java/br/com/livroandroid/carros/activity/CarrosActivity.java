package br.com.livroandroid.carros.activity;

import android.os.Bundle;
import android.widget.TextView;

import br.com.livroandroid.carros.R;

public class CarrosActivity extends BaseActivity {
    private int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carros);

        // Configura a Toolbar
        setUpToolbar();

        // Liga o up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            // Lê o tipo dos argumentos
            this.tipo = getIntent().getExtras().getInt("tipo");

            // Mostra o tipo do carro na tela
            TextView text = findViewById(R.id.text);
            text.setText("Carros " + getString(tipo));

        }

    }
}
