package br.com.livroandroid.carros.domain.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.livroandroid.carros.domain.Carro;

// Define as classes que precisam ser persistidas e a versão do banco
@Database(entities = {Carro.class}, version = 1)
public abstract class CarrosDatabase extends RoomDatabase {
    public abstract CarroDAO carroDAO();
}
