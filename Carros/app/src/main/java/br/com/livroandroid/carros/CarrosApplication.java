package br.com.livroandroid.carros;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import br.com.livroandroid.carros.domain.dao.CarroDAO;
import br.com.livroandroid.carros.domain.dao.CarrosDatabase;

public class CarrosApplication extends Application {
    private static final String TAG = "CarrosApplication";
    private static CarrosApplication instance = null;
    private CarrosDatabase dbInstance;

    public static CarrosApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "CarrosApplication.onCreate()");
        // Salva a instância para termos acesso como Singleton
        instance = this;
        Context appContext = getApplicationContext();
        // Configura o Room
        dbInstance = Room.databaseBuilder(
                appContext,
                CarrosDatabase.class,
                "carros.sqlite")
                .build();
    }

    // Retorna o DAO
    public CarroDAO getCarroDAO() {
        return dbInstance.carroDAO();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "CarrosApplication.onTerminate()");
    }
}