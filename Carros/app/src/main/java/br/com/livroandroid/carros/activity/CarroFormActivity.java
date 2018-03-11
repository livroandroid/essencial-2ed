package br.com.livroandroid.carros.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;
import br.com.livroandroid.carros.domain.Response;
import br.com.livroandroid.carros.domain.UploadService;
import br.com.livroandroid.carros.domain.event.RefreshListEvent;
import br.com.livroandroid.carros.utils.ImageResizeUtils;
import br.com.livroandroid.carros.utils.SDCardUtils;

public class CarroFormActivity extends BaseActivity {

    private static final String TAG = "carros";
    private Carro carro;
    private ImageView appBarImg;
    private TextView tNome;
    private TextView tDesc;
    private RadioGroup tTipo;

    // Caminho para salvar o arquivo
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro_form);

        // Configura a Toolbar
        setUpToolbar();

        // Liga o up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Lê o objeto carro enviado por parâmetro
        this.carro = getIntent().getParcelableExtra("carro");

        // Atualiza o título da tela com o nome do carro
        String title = carro != null ? carro.nome : getString(R.string.novo_carro);
        getSupportActionBar().setTitle(title);

        // Inicia as Views
        appBarImg = findViewById(R.id.appBarImg);
        tNome = findViewById(R.id.tNome);
        tDesc = findViewById(R.id.tDesc);
        tTipo = findViewById(R.id.radioTipo);

        // Atualiza valores do carro
        setCarro();

        // Tira a foto
        appBarImg.setOnClickListener(onClickFoto());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Salvar o estado caso gire a tela
        if(file != null) {
            outState.putSerializable("file", file);
        }
    }

    private View.OnClickListener onClickFoto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria o caminho do arquivo no sdcard
                String nomeFoto = String.format("foto_%s.jpg",System.currentTimeMillis());
                file = SDCardUtils.getPrivateFile(getBaseContext(), nomeFoto, Environment.DIRECTORY_PICTURES);
                // Chama a intent informando o arquivo para salvar a foto
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", file);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, 0);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && file != null) {
            // Tirou uma foto
            showFoto(file);
        }
    }

    // Atualiza a foto do carro
    private void showFoto(File file) {
        if (file != null && file.exists()) {
            Log.d("foto", file.getAbsolutePath());

            int w = appBarImg.getWidth();
            int h = appBarImg.getHeight();
            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(file), w, h, false);
            appBarImg.setImageBitmap(bitmap);
        }
    }

    // Copia os dados do carro para as views
    private void setCarro() {
        if(carro != null) {
            // Imagem do header
            Picasso.with(getContext()).load(carro.urlFoto).fit().into(appBarImg);

            // Descrição
            tNome.setText(carro.nome);
            tDesc.setText(carro.desc);

            // Tipo
            if (getString(R.string.classicos).equals(carro.tipo)) {
                tTipo.check(R.id.tipoClassico);
            } else if (getString(R.string.esportivos).equals(carro.tipo)) {
                tTipo.check(R.id.tipoEsportivo);
            } else if (getString(R.string.luxo).equals(carro.tipo)) {
                tTipo.check(R.id.tipoLuxo);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carro_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_salvar) {
            new SalvarCarroTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Task para buscar os carros
    private class SalvarCarroTask extends AsyncTask<Void, Void, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            try {
                // Cria o carro para salvar
                Carro c = getCarro();

                // Upload da foto
                if(file != null && file.exists()) {
                    Response response = UploadService.upload(file);
                    if(response.isOk()) {
                        c.urlFoto = response.url;
                    }
                }

                // Salva o carro
                Response response = CarroService.save(c);
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

                // Mostra alerta de sucesso
                toast(response.msg);
                Log.d(TAG,"Carro salvo: " + response.id);
                finish();
            }
        }
    }

    // Cria um carro com os valores do formulário
    private Carro getCarro() {
        Carro c = carro != null ? carro : new Carro();
        c.tipo = getTipo();
        c.nome = tNome.getText().toString();
        c.desc = tDesc.getText().toString();
        return c;
    }

    // Converte o valor do Radio para string
    public String getTipo() {
        switch (tTipo.getCheckedRadioButtonId()) {
            case R.id.tipoEsportivo:
                return getString(R.string.esportivos);
            case R.id.tipoLuxo:
                return getString(R.string.luxo);
        }
        return getString(R.string.classicos);
    }
}
