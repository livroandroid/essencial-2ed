package br.com.livroandroid.carros.domain.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.livroandroid.carros.domain.Carro;

@Dao
public interface CarroDAO {
    @Query("SELECT * FROM carro where id = :id")
    Carro getById(Long id);

    @Query("SELECT * FROM carro")
    List<Carro> findAll();

    @Insert
    void insert(Carro carro);

    @Delete
    void delete(Carro carro);
}