package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;

import br.com.alura.screenmatch.repository.ConsumoAPI;
import br.com.alura.screenmatch.repository.ConverteDados;
import br.com.alura.screenmatch.repository.SerieRepository;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=e593b597";
    private List<DadosSerie> dadosSerie = new ArrayList<>();


    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1; //O -1 serve só para inicializar a variável opcao com um valor diferente de 0,
        // para que o while (opcao != 0) execute pelo menos uma vez.
        //Se começasse com 0, o menu nunca entraria no loop, porque já atenderia a condição de saída.

        while (opcao != 0) {
            var menu = """
                  1 - Buscar séries
                  2 - Buscar episódios
                  3 - Listar séries buscadas
                  4- Buscar série por titulo
                  5- Buscar séries por ator
                  6- Top 5 Séries
                  7- Buscar séries pelo Gênero
                  8- Buscar séries por número de temporadas
                  9- Buscar episódio por trecho
                  10-Top 5 Episódios
                  11-Episódios a partir de uma data
                  
                  0 - Sair                                 
                  """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorNumeroTemporadas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodioDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSerie.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série listada pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodios> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodios(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }else{
            System.out.println("Série não encontrada!!");
        }
    }

    private void listarSeriesBuscadas(){
        series = repositorio.findAll();         // objetos  dados série em objetos Série e coletar em uma lista
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println); //Ordenação e Impressão: Ordena os objetos serie e imprime cada um deles.
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()){
            System.out.println("Dados da série: "+serieBusca.get());
        }else {
            System.out.println("Série não encontrada!!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do Ator ou Atriz para busca: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de q valor? ");
        var avaliacao = leitura.nextDouble();
        List<Serie>seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que  "+nomeAtor+ " trabalhou: ");
        seriesEncontradas.forEach(s->
                System.out.println(s.getTitulo()+ " avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5series(){
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s->
                System.out.println(s.getTitulo()+ " avaliação: " + s.getAvaliacao()));
    }

    private  void buscarSeriesPorCategoria(){
        System.out.println("Deseja buscar séries de que categoria/gênero? ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries buscadas por categoria: "+nomeGenero);
        seriesPorCategoria.forEach(System.out::println);

    }

    private void buscarSeriesPorNumeroTemporadas(){
        System.out.println("Deseja buscar séries com quantas temporadas? ");
        var numeroTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println(" A partir de qual Avaliação? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> seriesPorNumeroTemporadas = repositorio.seriesPorTemporadaEAvaliacao(numeroTemporadas, avaliacao);
        System.out.println("****Séries Filtradas*******");
        seriesPorNumeroTemporadas.forEach(s->
                System.out.println(s.getTitulo()+ " / Avaliação: " +s.getAvaliacao()));
    }
    private void buscarEpisodioPorTrecho(){
        System.out.println("Digite o episodio para busca: ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodios> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(),e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodios> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach( e->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(),e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));

        }
    }

    private void buscarEpisodioDepoisDeUmaData(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodios> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);

        }

    }
}
