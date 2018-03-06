package br.com.livroandroid.carros.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;
import br.com.livroandroid.carros.domain.Response;
import br.com.livroandroid.carros.domain.event.RefreshListEvent;

public class CarroActivity extends BaseActivity {

    private static final String TAG = "carros";
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
            // Confirma (S/N) se pode excluir
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Confirma excluir este carro?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Executa a thread para deletar o carro
                            new DeletarCarroTask().execute();
                        }
                    });
            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Thread para deletar o carro
    private class DeletarCarroTask extends AsyncTask<Void, Void, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            try {
                // Deleta o carro
                Response response = CarroService.delete(carro);
                return response;
            } catch (IOException e) {
                Log.e(TAG,"Erro: " + e.getMessage());
                return null;
            }
        }
        // Atualiza a interface
        protected void onPostExecute(Response response) {
            if(response != null) {
                // Dispara evento para atualizar a lista de carros
                EventBus.getDefault().post(new RefreshListEvent());

                // Mostra a mensagem de confirmação
                toast(response.msg);
                finish();
            }
        }
    }
}
