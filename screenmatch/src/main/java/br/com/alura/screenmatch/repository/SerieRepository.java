package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodios;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie>findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporadas, Double avaliacao);

    @Query("SELECT s FROM Serie s where s.totalTemporadas <= :totalTemporadas And s.avaliacao>= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

    //utilizando jpqe - ilke faz o mesmo papel do IGNORECASE
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILike %:trechoEpisodio%")
    List<Episodios> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :series Order By e.avaliacao DESC LIMIT 5")
    List<Episodios> topEpisodiosPorSerie(Serie series);

    @Query ("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :series AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodios> episodiosPorSerieEAno(Serie series, int anoLancamento);
}

