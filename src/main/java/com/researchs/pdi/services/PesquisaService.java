package com.researchs.pdi.services;

import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.repositories.PesquisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class PesquisaService {

    @Autowired
    private PesquisaRepository pesquisaRepository;

    @Autowired
    private EntrevistadoService entrevistadoService;

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private FolhaService folhaService;

    public Pesquisa novo(String descricao, Date data) {
        valida(descricao, data);

        Pesquisa pesquisa = new Pesquisa();
        pesquisa.setDescricao(descricao);
        pesquisa.setData(data);

        return pesquisaRepository.saveAndFlush(pesquisa);
    }

    private void valida(String descricao, Date data) {
        if (pesquisaRepository.findByDescricaoAndData(descricao, data) != null)
            throw new RuntimeException("Pesquisa já cadastrada");
    }

    public Pesquisa atualizar(Pesquisa pesquisa) {
        return pesquisaRepository.saveAndFlush(pesquisa);
    }

    public Pesquisa pesquisa(String nomePesquisa, Date data) {
        return pesquisaRepository.findByDescricaoAndData(nomePesquisa, data);
    }

    public List<Pesquisa> pesquisa() {
        return pesquisaRepository.findAll();
    }

    public List<Pesquisa> pesquisa(String descricaoPesquisa) {
        return pesquisaRepository.findByDescricao(descricaoPesquisa);
    }

    public List<Pesquisa> pesquisa(Date data) {
        return pesquisaRepository.findByData(data);
    }

    public Pesquisa atualizar(Pesquisa pesquisa, String descricao, Date data) {
        valida(pesquisa, descricao, data);
        pesquisa.setDescricao(descricao);
        pesquisa.setData(data);
        return pesquisaRepository.saveAndFlush(pesquisa);
    }

    private void valida(Pesquisa pesquisa, String descricao, Date data) {
        Pesquisa pesquisaGet = pesquisaRepository.findByDescricaoAndData(descricao, data);
        if (pesquisaGet != null && !pesquisa.equals(pesquisaGet))
            throw new RuntimeException("Pesquisa já cadastrada");
    }

    public void apagarTodasAsPesquisas() {
        for (Pesquisa pesquisa : pesquisa()) {
            entrevistadoService.apagar(pesquisa);
            respostaService.apagar(pesquisa);
            perguntaService.apagar(pesquisa);
            folhaService.apagar(pesquisa);
            pesquisaRepository.delete(pesquisa);
        }

    }

    public Pesquisa pesquisa(Integer idPesquisa) {
        return pesquisaRepository.findById(idPesquisa);
    }
}
