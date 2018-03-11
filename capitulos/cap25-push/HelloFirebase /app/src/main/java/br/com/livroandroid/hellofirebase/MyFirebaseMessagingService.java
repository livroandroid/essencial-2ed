package br.com.livroandroid.hellofirebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "firebase";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage);

        // Verifica se a mensagem é do tipo dados (data messages)
        if (! remoteMessage.getData().isEmpty()) {
            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "Dados: " + data);
            Log.d(TAG, "Nome: " + data.get("nome"));
            Log.d(TAG, "Sobrenome: " + data.get("sobrenome"));

            showNotification(remoteMessage);
        }

        // Verifica se a mensagem é do tipo notificação.
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage);
        }
    }

    // Cria uma notificação
    private void showNotification(RemoteMessage remoteMessage) {
        // Lê o título e a mensagem
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        title = title == null ? getString(R.string.app_name) : title;
        // Cria a intent
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        Map<String, String> data = remoteMessage.getData();
        for(String key: data.keySet()) {
            // Adiciona todos os parâmetros enviados na intent
            intent.putExtra(key, data.get(key));
        }
        // Cria a notificação
        NotificationUtil.create(getBaseContext(),1,intent,title, body);
    }
}
