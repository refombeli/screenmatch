package br.com.alura.screenmatch.repository;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
