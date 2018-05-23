package br.com.livroandroid.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import br.com.livroandroid.camera.lib.ImageResizeUtils;
import br.com.livroandroid.camera.lib.SDCardUtils;

public class MainActivity extends AppCompatActivity {
    // Caminho para salvar o arquivo
    private File file;
    private ImageView imgView;
    private static final String TAG = "carros";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.imagem);

        final Context context = this;

        ImageButton b = findViewById(R.id.btAbrirCamera);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria o caminho do arquivo no sdcard
                // /storage/sdcard/Android/data/br.com.livroandroid.multimidia/files/Pictures/foto.jpg
                file = SDCardUtils.getPrivateFile(getBaseContext(), "foto.jpg", Environment.DIRECTORY_PICTURES);
                // Chama a intent informando o arquivo para salvar a foto
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, 0);
            }
        });

        if (savedInstanceState != null) {
            // Se girou a tela recupera o estado
            file = (File) savedInstanceState.getSerializable("file");
            showImage(file);
        }

        // Upload
        findViewById(R.id.btUpload).setOnClickListener(onClickUpload());
    }

    private View.OnClickListener onClickUpload() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TaskUpload().execute();
            }
        };
    }

    // Task para fazer upload
    private class TaskUpload extends AsyncTask<Void, Void, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            if(file != null && file.exists()) {
                try {
                    return UploadService.upload(file);
                } catch (IOException e) {
                    Log.e(TAG,"Erro ao fazer upload", e);
                }
            }
            return null;
        }
        protected void onPostExecute(Response response) {
            if(response != null) {
                String msg = response.msg;
                String url = response.url;
                Toast.makeText(MainActivity.this, msg +"\n"+url , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Salvar o estado caso gire a tela
        outState.putSerializable("file", file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("foto", "resultCode: " + resultCode);
        if (resultCode == RESULT_OK && file != null) {
            showImage(file);
        }
    }

    // Atualiza a imagem na tela
    private void showImage(File file) {
        if (file != null && file.exists()) {
            Log.d("foto", file.getAbsolutePath());

            int w = imgView.getWidth();
            int h = imgView.getHeight();
            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(file), w, h, false);
            Toast.makeText(this, "w/h:" + imgView.getWidth() + "/" + imgView.getHeight() + " > " + "w/h:" + bitmap.getWidth() + "/" + bitmap.getHeight(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "file:" + file, Toast.LENGTH_SHORT).show();

            imgView.setImageBitmap(bitmap);
        }
    }
}