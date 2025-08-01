package br.com.alura.screenmatch.model;


import br.com.alura.screenmatch.repository.traducao.ConsultaMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name="series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String imagem;
    private String sinopse;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodios> episodios = new ArrayList<>();

    public  Serie(){}

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0.0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.imagem = dadosSerie.imagem();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public List<Episodios> getEpisodios(){
        return  episodios;
    }
    public void setEpisodios(List<Episodios> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return
                "Gênero= " + genero +
                        ",Titulo= " + titulo + '\'' +
                        ", Total de Temporadas= " + totalTemporadas +
                        ", Avaliação= " + avaliacao +
                        ", Atores= '" + atores + '\'' +
                        ", Imagem= '" + imagem + '\'' +
                        ", Sinopse= '" + sinopse + '\''+
                        ", Episódios= '" + episodios + '\'';
    }

}

