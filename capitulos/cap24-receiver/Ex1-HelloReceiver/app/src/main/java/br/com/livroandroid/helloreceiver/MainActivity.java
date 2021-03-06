package br.com.livroandroid.helloreceiver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cria o canal (channel) para a notificação
        NotificationUtil.createChannel(this);
    }

    public void onClickDispararBroadcast(View view) {
        Intent intent = new Intent(this, HelloReceiver.class);
        sendBroadcast(intent);
        Toast.makeText(this, "Intent enviada!", Toast.LENGTH_SHORT).show();
    }
}
