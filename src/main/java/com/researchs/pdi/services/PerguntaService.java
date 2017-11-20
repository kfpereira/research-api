package com.researchs.pdi.services;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.repositories.PerguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerguntaService {

    @Autowired
    private PerguntaRepository perguntaRepository;

    public Pergunta novo(Pesquisa pesquisa, Integer numero, String descricao) {
        valida(pesquisa, numero, descricao);

        Pergunta pergunta = new Pergunta();
        pergunta.setPesquisa(pesquisa);
        pergunta.setNumero(numero);
        pergunta.setDescricao(descricao);

        return perguntaRepository.saveAndFlush(pergunta);
    }

    private void valida(Pesquisa pesquisa, Integer numero, String descricao) {
        Pergunta pergunta = perguntaRepository.findByPesquisaAndNumero(pesquisa, numero);
        if (pergunta != null)
            throw new RuntimeException("Este número já foi cadastrado");

        List<Pergunta> perguntas = perguntaRepository.findByPesquisaAndDescricao(pesquisa, descricao);
        if (perguntas != null && perguntas.size() > 0)
            throw new RuntimeException("Pergunta já cadastrada");
    }

    public List<Pergunta> pesquisa() {
        return perguntaRepository.findAll();
    }

    public List<Pergunta> pesquisa(Pesquisa pesquisa) {
        return perguntaRepository.findByPesquisa(pesquisa);
    }

    public Pergunta pesquisa(Pesquisa pesquisa, Integer numero) {
        return perguntaRepository.findByPesquisaAndNumero(pesquisa, numero);
    }

    public Pergunta atualizar(Pergunta pergunta) {
        return perguntaRepository.saveAndFlush(pergunta);
    }

    public void apagar(Pesquisa pesquisa) {
        for (Pergunta pergunta : pesquisa(pesquisa))
            perguntaRepository.delete(pergunta);
    }

    public Pergunta pesquisa(Integer idPergunta) {
        return perguntaRepository.findById(idPergunta);
    }
}
