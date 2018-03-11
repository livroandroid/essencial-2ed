package br.com.livroandroid.hellofirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "firebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cria o canal (channel) para a notificação
        NotificationUtil.createChannel(this);

        // Imprime o token do Firebase para debug
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Firebase Token: " + refreshedToken);

        // Ao clicar na notificação os parâmetros são enviados pela Intent
        Log.d(TAG, "Nome: " + getIntent().getStringExtra("nome"));
        Log.d(TAG, "Sobrenome: " + getIntent().getStringExtra("sobrenome"));

    }
}
