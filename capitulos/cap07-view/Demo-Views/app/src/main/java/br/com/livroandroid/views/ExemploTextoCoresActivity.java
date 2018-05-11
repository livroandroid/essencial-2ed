package br.com.livroandroid.views;

import android.app.Activity;
import android.os.Bundle;

import br.com.livroandroid.cap07_view.R;

public class ExemploTextoCoresActivity extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_exemplo_texto_cores);
    }
}

