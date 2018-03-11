package br.com.livroandroid.carros.domain;

import java.util.List;

import br.com.livroandroid.carros.CarrosApplication;
import br.com.livroandroid.carros.domain.dao.CarroDAO;

public class FavoritosService {
    // Retorna todos os carros favoritados
    public static List<Carro> getCarros() {
        CarroDAO dao = CarrosApplication.getInstance().getCarroDAO();
        List<Carro> carros = dao.findAll();
        return carros;
    }

    // Verifica se um carro está favoritado
    public static boolean isFavorito(Carro carro) {
        CarroDAO dao = CarrosApplication.getInstance().getCarroDAO();
        boolean exists = dao.getById(carro.id) != null;
        return exists;
    }

    // Salva o carro ou deleta
    public static boolean favoritar(Carro carro) {
        CarroDAO dao = CarrosApplication.getInstance().getCarroDAO();
        boolean favorito = isFavorito(carro);
        if (favorito) {
            // Remove dos favoritos
            dao.delete(carro);
            return false;
        }
        // Adiciona nos favoritos
        dao.insert(carro);
        return true;
    }
}
