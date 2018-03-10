package br.com.livroandroid.carros.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;
import br.com.livroandroid.carros.domain.FavoritosService;
import br.com.livroandroid.carros.domain.Response;
import br.com.livroandroid.carros.domain.event.RefreshListEvent;
import br.com.livroandroid.carros.fragments.MapaFragment;

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

        // Evento no botão FAB
        findViewById(R.id.fab).setOnClickListener(onClickFavoritar());

        // Evento do botão Play
        findViewById(R.id.imgPlayVideo).setOnClickListener(onClickPlayVideo());

        if(savedInstanceState == null) {
            // Cria o fragment do Mapa
            MapaFragment mapaFragment = new MapaFragment();
            // Passa todos os parâmetros que esta activity recebeu para o fragment
            // Dentro dos parâmetros está o carro
            mapaFragment.setArguments(getIntent().getExtras());
            // Adiciona o fragment no layout
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mapaFragment, mapaFragment);
            ft.commit();
        }
    }

    private View.OnClickListener onClickPlayVideo() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para tocar o vídeo no player nativo
                String url = carro.urlVideo;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener onClickFavoritar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dispara a thread
                new TaskFavoritar().execute();
            }
        };
    }

    // Task para favoritar o carro
    private class TaskFavoritar extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // Salva o carro no banco de dados
            return FavoritosService.favoritar(carro);
        }
        protected void onPostExecute(Boolean favoritado) {
            // Alerta de sucesso
            toast(favoritado ? R.string.msg_carro_favoritado : R.string.msg_carro_desfavoritado);

            // Dispara evento para atualizar a lista de favoritos
            EventBus.getDefault().post(new RefreshListEvent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_editar) {
            // Abre a tela de cadastro (editar o carro)
            Intent intent = new Intent(this, CarroFormActivity.class);
            // Passa todos os parâmetros, inclusive o carro.
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_deletar) {
            // Confirma (S/N) se pode excluir
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Confirma excluir este carro?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Executa a thread para deletar o carro
                            new DeletarCarroTask().execute();
                        }
                    }).setNegativeButton(R.string.nao, null);
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
                Log.e(TAG, "Erro: " + e.getMessage());
                return null;
            }
        }

        // Atualiza a interface
        protected void onPostExecute(Response response) {
            if (response != null) {
                // Dispara evento para atualizar a lista de carros
                EventBus.getDefault().post(new RefreshListEvent());

                // Mostra a mensagem de confirmação
                toast(response.msg);
                finish();
            }
        }
    }

    // Desenha a cor do FAB conforme está favoritado ou não.
    private void setFavoriteColor(boolean favorito) {
        // Troca a cor conforme o status do favoritos
        int fundo = ContextCompat.getColor(this, favorito? R.color.favorito_on : R.color.favorito_off);
        int cor = ContextCompat.getColor(this, favorito ? R.color.yellow : R.color.favorito_on);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{fundo}));
        fab.setColorFilter(cor);
    }

}